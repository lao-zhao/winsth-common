package com.winsth.android.libs.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.widget.TextView;

public class CommonUtil {
	private CommonUtil() {}

	/**
	 * 获取类名称
	 * @param e 异常类对象(eg:new Exception())
	 * @return 返回类名称
	 */
	public static String getCName(Exception e) {
		return e.getStackTrace()[0].getClassName();
	}

	/**
	 * 获取类中方法的名称
	 * @param e 异常类对象(eg:new Exception())
	 * @return 返回类中方法的名称
	 */
	public static String getMName(Exception e) {
		return e.getStackTrace()[0].getMethodName();
	}

	/**
	 * 获取IMEI
	 * @param context 上下文环境
	 * @return 返回IMEI
	 */
	@SuppressLint("MissingPermission")
	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//		String imei = tm.getDeviceId();
//		return imei;
		if (tm != null && tm.getDeviceId() != null && !tm.getDeviceId().equals("")) {
			String imei = tm.getDeviceId();
			return imei;
		} else {
			WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			String address = wifiManager.getConnectionInfo().getMacAddress().replace(":", "");
			return address;
		}
	}

	/**
	 * 拨打电话
	 * @param activity 当前Activity
	 * @param phoneNO 电话号码
	 */
	public static void dial(Activity activity, String phoneNO) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + phoneNO));
		activity.startActivity(intent);
	}

	/**
	 * 发送短信
	 * @param activity 当前Activity
	 * @param to 短信接收者
	 * @param content 短信内容
	 */

	public static void sendSMS(Activity activity, String to, String content) {
		Uri smsToUri = Uri.parse("smsto:" + to);
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", content);
		activity.startActivity(intent);
	}

	/**
	 * 打开网址通过浏览器
	 * @param context 上下文
	 * @param url 网址
	 */
	public static void openURLByBrowser(Context context, String url) {
		if (!url.trim().equalsIgnoreCase("") && url != null) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			context.startActivity(intent);
		}
	}

	/**
	 * 设置TextView的字体加粗
	 * @param textView TextView组件
	 * @param text 要显示的文字
	 */
	public static void setBoldTextForTextView(TextView textView, String text) {
		TextPaint textPaint = textView.getPaint();
		textPaint.setFakeBoldText(true);
		textView.setText(text);
	}
}
