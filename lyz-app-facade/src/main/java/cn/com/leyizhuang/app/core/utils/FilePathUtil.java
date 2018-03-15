package cn.com.leyizhuang.app.core.utils;

/**
 * @author Jerry.Ren
 * create 2018-03-14 19:37
 * desc:
 **/
public class FilePathUtil {

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    //public static final String FILE_SEPARATOR = File.separator;

    public static String getRealFilePath(String path) {
        return path.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
    }

    public static String getHttpURLPath(String path) {
        return path.replace("\\", "/");
    }
}
