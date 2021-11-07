package com.mosi.utils;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

import static com.mosi.utils.ColorUtil.getRandomColor;


public class DrawableUtil {
    private DrawableUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 得到实心的drawable, 一般作为选中，点中的效果
     *
     * @param cornerRadius 圆角半径
     * @param solidColor   实心颜色
     * @return 得到实心效果
     */
    public static GradientDrawable getSolidRectDrawable(int cornerRadius, int solidColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        // 设置矩形的圆角半径
        gradientDrawable.setCornerRadius(cornerRadius);
        // 设置绘画图片色值
        gradientDrawable.setColor(solidColor);
        // 绘画的是矩形
        gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        return gradientDrawable;
    }

    /**
     * 背景选择器
     *
     * @param pressedDrawable 按下状态的Drawable
     * @param normalDrawable  正常状态的Drawable
     * @return 状态选择器
     */
    public static StateListDrawable getStateListDrawable(Drawable pressedDrawable, Drawable normalDrawable) {

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, normalDrawable);
        //设置不能用的状态
        //默认其他状态背景
        GradientDrawable gray = getSolidRectDrawable(10, Color.GRAY);
        stateListDrawable.addState(new int[]{}, gray);
        return stateListDrawable;
    }


    /**
     * 实体 得到随机色 状态选择器 默认10px
     *
     * @return 状态选择器
     */

    public static StateListDrawable getDrawable() {
        return getDrawable(10);
    }

    /**
     * 实体 得到随机色 状态选择器
     *
     * @param cornerRadius 圆角半径
     * @return 状态选择器
     */

    public static StateListDrawable getDrawable(int cornerRadius) {
        return getDrawable(cornerRadius, getRandomColor());
    }

    /**
     * 实体 按下的颜色加深
     *
     * @param cornerRadius 圆角半径
     * @param normalColor  正常的颜色
     * @return 状态选择器
     */

    public static StateListDrawable getDrawable(int cornerRadius, int normalColor) {
        return getDrawable(cornerRadius, ColorUtil.colorDeep(normalColor), normalColor);
    }

    /**
     * 实体  状态选择器
     *
     * @param cornerRadius 圆角半径
     * @param pressedColor 按下颜色
     * @param normalColor  正常的颜色
     * @return 状态选择器
     */
    public static StateListDrawable getDrawable(int cornerRadius, int pressedColor, int normalColor) {
        return getStateListDrawable(getSolidRectDrawable(cornerRadius, pressedColor), getSolidRectDrawable(cornerRadius, normalColor));
    }

}
