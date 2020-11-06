package com.winsth.android.libs.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {
    public static JSONObject getJSONObjectInstance() {
        return new JSONObject();
    }

    public static void put(JSONObject objet, String key, Object value) {
        if (!objet.has(key))
            try {
                objet.put(key, value);
            } catch (JSONException e) {
                LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
            }
    }

    public static JSONArray getJSONArrayInstance() {
        return new JSONArray();
    }

    public static void put(JSONArray array, JSONObject object) {
        array.put(object);
    }

    /**
     * 根据字符串获取JSON对象
     *
     * @param json
     * @return
     */
    public static JSONObject getJSONObject(String json) {
        JSONObject object = null;

        try {
            object = new JSONObject(json);
        } catch (JSONException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return object;
    }

    /**
     * 根据JSON数组和数组索引获取JSON对象
     *
     * @param array
     * @param index
     * @return
     */
    public static JSONObject getJSONObject(JSONArray array, int index) {
        JSONObject object = null;

        try {
            if (array != null && array.length() > 0)
                object = array.getJSONObject(index);
        } catch (JSONException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return object;
    }

    public static JSONArray getJSONArray(String jsonArray) {
        JSONArray array = null;

        try {
            if (jsonArray != null)
                array = new JSONArray(jsonArray);
        } catch (JSONException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return array;
    }

    /**
     * 根据关键字从JSON对象中获取数组
     *
     * @param object
     * @param key
     * @return
     */
    public static JSONArray getJSONArray(JSONObject object, String key) {
        JSONArray array = null;

        try {
            if (object != null && !TextUtils.isEmpty(getString(object, key)))
                array = object.getJSONArray(key);
        } catch (JSONException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return array;
    }

    public static String getString(JSONObject object, String key) {
        String str = "";

        try {
            if (object == null) {
                return str;
            }
            if (object.has(key)) {
                Object a = object.get(key);
                if (a != null && a != JSONObject.NULL)
                    str = a.toString();
//				str = object.getString(key);
            }
        } catch (JSONException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return str;
    }

    public static int getInt(JSONObject object, String key) {
        int i = -1;

        try {
            if (object != null)
                i = object.getInt(key);
        } catch (JSONException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return i;
    }
}
