package com.mosi.update.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.mosi.update.interfaces.OnDialogClickListener;
import com.mosi.update.R;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;


import java.io.File;

/**
 * @date: on 2019-10-10
 * @author: a112233
 * @email: mxnzp_life@163.com
 * @desc: 添加描述
 */
public class AppUtils {

    /**
     * 安装app
     *
     * @param context
     * @param file
     */
    public static void installApkFile(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            context.startActivity(intent);
        }
    }


    /**
     * 获取apk的安装路径
     *
     * @return
     */
    public static File getAppLocalPath(String versionName) {
        // 照片全路径

        // apk 保存名称
        String apkName = AppUtils.getAppName(AppUpdateUtils.getInstance().getContext());
        String imageName = getAppRootPath() + "/" + apkName + "_" + versionName + ".apk";

        File file = new File(imageName);
        if (!file.exists()) {
            file.mkdirs();// 创建文件夹
        }
        File out = new File(imageName);
        return out;
    }


    // android 10 适配
    public static File getAppLocalPath10(Context context, String versionName) {
        // apk 保存名称
        String apkName = AppUtils.getAppName(AppUpdateUtils.getInstance().getContext());
        StringBuffer fileName = new StringBuffer();
//        fileName.append(getAppRootPath());
        fileName.append("/");
        fileName.append(apkName);
        fileName.append("_");
        fileName.append(versionName);
        fileName.append(".apk");
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, fileName.toString());
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }

    /**
     * 获取apk存储的根目录
     *
     * @return
     */
    public static String getAppRootPath() {
        //构建下载路径
        String packageName = AppUpdateUtils.getInstance().getContext().getPackageName();
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + packageName + "/apks";
    }

    /**
     * 显示通用对话框
     *
     * @param activity
     * @param msg
     * @param clickListener
     * @param cancelable
     * @param title
     * @param cancelText
     * @param okText
     */
    public static void showDialog(Activity activity, String msg, final OnDialogClickListener clickListener, boolean cancelable, String title, String cancelText, String okText) {
        if (!activity.isFinishing()) {
            new AlertDialog.Builder(activity, R.style.AlertDialog)
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton(okText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (clickListener != null) {
                                clickListener.onOkClick(dialog);
                            }
                        }
                    })
                    .setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (clickListener != null) {
                                clickListener.onCancelClick(dialog);
                            }
                        }
                    })
                    .setCancelable(cancelable)
                    .create()
                    .show();
        }
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        try {
            if ((file.isFile())) {
                file.delete();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 删除文件夹的所有文件
     *
     * @param file
     * @return
     */
    public static boolean delAllFile(File file) {
        if (file == null || !file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null)
                for (File f : files) {
                    delAllFile(f);
                }
        }
        return file.delete();
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        PackageInfo packInfo = getPackInfo(context);
        if (packInfo != null) {
            versionName = packInfo.versionName;
        }
        return versionName;
    }

    /**
     * 获得apkinfo
     *
     * @param context
     * @return
     */
    private static PackageInfo getPackInfo(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo;
    }

    /**
     * 获得apk版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        PackageInfo packInfo = getPackInfo(context);
        if (packInfo != null) {
            versionCode = packInfo.versionCode;
        }
        return versionCode;
    }
}
