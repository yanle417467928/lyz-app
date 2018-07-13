package cn.com.leyizhuang.app.foundation.pojo.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Description: 顾客产品券转化顾客预存款
 * @Author Richard
 * @Date 2018/6/5 16:18
 */
@Getter
@Setter
@ToString
public class CustomerProductCouponTransferPreDepositRecord {

    /**
     * 转化记录id
     */
    private Long id;

    /**
     * 转化产品券id
     */
    private Long couponId;

    /**
     * 产品券顾客id
     */
    private Long cusId;

    /**
     * 产品券导购id
     */
    private Long sellerId;

    /**
     * 产品券门店id
     */
    private Long storeId;

    /**
     * 券商品id
     */
    private Long gid;

    /**
     * 转化券数量
     */
    private Integer qty;

    /**
     * 产品券单张转化金额
     */
    private Double singlePrice;

    /**
     * 转化总金额
     */
    private Double totalPrice;

    /**
     * 转化时间
     */
    private Date transferDate;

}
