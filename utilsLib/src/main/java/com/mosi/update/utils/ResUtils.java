package com.mosi.update.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import java.util.Locale;

/**
 * author： deemons
 * date:    2017/3/24
 * desc:
 */

public class ResUtils {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    @NonNull
    private static Context getContext() {
        return mContext;
    }

    /**
     * 得到Resouce对象
     */
    public static Resources getResource() {
        return getContext().getResources();
    }

    /**
     * 得到String.xml中的字符串
     */
    public static String getString(@StringRes int resId) {
        return getResource().getString(resId);
    }

    /**
     * 得到String.xml中的字符串,带占位符
     */
    public static String getString(@StringRes int id, Object... formatArgs) {
        return getResource().getString(id, formatArgs);
    }

    /**
     * 格式 String 字符版本
     */
    public static String getString(String format, Object... formatArgs) {
        return String.format(Locale.getDefault(), format, formatArgs);
    }

}
