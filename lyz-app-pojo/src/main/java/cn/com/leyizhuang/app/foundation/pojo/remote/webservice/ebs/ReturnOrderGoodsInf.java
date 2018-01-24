package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.core.constant.ProductType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 退单商品行
 *
 * @author Richard
 * Created on 2018-01-03 9:32
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReturnOrderGoodsInf {

    private Long rtLineId;

    /**
     * 订单头id
     */
    private Long rtHeaderId;

    /**
     * 分退单号
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
     * 原始分单行ID
     */
    private Long orderLineId;

    /**
     * 商品名称
     */
    private String goodsTitle;

    /**
     * 商品编码
     */
    private String sku;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 零售价
     */
    private Double lsPrice;

    /**
     * 会员价
     */
    private Double hyPrice;

    /**
     * 经销价
     */
    private Double jxPrice;

    /**
     * 结算价
     */
    private Double settlementPrice;

    /**
     * 退货价
     */
    private Double returnPrice;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    private String attribute5;


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


}
