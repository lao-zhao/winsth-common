package com.winsth.android.libs.utils;

import android.content.Context;

public class TypeUtil {
    private TypeUtil() {
    }

    /**
     * 有对象类型转换成整形
     *
     * @param obj 对象类型参数
     * @return 返回整形
     */
    public static int objToInt(Object obj) {
        int result = 0;

        try {
            if (obj != null)
                result = Integer.parseInt(String.valueOf(obj));
        } catch (Exception e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "Exception:" + e.getMessage(), true);
        }

        return result;
    }

    /**
     * 有对象类型转换成整形
     *
     * @param obj 对象类型参数
     * @return 返回float形
     */
    public static float objToFloat(Object obj) {
        float result = 0;

        try {
            if (obj != null)
                result = Float.parseFloat(String.valueOf(obj));
        } catch (Exception e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "Exception:" + e.getMessage(), true);
        }

        return result;
    }

    /**
     * 有对象类型转换成整形
     *
     * @param obj 对象类型参数
     * @return 返回double形
     */
    public static double objToDouble(Object obj) {
        double result = 0;

        try {
            if (obj != null)
                result = Double.parseDouble(String.valueOf(obj));
        } catch (Exception e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "Exception:" + e.getMessage(), true);
        }

        return result;
    }

    /**
     * 有对象类型转换成整形
     *
     * @param obj 对象类型参数
     * @return 返回long形
     */
    public static long objToLong(Object obj) {
        long result = 0;

        try {
            if (obj != null)
                result = Long.parseLong(String.valueOf(obj));
        } catch (Exception e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "Exception:" + e.getMessage(), true);
        }

        return result;
    }

    /**
     * 有对象类型转换成字符串类型
     *
     * @param obj 对象类型参数
     * @return 返回字符串
     */
    public static String objToString(Object obj) {
        String result = "";

        try {
            if (obj != null)
                result = String.valueOf(obj);
        } catch (Exception e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "Exception:" + e.getMessage(), true);
        }

        return result;
    }

    /**
     * 根据手机的分辨率从单位dp转成为单位 px(像素)
     *
     * @param context 上下文
     * @param dpValue 设置的dp值
     * @return 返回转换后的px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从单位px(像素)转成单位dp
     *
     * @param context 上下文
     * @param pxValue 设置的px值
     * @return 返回转换之后的dp值
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
