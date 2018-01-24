package cn.com.leyizhuang.app.foundation.pojo.management.order;

import lombok.*;

import java.math.BigDecimal;

/**
 * 订单账款明细
 *
 * @author liuh
 * Created on 2018-1-18
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaOrderAmount {

    private Long id;

    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 现金
     */
    private BigDecimal cashAmount;
    /**
     * 其他金额
     */
    private BigDecimal otherAmount;
    /**
     * pos金额
     */
    private BigDecimal posAmount;
    /**
     * 总金额
     */
    private BigDecimal allAmount;
    /**
     * 流水号
     */
    private String serialNumber;
    /**
     * 日期
     */
    private String date;
}
