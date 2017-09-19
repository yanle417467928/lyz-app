package cn.com.leyizhuang.app.foundation.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author GenerationRoad
 * @date 2017/9/19
 */
@Setter
@Getter
@ToString
public class DecorationCompanyDTO implements Serializable{

    // 自增主键
    private Long id;

    // 装饰公司名称
    private String name;

    // 装饰公司编码
    private String code;

    //公司地址
    private String address;

    //公司电话
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
