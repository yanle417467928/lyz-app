package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.AppReturnOrderStatus;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.MaReturnGoods;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.returnOrder.*;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author caiyu
 * @date 2017/12/16
 */
@Repository
public interface MaReturnOrderDAO {

    /**
     * 后台查看退货单
     *
     * @return 订单列表
     */
    List<MaReturnOrderInfo> findMaReturnOrderList();

    List<MaReturnOrderInfo> findMaReturnOrderListByScreen(@Param(value = "storeId") Long storeId, @Param(value = "status") String status);

    List<MaReturnOrderInfo> findMaReturnOrderPageGirdByInfo(String info);

    MaReturnOrderDetailInfo queryMaReturnOrderByReturnNo(String returnNumber);

    MaReturnOrderLogisticInfo getMaReturnOrderLogisticeInfo(String returnNumber);

    List<MaReturnGoods> getMaReturnOrderGoodsDetails(String returnNumber);

    List<MaReturnOrderProductCouponInfo> getReturnOrderProductCoupon(String returnNumber);

    List<MaReturnOrderBillingDetail> getMaReturnOrderBillingDetails(Long returnBillingID);

    String findReturnOrderTypeByReturnNumber(String returnNumber);

    List<MaReturnOrderGoodsInfo> findMaReturnOrderGoodsInfoByOrderNumber(String returnNumber);

    Long findReturnOrderBillingId(String returnNumber);

    void updateReturnOrderStatus(String returnNumber,AppReturnOrderStatus status);

    List<MaOrderGoodsInfo> findReturnOrderGoodsList(String returnNumber);

    MaOrdReturnBilling findReturnOrderBillingList(String returnNumber);

    void saveAppToEbsReturnOrderInf(MaStoreReturnOrderAppToEbsBaseInfo maStoreReturnOrderAppToEbs);

    MaStoreReturnOrderAppToEbsBaseInfo findMaStoreReturnOrderAppToEbsInfoByReturnNumber(String returnNumber);

    void saveReturnOrderBillingDetail(@Param(value = "maOrdReturnBillingDetailList")List<MaOrdReturnBillingDetail> maOrdReturnBillingDetailList);
}
