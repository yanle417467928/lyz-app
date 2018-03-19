package cn.com.leyizhuang.app.foundation.service;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * 二维码产生服务
 * Created by panjie on 2018/3/19.
 */
public interface QrcodeProduceService {

    File createQrcode(String url) throws WriterException, IOException;

    File createQrcode(String url,File logo) throws WriterException, IOException;

    File urlToFile(URL url) throws IOException;
}
