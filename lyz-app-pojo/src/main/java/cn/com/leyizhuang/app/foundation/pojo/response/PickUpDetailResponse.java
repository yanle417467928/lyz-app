package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.util.List;

/**
 * 配送员待取货单详情返回类
 * Created by caiyu on 2017/12/8.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PickUpDetailResponse {
    /**
     * 退货单号
     */
    private String returnNumber;
    /**
     * 顾客/收货人姓名
     */
    private String receiver;
    /**
     * 顾客/收货人电话
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
     * 取货地址
     */
    private String PickUpAddress;
    /**
     * 取货时间
     */
    private String pickUpTime;
    /**
     * 退货商品List
     */
    private List<GiftListResponseGoods> goodsList;
}
