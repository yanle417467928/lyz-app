package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes: 退单详情返回实体
 * Created with IntelliJ IDEA.
 * Date: 2017/12/5.
 * Time: 17:52.
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReturnOrderDetailResponse {

    /**
     * 退单号
     */
    private String returnNumber;
    /**
     * 退单时间
     */
    private String returnTime;
    /**
     * 退单类型（线上支付，货到付款）
     */
    private String returnType;
    /**
     * 退货单状态
     */
    private String returnStatus;
    /**
     * 退货原因
     */
    private String reasonInfo;
    /**
     * 配送方式
     */
    private String deliveryType;
    /**
     * 配送时间
     */
    private String deliveryTime;
    /**
     * 收货人姓名
     */
    private String receiver;
    /**
     * 收货人电话
     */
    private String receiverPhone;
    /**
     * 收货地址全称
     */
    private String shippingAddress;
    /**
     * 预约门店名称
     */
    private String bookingStoreName;
    /**
     * 预约门店电话
     */
    private String bookingStorePhone;
    /**
     * 门店详细地址
     */
    private String storeDetailedAddress;
    /**
     * 预约提货时间
     */
    private String bookingTime;
    /**
     * 商品list
     */
    private List<GiftListResponseGoods> goodsList;

    /**
     * 退货总数
     */
    private Integer returnQty;
    /**
     * 退货总金额
     */
    private Double totalReturnPrice;

}
