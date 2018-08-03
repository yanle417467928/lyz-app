package cn.com.leyizhuang.app.foundation.pojo.management.order;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单收款明细
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
     * 现金备注
     */
    public String cashRemarks;
    /**
     * pos备注
     */
    public String posRemarks;
    /**
     * 其它金额备注
     */
    public String otherRemarks;
    /**
     * 日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private Date payUpTime;

}
