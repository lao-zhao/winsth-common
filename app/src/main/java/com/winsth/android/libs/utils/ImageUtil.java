package com.winsth.android.libs.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageUtil {
    private ImageUtil() {
    }

    /**
     * 获取图片地址获取图片，一般用于加载网络图片
     *
     * @param url 图片地址
     * @return 返回图片
     */
    public static Bitmap getBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "Exception:" + e.getMessage(), true);
        } catch (IOException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "Exception:" + e.getMessage(), true);
        }

        return bitmap;
    }
}
