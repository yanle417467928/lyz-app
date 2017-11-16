package cn.com.leyizhuang.app.foundation.pojo.order;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.AppOrderType;
import lombok.*;

import java.util.Date;

/**
 * 订单基础信息
 *
 * @author Richard
 * Created on 2017-10-10 10:48
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderBaseInfo {

    private Long id;

    //订单号
    private String orderNumber;

    //订单创建时间
    private Date createTime;

    //订单类型
    private AppOrderType orderType;

    //有效期失效时间
    private Date effectiveEndTime;

    //订单头状态
    private String status;

    //订单物流状态
    private String deliveryStatus;

    //提货码
    private String pickUpCode;

    //配送方式
    private AppDeliveryType deliveryType;

    //订单下单主体类型，装饰公司、门店
    private String orderSubjectType;

    //下单人身份类型
    private String creatorIdentityType;

    //装饰公司订单主体信息
    private FitOrderSubjectInfo fitOrderInfo;

    //门店订单主体信息
    private StoreOrderSubjectInfo storeOrderInfo;

    //商品总金额
    private Double totalGoodsPrice;

    //是否已评价
    private Boolean isEvaluation;
}
