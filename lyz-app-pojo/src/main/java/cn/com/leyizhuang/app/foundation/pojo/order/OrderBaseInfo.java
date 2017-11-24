package cn.com.leyizhuang.app.foundation.pojo.order;

import cn.com.leyizhuang.app.core.constant.*;
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

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 订单创建时间
     */
    private Date createTime;

    /**
     * 订单类型
     */
    private AppOrderType orderType;

    /**
     * 有效期失效时间
     */
    private Date effectiveEndTime;

    /**
     * 订单头状态
     */
    private AppOrderStatus status;

    /**
     * 订单物流状态
     */
    private LogisticStatus deliveryStatus;

    /**
     * 提货码
     */
    private String pickUpCode;

    /**
     * 配送方式
     */
    private AppDeliveryType deliveryType;

    /**
     * 下单人门店(装饰公司)编码
     */
    private String storeCode;

    /**
     * 下单人门店(装饰公司)id
     */
    private Long storeId;
    /**
     * 订单下单主体类型，装饰公司、门店
     */
    private AppOrderSubjectType orderSubjectType;

    /**
     * 下单人身份类型
     */
    private AppIdentityType creatorIdentityType;


    /**
     * 门店组织全编码
     */
    private String storeStructureCode;

    /**
     * 下单人id
     */
    private Long creatorIdStore;

    /**
     * 下单人姓名
     */
    private String creatorNameStore;

    /**
     * 下单人手机
     */
    private String creatorPhoneStore;

    /**
     * 导购id
     */
    private Long salesConsultId;

    /**
     * 导购姓名
     */
    private String salesConsultName;

    /**
     * 导购手机
     */
    private String salesConsultPhone;

    /**
     *  顾客id
     */
    private Long customerId;

    /**
     * 顾客姓名
     */
    private String customerName;

    /**
     *  顾客手机
     */
    private String customerPhone;

    /**
     * 审核单号
     */
    private String auditNo;

    /**
     * 商品总金额
     */
    private Double totalGoodsPrice;

    /**
     * 是否已评价
     */
    private Boolean isEvaluated;

    /**
     * 线上支付方式
     */
    private OnlinePayType onlinePayType;

}
