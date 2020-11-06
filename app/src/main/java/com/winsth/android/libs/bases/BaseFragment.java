package com.winsth.android.libs.bases;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.winsth.android.libs.interfs.IBase;

public abstract class BaseFragment extends Fragment implements IBase {
    @Override
    public <T extends View> T findById(int viewId) {
        return null;
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
            getActivity().finish();
        }
    }

    @Override
    public void openActivity(Context context, Class<?> cls, Bundle bundle, boolean isActivityFinish) {
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        startActivity(intent);

        if (isActivityFinish) {
            getActivity().finish();
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
        getActivity().startService(intent);

        if (isActivityFinish) {
            getActivity().finish();
        }
    }

    @Override
    public void stopService(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        getActivity().stopService(intent);
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
            return ContextCompat.getDrawable(getActivity(), resId);
        }
    }

    @Override
    public int getColorById(int resId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return getResources().getColor(resId);
        } else {
            return ContextCompat.getColor(getActivity(), resId);
        }
    }
}
