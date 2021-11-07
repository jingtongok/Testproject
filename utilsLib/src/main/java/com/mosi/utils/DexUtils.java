package com.mosi.utils;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by gjt66888 on 2019/3/19.
 * <p>
 * dex 加密
 */

public class DexUtils {

    /**
     * 32位MD5加密
     *
     * @param content -- 待加密内容
     * @return 加密后
     */
    public static String md5Decode32(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
        //对生成的16字节数组进行补零操作
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    /**
     * 获取授权 注册- MD5 加密
     * usercode +  userpwd + deviceno + shopcode + userlogin.tokey + 固定值（其他接口类似）
     *
     * @return
     */
    public static String getLoginInfo(String devicebrand, String deviceno, String appversion) {

        StringBuffer md5Str = new StringBuffer();
        md5Str.append(devicebrand);
        md5Str.append(deviceno);
        md5Str.append(appversion);
        md5Str.append("TESTZ0");

        return md5Decode32(md5Str.toString());
    }

}
