package com.mosi.http;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mosi.app.MyApp;
import com.mosi.base.RxBus;

/**
 * @content: Author: gjt66888
 * Description:
 * Time: 2019/5/30
 */
public class NetworkConnectChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //**判断当前的网络连接状态是否可用*/
        String isConnected = NetWorkUtils.getCurrentNetType(MyApp.getContext());

        RxBus.getInstance().post(new NetworkChangeEvent(isConnected == null? false:true));
    }
}