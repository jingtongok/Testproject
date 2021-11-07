package com.mosi.update.model;


import com.liulishuo.filedownloader.util.FileDownloadHelper;

/**
 * @date: on 2019-10-09
 * @author: a112233
 * @email: mxnzp_life@163.com
 * @desc: 更新配置
 */
public class UpdateConfig {

    //是否是debug状态 打印log
    private boolean debug = true;

    //设置样式类型 默认是随意一个样式类型
    private int uiThemeType = TypeConfig.UI_THEME_AUTO;

    //更新信息的数据来源方式 默认用户自己提供更新信息
    private int dataSourceType = TypeConfig.DATA_SOURCE_TYPE_MODEL;

    //是否在通知栏显示进度 默认显示 显示的好处在于 如果因为网络原因或者其他原因导致下载失败的时候，可以点击通知栏重新下载
    private boolean showNotification = true;

    //通知栏下载进度提醒的Icon图标 默认为0 就是app的logo
    private int notificationIconRes;

    //自定义的Activity类
    private Class customActivityClass;

    //自定义Bean类 此类必须实现LibraryUpdateEntity接口
    private Object modelClass;

    //是否需要进行文件的MD5校验
    private boolean isNeedFileMD5Check;

    //是否静默下载
    private boolean isAutoDownloadBackground;

    //自定义下载
    private FileDownloadHelper.ConnectionCreator customDownloadConnectionCreator;

    public FileDownloadHelper.ConnectionCreator getCustomDownloadConnectionCreator() {
        return customDownloadConnectionCreator;
    }

    public UpdateConfig setCustomDownloadConnectionCreator(FileDownloadHelper.ConnectionCreator customDownloadConnectionCreator) {
        this.customDownloadConnectionCreator = customDownloadConnectionCreator;
        return this;
    }

    public boolean isAutoDownloadBackground() {
        return isAutoDownloadBackground;
    }

    public UpdateConfig setAutoDownloadBackground(boolean autoDownloadBackground) {
        isAutoDownloadBackground = autoDownloadBackground;
        return this;
    }

    public boolean isShowNotification() {
        return showNotification;
    }

    public UpdateConfig setShowNotification(boolean showNotification) {
        this.showNotification = showNotification;
        return this;
    }

    public Class getCustomActivityClass() {
        return customActivityClass;
    }

    public UpdateConfig setCustomActivityClass(Class customActivityClass) {
        this.customActivityClass = customActivityClass;
        return this;
    }

    public boolean isNeedFileMD5Check() {
        return isNeedFileMD5Check;
    }

    public UpdateConfig setNeedFileMD5Check(boolean needFileMD5Check) {
        isNeedFileMD5Check = needFileMD5Check;
        return this;
    }

    public int getNotificationIconRes() {
        return notificationIconRes;
    }

    public UpdateConfig setNotificationIconRes(int notificationIconRes) {
        this.notificationIconRes = notificationIconRes;
        return this;
    }

//    public int getDataSourceType() {
//        return dataSourceType;
//    }
//
//    public UpdateConfig setDataSourceType(int dataSourceType) {
//        this.dataSourceType = dataSourceType;
//        return this;
//    }

//    public Object getModelClass() {
//        return modelClass;
//    }
//
//    public UpdateConfig setModelClass(Object modelClass) {
//        this.modelClass = modelClass;
//        return this;
//    }

    public boolean isDebug() {
        return debug;
    }

    public UpdateConfig setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public int getUiThemeType() {
        return uiThemeType;
    }

    public UpdateConfig setUiThemeType(int uiThemeType) {
        this.uiThemeType = uiThemeType;
        return this;
    }
}
