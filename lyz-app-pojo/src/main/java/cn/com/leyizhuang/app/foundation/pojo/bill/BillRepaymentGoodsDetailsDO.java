package cn.com.leyizhuang.app.foundation.pojo.bill;

import lombok.*;

import java.util.Date;

/**
 * 账单还款商品明细表
 * @author GenerationRoad
 * @date 2018/6/26
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BillRepaymentGoodsDetailsDO {
    private Long id;
    //还款头id
    private Long repaymentId;
    //还款单号（BL_RC开头）
    private String repaymentNo;
    //订单号
    private String orderNo;
    //退单号
    private String returnNo;
    //信用金使用金额
    private Double orderCreditMoney;
    //订单创建时间
    private Date orderTime;
    //出货/反配时间
    private Date shipmentTime;
    //订单类型（order、return）
    private String orderType;
    //滞纳金
    private Double interestAmount;
}
