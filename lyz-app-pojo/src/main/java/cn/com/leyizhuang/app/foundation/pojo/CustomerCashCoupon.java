package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppCashCouponType;
import cn.com.leyizhuang.app.core.constant.CouponGetType;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 顾客优惠券
 * @author Richard
 * Created on 2017-09-19 11:00
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCashCoupon implements Serializable {


    private static final long serialVersionUID = 6456841161258591629L;

    private Long id;

    /**
     * 顾客id
     */
    private Long cusId;

    /**
     * 顾客现金券id
     */
    private Long ccid;

    /**
     * 数量
     */
    private Integer qty;

    /**
     * 是否已使用
     */
    private Boolean isUsed;

    /**
     * 使用时间
     */
    private Date useTime;

    /**
     * 使用订单号
     */
    private String useOrderNumber;
    /**
     * 获取时间
     */
    private Date getTime;

    /**
     * 使用条件，订单金额满足一定金额可使用
     */
    private Double condition;
    /**
     *  面额
     */
    private Double denomination;

    /**
     * 购买价
     */
    private Double purchasePrice;

    /**
     * 有效期开始时间
     */
    private Date effectiveStartTime;

    /**
     * 有效期结束时间
     */
    private Date effectiveEndTime;

    /**
     * 使用说明
     */
    private String description;

    /**
     * 优惠券标题
     */
    private String title;

    /**
     * 状态 ，true启用，false停用
     */
    private Boolean status;

    /**
     * 停用时间
     */
    private Date downTime;

    /**
     * 获取方式
     */
    private CouponGetType getType;

    /**
     * 城市id
     */
    private Long cityId;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 现金券类型
     */
    private AppCashCouponType type;

    /**
     * 指定门店
     */
    private Boolean isSpecifiedStore;

    /**
     * 修改时间
     */
    private Date lastUpdateTime;
}
