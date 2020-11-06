package com.winsth.android.libs.assists;

import android.os.AsyncTask;


import com.winsth.android.libs.utils.ConfigUtil;
import com.winsth.android.libs.utils.FileUtil;
import com.winsth.android.libs.utils.StringUtil;
import com.winsth.android.libs.utils.singleton.OKHttpUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AsyncTaskExecutor extends AsyncTask<Object, Object, Object> {
    private Map<String, String> mParams;

    /* 构造函数 */
    public  AsyncTaskExecutor() {
        mParams = new HashMap<String, String>();
    }

    /* 抽象函数 */
    protected abstract void onPreExecute();

    protected abstract Object doInBackground(Object... args);

    protected abstract void onProgressUpdate(Object... values);

    protected abstract void onPostExecute(Object result);

    /**
     * 获取访问链接
     *
     * @param baseUrl   基础URL
     * @param targetUrl 目标URL
     * @return 完整的访问链接
     */
    public String getFullUrl(String baseUrl, String targetUrl) {
        return baseUrl + targetUrl;
    }

    /**
     * 追加访问链接参数
     *
     * @param key   参数
     * @param value 参数值
     */
    public void addParam(String key, String value) {
        if (mParams == null) {
            mParams = new HashMap<String, String>();
        }

        mParams.put(key, value);
    }

    /**
     * 移除已经添加的参数
     */
    public void removeAllParams() {
        if (mParams != null && !mParams.isEmpty()) {
            mParams.clear();
        }
    }

    /**
     * 获取访问后台接口所需要的参数
     *
     * @return 后台接口所需要的参数
     */
    public Map<String, String> getParams() {
        return mParams;
    }

    /**
     * 根据访问链接和参数访问后台
     *
     * @param fullUrl 访问链接
     * @param params  参数
     * @return 请求到字符串
     */
    public String execReq(String fullUrl, Map<String, String> params) {
        return execReq(fullUrl, params, null);
    }

    /**
     * 根据访问链接和参数访问后台
     *
     * @param fullUrl 访问链接
     * @param params  参数
     * @param charset 解析返回结果的字符集
     * @return 请求到字符串
     */
    public String execReq(String fullUrl, Map<String, String> params, String charset) {
        if (!StringUtil.isNullOrEmpty(charset)) {
            return OKHttpUtil.getInstance().requestStringByPostForm(fullUrl, params, charset);
        } else {
            return OKHttpUtil.getInstance().requestStringByPostForm(fullUrl, params, ConfigUtil.TextCode.UTF_8);
        }
    }

    /**
     * 执行文件上传
     *
     * @param fullUrl      上传链接
     * @param params       参数
     * @param formFileList 文件列表
     * @return 请求到的上传结果
     */
    public String execPostFiles(String fullUrl, Map<String, String> params, List<OKHttpUtil.FormFile> formFileList) {
        return OKHttpUtil.getInstance().uploadFile(fullUrl, params, formFileList, "");
    }

    /**
     * 执行文件内容下载
     *
     * @param fullUrl     下载链接
     * @param params      参数集合
     * @param destFileDir 目标目标
     * @param pwd         文件密码
     * @return 请求到的文件 结果
     */
    public String execDownloadFile(String fullUrl, Map<String, String> params, String destFileDir, String pwd) {
        return OKHttpUtil.getInstance().downloadFile(fullUrl, params, destFileDir, pwd);
    }

    /**
     * 日志 详情获取
     *
     * @param fullUrl 下载链接
     * @param params  参数集合
     * @return 请求到的文件 内容
     */
    public String execDownloadFileAndRead(String fullUrl, Map<String, String> params) {
        return OKHttpUtil.getInstance().downloadFileRead(fullUrl, params);
    }

    /**
     * 执行文件内容下载
     *
     * @param fullUrl     下载链接
     * @param destFileDir 目标目标
     * @param fileName    文件名称
     * @return 请求到的文件内容
     */
    public String execDownloadFile(String fullUrl, String destFileDir, String fileName) {
        return OKHttpUtil.getInstance().downloadFile(fullUrl, destFileDir, fileName);//GET
    }

    /**
     * 日志详情下载
     * 执行Txt文件内容下载
     *
     * @param filePath 文件路径
     * @param charset  字符编码
     * @return 返回Txt文件内容
     */
    public String execDownloadFileContent(String filePath, String charset) {
        if (!StringUtil.isNullOrEmpty(charset)) {
            return FileUtil.downloadFileContent(filePath, charset);
        } else {
            return FileUtil.downloadFileContent(filePath, ConfigUtil.TextCode.UTF_8);
        }
    }
}
