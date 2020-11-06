package com.winsth.android.libs.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Locale;

public class DeviceUtil {
    private static WakeLock wakeLock = null;
    private static InputMethodManager imm = null;

    private DeviceUtil() {
    }

    /**
     * 获取电源锁
     *
     * @param context 上下文
     * @param tag     标签
     */
    public static void acquireWakeLock(Context context, String tag) {
        /*
         * PARTIAL_WAKE_LOCK:保持CPU 运转，屏幕和键盘灯有可能是关闭的。 SCREEN_DIM_WAKE_LOCK：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
         * SCREEN_BRIGHT_WAKE_LOCK：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯 FULL_WAKE_LOCK：保持CPU 运转，保持屏幕高亮显示，键盘灯也保持亮度
         * ACQUIRE_CAUSES_WAKEUP：强制使屏幕亮起，这种锁主要针对一些必须通知用户的操作. ON_AFTER_RELEASE：当锁被释放时，保持屏幕亮起一段时间
         */
        if (null == wakeLock) {
            PowerManager powerMgr = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = powerMgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag);
            if (null != wakeLock) {
                wakeLock.acquire();
            }
        }
    }

    /**
     * 释放电源锁
     */
    public static void releaseWakeLock() {
        if (null != wakeLock) {
            wakeLock.release();
            wakeLock = null;
        }
    }

    /**
     * 判断当前Android设备是否有Root权限
     *
     * @return true-有;false-无
     */
    public static boolean isRoot() {
        boolean result = false;

        try {
            result = !((!new File("/system/bin/su").exists()) && !new File("/system/xbin/su").exists());
        } catch (Exception e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "Exception:" + e.getMessage(), true);
        }

        return result;
    }

    /**
     * 获取手机设备信息
     *
     * @return 返回手机设备信息
     */
    public static String getDeviceInfo() {
        String deviceInfo = "";

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.getName().toLowerCase(Locale.CHINA).equals("brand")) {
                    deviceInfo += field.getName() + ":" + field.get(null).toString() + "\n";
                }
                if (field.getName().equalsIgnoreCase("device")) {
                    deviceInfo += field.getName() + ":" + field.get(null).toString() + "\n";
                }
                if (field.getName().equalsIgnoreCase("fingerprint")) {
                    deviceInfo += field.getName() + ":" + field.get(null).toString();
                }
            } catch (IllegalArgumentException e) {
                LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                        Exception()), "IllegalArgumentException:" + e.getMessage(), true);
            } catch (IllegalAccessException e) {
                LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                        Exception()), "IllegalAccessException:" + e.getMessage(), true);
            }
        }

        return deviceInfo;
    }

    /**
     * 获取系统SDK版本
     *
     * @return 返回版本
     */
    public static int getSDKVersion() {
        int version = 0;

        try {
            version = Build.VERSION.SDK_INT;
        } catch (Exception e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new
                    Exception()), "Exception:" + e.getMessage(), true);
        }

        return version;
    }

    /**
     * 获取手机屏幕的宽高
     *
     * @param activity 上下文
     * @return 返回屏幕的宽高点（即右下角的点）
     */
    @SuppressWarnings("deprecation")
    public static Point getScreenPoint(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        if (getSDKVersion() < 13)
            point = new Point(display.getWidth(), display.getHeight());
        else
            display.getSize(point);
        return point;
    }

    /**
     * 获取手机屏幕的宽高
     *
     * @param context 上下文
     * @return 返回屏幕的宽高点（即右下角的点）
     */
    @SuppressWarnings("deprecation")
    public static Point getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        if (getSDKVersion() < 13)
            point = new Point(display.getWidth(), display.getHeight());
        else
            display.getSize(point);
        return point;
    }

    /**
     * 获取手机屏幕的宽度（像素）
     *
     * @param activity 上下文
     * @return 返回手机屏幕的宽度
     */
    public static int getScreenWidth(Activity activity) {
        return getScreenSize(activity).x;
    }

    /**
     * 获取手机屏幕的高度（像素）
     *
     * @param activity 上下文
     * @return 返回手机屏幕高度
     */
    public static int getScreenHeight(Activity activity) {
        return getScreenSize(activity).y;
    }

    /**
     * 获取设备像素宽度
     * @param context 当前上下文
     * @return 返回设备像素宽度
     */
    public static int getDevicePxWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);

        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)

        return width;
    }

    /**
     * 获取设备像素高度
     * @param context 当前上下文
     * @return 返回设备像素高度
     */
    public static int getDevicePxHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);

        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)

        return height;
    }

    /**
     * 获取设备像素宽度
     * @param context 当前上下文
     * @return 返回设备像素宽度
     */
    public static int getDeviceDpWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);

        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)

        return screenWidth;
    }

    /**
     * 获取设备像素高度
     * @param context 当前上下文
     * @return 返回设备像素高度
     */
    public static int getDeviceDpHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);

        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)

        return screenHeight;
    }

    /**
     * 弹出软键盘
     * @param context 上下文
     */
    public static void popSoftKeybord(Context context) {
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 弹出软键盘
     * @param context 上下文
     */
    public static void hideSoftKeybord(Context context, View view) {
        if (imm == null) {
            imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
