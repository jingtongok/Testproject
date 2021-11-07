package com.mosi.update.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadLargeFileListener;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.mosi.base.RxBus;
import com.mosi.update.activity.UpdateBackgroundActivity;
import com.mosi.update.activity.UpdateType2Activity;
import com.mosi.update.interfaces.AppDownloadListener;
import com.mosi.update.interfaces.AppUpdateInfoListener;
import com.mosi.update.interfaces.MD5CheckListener;
import com.mosi.update.model.DownloadInfo;
import com.mosi.update.model.LibraryUpdateEntity;
import com.mosi.update.model.TypeConfig;
import com.mosi.update.model.UpdateConfig;
import com.mosi.update.model.UpdateEvent;
import com.mosi.update.service.UpdateReceiver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.mosi.update.utils.AppUtils.getAppLocalPath;


/**
 * @date: on 2019-10-09
 * @author: a112233
 * @email: mxnzp_life@163.com
 * @desc: 更新组件
 */
public class AppUpdateUtils {

    private static Application mContext;
    private static AppUpdateUtils updateUtils;
    private static UpdateConfig updateConfig;
    //是否初始化
    private static boolean isInit;

    //下载任务
    private BaseDownloadTask downloadTask;

    //是否开始下载
    private static boolean isDownloading = false;

    //本地保留下载信息
    private DownloadInfo downloadInfo;

    //apk下载的路径
    private static String downloadUpdateApkFilePath = "";

    //AppDownloadListener的集合
    private static List<AppDownloadListener> appDownloadListenerList;

    //MD5校验监听
    private static List<MD5CheckListener> md5CheckListenerList;

    //更新信息回调
    private static List<AppUpdateInfoListener> appUpdateInfoListenerList;

    //私有化构造方法
    private AppUpdateUtils() {
        appDownloadListenerList = new ArrayList<>();
        md5CheckListenerList = new ArrayList<>();
        appUpdateInfoListenerList = new ArrayList<>();
    }

    /**
     * 全局初始化
     *
     * @param context
     * @param config
     */
    public static void init(Application context, UpdateConfig config) {
        isInit = true;
        mContext = context;
        updateConfig = config;
        ResUtils.init(context);

        FileDownloadHelper.ConnectionCreator fileDownloadConnection = null;
        //初始化文件下载库
        if (updateConfig != null && updateConfig.getCustomDownloadConnectionCreator() != null) {
            fileDownloadConnection = updateConfig.getCustomDownloadConnectionCreator();
        } else {
            fileDownloadConnection = new FileDownloadUrlConnection
                    .Creator(new FileDownloadUrlConnection.Configuration()
                    .connectTimeout(30_000) // set connection timeout.
                    .readTimeout(30_000) // set read timeout.
            );
        }

        FileDownloader.setupOnApplicationOnCreate(mContext)
                .connectionCreator(fileDownloadConnection)
                .commit();
    }

    public static AppUpdateUtils getInstance() {
        if (updateUtils == null) {
            updateUtils = new AppUpdateUtils();
        }
        return updateUtils;
    }


    /**
     * 检查更新 调用者配置数据 最终三种方式都会到这里来 所以要做静默下载 在这里做就好了
     */
    public void checkUpdate(DownloadInfo info) {
        checkInit();

        if (info == null) {
            return;
        }

        //检查当前版本是否需要更新 如果app当前的版本号大于等于线上最新的版本号 不需要升级版本
        int versionCode = AppUtils.getVersionCode(mContext);
        if (versionCode >= info.getProdVersionCode()) {
            listenToUpdateInfo(true);
            clearAllListener();
            return;
        }

        //通知当前版本不是最新版本
        listenToUpdateInfo(false);

        UpdateConfig updateConfig = getUpdateConfig();

        //检查sdk的挂载 未挂载直接阻断
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.e("gjt", "sdk卡未加载");
            return;
        }

        int type = updateConfig.getUiThemeType();
        if (type == TypeConfig.UI_THEME_AUTO) {
            //随机样式
            String versionName = AppUtils.getVersionName(mContext);
            type = 300 + versionName.hashCode() % 12;
        } else if (type == TypeConfig.UI_THEME_CUSTOM) {
            Class customActivityClass = updateConfig.getCustomActivityClass();
            if (customActivityClass == null) {
                Log.e("gjt", "使用 UI_THEME_CUSTOM 这种UI类型的时候，必须要配置UpdateConfig中的customActivityClass参数为您自定义的Activity");
                return;
            }
            //用户自定义类型
            Intent intent = new Intent(mContext, updateConfig.getCustomActivityClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("info", info);
            mContext.startActivity(intent);
            return;
        }

        //如果用户开启了静默下载 就不需要展示更新页面了
        if (!updateConfig.isAutoDownloadBackground()) {
            //根据类型选择对应的样式
            if (type == TypeConfig.UI_THEME_B) {
                UpdateType2Activity.launch(mContext, info);
            }
        } else {
            //直接下载
            UpdateBackgroundActivity.launch(mContext, info);
            //移除掉之前的事件监听 因为用不到了根本就
            clearAllListener();
        }
    }

    /**
     * 开始下载
     *
     * @param info
     */
    public void download(DownloadInfo info) {
        checkInit();

        downloadInfo = info;

        FileDownloader.setup(mContext);
        File tempFile = null;
        // android 10 适配
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            tempFile = AppUtils.getAppLocalPath10(mContext, info.getProdVersionName());
            downloadUpdateApkFilePath = tempFile.getPath();

        } else {
            tempFile = getAppLocalPath(info.getProdVersionName());
            downloadUpdateApkFilePath = tempFile.getPath();
        }

        if (tempFile != null && tempFile.exists()) {
            if (tempFile.length() != info.getFileSize()) {
                AppUtils.deleteFile(downloadUpdateApkFilePath);
                AppUtils.deleteFile(FileDownloadUtils.getTempPath(downloadUpdateApkFilePath));
            }
        }

        downloadTask = FileDownloader.getImpl().create(info.getApkUrl())
                .setPath(downloadUpdateApkFilePath);
        downloadTask
                .addHeader("Accept-Encoding", "identity")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36")
                .setListener(fileDownloadListener)
                .setAutoRetryTimes(3)
                .start();
    }

    /**
     * 结束任务
     */
    public void cancelTask() {
        isDownloading = false;
        if (downloadTask != null) {
            downloadTask.pause();
        }
        UpdateReceiver.cancelDownload(mContext);
    }

    private FileDownloadListener fileDownloadListener = new FileDownloadLargeFileListener() {
        @Override
        protected void pending(BaseDownloadTask task, long soFarBytes, long totalBytes) {
            downloadStart();
            if (totalBytes < 0) {
                downloadTask.pause();
            }
        }

        @Override
        protected void progress(BaseDownloadTask task, long soFarBytes, long totalBytes) {
            downloading(soFarBytes, totalBytes);
            if (totalBytes < 0) {
                downloadTask.pause();
            }
        }

        @Override
        protected void paused(BaseDownloadTask task, long soFarBytes, long totalBytes) {
            for (AppDownloadListener appDownloadListener : getAllAppDownloadListener()) {
                appDownloadListener.pause();
            }
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            downloadComplete(task.getPath());
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            AppUtils.deleteFile(downloadUpdateApkFilePath);
            AppUtils.deleteFile(FileDownloadUtils.getTempPath(downloadUpdateApkFilePath));
            downloadError(e);
        }

        @Override
        protected void warn(BaseDownloadTask task) {

        }
    };

    /**
     * @param e
     */
    private void downloadError(Throwable e) {
        isDownloading = false;
        AppUtils.deleteFile(downloadUpdateApkFilePath);
        UpdateReceiver.send(mContext, -1);
        for (AppDownloadListener appDownloadListener : getAllAppDownloadListener()) {
            appDownloadListener.downloadFail(e.getMessage());
        }
        Log.e("gjt", "文件下载出错，异常信息为：" + e.getMessage());
    }

    /**
     * 下载完成
     *
     * @param path
     */
    private void downloadComplete(String path) {
        isDownloading = false;
        UpdateReceiver.send(mContext, 100);
        for (AppDownloadListener appDownloadListener : getAllAppDownloadListener()) {
            appDownloadListener.downloadComplete(path);
        }
        Log.e("gjt", "文件下载完成，准备安装，文件地址：" + downloadUpdateApkFilePath);
        //校验MD5
        File newFile = new File(path);
        if (newFile.exists()) {
            //如果需要进行MD5校验
            if (updateConfig.isNeedFileMD5Check()) {
                try {
                    String md5 = Md5Utils.getFileMD5(newFile);
                    if (!TextUtils.isEmpty(md5) && md5.equals(downloadInfo.getMd5Check())) {
                        //校验成功
                        for (MD5CheckListener md5CheckListener : getAllMD5CheckListener()) {
                            md5CheckListener.fileMd5CheckSuccess();
                        }
//                        AppUtils.installApkFile(mContext, newFile);
                        RxBus.getInstance().post(new UpdateEvent(ApiConstant.UPDATE_RETRUE_CODE, newFile));
                        Log.e("gjt", "文件MD5校验成功");
                    } else {
                        Log.e("gjt", "文件下载完成，准备安装，文件地址4：");
                        //校验失败
                        for (MD5CheckListener md5CheckListener : getAllMD5CheckListener()) {
                            md5CheckListener.fileMd5CheckFail(downloadInfo.getMd5Check(), md5);
                        }
                        Log.e("gjt", "文件MD5校验失败，originMD5：" + downloadInfo.getMd5Check() + "  localMD5：" + md5);
                    }
                } catch (Exception e) {
                    Log.e("gjt", "文件MD5解析失败，抛出异常：" + e.getMessage());
                    //安装文件
//                    AppUtils.installApkFile(mContext, newFile);
                    RxBus.getInstance().post(new UpdateEvent(ApiConstant.UPDATE_RETRUE_CODE, newFile));
                }
            } else {
                RxBus.getInstance().post(new UpdateEvent(ApiConstant.UPDATE_RETRUE_CODE, newFile));

                //安装文件
//                AppUtils.installApkFile(mContext, newFile);
            }
        }
    }


    /**
     * 正在下载
     *
     * @param soFarBytes
     * @param totalBytes
     */
    private void downloading(long soFarBytes, long totalBytes) {
        isDownloading = true;
        int progress = (int) (soFarBytes * 100.0 / totalBytes);
        if (progress < 0) progress = 0;
        UpdateReceiver.send(mContext, progress);
        for (AppDownloadListener appDownloadListener : getAllAppDownloadListener()) {
            appDownloadListener.downloading(progress);
        }
        Log.e("gjt", "文件正在下载中，进度为" + progress + "%");
    }

    /**
     * 开始下载
     */
    private void downloadStart() {
        Log.e("gjt", "文件开始下载");
        isDownloading = true;
        UpdateReceiver.send(mContext, 0);
        for (AppDownloadListener appDownloadListener : getAllAppDownloadListener()) {
            appDownloadListener.downloadStart();
        }
    }

    public static boolean isDownloading() {
        checkInit();
        return isDownloading;
    }

    public UpdateConfig getUpdateConfig() {
        if (updateConfig == null) {
            return new UpdateConfig();
        }
        return updateConfig;
    }

    /**
     * 初始化检测
     *
     * @return
     */
    private static void checkInit() {
        if (!isInit) {
            throw new RuntimeException("AppUpdateUtils需要先调用init方法进行初始化才能使用");
        }
    }

    /**
     * 获取Context
     *
     * @return
     */
    public Context getContext() {
        checkInit();
        return mContext;
    }

    /**
     * 重新下载
     */
    public void reDownload() {
        for (AppDownloadListener appDownloadListener : getAllAppDownloadListener()) {
            appDownloadListener.reDownload();
        }
        download(downloadInfo);
    }

    /**
     * 清除所有缓存的数据
     */
    public void clearAllData() {
        //删除任务中的缓存文件
        FileDownloader.getImpl().clearAllTaskData();
        //删除已经下载好的文件
        AppUtils.delAllFile(new File(AppUtils.getAppRootPath()));
    }

    private void requestSuccess(Object response) {
        LibraryUpdateEntity libraryUpdateEntity = (LibraryUpdateEntity) response;
        if (libraryUpdateEntity != null) {
            checkUpdate(new DownloadInfo()
                    .setForceUpdateFlag(libraryUpdateEntity.forceAppUpdateFlag())
                    .setProdVersionCode(libraryUpdateEntity.getAppVersionCode())
                    .setFileSize(Long.parseLong(libraryUpdateEntity.getAppApkSize()))
                    .setProdVersionName(libraryUpdateEntity.getAppVersionName())
                    .setApkUrl(libraryUpdateEntity.getAppApkUrls())
                    .setHasAffectCodes(libraryUpdateEntity.getAppHasAffectCodes())
                    .setMd5Check(libraryUpdateEntity.getFileMd5Check())
                    .setUpdateLog(libraryUpdateEntity.getAppUpdateLog()));
        }
    }

    public AppUpdateUtils addMd5CheckListener(MD5CheckListener md5CheckListener) {
        if (md5CheckListener != null && !md5CheckListenerList.contains(md5CheckListener)) {
            md5CheckListenerList.add(md5CheckListener);
        }
        return this;
    }

    public AppUpdateUtils addAppDownloadListener(AppDownloadListener appDownloadListener) {
        if (appDownloadListener != null && !appDownloadListenerList.contains(appDownloadListener)) {
            appDownloadListenerList.add(appDownloadListener);
        }
        return this;
    }

    public AppUpdateUtils addAppUpdateInfoListener(AppUpdateInfoListener appUpdateInfoListener) {
        if (appUpdateInfoListener != null && !appUpdateInfoListenerList.contains(appUpdateInfoListener)) {
            appUpdateInfoListenerList.add(appUpdateInfoListener);
        }
        return this;
    }

    public List<AppUpdateInfoListener> getAllAppUpdateInfoListener() {
        List<AppUpdateInfoListener> listeners = new ArrayList<>();
        listeners.addAll(appUpdateInfoListenerList);
        return listeners;
    }

    private List<AppDownloadListener> getAllAppDownloadListener() {
        List<AppDownloadListener> listeners = new ArrayList<>();
        listeners.addAll(appDownloadListenerList);
        return listeners;
    }

    private List<MD5CheckListener> getAllMD5CheckListener() {
        List<MD5CheckListener> listeners = new ArrayList<>();
        listeners.addAll(md5CheckListenerList);
        return listeners;
    }

    //是否有新版本更新
    private void listenToUpdateInfo(boolean isLatest) {
        for (AppUpdateInfoListener appUpdateInfoListener : getAllAppUpdateInfoListener()) {
            appUpdateInfoListener.isLatestVersion(isLatest);
        }
    }

    //移除所有监听
    protected static void clearAllListener() {
        md5CheckListenerList.clear();
        appUpdateInfoListenerList.clear();
        appDownloadListenerList.clear();
    }
}
