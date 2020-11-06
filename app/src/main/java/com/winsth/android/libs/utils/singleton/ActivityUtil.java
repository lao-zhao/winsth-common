package com.winsth.android.libs.utils.singleton;

import android.app.Activity;

import com.winsth.android.libs.utils.CommonUtil;
import com.winsth.android.libs.utils.LogUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by aaron.zhao on 2016/4/5.
 */
public class ActivityUtil {
    private static ActivityUtil mInstance = null;
    private List<Activity> activityList = new LinkedList<Activity>();

    private ActivityUtil() {
    }

    public synchronized static ActivityUtil getInstance() {
        if (mInstance == null) {
            synchronized (ActivityUtil.class) {
                if (mInstance == null) {
                    mInstance = new ActivityUtil();
                }
            }
        }

        return mInstance;
    }

    public void addActivity(Activity activity) {
        if (!activityList.contains(activity))
            activityList.add(activity);
    }

    public void quit() {
        try {
            for (Activity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        } finally {
            System.exit(0);
        }
    }

    public void PopToActivity(Class<?> cla) {
        try {
            for (Activity activity1 : activityList) {
                String name = activity1.getClass().toString();
                if (activity1.getClass().equals(cla)) {
                    activity1.finish();
                }
            }
        } catch (Exception e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }
    }
}
