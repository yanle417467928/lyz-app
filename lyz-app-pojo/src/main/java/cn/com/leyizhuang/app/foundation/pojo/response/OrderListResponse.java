package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.util.List;

/**
 * 我的订单列表返回类
 *
 * @author caiyu
 * @date 2017/11/13
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponse {
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
     * 商品图片
     */
    private List<String> goodsImgList;
    /**
     * 商品总数量
     */
    private Integer count;
    /**
     * 订单总金额
     */
    private Double price;
    /**
     * 应付款
     */
    private Double amountPayable;
    /**
     * 过期时间
     */
    private Long endTime;
    /**
     * 配送方式
     */
    private String deliveryType;
    /**
     * 配送地址或门店地址
     */
    private String shippingAddress;

    /**
     *  楼盘信息
     */
    private String estateInfo;
    //顾客预存款余额
    private Double preDeposit = 0D;

    //导购信用额度余额
    private Double creditMoney = 0D;

    //门店预存款余额
    private Double stPreDeposit = 0D;

    //门店信用金余额
    private Double stCreditMoney = 0D;

}
