package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.ProductCouponGetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品券
 *
 * @author Richard
 * Created on 2017-09-29 10:00
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCoupon implements Serializable{

    private static final long serialVersionUID = -7523497825553900797L;

    private Long id;

    //用户id
    private Long customerId;

    //商品Id
    private Long goodsId;

    //数量
    private Integer quantity;

    //产品券获取途径
    private ProductCouponGetType getType;

    //获取时间
    private Date getTime;

    //生效开始时间
    private Date effectiveStartTime;

    //生效结束时间
    private Date effectiveEndTime;

    //是否使用
    private Boolean isUsed;

    //使用时间
    private Date useTime;

    //使用订单号
    private String useOrderNumber;

    //产生券的相关订单号
    private String getOrderNumber;

    //购买价格
    private Double buyPrice;


}
