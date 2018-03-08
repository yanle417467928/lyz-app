package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 员工登录接口返回对象
 *
 * @author Richard
 * Created on 2017-09-19 13:57
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
public class CustomerLoginResponse implements Serializable {

    //openId是否存在
    private Boolean isExist;
    //用户id
    private Long userId;
    //用户电话
    private String mobile;
    //城市ID
    private Long cityId;
    //等级编码
    private String rankCode;

}
