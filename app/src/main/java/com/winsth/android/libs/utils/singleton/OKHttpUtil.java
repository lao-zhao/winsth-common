package com.winsth.android.libs.utils.singleton;

import com.winsth.android.libs.assists.sslcers.MySSLSocketFactory;
import com.winsth.android.libs.assists.sslcers.TrustAllCerts;
import com.winsth.android.libs.assists.sslcers.TrustAllHostnameVerifier;
import com.winsth.android.libs.utils.CommonUtil;
import com.winsth.android.libs.utils.ConfigUtil;
import com.winsth.android.libs.utils.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;

/**
 * Created by aaron.zhao on 2016/4/6.
 */
public class OKHttpUtil {
    private static final String DEFAULT_CODE = ConfigUtil.TextCode.UTF_8;
    private static final int CON_TIMEOUT0 = 1 * 60; // 时间单位：秒
    private static final int CON_TIMEOUT = 5 * 60; // 时间单位：秒

    private OkHttpClient mOkHttpClient;
    private static OKHttpUtil mInstance;
    private int SUCCESS = 1;

    private OKHttpUtil() {
        if (mOkHttpClient == null) {
            Builder builder = new OkHttpClient().newBuilder();
//            builder.connectionSpecs(createConnectionSpec());
            builder.sslSocketFactory(createSSLSocketFactory());
            builder.hostnameVerifier(new TrustAllHostnameVerifier());
            builder.connectTimeout(CON_TIMEOUT0, TimeUnit.SECONDS);
            builder.readTimeout(CON_TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(CON_TIMEOUT, TimeUnit.SECONDS);
            mOkHttpClient = builder.build();
        }
    }

    private static List<ConnectionSpec> createConnectionSpec() {
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_0, TlsVersion.TLS_1_1, TlsVersion.TLS_1_2,
                TlsVersion.SSL_3_0).cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA).build();
        return Collections.singletonList(spec);
    }

    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

//            ssfFactory = sc.getSocketFactory();
            ssfFactory = new MySSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
        return ssfFactory;
    }

    public static OKHttpUtil getInstance() {
        if (mInstance == null) {
            synchronized (OKHttpUtil.class) {
                if (mInstance == null) {
                    mInstance = new OKHttpUtil();
                }
            }
        }

        return mInstance;
    }

    /***********************************************************
     * 外部方法
     **********************************************************/
    public String requestStringByGet(String url, Map<String, String> params, String textCode) {
        String result = "";

        try {
            Response response = requestDataByGet(url, params);//No
            if (response != null) {
                if (textCode == null || textCode.equals("")) {
                    result = new String(response.body().bytes(), DEFAULT_CODE);
                } else {
                    result = new String(response.body().bytes(), textCode);
                }
            }
        } catch (IOException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return result;
    }

    public String requestStringByPost(String url, Map<String, String> params, String textCode, String contentType) {
        String result = "";

        try {
            Response response = requestDataByPost(url, params, contentType);//No
            if (response != null) {
                if (textCode == null || textCode.equals("")) {
                    result = new String(response.body().bytes(), DEFAULT_CODE);
                } else {
                    result = new String(response.body().bytes(), textCode);
                }
            }
        } catch (IOException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return result;
    }

    public String requestStringByPostForm(String url, Map<String, String> params, String textCode) {
        String result = "";

        try {
            Response response = requestDataByPostForm(url, params);
            if (response != null && response.isSuccessful()) {
                if (textCode == null || textCode.equals("")) {
                    result = new String(response.body().bytes(), DEFAULT_CODE);
                } else {
                    result = new String(response.body().bytes(), textCode);
                }
            } else if (response != null && !response.isSuccessful()) {
                result = "Exception:" + response.code();
            }
        } catch (IOException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        } catch (Exception e) {
            result = e.toString();

            e.printStackTrace();
        }

        return result;
    }

    public InputStream requestFileStream(String filePath) {
        InputStream inputStream = null;

        try {
            Response response = requestDataByGet(filePath, null);//No
            if (response != null) {
                inputStream = response.body().byteStream();
            }
        } catch (Exception e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return inputStream;
    }

    public String uploadFile(String url, Map<String, String> params, List<FormFile> formFileList, String textCode) {
        String result = "";

        try {
            Response response = uploadMultiFile(url, params, formFileList);
            if (response != null && response.isSuccessful()) {
                if (textCode == null || textCode.equals("")) {
                    result = new String(response.body().bytes(), DEFAULT_CODE);
                } else {
                    result = new String(response.body().bytes(), textCode);
                }
            } else if (response != null && !response.isSuccessful()) {
                result = "Exception:" + response.code();
            }
        } catch (IOException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        } catch (Exception e) {
            result = e.toString();

            e.printStackTrace();
        }

        return result;
    }

    public String downloadFile(String url, String destFileDir, String fileName) {
        String result = String.valueOf(false);

        try {
            /* 判断存储文件的文件夹是否存在，不存在就创建 */
            File fileDir = new File(destFileDir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            /* 下载文件 */
            Response response = downloadFile(url);
            if (response != null && response.isSuccessful()) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                String SDPath = destFileDir;
                try {
                    is = response.body().byteStream();
//					result = new String(response.body().bytes(), DEFAULT_CODE);
                    File file = new File(SDPath, fileName);
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();

                    result = String.valueOf(true);
                } catch (Exception e) {
                    LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return result;
    }

    public String downloadFile(String url, Map<String, String> params, String destFileDir, String pwd) {
        String result = String.valueOf(false);

        try {
            /* 判断存储文件的文件夹是否存在，不存在就创建 */
            File fileDir = new File(destFileDir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            /* 下载文件 */
            Response response = downloadFile(url, params);//下载 标签zip文件
            if (response != null && response.isSuccessful()) {
                Headers responseHeaders = response.headers();
                if (!responseHeaders.toString().contains("Content-Disposition")) {
                    result = new String(response.body().bytes(), DEFAULT_CODE);

                    return result;
                }
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                String SDPath = destFileDir;
                try {
                    String fileName1 = responseHeaders.get("Content-Disposition").split("filename=")[1];
                    String fileName2 = fileName1.split(".zip")[0];
                    is = response.body().byteStream();
                    File file = new File(SDPath, fileName2 + "_pwd=" + pwd + ".zip");
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();

                    result = String.valueOf(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
                    }
                }
            } else if (response != null && !response.isSuccessful()) {
                result = "Exception:" + response.code();
            }
        } catch (Exception e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return result;
    }

    public String downloadFileRead(String url, Map<String, String> params) {
        String result = "";

        try {
            /* 下载文件 */
            Response response = downloadFile(url, params);//下载 日志文件
            if (response != null && response.isSuccessful()) {
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();

                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int count = -1;
                    while ((count = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, count);
                    }

                    result = new String(outStream.toByteArray(), ConfigUtil.TextCode.UTF_8);

                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
                    }
                }
            } else if (response != null && !response.isSuccessful()) {
                result = "Exception:" + response.code();
            }
        } catch (Exception e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return result;
    }

    private String addParams(String url, Map<String, String> params) {
        String newUrl = url;

        if (params != null && !params.equals("")) {
            newUrl += "?";

            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }

            String temp = sb.toString();
            if (temp.lastIndexOf("&") != -1) {
                newUrl += temp.substring(0, temp.length() - 1);
            }
        }

        return newUrl;
    }

    /***********************************************************
     * 内部方法
     **********************************************************/
    private Response requestDataByPostForm(String url, Map<String, String> params) throws Exception {
        Response response = null;

        try {
            FormBody.Builder bodyBuilfer = new FormBody.Builder();
            if (params != null && !params.equals("")) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    bodyBuilfer.add(entry.getKey(), entry.getValue());
                }
            }
            RequestBody body = bodyBuilfer.build();

            Request.Builder builder = new Request.Builder();
            builder.url(url);
            builder.post(body);
            Request request = builder.build();

            response = mOkHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                response = null;
            }
        } catch (IOException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        } catch (Exception e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return response;
    }

    private Response uploadMultiFile(String url, Map<String, String> params, List<FormFile> formFileList) throws Exception {
        Response response = null;

        try {
            MultipartBody.Builder fileBuilder = new MultipartBody.Builder();
            fileBuilder.setType(MultipartBody.FORM);

            if (params != null && !params.equals("")) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    fileBuilder.addFormDataPart(entry.getKey(), entry.getValue());
                }
            }
            if (formFileList != null && formFileList.size() > 0) {
                for (FormFile formFile : formFileList) {
                    fileBuilder.addFormDataPart(
                            formFile.getName(),
                            formFile.getFile().getName(),
                            RequestBody.create(MediaType.parse(formFile.getMediaType()), formFile.getFile())
                    );
                }
            }
            RequestBody body = fileBuilder.build();

            Request.Builder builder = new Request.Builder();
            builder.url(url);
            builder.post(body);
            Request request = builder.build();

            response = mOkHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
//                response = null;
            }
        } catch (IOException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
            throw new Exception(e.getMessage());
        }

        return response;
    }

    /**
     * 文件下载
     *
     * @param url    链接
     * @param params 请求参数：键值对
     * @return
     */
    private Response downloadFile(String url, Map<String, String> params) {
        Response response = null;

        try {
            MultipartBody.Builder fileBuilder = new MultipartBody.Builder();
            fileBuilder.setType(MultipartBody.FORM);

            if (params != null && !params.equals("")) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    fileBuilder.addFormDataPart(entry.getKey(), entry.getValue());
                }
            }
            RequestBody body = fileBuilder.build();

            Request.Builder builder = new Request.Builder();
            builder.url(url);
            builder.post(body);
            Request request = builder.build();

            response = mOkHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
//                response = null;
            }
        } catch (IOException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return response;
    }

    private Response requestDataByGet(String url, Map<String, String> params) {//No
        Response response = null;

        try {
            Request.Builder builder = new Request.Builder();
            builder.url(addParams(url, params));
            Request request = builder.build();

            response = mOkHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                response = null;
            }
        } catch (IOException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return response;
    }

    private Response requestDataByPost(String url, Map<String, String> params, String contentType) {
        Response response = null;

        try {
            RequestBody requestBody = null;

            if (contentType.equals("json")) {
                String info = "{\"args\":{\"code\":\"123\",\"id\":\"123\",\"location\":\"sssssaa\"},\"value\":\"1\",\"method\":\"set\"," +
                        "\"password\":\"test\"}";

                info = buildJSONObject();
                requestBody = RequestBody.create(MediaType.parse(FormFile.TEXT_JSON), info);
            } else {
                FormBody.Builder bodyBuilfer = new FormBody.Builder();
                if (params != null && !params.equals("")) {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        bodyBuilfer.add(entry.getKey(), entry.getValue());
                    }
                }
                requestBody = bodyBuilfer.build();
            }

            Request.Builder builder = new Request.Builder();
            builder.url(url);
            builder.post(requestBody);
            Request request = builder.build();

            response = mOkHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                response = null;
            }
        } catch (IOException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        } catch (Exception e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return response;
    }

    private Response downloadFile(String url) {
        Response response = null;

        try {
            Request.Builder builder = new Request.Builder();
            builder.url(url);
            Request request = builder.build();

            response = mOkHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                response = null;
            }
        } catch (IOException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return response;
    }

    private String buildJSONObject() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");

        buffer.append("}");

        return buffer.toString();
    }

    public static class FormFile {
        public static final String IMG_PNG = "image/png";
        public static final String IMG_JPG = "image/jpg";
        public static final String IMG_JPEG = "image/jpeg";
        public static final String MEDIA_AUDIO = "application/octet-stream";
        public static final String TEXT_JSON = "application/json; charset=utf-8";
        public static final String TEXT_TXT = "text/xml; charset=utf-8";

        private String name;
        private File file;
        private String mediaType;

        public FormFile(String name, File file, String mediaType) {
            this.name = name;
            this.file = file;
            this.mediaType = mediaType;
        }

        public String getName() {
            return name;
        }

        public File getFile() {
            return file;
        }

        public String getMediaType() {
            return mediaType;
        }
    }
}