package cn.com.leyizhuang.app.core.pay.wechat.util;

import cn.com.leyizhuang.app.core.constant.AppApplicationConstant;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * 调用微信接口工具类
 *
 * @author Jerry.Ren
 * Date: 2017/11/8.
 * Time: 14:30.
 */

public class WechatUtil {

    /**
     * 微信参数配置
     * API_KEY 第二代商户平台密钥和以前一样：BA7BE39E2620A8F12F975CD6E555CA5D
     *
     */
    public static final String API_KEY = "BA7BE39E2620A8F12F975CD6E555CA5D";
    /**
     * 开发平台第二代APP应用ID：wxa4e8a82e226587dd
     */
    public static final String APPID = "wxa4e8a82e226587dd";
    /**
     * 第二代商户平台ID :1492546942
     */
    public static final String MCH_ID = "1492546942";

    /**
     * 随机字符串生成
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) { // length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 请求xml组装
     *
     * @param parameters
     * @return
     */
    public static String getRequestXml(SortedMap<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(key) || "body".equalsIgnoreCase(key) || "sign".equalsIgnoreCase(key)) {
                sb.append("<" + key + ">" + "<![CDATA[" + value + "]]></" + key + ">");
            } else {
                sb.append("<" + key + ">" + value + "</" + key + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    public static String setXML(String return_code, String return_msg) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        sb.append("<return_code>" + "<![CDATA[" + return_code + "]]></return_code>");
        sb.append("<return_msg>" + "<![CDATA[" + return_msg + "]]></return_msg>");
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 生成签名
     *
     * @param characterEncoding
     * @param parameters
     * @return
     */
    public static String createSign(String characterEncoding, SortedMap<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    /**
     * 请求方法
     *
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        try {

            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            System.out.println("连接超时：{}" + ce);
        } catch (Exception e) {
            System.out.println("https请求异常：{}" + e);
        }
        return null;
    }

    /**
     * 微信退款ssl请求
     *
     * @param url  请求路径
     * @param body 请求参数主体（xml格式字符串）
     * @return 微信返回信息
     * @throws Exception 异常
     * @author Jerry.Ren
     */
    public static String refundBySslPost(String url, String body) throws Exception {
        String result = "";
        //商户id
        //指定读取证书格式为PKCS12
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        //读取本机存放的PKCS12证书文件
        FileInputStream instream = new FileInputStream(new File(AppApplicationConstant.wechatApiClinetCert));
        try {
            //指定PKCS12的密码(商户ID)
            keyStore.load(instream, MCH_ID.toCharArray());
        } finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, MCH_ID.toCharArray()).build();
        //指定TLS版本
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"},
                null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        //设置httpclient的SSLSocketFactory
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpPost httppost = new HttpPost(url);
            StringEntity reqEntity = new StringEntity(body, "UTF-8");
            httppost.setEntity(reqEntity);
            //System.out.println("Executing request: " + httppost.getRequestLine());
            CloseableHttpResponse response = null;
            try {
                response = httpclient.execute(httppost);
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("请求失败: {}" + e);
                throw new RuntimeException(e);
            } finally {
                try {
                    if (response != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("请求失败: {}" + e);
            throw new RuntimeException(e);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * xml解析
     *
     * @param strxml
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public static Map doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if (null == strxml || "".equals(strxml)) {
            return null;
        }

        Map m = new HashMap();

        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }
            m.put(k, v);
        }
        // 关闭流
        in.close();
        return m;
    }

    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }

    public static String getNonceStr() {
        Random random = new Random();
        return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "GBK");
    }

    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /**
     * 检查微信返回结果的签名是不是调用的签名
     *
     * @param map
     * @return
     */
    public static boolean verifyNotify(Map<Object, Object> map) {
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        String sign = (String) map.get("sign");
        for (Object keyValue : map.keySet()) {
            if (!"sign".equals(keyValue.toString())) {
                parameterMap.put(keyValue.toString(), map.get(keyValue));
            }
        }
        String createSign = createSign("UTF-8", parameterMap);
        return createSign.equals(sign);
    }

    /**
     * 将流转换成字符串
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String streamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        inputStream.close();

        return new String(outputStream.toByteArray(), "UTF-8");
    }
}
