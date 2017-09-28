package cn.com.leyizhuang.app.foundation.pojo;

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
public class UserCashCoupon implements Serializable {


    private static final long serialVersionUID = 6456841161258591629L;

    private Long id;

    //顾客id
    private Long customerId;

    //现金优惠券id
    private Long cashCouponId;

    //数量
    private Integer quantity;

    //是否使用
    private Boolean isUsed;

    //使用时间
    private Date userTime;

    //使用订单号
    private String userOrderNumber;

    //状态，true：启用 false:停用
    private Boolean status;

    //手动停用时间
    private Date downTime;

}
