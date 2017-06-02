package cn.com.leyizhuang.app.foundation.pojo.dto;

import cn.com.leyizhuang.app.core.constant.IdentityTypeEnum;
import cn.com.leyizhuang.app.core.constant.SexEnum;
import cn.com.leyizhuang.app.foundation.pojo.MemberDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.text.SimpleDateFormat;

/**
 * 会员视图层~服务层传输实体模型
 *
 * @author Richard
 *         Created on 2017-05-23 14:20
 **/
@Getter
@Setter
@ToString
public class AppAdminMemberDTO {

    private Long id;

    @NotNull(message = "必须选择会员所在城市")
    @Length(max = 10,message = "城市名称不能超过10个汉字")
    private String city;

    @NotNull(message = "必须选择会员归属门店")
    private String store;

    @NotNull(message = "必须选择服务导购")
    private String seller;

    @NotNull(message = "必须选择会员身份")
    private IdentityTypeEnum identityType;

    @NotNull(message = "会员姓名不能为空")
    @Length(min = 2,max = 10,message = "会员姓名的长度必须控制在2~10位")
    private String name;
    private String birthday;

    private String birthdayStr;
    @Pattern(regexp = "^(1[3584]\\d{9})$",
            message = "请输入正确格式的'联系电话'")
    @Length(min = 11, max = 11, message = "请输入正确长度的'联系电话'")
    @NotNull(message = "'联系电话'不能为空")
    private String mobile;

    private SexEnum sex;
    @NotNull(message = "会员状态不能为空")
    private Boolean status;

    public static AppAdminMemberDTO transform(MemberDO memberDO) {
        AppAdminMemberDTO memberDTO = new AppAdminMemberDTO();
        memberDTO.setId(memberDO.getId());
        memberDTO.setCity(memberDO.getCity());
        memberDTO.setStore(memberDO.getStore().getName());
        memberDTO.setSeller(memberDO.getManager().getName());
        memberDTO.setIdentityType(memberDO.getIdentityType());
        memberDTO.setName(memberDO.getName());
        memberDTO.setBirthday(memberDO.getBirthday().toString());
        memberDTO.setMobile(memberDO.getAuth().getMobile());
        memberDTO.setSex(memberDO.getSex());
        memberDTO.setStatus(memberDO.getAuth().getStatus());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        memberDTO.setBirthdayStr(sdf.format(memberDO.getBirthday()));
        return memberDTO;
    }
}
