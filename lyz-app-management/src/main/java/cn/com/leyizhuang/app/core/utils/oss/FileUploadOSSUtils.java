package cn.com.leyizhuang.app.core.utils.oss;

import cn.com.leyizhuang.app.core.utils.oss.utils.ImageClientUtils;
import cn.com.leyizhuang.app.core.utils.oss.utils.ImageCompressUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 上传文件到OSS
 *
 * @author Richard
 * Created on 2017-10-20 9:28
 **/
public class FileUploadOSSUtils {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmssSSS");


    /**
     * 上传图片到oss方法
     *
     * @param imgFile
     * @return
     */
    public static String uploadProfilePhoto(MultipartFile imgFile, String pathName) {
        String name = imgFile.getOriginalFilename();
        String ext = name.substring(name.lastIndexOf("."));

        try {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            Date dt = calendar.getTime();
            String fileName = pathName + year + "/" + month + "/" + SDF.format(dt) + ext;

            String path = "";
            long fileSize = imgFile.getSize();
            if (fileSize > 1024 * 1024) {
                byte[] compressFile = ImageCompressUtil.compressFile(imgFile.getBytes());
                fileSize = compressFile.length;
                path = ImageClientUtils.getInstance().uploadImage(new ByteArrayInputStream(compressFile), fileSize,
                        fileName);
            } else {
                path = ImageClientUtils.getInstance().uploadImage(imgFile.getInputStream(), fileSize, fileName);
            }

            String url = ImageClientUtils.getInstance().getAbsProjectImagePath(path);

            return url;
        } catch (Exception e) {
            return null;
        }
    }
}
