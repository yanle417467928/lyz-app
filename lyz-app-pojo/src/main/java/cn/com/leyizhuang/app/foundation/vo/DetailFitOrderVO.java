package cn.com.leyizhuang.app.foundation.vo;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import lombok.*;

import java.util.Date;

/**
 * 装饰公司订单
 * Created by liuh on 2017/12/16.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DetailFitOrderVO {
    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 订单创建时间
     */
    private Date createTime;

    /**
     * 配送方式
     */
    private AppDeliveryType deliveryType;
    /**
     * 订单状态
     */
    private AppOrderStatus status;

    /**
     * 代付人
     */
    private String payhelper;

    /**
     * 门店
     */
    private String storeName;
    /**
     * 代付金额
     */
    private Double payhelperAmount;
    /**
     * 是否付清
     */
    private Boolean isPayOver;


}
