package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单经销差价信息
 *
 * @author Richard
 * Created on 2018-01-02 13:39
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderJxPriceDifferenceReturnInf implements Serializable {

    private static final long serialVersionUID = 2838096100120401943L;
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

    private Long receiptId;

    /**
     * 订单号
     */
    private String mainOrderNumber;

    /**
     * 分公司id
     */
    private Long sobId;

    /**
     * 收款日期
     */
    private Date receiptDate;

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
    private String receiptNumber;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    private String attribute5;


}
