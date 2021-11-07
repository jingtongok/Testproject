package com.mosi.update.model;

/**
 * @date: on 2019-10-13
 * @author: ctetin
 * @email: mxnzp_life@163.com
 * @desc: 类型配置类
 */
public class TypeConfig {
    /**
     * 更新信息的来源
     */
    public static final int DATA_SOURCE_TYPE_MODEL = 10;//调用方提供信息model

    /**
     * UI样式类型
     */
    public static final int UI_THEME_AUTO = 300;//sdk自主决定，随机从十几种样式中选择一种，并保证同一个设备选择的唯一的
    public static final int UI_THEME_CUSTOM = 399;//用户自定义UI
    public static final int UI_THEME_B = 302;//类型B，具体样式效果请关注demo
}
