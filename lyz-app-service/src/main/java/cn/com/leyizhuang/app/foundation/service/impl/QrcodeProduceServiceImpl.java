package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.service.QrcodeProduceService;

import com.google.zxing.WriterException;
import me.luzhuo.qrcode2.encode.*;
import me.luzhuo.qrcode2.utils.QRUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.*;
import java.net.URL;

/**
 * Created by panjie on 2018/3/19.
 */
@Service
public class QrcodeProduceServiceImpl implements QrcodeProduceService {

    private static String contents = "http://eqtest.leyizhuang.com.cn:8089/qrcode/register/24"; // 二维码内容
    private static int width = 340; // 二维码图片宽度
    private static int height = 340; // 二维码图片高度
    private static File savefile = new File("C:\\我的电脑\\" + File.separator + System.currentTimeMillis() + ".png"); // 保存文件
    private static File basemapfile =  new File(ClassUtils.getDefaultClassLoader().getResource("").getPath()+"\\src\\main\\resources\\static\\images\\base.png"); // 底图
    private static File logofile = new File("C:\\我的电脑\\logo.jpg"); // logo


    public static void main(String[] args) throws WriterException, IOException {
        // 计算底图的尺寸, 可以根据底图的尺寸计算二维码的大小, 没有该需求则不需要
        int[] size = QRUtils.getImageFilePxSize(basemapfile);
        if(size == null) return;
        width = size[0]; height = size[1];

        // 1. 创建QRCode源数据组
        QRCode qrcode = new QRCode().createQRCode(contents);

        // 2. 样式类处理QRCode源数据组
        QRStyle qrstyle = new QRStyle(qrcode, QRStyle.QRStyles.Default);
        // QRStyle qrstyle = new QRStyle(qrcode, QRStyles.OnlyBlack);
        // QRStyle qrstyle = new QRStyle(qrcode, QRStyles.DefaultTranslucent);
        // QRStyle qrstyle = new QRStyle(qrcode, QRStyles.DefaultTranslucentBorder);
        // QRStyle qrstyle = new QRStyle(qrcode, QRStyle.QRStyles.DefaultPoint);

        // 3. 将QRCode生成图形数据阵
        QRMatrix qrMatrix = new QRMatrix().createMatrix(qrcode, width, height, qrstyle);

        // 4. 将图形数据阵生成图形
        // QRGraphical qrGraphical = new QRGraphical().decode(qrMatrix);
        QRGraphical qrGraphical = new QRGraphical().decode(qrMatrix, basemapfile);

        // 5. 将生成的图形写入文件
        //new QRWriteToFile().writeToFile(qrGraphical, savefile);
        new QRWriteToFile().writeToFile(qrGraphical, savefile, logofile);
    }

    /**
     * 创建一个二维码
     * @param url 连接地址
     * @return
     */
    public File createQrcode(String url) throws WriterException, IOException{
        String savePath = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"\\src\\main\\resources\\static\\images";
        String fileName = File.separator + System.currentTimeMillis()+".png";
        savefile = new File(savePath+fileName);
        // 计算底图的尺寸, 可以根据底图的尺寸计算二维码的大小, 没有该需求则不需要
        int[] size = QRUtils.getImageFilePxSize(basemapfile);
        if(size == null) return null;
        width = size[0]; height = size[1];

        // 1. 创建QRCode源数据组
        QRCode qrcode = new QRCode().createQRCode(url);
        QRStyle qrstyle = new QRStyle(qrcode, QRStyle.QRStyles.Default);
        QRMatrix qrMatrix = new QRMatrix().createMatrix(qrcode, width, height, qrstyle);
        QRGraphical qrGraphical = new QRGraphical().decode(qrMatrix, basemapfile);
        new QRWriteToFile().writeToFile(qrGraphical, savefile);

        return  savefile;
    }

    /**
     * 生成有logo的二维码
     * @param url
     * @param logo
     * @return
     * @throws WriterException
     * @throws IOException
     */
    public File createQrcode(String url,File logo) throws WriterException, IOException{
        String savePath = "lyz-app-management\\src\\main\\resources\\static\\images";
        String fileName = File.separator + System.currentTimeMillis()+".png";
        savefile = new File(savePath+fileName);
        // 计算底图的尺寸, 可以根据底图的尺寸计算二维码的大小, 没有该需求则不需要
        int[] size = QRUtils.getImageFilePxSize(basemapfile);
        if(size == null) return null;
        width = size[0]; height = size[1];

        // 1. 创建QRCode源数据组
        QRCode qrcode = new QRCode().createQRCode(url);
        QRStyle qrstyle = new QRStyle(qrcode, QRStyle.QRStyles.Default);
        QRMatrix qrMatrix = new QRMatrix().createMatrix(qrcode, width, height, qrstyle);
        QRGraphical qrGraphical = new QRGraphical().decode(qrMatrix, basemapfile);
        new QRWriteToFile().writeToFile(qrGraphical, savefile, logo);

        return  savefile;
    }

    public File urlToFile(URL url) throws IOException {
        String savePath = "lyz-app-management\\src\\main\\resources\\static\\images";
        String fileName = File.separator + System.currentTimeMillis()+".png";
        File file = new File(savePath+fileName);

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }

        return file;
    }

    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
