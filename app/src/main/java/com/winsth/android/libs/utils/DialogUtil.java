package com.winsth.android.libs.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.winsth.android.libs.R;

import java.util.Calendar;
import java.util.Date;

public class DialogUtil {
    private static Toast mToast;
    private static ProgressDialog mProgressDialog;

    private DialogUtil() {
    }

    /**
     * 显示Toast信息
     *
     * @param context 上下文
     * @param msg     要显示的信息
     */
    public static void showToast(Context context, String msg) {
        if (mToast == null) {
            if (msg == null)
                msg = "";

            if (msg.length() < 8)
                mToast = Toast.makeText(context, "default prompt message", Toast.LENGTH_SHORT);
            else
                mToast = Toast.makeText(context, "default prompt message", Toast.LENGTH_LONG);
        }

        mToast.setText(msg);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    /**
     * 显示弹出框
     *
     * @param context               上下文
     * @param title                 弹出框标题
     * @param msg                   弹出框信息
     * @param positiveText          弹出框确认按钮文本
     * @param negativeText          弹出框取消按钮文本
     * @param positiveClickListener 弹出框确认按钮事件监听
     * @param negativeClickListener 弹出框取消按钮事件监听（如果该事件传值为null，那么不显示取消按钮）
     * @return 返回弹出框
     */
    public static AlertDialog showAlertDialog(Context context, String title, String msg, String positiveText, String negativeText, DialogInterface
            .OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveText, positiveClickListener);
        if (negativeClickListener != null)
            builder.setNegativeButton(negativeText, negativeClickListener);
        builder.setCancelable(false);

        AlertDialog alertDialog = builder.show();
        return alertDialog;
    }

    /**
     * 显示弹出框
     *
     * @param context               上下文
     * @param title                 弹出框标题
     * @param msg                   弹出框信息
     * @param positiveText          弹出框确认按钮文本资源
     * @param positiveClickListener 弹出框确认按钮事件监听
     * @param cancelClickListener   基于手机的取消事件监听
     * @return 返回弹出框
     */
    public static AlertDialog showAlertDialog(Context context, String title, String msg, String positiveText, DialogInterface.OnClickListener
            positiveClickListener, DialogInterface.OnCancelListener cancelClickListener) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveText, positiveClickListener);
        builder.setOnCancelListener(cancelClickListener);

        AlertDialog alertDialog = builder.show();
        return alertDialog;
    }

    /**
     * 显示弹出框
     *
     * @param context               上下文
     * @param title                 弹出框标题
     * @param msg                   弹出框信息
     * @param positiveText          弹出框确认按钮文本资源
     * @param neutralText           弹出框普通按钮文本资源
     * @param negativeText          弹出框取消按钮文本资源
     * @param positiveClickListener 弹出框确认按钮事件监听
     * @param neutralClickListener  基于普通按钮的监听事件
     * @param negativeClickListener 基于手机的取消事件监听
     * @return 返回弹出框
     */
    public static AlertDialog showAlertDialog(Context context, String title, String msg, String positiveText, String neutralText, String negativeText,
                                              DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener neutralClickListener,
                                              DialogInterface.OnClickListener negativeClickListener) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setIcon(R.mipmap.ic_warn);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveText, positiveClickListener);
        builder.setNeutralButton(neutralText, neutralClickListener);
        builder.setNegativeButton(negativeText, negativeClickListener);

        AlertDialog alertDialog = builder.show();
        return alertDialog;
    }

    /**
     * 显示弹出框
     *
     * @param context               上下文
     * @param title                 弹出框标题
     * @param v                     弹出框中所带的View
     * @param negativeText          弹出框取消按钮文本资源
     * @param negativeClickListener 基于手机的取消事件监听
     * @return 返回弹出框
     */
    public static AlertDialog showAlertDialog(Context context, String title, View v, String negativeText, DialogInterface.OnClickListener
            negativeClickListener) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setView(v);
        builder.setNegativeButton(negativeText, negativeClickListener);

        AlertDialog alertDialog = builder.show();
        return alertDialog;
    }

    /**
     * 显示弹出框
     *
     * @param context               上下文
     * @param title                 弹出框标题
     * @param v                     弹出框中所带的View
     * @param positiveText          弹出框指定功能按钮文本资源
     * @param positiveClickListener 基于弹出框中指定按钮的事件监听
     * @param negativeText          弹出框取消按钮文本资源
     * @param negativeClickListener 基于手机的取消事件监听
     * @return 返回弹出框
     */
    public static AlertDialog showAlertDialog(Context context, String title, View v, String positiveText, String negativeText, DialogInterface
            .OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        // builder.setIcon(R.drawable.info30);
        builder.setView(v);
        builder.setPositiveButton(positiveText, positiveClickListener);
        builder.setNegativeButton(negativeText, negativeClickListener);

        AlertDialog alertDialog = builder.show();
        return alertDialog;
    }

    public static AlertDialog showAlertDialog(Context context, View v) {
        Builder builder = new Builder(context);
        builder.setView(v);

        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    /**
     * 不依附与Activity的弹出框
     *
     * @param context               上下文
     * @param title                 弹出框标题
     * @param msg                   弹出框信息
     * @param positiveText          弹出框确认按钮文本
     * @param negativeText          弹出框取消按钮文本
     * @param positiveClickListener 弹出框确认按钮事件监听
     * @param negativeClickListener 弹出框取消按钮事件监听（如果该事件传值为null，那么不显示取消按钮）
     * @return 返回弹出框
     */
    public static AlertDialog showSystemAlertDialog(Context context, String title, String msg, String positiveText, String negativeText, DialogInterface
            .OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveText, positiveClickListener);
        if (negativeClickListener != null)
            builder.setNegativeButton(negativeText, negativeClickListener);
        builder.setCancelable(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
        return alertDialog;
    }

    /**
     * 显示选择日期对话框
     *
     * @param context  上下文
     * @param callBack 回调事件
     * @return 返回显示日期对话框对象
     */
    public static DatePickerDialog showDatePickerDialog(Context context, OnDateSetListener callBack) {
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int monthOfYear = mCalendar.get(Calendar.MONTH);
        int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePickerDialog = new DatePickerDialog(context, callBack, year, monthOfYear, dayOfMonth);
        mDatePickerDialog.show();
        return mDatePickerDialog;
    }

    /**
     * 显示选择日期对话框
     *
     * @param context  上下文
     * @param callBack 回调事件
     * @return 返回显示日期对话框对象
     */
    public static DatePickerDialog showDatePickerDialog(Context context, OnDateSetListener callBack, Date date) {
        Calendar mCalendar = Calendar.getInstance();
        if (date != null)
            mCalendar.setTime(date);
        int year = mCalendar.get(Calendar.YEAR);
        int monthOfYear = mCalendar.get(Calendar.MONTH);
        int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePickerDialog = new DatePickerDialog(context, callBack, year, monthOfYear, dayOfMonth);
        mDatePickerDialog.show();
        return mDatePickerDialog;
    }

    /**
     * 显示选择日期对话框
     *
     * @param context   上下文
     * @param callBack  回调事件
     * @param isShowDay 是否显示日
     * @return 返回显示日期对话框对象
     */
    public static DatePickerDialog showDatePickerDialog(Context context, OnDateSetListener callBack, boolean isShowDay) {
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int monthOfYear = mCalendar.get(Calendar.MONTH);
        int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePickerDialog = new DatePickerDialog(context, callBack, year, monthOfYear, dayOfMonth);
        mDatePickerDialog.show();

        if (!isShowDay) {
            int SDKVersion = DeviceUtil.getSDKVersion();// 获取系统版本
            DatePicker datePicker = findDatePicker((ViewGroup) mDatePickerDialog.getWindow().getDecorView());// 设置弹出年月日
            if (datePicker != null) {
                if (SDKVersion < 11) {
                    ((ViewGroup) datePicker.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                } else if (SDKVersion > 14) {
                    ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                }
            }
        }

        return mDatePickerDialog;
    }

    /**
     * 显示选择年对话框
     *
     * @param context  上下文
     * @param callBack 回调事件
     * @return 返回显示日期对话框对象
     */
    public static DatePickerDialog showYearDatePickerDialog(Context context, OnDateSetListener callBack) {
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int monthOfYear = mCalendar.get(Calendar.MONTH);
        int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePickerDialog = new DatePickerDialog(context, callBack, year, monthOfYear, dayOfMonth);
        mDatePickerDialog.show();

        int SDKVersion = DeviceUtil.getSDKVersion();// 获取系统版本
        DatePicker datePicker = findDatePicker((ViewGroup) mDatePickerDialog.getWindow().getDecorView());// 设置弹出年月日
        if (datePicker != null) {
            if (SDKVersion < 11) {
                ((ViewGroup) datePicker.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                ((ViewGroup) datePicker.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            } else if (SDKVersion > 14) {
                ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            }
        }

        return mDatePickerDialog;
    }

    /**
     * 显示进度条
     *
     * @param context    上下文
     * @param titleResId 进度提示标题资源ID
     * @param msgResId   进度提示内容资源ID
     */
    public static void showProgressDialog(Context context, int titleResId, int msgResId) {
        String strTitle = context.getString(titleResId);
        String strMessage = context.getString(msgResId);

        showProgressDialog(context, strTitle, strMessage);
    }

    /**
     * 显示进度条
     *
     * @param context 上下文
     * @param title   进度提示标题
     * @param msg     进度提示内容
     */
    public static void showProgressDialog(Context context, String title, String msg) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setIcon(R.mipmap.ic_warn);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    /**
     * 取消进度条
     */
    public static void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    // *******************************************
    // 私有方法
    // *******************************************

    /**
     * 从当前Dialog中查找DatePicker子控件
     *
     * @param group 控件组
     * @return 返回DatePicker
     */
    private static DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }
}
