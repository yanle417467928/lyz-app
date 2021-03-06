package cn.com.leyizhuang.app.foundation.pojo.response;

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
public class VerifyCodeResponse implements Serializable {


    private String smsCode;

    public VerifyCodeResponse() {
    }

    public VerifyCodeResponse(String smsCode) {
        this.smsCode = smsCode;
    }


}
