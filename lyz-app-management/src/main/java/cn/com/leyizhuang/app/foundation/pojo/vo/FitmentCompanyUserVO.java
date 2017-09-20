package cn.com.leyizhuang.app.foundation.pojo.vo;

import cn.com.leyizhuang.app.core.constant.SexType;

/**
 * @author GenerationRoad
 * @date 2017/9/20
 */
public class FitmentCompanyUserVO {

    // 自增主键
    private Long id;

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
