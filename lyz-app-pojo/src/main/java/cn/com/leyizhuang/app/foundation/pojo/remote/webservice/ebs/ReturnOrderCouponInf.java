package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.core.constant.ReturnOrderType;
import lombok.*;

import java.util.Date;

/**
 * 退单基础信息
 *
 * @author Richard
 * Created on 2018-01-02 13:39
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ReturnOrderCouponInf {


    private Long lineId;

    /**
     * 主退单号
     */
    private String mainReturnNumber;


    /**
     * 原主单号
     */

    private String mainOrderNumber;

    /**
     * 券类型（1.产品券 2.优惠券）
     */
    private Long couponTypeId;

    /**
     * 商品编码
     */
    private String sku;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 券价格
     */
    private Double buyPrice;


    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    private String attribute5;

}
