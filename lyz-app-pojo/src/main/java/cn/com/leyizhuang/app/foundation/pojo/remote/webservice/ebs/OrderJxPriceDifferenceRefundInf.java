package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import lombok.*;

import java.util.Date;

/**
 * @author Jerry.Ren
 * create 2018-01-23 10:31
 * desc: 订单经销差价退还信息
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderJxPriceDifferenceRefundInf {

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

    private Long refundId;

    /**
     * 退单号
     */
    private String returnNumber;
    /**
     * 订单号
     */
    private String mainOrderNumber;

    /**
     * 分公司id
     */
    private Long sobId;

    /**
     * 退还日期
     */
    private Date refundDate;

    /**
     * 商品编码
     */
    private String sku;

    /**
     * 金额
     */
    private Double amount;

    /**
     * 描述
     */
    private String description;

    /**
     * 门店组织编码
     */
    private String storeOrgCode;

    /**
     * 门店编码
     */
    private String diySiteCode;

    /**
     * 收款编号
     */
    private String refundNumber;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    private String attribute5;


}
