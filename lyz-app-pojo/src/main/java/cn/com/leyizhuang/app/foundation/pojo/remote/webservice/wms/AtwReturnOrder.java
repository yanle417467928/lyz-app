package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.SalesConsult;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderLogisticInfo;
import lombok.*;

import java.util.Date;

/**
 * @author Jerry.Ren
 * Notes:APP~WMS订单下传接口表
 * Created with IntelliJ IDEA.
 * Date: 2017/12/21.
 * Time: 10:51.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AtwReturnOrder {

    private Long id;

    /**
     * 创建时间（本条数据创建时间）
     */
    private Date createTime;
    /**
     * 门店地址
     */
    private String diySiteAddress;
    /**
     * 门店编码
     * diy_site_id字面上的意义是门店ID，但因为历史原因这个标签必须传递门店编码
     */
    private String diySiteId;
    /**
     * 门店名称
     */
    private String diySiteTitle;
    /**
     * 门店电话
     */
    private String diySiteTel;
    /**
     * 订单备注信息
     */
    private String remarkInfo;
    /**
     * 原单号（主单号）
     */
    private String orderNumber;
    /**
     * 退单号
     */
    private String returnNumber;
    /**
     * 退单时间
     */
    private Date returnTime;
    /**
     * 退货单状态一期：1:待通知物流 2:待取货 3: 待确认收货 4 待退款（物流确认） 5 已完成 6 退货取消
     * 退货单状态二期: 1:退货中    2:已取消  3:待退货     4:待退款         5: 已完成   6: 取消中
     */
    private Integer statusId;
    /**
     * 原订单配送方式
     */
    private String deliverTypeTitle;

    /**
     * 省
     */
    private String province;
    /**
     * 街道
     */
    private String subdistrict;
    /**
     * 小区名
     */
    private String residenceName;
    /**
     * 城市
     */
    private String city;
    /**
     * 行政区划
     */
    private String disctrict;
    /**
     * 退款金额
     */
    private Double returnPrice;
    /**
     * 原订单收货地址
     */
    private String shoppingAddress;
    /**
     * 导购真实姓名
     */
    private String sellerRealName;

    /**
     * 商品行总数
     */
    private Integer goodsLineQuantity;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建人电话
     */
    private String creatorPhone;
    /**
     * 退货人
     */
    private String rejecter;
    /**
     * 退货人电话
     */
    private String rejecterPhone;
    /**
     * 退货人地址
     */
    private String rejecterAddress;
    /**
     * 接口传输标识
     */
    private Boolean sendFlag;
    /**
     * 接口错误信息
     */
    private String errorMessage;
    /**
     * wms收到信息时间
     */
    private Date sendTime;
    /**
     * 配送员编码
     */
    private String deliveryClerkNo;

    public static AtwReturnOrder transform(ReturnOrderBaseInfo baseInfo, ReturnOrderLogisticInfo logisticInfo, AppStore store,
                                           OrderBaseInfo orderBaseInfo, int goodsLineQuantity, SalesConsult salesConsult) {
        AtwReturnOrder atwReturnOrder = new AtwReturnOrder();

        atwReturnOrder.setCreateTime(new Date());
        atwReturnOrder.setCreator(baseInfo.getCreatorName());
        atwReturnOrder.setCreatorPhone(baseInfo.getCreatorPhone());
        atwReturnOrder.setDeliverTypeTitle(logisticInfo.getDeliveryType().getDescription());
        atwReturnOrder.setDiySiteAddress(store.getDetailedAddress());
        atwReturnOrder.setDiySiteId(store.getStoreCode());
        atwReturnOrder.setDiySiteTel(store.getPhone());
        atwReturnOrder.setDiySiteTitle(store.getStoreName());
        atwReturnOrder.setGoodsLineQuantity(goodsLineQuantity);
        atwReturnOrder.setOrderNumber(baseInfo.getOrderNo());
        //如果是退货到店则收货信息设置为导购信息
        if (AppDeliveryType.RETURN_STORE.equals(logisticInfo.getDeliveryType())) {
            atwReturnOrder.setRejecter(null == salesConsult?"":salesConsult.getConsultName());
            atwReturnOrder.setRejecterAddress(store.getDetailedAddress());
            atwReturnOrder.setRejecterPhone(null == salesConsult?"":salesConsult.getConsultMobilePhone());
        } else {
            atwReturnOrder.setRejecter(logisticInfo.getRejecter());
            atwReturnOrder.setRejecterAddress(logisticInfo.getDetailedAddress());
            atwReturnOrder.setRejecterPhone(logisticInfo.getRejecterPhone());
        }
        atwReturnOrder.setRemarkInfo(baseInfo.getRemarksInfo());
        atwReturnOrder.setReturnNumber(baseInfo.getReturnNo());
        atwReturnOrder.setReturnPrice(baseInfo.getReturnPrice());
        atwReturnOrder.setReturnTime(baseInfo.getReturnTime());
        atwReturnOrder.setSellerRealName(orderBaseInfo.getSalesConsultName());
        atwReturnOrder.setShoppingAddress(logisticInfo.getReturnFullAddress());
//        atwReturnOrder.setProvince();
        atwReturnOrder.setCity(logisticInfo.getDeliveryCity());
        atwReturnOrder.setDisctrict(logisticInfo.getDeliveryCounty());
        atwReturnOrder.setSubdistrict(logisticInfo.getDeliveryStreet());
        atwReturnOrder.setResidenceName(logisticInfo.getResidenceName());
        atwReturnOrder.setStatusId(baseInfo.getReturnStatus().getValue());

        return atwReturnOrder;
    }
}
