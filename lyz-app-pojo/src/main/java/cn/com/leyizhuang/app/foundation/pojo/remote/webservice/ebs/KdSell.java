package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Description: 金蝶销退货明细
 * @Author Richard
 * @Date 2018/5/25 15:10
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class KdSell {

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
     * 序列号
     */
    private Long lineId;

    /**
     * 类型 （发S/退R）
     */
    private String sellType;

    /**
     * 是否小型装饰公司业务（是Y/否N）
     */
    private AppWhetherFlag isSmalFit;

    /**
     * 主单号(订单对应主订单号，退单对应主退单号)
     */
    private String mainOrderNumber;

    /**
     * 分单号（订单对应分单号，退单对应分退单号）
     */

    private String orderNumber;

    /**
     * 门店id
     */
    private String storeCode;

    /**
     * 如果是小型装饰公司，带对应直营门店编码
     */
    private String zyStoreCode;

    /**
     * 物料sku
     */
    private String sku;

    /**
     * 物料单位
     */
    private String unit;

    /**
     * 数量
     */
    private Integer qty;

    /**
     * 产品券标志（Y/N）
     */
    private AppWhetherFlag isProductCoupon;

    /**
     * 价格 （return_price 或产品券 purchase_price）
     */
    private Double price;

    /**
     * 导购id
     */
    private Long shoppersId;

    /**
     * 赠品标志（Y/N）
     */
    private AppWhetherFlag isPresent;

    /**
     * 处理时间
     */
    private Date handleTime;

    /**
     * 账套
     */
    private Long sobId;
    /**
     * 备用字段 1~5
     */
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;


}
