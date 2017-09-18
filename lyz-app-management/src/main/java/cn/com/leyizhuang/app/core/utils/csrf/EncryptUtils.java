package cn.com.leyizhuang.app.core.utils.csrf;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.HashMap;
import java.util.Map;

/**
 * 加密工具
 *
 * @author Richard
 * Created on 2017-09-15 9:53
 **/
public class EncryptUtils {

    /**
     * 根据原始用户名、密码得到加密后的密码以及salt（盐）
     *  md5加密 三次，转hex
     * @param username
     * @param password
     * @return
     */
    public static Map<String, String> getPasswordAndSalt(String username, String password) {
        Map<String,String> paramMap = new HashMap<>();
        int hashIterations = 3;
        String algorithmName = "md5";
        if (null != username && null != password) {
            String salt1 = username;
            String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();
            SimpleHash hash = new SimpleHash(algorithmName, password, salt1 + salt2, hashIterations);
            String encodedPassword = hash.toHex();
            paramMap.put("encodedPassword",encodedPassword);
            paramMap.put("salt",salt2);
            return paramMap;
        } else {
            throw new RuntimeException("加密失败，用户名或密码为空");
        }

    }
}
