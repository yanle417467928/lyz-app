package cn.com.leyizhuang.app.foundation.vo.management.returnOrder;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.AppOrderType;
import cn.com.leyizhuang.app.core.constant.AppReturnOrderStatus;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.MaReturnGoods;
import cn.com.leyizhuang.app.foundation.pojo.management.returnOrder.MaReturnOrderBillingDetail;
import cn.com.leyizhuang.app.foundation.pojo.management.returnOrder.MaReturnOrderProductCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import lombok.*;

import java.util.List;

/**
 * @author liuh
 * Notes: 退单详情返
 * Created with IntelliJ IDEA.
 * Date: 2017/12/5.
 * Time: 17:52.
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaReturnOrderDetailVO {


    /**
     * 退单号
     */
    private String returnNumber;
    /**
     * 退单类型（线上支付，货到付款）
     */
    private String returnType;
    /**
     * 退货单状态
     */
    private AppReturnOrderStatus returnStatus;
    /**
     * 退单时间
     */
    private String returnTime;
    /**
     *原订单号
     */
    private String orderNumber;
    /**
     *原订单类型
     */
    private AppOrderType orderType;
    /**
     *门店名称
     */
    private String storeName;

    /**
     * 建单人姓名
     */
    private String creatorName;
    /**
     * 建单人类型
     */
    private AppIdentityType creatorIdentityType;
    /**
     * 顾客id
     */
    private Long customerId;
    /**
     * 顾客姓名
     */
    private String customerName;
    /**
     * 预约门店名称
     */
    private String bookingStoreName;
    /**
     * 收货地址全称
     */
    private String shippingAddress;
    /**
     * 预约提货时间
     */
    private String bookingTime;
    /**
     * 退货原因
     */
    private String reasonInfo;

    /**
     * 商品list
     */
    private List<MaReturnGoods> goodsList;
    /**
     * 退款明细
     */
    private List<MaReturnOrderBillingDetail> retrunBillingList;
    /**
     * 退劵明细
     */
    private List<MaReturnOrderProductCouponInfo> returnOrderProductCouponList;
}
