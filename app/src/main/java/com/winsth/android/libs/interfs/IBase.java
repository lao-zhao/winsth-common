package com.winsth.android.libs.interfs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

/**
 * Created by aaron.zhao on 2016/4/5.
 */
public interface IBase {
    <T extends View> T findById(int viewId);

    <T extends View> T findById(View root, int viewId);

    void openActivity(Context context, Class<?> cls, boolean isActivityFinish);

    void openActivity(Context context, Class<?> cls, Bundle bundle, boolean isActivityFinish);

    void openActivityForResult(Context context, Class<?> cls, int requestCode);

    void openActivityForResult(Context context, Class<?> cls, int requestCode, Bundle bundle);

    void openService(Context context, Class<?> cls, Bundle bundle, boolean isActivityFinish);

    void stopService(Context context, Class<?> cls);

    String getStringById(int resId);

    Drawable getDrawableById(int resId);

    int getColorById(int resId);
}
