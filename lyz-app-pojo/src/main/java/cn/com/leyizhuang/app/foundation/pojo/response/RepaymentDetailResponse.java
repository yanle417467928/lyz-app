package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

/**
 * 还款详情返回类
 * Created by caiyu on 2017/12/21.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentDetailResponse {
    /**
     * 还款金额
     */
    private Double repaymentMoney;
    /**
     * 还款类型
     */
    private String repaymentType;
    /**
     * 还款编号
     */
    private String repaymentNumber;
    /**
     * 还款时间
     */
    private String repaymentTime;
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 顾客姓名
     */
    private String customerName;
    /**
     * 顾客电话
     */
    private String customerPhone;

}
