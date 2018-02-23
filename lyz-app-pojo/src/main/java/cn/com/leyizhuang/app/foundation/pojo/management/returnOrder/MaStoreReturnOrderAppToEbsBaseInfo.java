package cn.com.leyizhuang.app.foundation.pojo.management.returnOrder;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.ReturnOrderType;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 门店退货接口基本信息类
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaStoreReturnOrderAppToEbsBaseInfo {

    //退单头id
    private Long rtHeaderId;

    //分退单号
    private String returnNumber;

    //主退单号
    private String mainReturnNumber;

    //原分单号
    private String orderNumber;

    //原主单号
    private String mainOrderNumber;

    //分公司id
    private Long sobId;

    //到店退货日期
    private Date rtFullFlag;

    //到店退货日期
    private Date returnDate;

    //分单应退金额
    private BigDecimal refundAmount;

    //退货单类型
    private ReturnOrderType returnType;

    //顾客id
    private Long userId;

    //导购id
    private Long sellerId;

    //门店编码
    private String diySiteCode;

    //门店组织编码
    private String storeOrgCode;

    //销售单类型（1.要货单："B2B" HR产品直接无价批发给直营门店\r（以后在SCRM-APP下单）\r\n
    // 2.要货单："B2B" HR产品经销价批发给经销门店\r（分销业务）\r\n
    // 3.销售订单："B2C" LYZ和YR产品直接零售价销售给门店\r（目前无此业务）\r\n
    // 4.销售订单："B2C" HR,LYZ和YR产品直接零售价销售给会员
    private Integer orderTypeId;

    //配送方式（SELF_TAKE门店自提/HOUSE_DELIVERY送货上门：原始订单的配送方式）\r\n
    private AppDeliveryType deliverTypeTitle;

    private Date createTime;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    private String attribute5;
}
