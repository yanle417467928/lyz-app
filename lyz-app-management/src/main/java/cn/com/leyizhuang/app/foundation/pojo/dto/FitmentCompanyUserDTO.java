package cn.com.leyizhuang.app.foundation.pojo.dto;

import cn.com.leyizhuang.app.core.constant.SexType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author GenerationRoad
 * @date 2017/9/20
 */
@Setter
@Getter
@ToString
public class FitmentCompanyUserDTO implements Serializable {
    private static final long serialVersionUID = -5457859636157519043L;

    // 自增主键
    private Long id;

    // 员工姓名mobile
    private String userName;

    // 员工手机号码
    private String mobile;

    // 员工密码
    private String password;

    // 是否是主账号
    private Boolean isMain = true;

    // 装饰公司id
    private Long companyId;

    // 是否被冻结，默认未冻结
    private Boolean frozen = false;

    //性别
    private Boolean sex;

    //年龄
    private Integer age;
}
