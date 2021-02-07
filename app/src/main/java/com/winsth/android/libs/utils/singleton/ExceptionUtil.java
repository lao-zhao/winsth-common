package com.winsth.android.libs.utils.singleton;

import android.content.Context;
import android.os.Looper;

import com.winsth.android.libs.utils.CommonUtil;
import com.winsth.android.libs.utils.DialogUtil;
import com.winsth.android.libs.utils.LogUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by aaron.zhao on 2016/4/5.
 */
public class ExceptionUtil implements Thread.UncaughtExceptionHandler {
    private Context mContext = null;
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    private static ExceptionUtil mInstance;
    private boolean isLogSave;

    private ExceptionUtil() {
    }

    public static ExceptionUtil getInstance() {
        if (mInstance == null) {
            synchronized (ExceptionUtil.class) {
                if (mInstance == null) {
                    mInstance = new ExceptionUtil();
                }
            }
        }

        return mInstance;
    }

    public void uncaughtException(Thread thread, Throwable throwable) {
        if (!handleException(throwable) && mDefaultUncaughtExceptionHandler != null) {
            mDefaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "InterruptedException:" + e.getMessage(),
                        isLogSave);
            }
        }
    }

    /**
     * 捕获全局异常
     *
     * @param context 上下文
     */
    public void init(Context context, boolean isLogSave) {
        this.mContext = context;
        this.isLogSave = isLogSave;

        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private boolean handleException(Throwable throwable) {
        if (throwable == null)
            return true;

        String msg = throwable.getLocalizedMessage();
        if (msg == null)
            return false;

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                DialogUtil.showToast(mContext, "App has an unknown error, please contact the supplier.");
                Looper.loop();
            }
        }.start();

        LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), getErrorInfo(throwable), isLogSave);

        return true;
    }

    private String getErrorInfo(Throwable throwable) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        printWriter.close();

        String errorInfo = writer.toString();

        return errorInfo;
    }
}
