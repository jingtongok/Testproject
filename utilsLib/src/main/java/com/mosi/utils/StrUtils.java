package com.mosi.utils;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;

/**
 * Created by gjt66888 on 2018/11/16.
 */

public class StrUtils {

    /**
     * 判断是否是一个String 是返回false,不是返回true(null;"";"    ";)
     */
    public static Boolean isString(String str) {
        if (str != null) {
            str = str.trim();
            if (str.isEmpty() || str.equals("") || str.length() == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 判断String是否是空的如果是则设置为默认的值
     *
     * @param str   判断的字符串
     * @param moren 默认的字符串
     * @author whf
     * @date 创建时间：2016-11-7 上午11:24:31
     */
    public static String setString(String str, String moren) {
        if (isString(str)) {
            return moren;
        } else {
            return str;
        }
    }


    /**
     * 判断是手机号和座机
     *
     * @param mobileNums
     * @return
     */
    public static boolean isMobileNO(String mobileNums) {
        /**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         */

        if (isString(mobileNums))
            return false;

        String telRegex = "";

        if (mobileNums.length() > 9) {
            if (!mobileNums.contains("-")) {
                telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
                // "[1]"代表下一位为数字可以是几，"[0-9]"代表可以为0-9中的一个，"[5,7,9]"表示可以是5,7,9中的任意一位,[^4]表示除4以外的任何一个,\\d{9}"代表后面是可以是0～9的数字，有9位。
            } else {
                telRegex = "^[0][1-9]{2,3}-[0-9]{7,8}$";//带区号
            }
        } else {
            telRegex = "^[1-9]{1}[0-9]{7,8}$";//不带区号
        }

        return mobileNums.matches(telRegex);
    }

    /**
     * 数字转百分百
     */
    public static String isBFB(double num) {

        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2); //保留到小数点后2位,不设置或者设置为0表示不保留小数
        return numberFormat.format(num);//结果56.97%
    }


    /**
     * 金额没三位加，并保留两位小数点
     *
     * @param data
     * @return
     */
    public static String isPrice(double data) {
        DecimalFormat df = new DecimalFormat("#,###.00");
        return df.format(data);
    }

    /**
     * 保留两位小数点
     *
     * @param data
     * @return
     */
    public static String isDouble(double data) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(data);
    }

    /**
     * 保留两位小数点
     *
     * @param data
     * @return
     */
    public static String isDouble(double data, String type) {
        DecimalFormat df = new DecimalFormat(type);
        return df.format(data);
    }

    /**
     * 数字中间加*
     *
     * @param stcd
     * @return
     */
    public static String isNumber(String stcd) {

        if (isString(stcd))
            return "";

        int sum = stcd.length();

        if (sum <= 24) {
            return stcd;
        }

        String leftStr = stcd.substring(0, 10);
        String rightStr = stcd.substring(sum - 10, sum);

        return leftStr + "****" + rightStr;
//            int content = sum - 20;
//            return stcd.replaceAll("(\\d{10})\\d{content}(\\d{10})", "$1****$2");
    }


    /**
     * 防抖
     *
     * @return
     */

    private static final int MIN_DELAY_TIME = 1000;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }


    // 数据检验
    public static boolean validateUserName(String userName) {
        String phonePattern = "^[a-zA-Z0-9]{1,32}$";
        Pattern pattern = Pattern.compile(phonePattern);
        return pattern.matcher(userName).matches();
    }

    // 数据检验
    public static boolean validateNumber(String userName) {
        String phonePattern = "^[0-9]{1,6}$";
        Pattern pattern = Pattern.compile(phonePattern);
        return pattern.matcher(userName).matches();
    }

    // 单据类型：type
    // 根据单据类型来盘点单据号前两位：0是出库 ”CK”, 1是入库 ”RK”。
    public static String getStcd(int type) {
        long randomNumber = Math.round((Math.random() + 1) * 1000); //随机四位数
        String date = DateUtils.toDateSS("yyyyMMddHHmmss");
        String bh = type == 0 ? "CK" : "RK";
        return bh + date + randomNumber;
    }

    /**
     * Called when the activity is first created.
     */
    public static void getCreateFile(String path) {

        File file = new File(path);

        if (!file.exists())

            file.mkdir();
    }

    /**
     * 判断IP地址的合法性，这里采用了正则表达式的方法来判断
     * return true，合法
     */
    public static boolean ipCheck(String text) {
        if (text != null && !text.isEmpty()) {
            // 定义正则表达式
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." +
                    "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            // 判断ip地址是否与正则表达式匹配
            if (text.matches(regex)) {
                // 返回判断信息
                return true;
            } else {
                // 返回判断信息
                return false;
            }
        }
        return false;
    }

    public static boolean isNumeric(CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        } else {
            int sz = cs.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

}


