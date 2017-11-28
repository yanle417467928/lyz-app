package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

/**
 * 欠款查询返回类
 * Created by caiyu on 2017/11/28.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArrearageListResponse {

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 顾客姓名
     */
    private String customerName;

    /**
     * 欠款金额
     */
    private Double arrearageMoney;
}
