package com.winsth.android.libs.bases;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.winsth.android.libs.interfs.IBase;

/**
 * Created by aaron.zhao on 2016/4/5.
 */
public abstract class BaseActivity extends AppCompatActivity implements IBase {
    @Override
    public <T extends View> T findById(int viewId) {
        return (T) findViewById(viewId);
    }

    @Override
    public <T extends View> T findById(View root, int viewId) {
        return (T) root.findViewById(viewId);
    }

    @Override
    public void openActivity(Context context, Class<?> cls, boolean isActivityFinish) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);

        if (isActivityFinish) {
            this.finish();
        }
    }

    @Override
    public void openActivity(Context context, Class<?> cls, Bundle bundle, boolean isActivityFinish) {
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        startActivity(intent);

        if (isActivityFinish) {
            this.finish();
        }
    }

    @Override
    public void openActivityForResult(Context context, Class<?> cls, int requestCode) {
        openActivityForResult(context, cls, requestCode, null);
    }

    @Override
    public void openActivityForResult(Context context, Class<?> cls, int requestCode, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void openService(Context context, Class<?> cls, Bundle bundle, boolean isActivityFinish) {
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        startService(intent);

        if (isActivityFinish) {
            this.finish();
        }
    }

    @Override
    public void stopService(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        stopService(intent);
    }

    @Override
    public String getStringById(int resId) {
        return getResources().getString(resId);
    }

    @Override
    public Drawable getDrawableById(int resId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(resId);
        } else {
            return ContextCompat.getDrawable(this, resId);
        }
    }

    @Override
    public int getColorById(int resId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return getResources().getColor(resId);
        } else {
            return ContextCompat.getColor(this, resId);
        }
    }
}
