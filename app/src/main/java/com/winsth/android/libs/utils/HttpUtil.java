package com.winsth.android.libs.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpUtil {
    private static final int TIMEOUT = 10 * 60 * 1000;
    private static HttpParams mHttpParams;
    private static HttpClient mHttpClient;
    private static final String DEFAULT_FONTCODE = ConfigUtil.TextCode.UTF_8;

    private HttpUtil() {
    }

    /**
     * 获取云端流数据
     *
     * @param url               链接云端（服务器）的URL
     * @param nameValuePairList 数据参数
     * @return 返回获取到的数据
     */
    public static String getDataByInputStream(String url, List<NameValuePair> nameValuePairList) {
        String result = "";

        InputStream mInputStream = getInputStreamDataByPost(url, nameValuePairList);
        if (mInputStream != null) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int i = -1;
                while ((i = mInputStream.read(buffer)) > 0) {
                    byteArrayOutputStream.write(buffer, 0, i);
                }
                result = byteArrayOutputStream.toString();
            } catch (IOException e) {
                LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "IOException:" + e.getMessage(), true);
            }
        }

        return result;
    }

    /**
     * 获取云端流数据
     *
     * @param url               链接云端（服务器）的URL
     * @param nameValuePairList 数据参数
     * @return 返回获取到的数据
     */
    public static InputStream getInputStreamDataByPost(String url, List<NameValuePair> nameValuePairList) {
        InputStream mInputStream = null;

        HttpEntity mHttpEntity = getHttpEntityDataByPost(url, nameValuePairList);
        if (mHttpEntity != null) {
            try {
                mInputStream = mHttpEntity.getContent();
            } catch (IllegalStateException e) {
                LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "IllegalStateException:" + e.getMessage(),
						true);
            } catch (IOException e) {
                LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "IOException:" + e.getMessage(), true);
            }
        }

        return mInputStream;
    }

    /**
     * 获取云端字符串数据
     *
     * @param url      链接云端（服务器）的URL
     * @param textCode 设置返回的体字体编码（可以使用类库中ConfigUtils.TextCode里面列举的字体编码）
     * @return 返回获取到的数据
     */
    public static String getStringDataByPost(String url, String textCode) {
        return getStringDataByPost(url, null, textCode);
    }

    /**
     * 获取云端字符串数据
     *
     * @param url               链接云端（服务器）的URL
     * @param nameValuePairList 参数数据
     * @param textCode          设置返回的体字体编码（可以使用类库中ConfigUtils.TextCode里面列举的字体编码）
     * @return 返回获取到的数据
     */
    public static String getStringDataByPost(String url, List<NameValuePair> nameValuePairList, String textCode) {
        String result = "";

        HttpEntity mHttpEntity = getHttpEntityDataByPost(url, nameValuePairList);
        if (mHttpEntity != null)
            try {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                InputStream is = mHttpEntity.getContent();
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }

                byte[] data = outStream.toByteArray();
                outStream.close();
                is.close();

                if (textCode != null && !textCode.equalsIgnoreCase("")) {
                    return new String(data, textCode);
//					result = EntityUtils.toString(mHttpEntity, fontCode);
                } else {
                    return new String(data, DEFAULT_FONTCODE);
//					result = EntityUtils.toString(mHttpEntity, DEFAULT_FONTCODE);
                }
            } catch (ParseException e) {
                LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "ParseException:" + e.getMessage(), true);
            } catch (IOException e) {
                LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "IOException:" + e.getMessage(), true);
            }

        return result;
    }

    /**
     * 获取云端JSONArray数据
     *
     * @param url               链接云端（服务器）的URL
     * @param nameValuePairList 数据参数
     * @param fontCode          设置返回的体字体编码（可以使用类库中ConfigHelper.FontCode里面列举的字体编码）
     * @return 返回获取到的数据
     */
    public static JSONArray getJsonArrayDataByPost(String url, List<NameValuePair> nameValuePairList, String fontCode) {
        JSONArray mJsonArray = null;

        HttpEntity mHttpEntity = getHttpEntityDataByPost(url, null);
        if (mHttpEntity != null) {
            try {
                String strResult = "";
                if (fontCode != null && !fontCode.equalsIgnoreCase(""))
                    strResult = EntityUtils.toString(mHttpEntity, fontCode);
                else
                    strResult = EntityUtils.toString(mHttpEntity, DEFAULT_FONTCODE);
                mJsonArray = new JSONArray(strResult);
            } catch (JSONException e) {
                LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "JSONException:" + e.getMessage(), true);
            } catch (ParseException e) {
                LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "ParseException:" + e.getMessage(), true);
            } catch (IOException e) {
                LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "IOException:" + e.getMessage(), true);
            }
        }

        return mJsonArray;
    }

    /**
     * 获取云端数据实体
     *
     * @param url               链接云端（服务器）的URL
     * @param nameValuePairList 数据参数
     * @return 返回获取到的数据
     */
    public static HttpEntity getHttpEntityDataByPost(String url, List<NameValuePair> nameValuePairList) {
        HttpEntity mHttpEntity = null;

        mHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(mHttpParams, TIMEOUT);
        HttpConnectionParams.setSoTimeout(mHttpParams, TIMEOUT);
        if (url.substring(0, 5).equalsIgnoreCase("https"))
            mHttpClient = initHttpClient(mHttpParams);
        else
            mHttpClient = new DefaultHttpClient(mHttpParams);

        HttpPost mHttpPost = new HttpPost(url);
        HttpResponse mHttpResponse = null;
        try {
            mHttpPost.setParams(mHttpParams);
            if (nameValuePairList != null && nameValuePairList.size() > 0)
                mHttpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, HTTP.UTF_8));
            mHttpResponse = mHttpClient.execute(mHttpPost);

            if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                mHttpEntity = mHttpResponse.getEntity();
            } else {
                mHttpEntity = null;
            }
        } catch (IllegalStateException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "IllegalStateException:" + e.getMessage(), true);
        } catch (ParseException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "ParseException:" + e.getMessage(), true);
        } catch (IOException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "IOException:" + e.getMessage(), true);
        }

        return mHttpEntity;
    }

    /**
     * 获取云端字节数组数据
     *
     * @param targetURL 链接云端（服务器）的URL
     * @param params    参数数据
     * @param encode    指定的编码
     * @return 返回字节数组数据
     */
    public static byte[] getByteArrayDataByPost(String targetURL, Map<String, String> params, String encode) {
        byte[] byteArray = null;

        try {
            StringBuilder parambuilder = new StringBuilder("");
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    parambuilder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encode)).append("&");
                }
                parambuilder.deleteCharAt(parambuilder.length() - 1);
            }
            byte[] data = parambuilder.toString().getBytes();
            URL url = new URL(targetURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, " +
					"application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd" +
					".ms-powerpoint, application/msword, */*");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; " +
					".NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            conn.setRequestProperty("Connection", "Keep-Alive");

            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(data);
            outStream.flush();
            outStream.close();
            if (conn.getResponseCode() == 200) {
                byteArray = readStream(conn.getInputStream());
            }
        } catch (UnsupportedEncodingException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "UnsupportedEncodingException:" + e.getMessage(),
					true);
        } catch (MalformedURLException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "MalformedURLException:" + e.getMessage(), true);
        } catch (IOException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "IOException:" + e.getMessage(), true);
        }

        return byteArray;
    }

    private static HttpClient initHttpClient(HttpParams params) {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactoryImp(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient(params);
        }
    }

    private static byte[] readStream(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = -1;

        try {
            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            byteArrayOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), "Exception:" + e.getMessage(), true);
        }

        return byteArrayOutputStream.toByteArray();
    }

    private static class SSLSocketFactoryImp extends SSLSocketFactory {
        final SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryImp(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                }
            };
            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
