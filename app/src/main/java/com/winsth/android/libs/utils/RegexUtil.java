package com.winsth.android.libs.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    private RegexUtil() {
    }

    /**
     * 手机号码是否有效
     *
     * @param phoneNO 手机号码
     * @return 有效：true；无效：false
     */
    public static boolean isValidPhoneNO(String phoneNO) {
        String strExpression = "^((13[0-9])|(147)|(15[^4,\\D])|(18[0-9]))\\d{8}$";
        return isMatch(strExpression, phoneNO);
    }

    /**
     * 判断字符转中是否全部为数字
     *
     * @param number 字符串
     * @return 数字：true；非数字：false
     */
    public static boolean isNumber(String number) {
        String strExpression = "[0-9]*";
        return isMatch(strExpression, number);
    }

    /**
     * 判断字符串是否全为字母
     *
     * @param str 原始字符串
     * @return 返回结果：true-是；false-否
     */
    public static boolean isLetter(String str) {
        String strExpression = "^[A-Za-z]+$";
        return isMatch(strExpression, str);
    }

    /**
     * 判断Email格式是否正确
     *
     * @param email Email字符串
     * @return true：符合；false：不符合
     */
    public static boolean isEmail(String email) {
        String strExpression = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        return isMatch(strExpression, email);
    }

    /**
     * 判断给定的字符串是否固定电话号码
     *
     * @param phoneNo 固定电话字符串
     * @return true：符合；false：不符合
     */
    public static boolean isPhone(String phoneNo) {
        String strExpression = "^((0\\d{2,3})-)(\\d{7,8})(-(\\d{3,}))?$";
        return isMatch(strExpression, phoneNo);
    }

    /**
     * 判断给定字符串是否为邮编格式
     *
     * @param postCode 邮编格式的字符串
     * @return true：符合；false：不符合
     */
    public static boolean isPostcode(String postCode) {
        String strExpression = "^[1-9][0-9]{5}$";
        return isMatch(strExpression, postCode);
    }

    /**
     * 判断给定字符串是否为网址格式
     *
     * @param url 网址格式的字符串
     * @return true：符合；false：不符合
     */
    public static boolean isURL(String url) {
        String strExpression = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-" + "Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{" +
				"2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}" + "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|" +
				"[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-" + "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0" + "-9\\-]+\\.)" +
				"*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/" + "[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*$";
        return isMatch(strExpression, url);
    }

    /**
     * 判断字符串是否包含数字和字母
     *
     * @param str 原始字符串
     * @return 返回结果：true-包含；false-不包含
     */
    public static boolean isNumberAndLetter(String str) {
        String strExpression = "^[A-Za-z0-9]+$";
        return isMatch(strExpression, str);
    }

    /**
     * 判断是否为小数
     *
     * @param decimal 原始数字
     * @return 返回匹配结果
     */
    public static boolean isDecimal(String decimal) {
        String strExpression = "[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+";
        return isMatch(strExpression, decimal);
    }

    /**
     * 判断是否为正确的身份证格式
     *
     * @param idCard 原字符串
     * @return 返回匹配结果
     */
    public static boolean isIdentityCard(String idCard) {
        String strExpression = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
        return isMatch(strExpression, idCard);
    }

    private static boolean isMatch(String strExpression, String orginal) {
        boolean isMatch = false;

        Pattern pattern = Pattern.compile(strExpression);
        Matcher matcher = pattern.matcher(orginal);
        if (matcher.find())
            isMatch = true;

        return isMatch;
    }
}
