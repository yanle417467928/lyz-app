package cn.com.leyizhuang.app.web;

import cn.com.leyizhuang.common.core.utils.Base64Utils;

/**
 * @author Created on 2017-09-19 14:02
 **/
public class TestMD5 {
    public static void main(String[] args) {
        /*String password = DigestUtils.md5DigestAsHex("admin123".getBytes());
        System.out.println(password);*/

        String a = Base64Utils.encode("admin");
        System.out.println(a);

        String c = Base64Utils.decode(a);
        System.out.println(c);

    }
}
