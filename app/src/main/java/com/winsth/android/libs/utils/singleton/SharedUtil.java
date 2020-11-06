package com.winsth.android.libs.utils.singleton;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.winsth.android.libs.utils.CommonUtil;
import com.winsth.android.libs.utils.LogUtil;

/**
 * SharedPreferences数据库方法封装类
 */
public class SharedUtil {
    private SharedPreferences mSharedPreferences;

    /**
     * 该类不能够实例化
     */
    private SharedUtil() {
    }

    private SharedUtil(Context context, String sharedName) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE);
        }
    }

    private static SharedUtil mInstance;

    public static SharedUtil getInstance(Context context, String sharedName) {
        if (mInstance == null) {
            synchronized (SharedUtil.class) {
                if (mInstance == null) {
                    mInstance = new SharedUtil(context, sharedName);
                }
            }
        }

        return mInstance;
    }

    /**
     * 保存数据到SharedPreferences
     *
     * @param key   保存值对应的键值
     * @param value 保存值
     * @return 保存成功：true;保存失败：false
     */
    public boolean saveValueByKey(String key, String value) {
        boolean result = false;

        try {
            Editor editor = mSharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();

            result = true;
        } catch (Exception e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "Exception:" + e.getMessage(), true);
        }

        return result;
    }

    /**
     * 根据键获取其所对应的值
     *
     * @param key 值所对应的的键
     * @return 返回键所对应的的值
     */
    public String getValueByKey(String key) {
        String result = mSharedPreferences.getString(key, "");
        return result;
    }

    /**
     * 删除保存的键值
     *
     * @param key 值所对应的的键
     * @return 删除成功：true;删除失败：false
     */
    public boolean removeKey(String key) {
        boolean result = false;

        try {
            Editor editor = mSharedPreferences.edit();
            editor.remove(key);
            editor.commit();

            result = true;
        } catch (Exception e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "Exception:" + e.getMessage(), true);
        }

        return result;
    }
}
