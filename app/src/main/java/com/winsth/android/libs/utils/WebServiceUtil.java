package com.winsth.android.libs.utils;

import android.text.TextUtils;
import android.util.Log;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;

/**
 * 作者 Aaron Zhao
 * 时间 2020/5/15 9:10
 * 文件 WebServiceUtil.java
 * 描述 连接WebService
 */
public class WebServiceUtil {
    private static final String TAG = WebServiceUtil.class.getSimpleName();

    /**
     * 调用webservice接口获取 SoapObject 对象
     *
     * @param serviceNameSpace webservice的命名空间
     * @param serviceUrl       webservice的请求地址 要带?wsdl
     * @param mathodName       调用webservice的函数名
     * @param isHasParameters  是否带参
     * @param parameters       参数集合，Map<Key(形参) ,Vlaue（实参）>
     * @return SoapObject 对象
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static String call(String serviceNameSpace, String serviceUrl, String mathodName, boolean isHasParameters, Map<String, Object> parameters) {
        //要调用的方法名称
        SoapObject request = new SoapObject(serviceNameSpace, mathodName);
        //带参
        if (isHasParameters) {
            if (parameters != null) {
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (!TextUtils.isEmpty(key) && value != null) {
                        // 设置参数：当这里的参数设置为arg0、arg1、arg2……argn的时候竟然是正确的。
                        request.addProperty(key, value);
                    }
                }
            }
        }

        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
        envelope.bodyOut = request;//由于是发送请求，所以是设置bodyOut
//        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true

        HttpTransportSE httpTransportSE = new HttpTransportSE(serviceUrl);
        try {
            httpTransportSE.call(null, envelope);//调用
            // 获取返回的数据
            SoapObject soapObject = (SoapObject) envelope.bodyIn;
            String respoend = soapObject.getProperty(0).toString();
            Log.d(TAG, "接口-" + mathodName + "-请求结果：" + respoend);
            return respoend;
        } catch (IOException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        } catch (XmlPullParserException e) {
            LogUtil.exportLog(CommonUtil.getCName(new Exception()), CommonUtil.getMName(new Exception()), e.getMessage(), true);
        }

        return "";
    }
}