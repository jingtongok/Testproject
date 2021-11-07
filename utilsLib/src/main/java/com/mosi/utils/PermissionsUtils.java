package com.mosi.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.mosi.update.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PermissionsUtils {


    //权限申请 - 不在询问时 跳转到手动开启权限页面  -  默认
    public static Intent getDefaultStartActivity(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return intent;
    }

    //权限申请 - 不在询问时 跳转到手动开启权限页面  -  华为
    public static Intent getHuaweiStartActivity(Context context) {
        Intent intent = null;
        try {
            intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            intent.setComponent(comp);
        } catch (Exception e) {
            ToastUtil.showToast("跳转失败");
            e.printStackTrace();
            getDefaultStartActivity(context);
        }
        return intent;
    }

    //权限申请 - 不在询问时 跳转到手动开启权限页面  -  vivo
    public static Intent getVivoStartActivity(Context context) {
        Intent intent = null;
        try {
            intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } catch (Exception e) {
            ToastUtil.showToast("跳转失败");
            e.printStackTrace();
        }
        return intent;
    }

    //权限申请 - 不在询问时 跳转到手动开启权限页面  -  小米
    private static String getMiuiVersion() {
        String propName = "ro.miui.ui.version.name";
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return line;
    }

    public static Intent getMiuiStartActivity(Context context) {

        String rom = getMiuiVersion();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if ("V6".equals(rom) || "V7".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", context.getPackageName());
        } else if ("V8".equals(rom) || "V9".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", context.getPackageName());
        } else {
            ToastUtil.showToast("跳转失败");
            getDefaultStartActivity(context);
        }

        return intent;
    }

    //权限申请 - 不在询问时 跳转到手动开启权限页面  -  三星
    public static Intent getSumStartActivity(Context context) {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //权限申请 - 不在询问时 跳转到手动开启权限页面  -  OPPO
    public static Intent getOppoStartActivity(Context context) {

        Intent intent = null;
        try {
            intent = new Intent();

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } catch (Exception e) {
            ToastUtil.showToast("跳转失败");
            e.printStackTrace();
            getDefaultStartActivity(context);
        }

        return intent;
    }

    /**
     * 获取手机厂商信息 - getPhoneBrand
     */
    public static Intent getPhoneBrand(Context context) {

        String phoneBrand = PhoneInfo.getPhoneBrand();
        switch (phoneBrand) {
            case "huawei":
                return getHuaweiStartActivity(context);
            case "xiaomi":
                return getMiuiStartActivity(context);
            case "oppo":
                return getOppoStartActivity(context);
            case "samsung":
                return getSumStartActivity(context);
            case "vivo":
                return getVivoStartActivity(context);
            default:
                return getDefaultStartActivity(context);
        }
    }
}
