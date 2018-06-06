package cn.com.leyizhuang.app.foundation.pojo.management.order;

import cn.com.leyizhuang.app.core.constant.*;
import lombok.*;

import java.util.Date;

/**
 * 订单信息临时类
 * @author liuh
 * @date 2018/1/15
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaOrderTempInfo {

    private Long id;

    /**
     * 城市id
     */
    private Long cityId;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 分公司id
     */
    private Long sobId;
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
     * 门店组织id
     */
    private Long storeOrgId;

    /**
     * 下单人id
     */
    private Long creatorId;

    /**
     * 下单人姓名
     */
    private String creatorName;

    /**
     * 下单人手机
     */
    private String creatorPhone;

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
     * 顾客id
     */
    private Long customerId;

    /**
     * 顾客姓名
     */
    private String customerName;

    /**
     * 顾客手机
     */
    private String customerPhone;

    /**
     * 顾客类型
     */
    private AppCustomerType customerType;
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
     * 订单备注
     */
    private String remark;
    /**
     * (装饰公司）销售经理id
     */
    private Long salesManagerId;
    /**
     * (装饰公司）销售经理门店id
     */
    private Long salesManagerStoreId;

}
