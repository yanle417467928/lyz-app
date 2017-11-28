package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2017/11/24
 */
@Setter
@Getter
@ToString
public class DeliveryAgencyFundResponse {

    private Long id;
    //订单编号
    private String orderNumber;
    //会员姓名
    private String customerName;
    //会员电话
    private String customerPhone;
    //导购姓名
    private String sellerName;
    //导购电话
    private String sellerPhone;
    //代收金额
    private Double agencyMoney;
    //实收金额
    private Double realMoney;
    //收款方式
    private String paymentMethod;
    //代收时间
    private String createTime;
}
