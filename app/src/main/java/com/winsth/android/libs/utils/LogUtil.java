package com.winsth.android.libs.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class LogUtil {
    private static final String CUR_CLASS_NAME = "LogUtil";
    private static final String LOG_ROOT = "/" + ConfigUtil.Folder.FOLDER_LOG + "/";

    private LogUtil() {
    }

    /**
     * 导出日志
     *
     * @param className  类名称
     * @param methodName 方法名称
     * @param message    错误消息
     * @param isLogSave  是否保存到文件
     */
    public static void exportLog(String className, String methodName, String message, boolean isLogSave) {
        if (isLogSave)
            writeLog(className, methodName, message);
        else
            printLog(className, methodName, message);
    }

    // *********************************************
    // Private method
    // *********************************************
    private static void printLog(String className, String methodName, String message) {
        try {
            String strTag = className + "-->" + methodName;
            Log.e(strTag, message);
        } catch (Exception e) {
            Log.e(CUR_CLASS_NAME + "-->printLog-->", "Exception:" + e.getMessage());
        }
    }

    private static void writeLog(String className, String methodName, String message) {
        try {
            File filePath = null;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                filePath = new File(Environment.getExternalStorageDirectory().getCanonicalPath() + LOG_ROOT);
                writeFile(filePath, className, methodName, message);
            }
        } catch (IOException e) {
            Log.e(CUR_CLASS_NAME + "-->writeLog-->", "IOException:" + e.getMessage());
        } catch (Exception e) {
            Log.e(CUR_CLASS_NAME + "-->writeLog-->", "Exception:" + e.getMessage());
        }
    }

    private static void writeFile(File filePath, String className, String methodName, String message) {
        try {
            if (!filePath.exists()) {
                filePath.mkdirs();
            }

            String strFileName = DateTimeUtil.formatDateTime("yyyy-MM-dd", new Date()) + ".txt";
            File file = new File(filePath, strFileName);
            FileWriter fileWriter = new FileWriter(file.getAbsolutePath(), file.exists());

            String strMsg = getMessage(className, methodName, message);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(strMsg);
            bufferedWriter.close();
        } catch (IOException e) {
            Log.e(CUR_CLASS_NAME + "-->writeFile-->", "IOException:" + e.getMessage());
        } catch (Exception e) {
            Log.e(CUR_CLASS_NAME + "-->writeFile-->", "Exception:" + e.getMessage());
        }
    }

    private static String getMessage(String className, String methodName, String message) {
        String strMessage = "";

        String strTime = DateTimeUtil.formatDateTime("yyyy-MM-dd HH:mm:ss", new Date());
        StringBuffer builder = new StringBuffer();
        builder.append("TRACE__TIME:" + strTime + "\n");
        builder.append("CLASS__NAME:" + className + "\n");
        builder.append("METHOD_NAME:" + methodName + "\n");
        builder.append("TRACE__INFO:" + message + "\n\r");
        strMessage = builder.toString();

        return strMessage;
    }
}
