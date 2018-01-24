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
public class ReturnOrderBaseInf {

    /**
     * 退单头id
     */
    private Long rtHeaderId;

    /**
     * 退单分单号
     */
    private String returnNumber;

    /**
     * 主退单号
     */
    private String mainReturnNumber;

    /**
     * 原分单号
     */
    private String orderNumber;

    /**
     * 原主单号
     */

    private String mainOrderNumber;

    /**
     * 分公司 id
     */
    private Long sobId;

    /**
     * 退单日期
     */
    private Date returnDate;

    /**
     * 是否整单退
     */
    private AppWhetherFlag rtFullFlag;

    /**
     * 应退金额
     */
    private Double refundAmount;

    /**
     * 退单类型
     */
    private ReturnOrderType returnType;

    /**
     * 顾客id
     */
    private Long userId;

    /**
     * 导购id
     */
    private Long sellerId;

    /**
     * 门店编码
     */
    private String diySiteCode;

    /**
     * 门店组织编码
     */
    private Long storeOrgCode;

    /**
     * 销售单类型
     * 1.要货单："B2B" HR产品直接无价批发给直营门店（以后在SCRM-APP下单）
     * 2.要货单："B2B" HR产品经销价批发给经销门店（分销业务）
     * 3.销售订单："B2C" LYZ和YR产品直接零售价销售给门店（目前无此业务）
     * 4.销售订单："B2C" HR,LYZ和YR产品直接零售价销售给会员
     */
    private Long orderTypeId;

    /**
     * 配送方式
     */
    private AppDeliveryType deliverTypeTitle;


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

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    private String attribute5;

}
