package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.core.constant.CouponGetType;
import cn.com.leyizhuang.app.core.constant.OrderCouponType;
import cn.com.leyizhuang.app.core.constant.ProductType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 订单使用的券
 *
 * @author Richard
 * Created on 2018-01-03 9:32
 **/
@Getter
@Setter
@ToString
public class OrderCouponInf {

    private Long id;
    /**
     * 分公司id
     */
    private Long sobId;
    /**
     * 订单号
     */
    private String mainOrderNumber;

    /**
     * 券类型id
     */
    private Long couponTypeId;

    /**
     * 券类型: 产品券、现金券
     */
    private OrderCouponType couponType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否传输成功
     */
    private AppWhetherFlag sendFlag;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 传输成功时间
     */
    private Date sendTime;

    /**
     * 是否历史券
     */
    private AppWhetherFlag historyFlag;

    /**
     * 无用字段，适应EBS接口表
     */
    private String promotion;

    /**
     * 券id
     */
    private Long couponId;

    /**
     * 券购买时价值
     */
    private Double buyPrice;

    /**
     * 券使用时价值
     */
    private Double costPrice;

    /**
     * 券获取方式
     */
    private CouponGetType getType;

    /**
     * 商品编码（仅当券类型是产品券时这个属性才有值）
     */
    private String sku;

    /**
     * APP单据产品类型
     */
    private ProductType productType;

    /**
     * 数量
     */
    private Integer quantity;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

}
