package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * 我的订单列表查询返回结果
 *
 * @author caiyu
 * @date 2017/11/13
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageInfoVO {
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 顾客id
     */
    private Long customerId;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 顾客姓名
     */
    private String customerName;
    /**
     * 顾客电话
     */
    private String customerPhone;
    /**
     * 订单状态
     */
    private String status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 是否已评价
     */
    private Boolean isEvaluated;
    /**
     * 订单总金额
     */
    private Double price;
    /**
     * 应付款
     */
    private Double amountPayable;

    /**
     * 配送方式
     */
    private String deliveryType;

    /**
     * 配送地址或门店地址
     */
    private String shippingAddress;
    /**
     * 楼盘信息
     */
    private String estateInfo;

    /**
     * 商品图片
     */
    private List<String> goodsImgList;
    /**
     * 商品总数量
     */
    private Integer count;

    /**
     * 过期时间
     */
    private Long endTime;

    private List<OrderGoodsInfo> orderGoodsInfoList;

    /**
     * 过期时间
     */
    private Date effectiveEndTime;

    /**
     * 订单id
     */
    private Long oid;

    /**
     *  门店名字
     */
    private String storeName;

    /**
     * 是否货到付款
     */
    private Boolean isCashDelivery;

    //顾客预存款余额
    private Double preDeposit = 0D;

    //导购信用额度余额
    private Double creditMoney = 0D;

    //门店预存款余额
    private Double stPreDeposit = 0D;

    //门店信用金余额
    private Double stCreditMoney = 0D;
}
