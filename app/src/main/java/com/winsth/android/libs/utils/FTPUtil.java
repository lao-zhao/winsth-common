package com.winsth.android.libs.utils;

import android.content.Context;

import com.winsth.android.libs.R;
import com.winsth.android.libs.assists.ProgressInputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

public class FTPUtil {
    private Context mContext;

    private String mHostName;
    private int mServerPort;
    private String mUserName;
    private String mPassword;

    private FTPClient mFTPClient;

    public FTPUtil(Context context, String hostName, int serverPort, String userName, String password) {
        this.mContext = context;
        this.mHostName = hostName;
        this.mServerPort = serverPort;
        this.mUserName = userName;
        this.mPassword = password;

        if (mFTPClient == null) {
            this.mFTPClient = new FTPClient();
        }
    }

    /* 文件上传方法 */

    /**
     * 上传单个文件.
     *
     * @param singleFile 本地文件
     * @param remotePath FTP目录
     * @param listener   监听器
     * @throws IOException
     */
    public void uploadSingleFile(File singleFile, String remotePath, UploadProgressListener listener) throws IOException {
        // 上传之前初始化
        this.uploadBeforeOperate(remotePath, listener);

        boolean flag;
        flag = uploadingSingle(singleFile, listener);
        if (flag) {
            listener.onUploadProgress(mContext.getResources().getString(R.string.ftp_upload_success), 0, singleFile);
        } else {
            listener.onUploadProgress(mContext.getResources().getString(R.string.ftp_upload_fail), 0, singleFile);
        }

        // 上传完成之后关闭连接
        this.uploadAfterOperate(listener);
    }

    /**
     * 上传多个文件.
     *
     * @param fileList   本地文件列表
     * @param remotePath FTP目录
     * @param listener   监听器
     * @throws IOException
     */
    public void uploadMultiFile(LinkedList<File> fileList, String remotePath, UploadProgressListener listener) throws IOException {
        // 上传之前初始化
        this.uploadBeforeOperate(remotePath, listener);

        boolean flag;

        for (File singleFile : fileList) {
            flag = uploadingSingle(singleFile, listener);
            if (flag) {
                listener.onUploadProgress(mContext.getResources().getString(R.string.ftp_upload_success), 0, singleFile);
            } else {
                listener.onUploadProgress(mContext.getResources().getString(R.string.ftp_upload_fail), 0, singleFile);
            }
        }

        // 上传完成之后关闭连接
        this.uploadAfterOperate(listener);
    }

    /**
     * 上传单个文件.
     *
     * @param localFile 本地文件
     * @return true上传成功, false上传失败
     * @throws IOException
     */
    private boolean uploadingSingle(File localFile, UploadProgressListener listener) throws IOException {
        boolean flag = true;
        // 不带进度的方式
        // // 创建输入流
        // InputStream inputStream = new FileInputStream(localFile);
        // // 上传单个文件
        // flag = ftpClient.storeFile(localFile.getName(), inputStream);
        // // 关闭文件流
        // inputStream.close();

        // 带有进度的方式
        BufferedInputStream buffIn = new BufferedInputStream(new FileInputStream(localFile));
        ProgressInputStream progressInput = new ProgressInputStream(mContext, buffIn, listener, localFile);
        flag = mFTPClient.storeFile(localFile.getName(), progressInput);
        buffIn.close();

        return flag;
    }

    /**
     * 上传文件之前初始化相关参数
     *
     * @param remotePath FTP目录
     * @param listener   监听器
     * @throws IOException
     */
    private void uploadBeforeOperate(String remotePath, UploadProgressListener listener) throws IOException {
        // 打开FTP服务
        try {
            this.openConnect();
            listener.onUploadProgress(mContext.getResources().getString(R.string.ftp_connect_success), 0, null);
        } catch (IOException e) {
            e.printStackTrace();
            listener.onUploadProgress(mContext.getResources().getString(R.string.ftp_connect_fail), 0, null);
            return;
        }

        // 设置模式
        mFTPClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.STREAM_TRANSFER_MODE);
        // FTP下创建文件夹
        mFTPClient.makeDirectory(remotePath);
        // 改变FTP目录
        mFTPClient.changeWorkingDirectory(remotePath);
        // 上传单个文件
    }

    /**
     * 上传完成之后关闭连接
     *
     * @param listener
     * @throws IOException
     */
    private void uploadAfterOperate(UploadProgressListener listener) throws IOException {
        this.closeConnect();
        listener.onUploadProgress(mContext.getResources().getString(R.string.ftp_disconnect_success), 0, null);
    }

    /* 文件下载方法 */

    /**
     * 下载单个文件，可实现断点下载.
     *
     * @param serverPath Ftp目录及文件路径
     * @param localPath  本地目录
     * @param fileName   下载之后的文件名称
     * @param listener   监听器
     * @throws IOException
     */
    public void downloadSingleFile(String serverPath, String localPath, String fileName, DownLoadProgressListener listener) throws Exception {
        // 打开FTP服务
        try {
            this.openConnect();
            listener.onDownLoadProgress(mContext.getResources().getString(R.string.ftp_connect_success), 0, null);
        } catch (IOException e) {
            e.printStackTrace();
            listener.onDownLoadProgress(mContext.getResources().getString(R.string.ftp_connect_fail), 0, null);
            return;
        }

        // 先判断服务器文件是否存在
        FTPFile[] files = mFTPClient.listFiles(serverPath);
        if (files.length == 0) {
            listener.onDownLoadProgress(mContext.getResources().getString(R.string.ftp_file_not_exists), 0, null);
            return;
        }

        // 创建本地文件夹
        File mkFile = new File(localPath);
        if (!mkFile.exists()) {
            mkFile.mkdirs();
        }

        localPath = localPath + fileName;
        // 接着判断下载的文件是否能断点下载
        long serverSize = files[0].getSize(); // 获取远程文件的长度
        File localFile = new File(localPath);
        long localSize = 0;
        if (localFile.exists()) {
            localSize = localFile.length(); // 如果本地文件存在，获取本地文件的长度
            if (localSize >= serverSize) {
                File file = new File(localPath);
                file.delete();
            }
        }

        // 进度
        long step = serverSize / 100;
        long process = 0;
        long currentSize = 0;
        // 开始准备下载文件
        OutputStream out = new FileOutputStream(localFile, true);
        mFTPClient.setRestartOffset(localSize);
        InputStream input = mFTPClient.retrieveFileStream(serverPath);
        byte[] b = new byte[1024];
        int length = 0;
        while ((length = input.read(b)) != -1) {
            out.write(b, 0, length);
            currentSize = currentSize + length;
            if (currentSize / step != process) {
                process = currentSize / step;
                if (process % 5 == 0) { // 每隔%5的进度返回一次
                    listener.onDownLoadProgress(mContext.getResources().getString(R.string.ftp_down_loading), process, null);
                }
            }
        }
        out.flush();
        out.close();
        input.close();

        // 此方法是来确保流处理完毕，如果没有此方法，可能会造成现程序死掉
        if (mFTPClient.completePendingCommand()) {
            listener.onDownLoadProgress(mContext.getResources().getString(R.string.ftp_down_success), 0, new File(localPath));
        } else {
            listener.onDownLoadProgress(mContext.getResources().getString(R.string.ftp_down_fail), 0, null);
        }

        // 下载完成之后关闭连接
        this.closeConnect();
        listener.onDownLoadProgress(mContext.getResources().getString(R.string.ftp_disconnect_success), 0, null);

        return;
    }

    // -------------------------------------------------------文件删除方法------------------------------------------------

    /**
     * 删除Ftp下的文件.
     *
     * @param serverPath Ftp目录及文件路径
     * @param listener   监听器
     * @throws IOException
     */
    public void deleteSingleFile(String serverPath, DeleteFileProgressListener listener) throws Exception {

        // 打开FTP服务
        try {
            this.openConnect();
            listener.onDeleteProgress(mContext.getResources().getString(R.string.ftp_connect_success));
        } catch (IOException e) {
            e.printStackTrace();
            listener.onDeleteProgress(mContext.getResources().getString(R.string.ftp_connect_fail));
            return;
        }

        // 先判断服务器文件是否存在
        FTPFile[] files = mFTPClient.listFiles(serverPath);
        if (files.length == 0) {
            listener.onDeleteProgress(mContext.getResources().getString(R.string.ftp_file_not_exists));
            return;
        }

        // 进行删除操作
        boolean flag = true;
        flag = mFTPClient.deleteFile(serverPath);
        if (flag) {
            listener.onDeleteProgress(mContext.getResources().getString(R.string.ftp_delete_file_success));
        } else {
            listener.onDeleteProgress(mContext.getResources().getString(R.string.ftp_delete_file_fail));
        }

        // 删除完成之后关闭连接
        this.closeConnect();
        listener.onDeleteProgress(mContext.getResources().getString(R.string.ftp_disconnect_success));

        return;
    }

    // -------------------------------------------------------打开关闭连接------------------------------------------------

    /**
     * 打开FTP服务.
     *
     * @throws IOException
     */
    public void openConnect() throws IOException {
        // 中文转码
        mFTPClient.setControlEncoding("UTF-8");
        int reply; // 服务器响应值
        // 连接至服务器
        mFTPClient.connect(mHostName, mServerPort);
        // 获取响应值
        reply = mFTPClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            // 断开连接
            mFTPClient.disconnect();
            throw new IOException("connect fail: " + reply);
        }
        // 登录到服务器
        mFTPClient.login(mUserName, mPassword);
        // 获取响应值
        reply = mFTPClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            // 断开连接
            mFTPClient.disconnect();
            throw new IOException("connect fail: " + reply);
        } else {
            // 获取登录信息
            FTPClientConfig config = new FTPClientConfig(mFTPClient.getSystemType().split(" ")[0]);
            config.setServerLanguageCode("zh");
            mFTPClient.configure(config);
            // 使用被动模式设为默认
            mFTPClient.enterLocalPassiveMode();
            // 二进制文件支持
            mFTPClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
        }
    }

    /**
     * 关闭FTP服务.
     *
     * @throws IOException
     */
    public void closeConnect() throws IOException {
        if (mFTPClient != null) {
            // 退出FTP
            mFTPClient.logout();
            // 断开连接
            mFTPClient.disconnect();
        }
    }

    /* 上传、下载、删除监听 */
    /* 上传进度监听 */
    public interface UploadProgressListener {
        void onUploadProgress(String currentStep, long uploadSize, File file);
    }

    /* 下载进度监听 */
    public interface DownLoadProgressListener {
        void onDownLoadProgress(String currentStep, long downProcess, File file);
    }

    /* 文件删除监听 */
    public interface DeleteFileProgressListener {
        void onDeleteProgress(String currentStep);
    }
}
