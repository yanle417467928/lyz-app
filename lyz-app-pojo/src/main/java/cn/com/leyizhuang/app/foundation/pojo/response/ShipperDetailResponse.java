package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import lombok.*;

import java.util.List;

/**
 * 出货单详情返回类
 * Created by caiyu on 2017/11/22.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShipperDetailResponse {
    /**
     * 出货单号
     */
    private String shipperNumber;
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 收货人姓名
     */
    private String receiver;
    /**
     * 收货人电话
     */
    private String receiverPhone;
    /**
     * 导购姓名
     */
    private String sellerName;
    /**
     * 导购电话
     */
    private String sellerPhone;
    /**
     * 收货地址
     */
    private String shippingAddress;
    /**
     * 配送日期时间
     */
    private String deliveryTime;
    /**
     * 代收金额
     */
    private Double collectionAmount;
    /**
    * 付款方式
    */
    private OnlinePayType onlinePayType;

    /**
     * 商品list
     */
    private List<GiftListResponseGoods> goodsList;

}
