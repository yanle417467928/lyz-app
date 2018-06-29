package cn.com.leyizhuang.app.foundation.pojo.response;

import java.util.Date;

/**
 * 账单明细响应类
 * Created by 12421 on 2018/6/28.
 */
public class BillRepaymentGoodsInfoResponse {

    private Long bill_id;
    //单号
    private String number;
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
