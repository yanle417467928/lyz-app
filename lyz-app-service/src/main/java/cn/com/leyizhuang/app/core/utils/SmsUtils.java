package cn.com.leyizhuang.app.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * 短信发工具类
 *
 * @author Richard
 * Created on 2017-09-21 15:55
 **/
public class SmsUtils {

    /**
     * @param encode
     * @param enpass
     * @param userName
     * @param mobile
     * @param content
     * @return
     */
    public static String sendMessageQrCode(String encode, String enpass, String userName, String mobile, String content) throws IOException {
        String url = "http://www.mob800.com/interface/Send.aspx?enCode=" + encode + "&enPass="
                + enpass + "&userName=" + userName + "&mob=" + mobile + "&msg=" + content;
        StringBuffer returnCode = null;
        URL u = new URL(url);
        URLConnection connection = u.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) connection;
        httpConn.setRequestProperty("Content-type", "text/html");
        httpConn.setRequestProperty("Accept-Charset", "utf-8");
        httpConn.setRequestProperty("contentType", "utf-8");
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        if (httpConn.getResponseCode() >= 300) {
            throw new RuntimeException("短信接口连接异常");
        }
        inputStream = httpConn.getInputStream();
        inputStreamReader = new InputStreamReader(inputStream);
        reader = new BufferedReader(inputStreamReader);

        while ((tempLine = reader.readLine()) != null) {
            resultBuffer.append(tempLine);
        }
        returnCode = resultBuffer;
        reader.close();
        inputStreamReader.close();
        inputStream.close();
        return returnCode.toString();
    }

}

