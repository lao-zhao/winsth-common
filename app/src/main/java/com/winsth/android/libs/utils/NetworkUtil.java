package com.winsth.android.libs.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import java.util.List;

public class NetworkUtil {
    private NetworkUtil() {
    }

    /**
     * 加入网络状态枚举
     *
     * @author aaron.zhao
     */
    public enum NetworkStatus {
        NO_NETWORK, CONNECTING, CONNECTED
    }

    /**
     * 判断当前网络是否可用
     *
     * @param context 上下文参数
     * @return NO_NETWORK：无网络：CONNECTING：网络正在连接；CONNECTED：网络已连接
     */
    public static NetworkStatus isAvailable(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (mConnectivityManager != null) {
            NetworkInfo wifiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifiNetworkInfo != null && mobileNetworkInfo != null) {
                State wifi = wifiNetworkInfo.getState();
                State mobile = mobileNetworkInfo.getState();
                if (mobile == State.CONNECTING || wifi == State.CONNECTING) {
                    return NetworkStatus.CONNECTING;
                }
                if (mobile == State.CONNECTED || wifi == State.CONNECTED) {
                    return NetworkStatus.CONNECTED;
                }
            }

            if (wifiNetworkInfo != null && mobileNetworkInfo == null) {
                State wifi = wifiNetworkInfo.getState();
                if (wifi == State.CONNECTING) {
                    return NetworkStatus.CONNECTING;
                }
                if (wifi == State.CONNECTED) {
                    return NetworkStatus.CONNECTED;
                }
            }

            if (wifiNetworkInfo == null && mobileNetworkInfo != null) {
                State mobile = mobileNetworkInfo.getState();
                if (mobile == State.CONNECTING) {
                    return NetworkStatus.CONNECTING;
                }
                if (mobile == State.CONNECTED) {
                    return NetworkStatus.CONNECTED;
                }
            }
        }

        return NetworkStatus.NO_NETWORK;
    }

    /**
     * 判断当前网络是否可用
     *
     * @return 0-No network;1-Network is connecting;2-Network has connected
     */
    public static int isNetworkAvailable(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (mConnectivityManager != null) {
            State mMobile = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            State mWifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if (mMobile == State.CONNECTING || mWifi == State.CONNECTING) {
                return 1;
            }
            if (mMobile == State.CONNECTED || mWifi == State.CONNECTED) {
                return 2;
            }
        }

        return 0;
    }

    /**
     * 判断GPS是否可用
     *
     * @param context 上下文
     * @return 可用：true;不可用：false
     */
    public static boolean isGPSAvailable(Context context) {
        boolean result = false;

        LocationManager mLocationManager = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
        List<String> mProviders = mLocationManager.getProviders(true);
        if (mProviders != null && mProviders.size() > 0) {
            result = true;
        }

        return result;
    }

    /**
     * 根据状态值判断网络是否可用
     *
     * @param context 上下文
     * @return 可用：true;不可用：false
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (mConnectivityManager != null) {
            State mMobile = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            State mWifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if (mMobile == State.CONNECTING || mWifi == State.CONNECTING) {
                return false;
            }
            if (mMobile == State.CONNECTED || mWifi == State.CONNECTED) {
                return true;
            }
        }

        return false;
    }
}
