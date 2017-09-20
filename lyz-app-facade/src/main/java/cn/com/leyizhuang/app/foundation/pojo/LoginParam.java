package cn.com.leyizhuang.app.foundation.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 获取Token时所需要的认证信息类
 *
 * @author Richard
 * Created on 2017-09-19 9:32
 **/
@Getter
@Setter
@ToString
public class LoginParam {
    private String name;
    private String password;

}
