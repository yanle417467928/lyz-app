package cn.com.leyizhuang.app.foundation.pojo.datatransfer;

import lombok.*;

import java.util.Date;

/**
 * @author GenerationRoad
 * @date 2018/3/24
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TdOrderSmall {

    /**
     * 主单号
     */
    private String mainOrderNumber;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 付款时间
     */
    private Date payTime;

    /**
     * 订单状态
     */
    private Long statusId;

    /**
     * 配送方式
     */
    private String deliverTypeTitle;

    /**
     * 是否是导购代下单
     */
    private Boolean isSellerOrder;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户
     */
    private String username;

    /**
     * 导购的id
     */
    private Long sellerId;

    /**
     * 配送门店编码
     */
    private String diySiteCode;

    /**
     * 销售顾问的用户名
     */
    private String sellerUsername;

    /**
     * 销售顾问的真实姓名
     */
    private String sellerRealName;

    /**
     * 真实用户名
     */
    private String realUserUsername;

    /**
     * 真实用户的真实姓名
     */
    private String realUserRealName;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 城市
     */
    private String city;

    /**
     * 纸质销货单号
     */
    private String paperSalesNumber;

    /**
     * 商品总金额
     */
    private Double totalGoodsPrice;


}
