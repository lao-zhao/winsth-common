package com.winsth.android.libs.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.winsth.android.libs.R;
import com.winsth.android.libs.utils.DialogUtil;

public class NetworkBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mActiveNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (mActiveNetworkInfo != null) {
            DialogUtil.showToast(context, context.getResources().getString(R.string.network_connected));
        } else {
            DialogUtil.showToast(context, context.getResources().getString(R.string.network_disconnect));
        }
    }
}

