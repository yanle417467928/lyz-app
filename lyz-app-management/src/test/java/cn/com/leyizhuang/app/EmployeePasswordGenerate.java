package cn.com.leyizhuang.app;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.common.core.utils.Base64Utils;
import org.springframework.util.DigestUtils;

/**
 * 员工密码生成
 *
 * @author Richard
 * Created on 2017-09-24 17:38
 **/
public class EmployeePasswordGenerate {
    public static void main(String[] args) {
        String password = "YWRtaW4=";
        String salt = org.apache.commons.codec.digest.DigestUtils.md5Hex("XQ-000510" +
                AppConstant.APP_USER_SALT);
        String md5Password = DigestUtils.md5DigestAsHex((Base64Utils.decode(password) + salt).getBytes());
        System.out.println("salt:" + salt);
        System.out.println("password:" + md5Password);
    }
}
