package com.winsth.android.libs.assists;

import android.content.Context;


import com.winsth.android.libs.utils.FTPUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import com.winsth.android.libs.R;

public class ProgressInputStream extends InputStream {
    private static final int TEN_KILOBYTES = 1024 * 10; // 每上传10K返回一次

    private Context mContext;
    private InputStream mInputStream;

    private long mProgress;
    private long mLastUpdate;

    private boolean mClosed;

    private FTPUtil.UploadProgressListener mListener;
    private File mLocalFile;

    public ProgressInputStream(Context context, InputStream inputStream, FTPUtil.UploadProgressListener listener, File localFile) {
        this.mContext = context;
        this.mInputStream = inputStream;
        this.mProgress = 0;
        this.mLastUpdate = 0;
        this.mListener = listener;
        this.mLocalFile = localFile;

        this.mClosed = false;
    }

    @Override
    public int read() throws IOException {
        int count = mInputStream.read();
        return incrementCounterAndUpdateDisplay(count);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int count = mInputStream.read(b, off, len);
        return incrementCounterAndUpdateDisplay(count);
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (mClosed)
            throw new IOException("already closed");
        mClosed = true;
    }

    private int incrementCounterAndUpdateDisplay(int count) {
        if (count > 0)
            mProgress += count;
        mLastUpdate = maybeUpdateDisplay(mProgress, mLastUpdate);
        return count;
    }

    private long maybeUpdateDisplay(long progress, long lastUpdate) {
        if (progress - lastUpdate > TEN_KILOBYTES) {
            lastUpdate = progress;
            this.mListener.onUploadProgress(mContext.getResources().getString(R.string.ftp_upload_loading), progress, this.mLocalFile);
        }
        return lastUpdate;
    }
}
