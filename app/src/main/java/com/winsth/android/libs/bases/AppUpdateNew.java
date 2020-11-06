package com.winsth.android.libs.bases;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.winsth.android.libs.R;
import com.winsth.android.libs.utils.CommonUtil;
import com.winsth.android.libs.utils.ConfigUtil;
import com.winsth.android.libs.utils.DialogUtil;
import com.winsth.android.libs.utils.HttpUtil;
import com.winsth.android.libs.utils.JSONUtil;
import com.winsth.android.libs.utils.LogUtil;
import com.winsth.android.libs.utils.singleton.AppBaseUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by aaron.zhao on 2016/4/5.
 */
public class AppUpdateNew {
    private Context mContext;

    private String checkVerURL = "";
    private String dialogTitle = "";
    private String savePath = "";
    private String saveFileName = "";

    private ProgressBar progressBar;
    private int progress = 0;

    private Thread checkThread;
    private Thread downloadThread;

    private String returnCode = "", returnMsg = "", downloadURL = "";
    private String downloadVerURL = "";

    private boolean interceptFlag = false;
    private boolean isNOUpdateDisplay = false;

    private AlertDialog alertDialog;
    private Map<String, String> params;

    private static final int DOWNLOAD_UPDATE = 1;
    private static final int DOWNLOAD_OVER = 2;
    private static final int NEW_UPDATE = 3;
    private static final int NO_UPDATE = 4;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_UPDATE:
                    progressBar.setProgress(progress);
                    break;
                case DOWNLOAD_OVER:
                    hideDialog();
                    installAPK();
                    break;
                case NEW_UPDATE:
                    String updateMsg = (String) msg.obj;
                    showUpdateDialog(updateMsg);
                    break;
                case NO_UPDATE:
                    if (isNOUpdateDisplay)
                        DialogUtil.showToast(mContext, mContext.getResources().getString(R.string.update_no));
                    break;
            }
            return false;
        }
    });

    public AppUpdateNew(Context context, String chkVerURL, Map<String, String> params, String savePath, String saveFileName) {
        this.mContext = context;
        this.checkVerURL = chkVerURL;
        this.params = params;
        this.savePath = savePath;
        this.saveFileName = saveFileName;

        dialogTitle = context.getResources().getString(R.string.update_title);
    }

    public void checkUpdateInfo(boolean isNOUpdateDisplay, String returnCodeKey, String returnMsgKey, String downloadURLKey) {
        this.returnCode = returnCodeKey;
        this.returnMsg = returnMsgKey;
        this.downloadURL = downloadURLKey;
        this.isNOUpdateDisplay = isNOUpdateDisplay;

        checkThread = new Thread(checkVerRunnable);
        checkThread.start();
    }

    private void showUpdateDialog(String updateMsg) {
        String downloadText = mContext.getResources().getString(R.string.button_download);
        String ignoreText = mContext.getResources().getString(R.string.button_ignore);

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.dialog_update_info, null);
        TextView tvMsg = (TextView) v.findViewById(R.id.tvMsg);
        tvMsg.setText(updateMsg);

        showAlertDialog(mContext, dialogTitle, v, downloadText, ignoreText, prepareDownloadListener, cancelUpdateListener);
    }

    private void showDownloadDialog() {
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.dialog_update_progress_bar, null);
        progressBar = (ProgressBar) v.findViewById(R.id.progress);
        String cancelText = mContext.getResources().getString(R.string.button_cancel);
        showAlertDialog(mContext, dialogTitle, v, null, cancelText, null, cancelDownloadListener);

        downloadAPK();
    }

    private void showAlertDialog(Context context, String title, View v, String positiveText, String negativeText, DialogInterface.OnClickListener
            positiveClickListener, DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(dialogTitle);
        builder.setIcon(R.mipmap.ic_warn);
        builder.setView(v);
        if (positiveText != null && !positiveText.equalsIgnoreCase(""))
            builder.setPositiveButton(positiveText, positiveClickListener);
        if (negativeText != null && !negativeText.equalsIgnoreCase(""))
            builder.setNegativeButton(negativeText, negativeClickListener);

        alertDialog = builder.create();
        alertDialog.show();
    }

    private void hideDialog() {
        if (alertDialog != null)
            alertDialog.dismiss();
    }

    private void downloadAPK() {
        downloadThread = new Thread(downloadVerRunnable);
        downloadThread.start();
    }

    private void installAPK() {
        File apkFile = new File(savePath, saveFileName);
        if (!apkFile.exists()) {
            return;
        }

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(AppBaseUtil.getInstance().getUriOfFile(mContext, apkFile), "application/vnd.android.package-archive");
//        mContext.startActivity(intent);
        if (mContext.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            mContext.startActivity(intent);
        }

    }

    private Runnable checkVerRunnable = new Runnable() {
        @Override
        public void run() {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                    nameValuePairList.add(nameValuePair);
                }
            }
            String result = HttpUtil.getStringDataByPost(checkVerURL, nameValuePairList, ConfigUtil.TextCode.UTF_8);
            if (result != null && !result.equalsIgnoreCase("") && result.contains("{")) {
                JSONObject jsonObject = JSONUtil.getJSONObject(result);
                String rc = JSONUtil.getString(jsonObject, returnCode);
                String rm = JSONUtil.getString(jsonObject, returnMsg);

                if (!rc.equals("0")) {
                    handler.sendMessage(Message.obtain(handler, NO_UPDATE, rm));
                } else {
                    downloadVerURL = JSONUtil.getString(jsonObject, downloadURL);
                    handler.sendMessage(Message.obtain(handler, NEW_UPDATE, rm));
                }
            } else {
                handler.sendMessage(Message.obtain(handler, NO_UPDATE, mContext.getResources().getString(R.string.network_disconnect)));
            }
        }
    };

    private Runnable downloadVerRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(downloadVerURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();

                int length = con.getContentLength();
                InputStream inputStram = con.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }

                File ApkFile = new File(savePath, saveFileName);
                FileOutputStream fileOutputStream = new FileOutputStream(ApkFile);
                int count = 0;

                byte[] buffer = new byte[1024];

                do {
                    int numread = inputStram.read(buffer);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    handler.sendMessage(Message.obtain(handler, DOWNLOAD_UPDATE, progress));
                    if (numread <= 0) {
                        handler.sendMessage(Message.obtain(handler, DOWNLOAD_OVER));
                        break;
                    }
                    fileOutputStream.write(buffer, 0, numread);
                } while (!interceptFlag);

                fileOutputStream.close();
                inputStram.close();
                con.disconnect();
            } catch (MalformedURLException e) {
                LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), ":MalformedURLException" + e.getMessage(),
                        true);
            } catch (IOException e) {
                LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), ":IOException" + e.getMessage(), true);
            }
        }
    };

    private DialogInterface.OnClickListener cancelUpdateListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    private DialogInterface.OnClickListener cancelDownloadListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            interceptFlag = true;
        }
    };

    private DialogInterface.OnClickListener prepareDownloadListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            // 显示下载框
            showDownloadDialog();
        }
    };
}
