package cn.com.leyizhuang.app.foundation.pojo.rest;

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
public class QrCodeResponse implements Serializable{


    private String smsCode;

    public QrCodeResponse() {
    }

    public QrCodeResponse(String smsCode) {
        this.smsCode = smsCode;
    }


}
