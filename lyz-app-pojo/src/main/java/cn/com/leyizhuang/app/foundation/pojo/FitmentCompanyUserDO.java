package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.SexType;
import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2017/9/20
 */
@Setter
@Getter
@ToString
public class FitmentCompanyUserDO extends BaseDO {

    // 员工姓名
    private String name;

    // 员工手机号码
    private String phone;

    // 员工密码
    private String password;

    // 是否是主账号
    private Boolean isMain = true;

    // 装饰公司id
    private Long companyId;

    // 是否被冻结，默认未冻结
    private Boolean frozen = false;

    //性别
    private SexType sex;

    //年龄
    private Integer age;
}
