package com.winsth.android.libs.utils;

import java.io.UnsupportedEncodingException;

public class StringUtil {
    private StringUtil() {
    }

    /**
     * 判断字符串对象是否为null或者为空
     *
     * @param originalString 初始字符串
     * @return true-空或者为null、false-非空或者不为null
     */
    public static boolean isNullOrEmpty(String originalString) {
        if (originalString == null)
            return true;
        String str = originalString.trim();
        return str == null || str.length() == 0;
    }

    /**
     * 判断是否是一个中文汉字
     *
     * @param c 字符
     * @return true-表示是中文汉字，false-表示是英文字母
     */
    public static boolean isChineseChar(char c) {
        boolean result = false;

        try {
            // 如果字节数大于1是汉字，以这种方式区别英文字母和中文汉字并不是十分严谨
            result = String.valueOf(c).getBytes(ConfigUtil.TextCode.GBK).length > 1;
        } catch (UnsupportedEncodingException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "UnsupportedEncodingException:" + e.getMessage(),
					true);
        }

        return result;
    }

    /**
     * 替代字符串
     *
     * @param originalString 原始字符串
     * @param length         指定长度
     * @return 返回替代源字符串的字符串
     */
    public static String subString(String originalString, int length) {
        String result = "";

        if (originalString != null && !originalString.equalsIgnoreCase("")) {
            if (length <= 0 || length >= originalString.length())
                result = originalString;
            else
                result = originalString.substring(0, length) + "...";
        }

        return result;
    }

    /**
     * 按字节截取字符串
     *
     * @param orignal  原始字符串
     * @param textCode 字符串编码
     * @param start    开始位置
     * @param length   截取位数
     * @return 截取后的字符串
     */
    public static String subString(String orignal, String textCode, int start, int length) {
        String result = "";

        try {
            if (orignal != null && !"".equals(orignal)) {
                byte[] bytes = orignal.getBytes(textCode);

                if (length > 0 && length < bytes.length) {
                    byte[] temps = new byte[length];
                    for (int i = start; i < start + length; i++) {
                        temps[i - start] = bytes[i];
                    }

                    result = new String(temps, textCode);
                }
            }
        } catch (UnsupportedEncodingException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "UnsupportedEncodingException:" + e.getMessage(), true);
        }

        return result;
    }
}
