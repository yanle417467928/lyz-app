package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.core.constant.ProductType;
import lombok.*;

import java.util.Date;

/**
 * Created by caiyu on 2018/5/23.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderReceivablePriceInf {

    /**
     * 自增id
     */
    private Long id;

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
     * 主单号
     */
    private String mainOrderNumber;

    /**
     * 主单总金额
     */
    private Double mainTotalAmount;

    /**
     * 分单号
     */
    private String orderNumber;

    /**
     * 分单总金额
     */
    private Double totalAmount;

    /**
     * 分单类型
     */
    private ProductType productType;

    /**
     * 顾客
     */
    private String customer;

    /**
     * 导购
     */
    private String seller;

    /**
     * 发货时间
     */
    private Date shipDate;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    private String attribute5;


}
