package cn.com.leyizhuang.app.foundation.vo;

import cn.com.leyizhuang.app.core.constant.IdentityType;
import cn.com.leyizhuang.app.core.constant.SexType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 会员视图层模型
 *
 * @author Richard
 *         Created on 2017-05-23 14:20
 **/
@Getter
@Setter
@ToString
public class AppAdminMemberVO {

    //会员ID
    private Long id;

    //会员归属门店ID
    @NotNull(message = "必须选择会员的归属门店")
    private Long store;

    private String storeName;

    //会员销售顾问ID
    @NotNull(message = "必须选择会员的服务导购")
    private Long salesConsult;

    private String salesConsultName;

    //会员身份类型
    @NotNull(message = "必须选择会员身份类型")
    private IdentityType identityType;

    //会员姓名
    @NotNull(message = "会员姓名不能为空")
    @Length(min = 2,max = 20,message = "会员姓名的长度必须控制在2~20位")
    private String name;

    //会员生日
    private String birthday;

    //会员电话
    @Pattern(regexp = "^(1[3584]\\d{9})$",
            message = "请输入正确格式的'联系电话'")
    @Length(min = 11, max = 11, message = "请输入正确长度的'联系电话'")
    @NotNull(message = "'联系电话'不能为空")
    private String mobile;
    //会员性别
    private SexType sex;

   //会员账号状态
   @NotNull(message = "会员状态不能为空")
    private Boolean status;


}
