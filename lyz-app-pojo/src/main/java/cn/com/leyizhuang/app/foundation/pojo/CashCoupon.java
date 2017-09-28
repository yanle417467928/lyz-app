package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.SexType;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 *  现金优惠券
 *
 * @author Richard
 * Created on 2017-09-19 11:00
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CashCoupon implements Serializable {


    private static final long serialVersionUID = 6456841161258591629L;

    private Long id;

    //创建时间
    private Date createTime;

    //面额
    private Double denomination;

    //有效期开始时间
    private Date effectiveStartTime;

    //有效期结束时间
    private Date effectiveEndTime;

    //使用说明
    private String description;

    //初始数量
    private Integer initialQuantity;

    //剩余数量
    private Integer remainingQuantity;

}
