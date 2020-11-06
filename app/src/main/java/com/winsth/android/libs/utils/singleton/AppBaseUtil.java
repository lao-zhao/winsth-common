package com.winsth.android.libs.utils.singleton;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.winsth.android.libs.utils.CommonUtil;
import com.winsth.android.libs.utils.LogUtil;

import java.io.File;

/**
 * Created by aaron.zhao on 2016/4/5.
 */
public class AppBaseUtil {
    private static AppBaseUtil mInstance;

    private AppBaseUtil() {
    }

    public static AppBaseUtil getInstance() {
        if (mInstance == null) {
            synchronized (AppBaseUtil.class) {
                if (mInstance == null) {
                    mInstance = new AppBaseUtil();
                }
            }
        }

        return mInstance;
    }

    /**
     * 获取应用程序包名称
     *
     * @param context 上下文
     * @return 返回应用程序包名称
     */
    public String getAppPackageName(Context context) {
        String appInfo = "";

        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "NameNotFoundException:" + e.getMessage(), true);
        }

        if (mPackageInfo != null) {
            appInfo = mPackageInfo.packageName;
        }

        return appInfo;
    }

    public Uri getUriOfFile(Context context, File file) {
        Uri myUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                myUri = FileProvider.getUriForFile(context, getAppPackageName(context) + ".fileprovider", file);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            myUri = Uri.fromFile(file);
        }
        return myUri;
    }

    /**
     * 获取应用程序版本名称
     *
     * @param context 上下文
     * @return 返回应用程序版本名称
     */
    public String getAppVersionName(Context context) {
        String appInfo = "";

        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "NameNotFoundException:" + e.getMessage(), true);
        }

        if (mPackageInfo != null) {
            appInfo = mPackageInfo.versionName;
        }

        return appInfo;
    }

    /**
     * 获取应用程序信息
     *
     * @param context 上下文
     * @return 返回应用程序版本代码
     */
    public int getAppVersionCode(Context context) {
        int appInfo = -1;

        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "NameNotFoundException:" + e.getMessage(), true);
        }

        if (mPackageInfo != null) {
            appInfo = mPackageInfo.versionCode;
        }

        return appInfo;
    }

    /**
     * 创建桌面快捷方式
     *
     * @param context 上下文
     * @param appName 应用程序名称
     * @param appIcon 应用程序图标
     */
    public void addShortcuts(Activity context, int appName, int appIcon) {
        Intent shortcuts = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");// 快捷方式的名称
        shortcuts.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(appName));
        shortcuts.putExtra("duplicate", false); // 不允许重复创建

        ComponentName comp = new ComponentName(context.getPackageName(), context.getPackageName() + "." + context.getLocalClassName());//
        // 确保通过快捷方式和桌面图标打开的是一个应用程序
        Intent shortcutsIntent = new Intent(Intent.ACTION_MAIN);
        shortcutsIntent.setComponent(comp);
        shortcutsIntent.setClassName(context, context.getClass().getName());

        shortcuts.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutsIntent);

        // 快捷方式的图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, appIcon);
        shortcuts.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

        context.sendBroadcast(shortcuts);
    }

    /**
     * 判断快捷方式是否存在
     *
     * @param context 上下文
     * @param appName 应用程序名称
     * @return 返回是否创建:true-已创建;false-未创建
     */
    public boolean isExistShortcuts(Activity context, int appName) {
        boolean isInstallShortcuts = false;

        final String strUri;
        if (Build.VERSION.SDK_INT < 8)
            strUri = "content://com.android.launcher.settings/favorites?notify=true";
        else
            strUri = "content://com.android.launcher2.settings/favorites?notify=true";

        final Uri CONTENT_URI = Uri.parse(strUri);

        final ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(CONTENT_URI, null, "title=?", new String[]{context.getString(appName).trim()}, null);
        if (c != null && c.getCount() > 0) {
            isInstallShortcuts = true;
        }

        return isInstallShortcuts;
    }

    /**
     * 删除快捷方式
     *
     * @param context 上下文
     * @param appName 应用程序名称
     */
    public void deleteShortcuts(Activity context, int appName) {
        Intent shortcuts = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");

        // 快捷方式的名称
        shortcuts.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(appName));
        String appClass = context.getPackageName() + "." + context.getLocalClassName();
        ComponentName comp = new ComponentName(context.getPackageName(), appClass);
        shortcuts.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));

        context.sendBroadcast(shortcuts);
    }
}
