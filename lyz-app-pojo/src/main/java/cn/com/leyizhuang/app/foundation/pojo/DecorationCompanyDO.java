package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2017/9/14
 */
@Setter
@Getter
@ToString
public class DecorationCompanyDO extends BaseDO {

    // 装饰公司名称
    private String name;

    // 装饰公司编码
    private String code;

    //公司地址
    private String address;

    //公司电话
    private String phone;

    // EBS提供的SOB_ID
    private Long sobId;

    // 信用金
    private Double credit = 0d;

    // 赞助金
    private Double promotionMoney = 0d;

    // 钱包金额
    private Double walletMoney = 0d;

    // 是否被冻结，默认未冻结
    private Boolean frozen = false;
}
