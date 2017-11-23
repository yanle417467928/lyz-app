package cn.com.leyizhuang.app.foundation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author GenerationRoad
 * @date 2017/9/19
 */
@Setter
@Getter
@ToString
public class FitmentCompanyDTO implements Serializable {

    private static final long serialVersionUID = 5186898817820557917L;
    // 自增主键
    private Long id;

    // 装饰公司名称
    @Length(min = 2, max = 25, message = "'装饰公司名称'的长度必须在2~25位之间")
    @NotNull(message = "'装饰公司名称'不能为空")
    private String name;

    // 装饰公司编码
    @Length(min = 2, max = 25, message = "'装饰公司编码'的长度必须在2~25位之间")
    @NotNull(message = "'装饰公司编码'不能为空")
    @Pattern(regexp = "^[^\\u4e00-\\u9fa5]+$",
            message = "请输入正确的'装饰公司编码'")
    private String code;

    //公司地址
    @Length(min = 0, max = 25, message = "'装饰公司地址'的长度必须在0~25位之间")
    private String address;

    //公司电话
    @Length(max = 11, message = "请输入正确的'装饰公司电话'")
    private String phone;

    // 信用金
    private Double credit = 0d;

    // 赞助金
    private Double promotionMoney = 0d;

    // 钱包金额
    private Double walletMoney = 0d;

    // 是否被冻结，默认未冻结
    private Boolean frozen = false;
}
