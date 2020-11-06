package com.winsth.android.libs.utils.singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorUtil {
    private ExecutorService mExecutorServiceInstance;

    private ExecutorUtil() {
        if (mExecutorServiceInstance == null) {
            mExecutorServiceInstance = Executors.newFixedThreadPool(6);
        }
    }

    private static ExecutorUtil mInstance;

    public static ExecutorUtil getInstance() {
        if (mInstance == null) {
            synchronized (ExecutorUtil.class) {
                if (mInstance == null) {
                    mInstance = new ExecutorUtil();
                }
            }
        }

        return mInstance;
    }

    public void execute(Runnable runnable) {
        mExecutorServiceInstance.execute(runnable);
    }
}
