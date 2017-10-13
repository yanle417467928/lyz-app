package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

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
@NoArgsConstructor
public class EmployeeLoginResponse implements Serializable{

    private static final long serialVersionUID = 768360299867556490L;

    //身份类型
    private int type;

    private Long userId;


}
