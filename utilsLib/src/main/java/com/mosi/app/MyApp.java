package com.mosi.app;

import android.app.Application;

import com.mosi.app.BaseApp;
import com.mosi.update.R;
import com.mosi.update.model.TypeConfig;
import com.mosi.update.model.UpdateConfig;
import com.mosi.update.utils.AppUpdateUtils;
import com.tencent.bugly.Bugly;
import com.mosi.uikit.toast.ToastUtils;


/**
 * Author: gjt66888
 * Description:
 * Time: 2020/03/28
 */

public class MyApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();

        //**************************************相关第三方SDK的初始化等操作*************************************************
        ToastUtils.init(this);

//        Bugly.init(getApplicationContext(), "a74b46f6a3", false);

        //更新库配置
        UpdateConfig updateConfig = new UpdateConfig()
                .setDebug(true)//是否是Debug模式
                .setShowNotification(true)//配置更新的过程中是否在通知栏显示进度
                .setNotificationIconRes(R.mipmap.ic_launcher)//配置通知栏显示的图标
                .setUiThemeType(TypeConfig.UI_THEME_AUTO)//配置UI的样式，一种有12种样式可供选择
                .setAutoDownloadBackground(false)//是否需要后台静默下载，如果设置为true，则调用checkUpdate方法之后会直接下载安装，不会弹出更新页面。当你选择UI样式为TypeConfig.UI_THEME_CUSTOM，静默安装失效，您需要在自定义的Activity中自主实现静默下载，使用这种方式的时候建议setShowNotification(false)，这样基本上用户就会对下载无感知了
                .setNeedFileMD5Check(false);//是否需要进行文件的MD5检验，如果开启需要提供文件本身正确的MD5校验码，DEMO中提供了获取文件MD5检验码的工具页面，也提供了加密工具类Md5Utils
//                .setCustomDownloadConnectionCreator(new OkHttp3Connection.Creator(builder))//如果你想使用okhttp作为下载的载体，可以使用如下代码创建一个OkHttpClient，并使用demo中提供的OkHttp3Connection构建一个ConnectionCreator传入，在这里可以配置信任所有的证书，可解决根证书不被信任导致无法下载apk的问题
//                .setModelClass(new UpdateModel());
        //初始化
        AppUpdateUtils.init((Application) MyApp.getContext(), updateConfig);
    }
}
