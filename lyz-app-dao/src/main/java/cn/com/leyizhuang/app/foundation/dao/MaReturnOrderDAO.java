package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.goods.MaReturnGoods;
import cn.com.leyizhuang.app.foundation.pojo.management.returnOrder.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderGoodsInfo;
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
    List<MaReturnOrderInfo> findMaReturnOrderList(@Param("list") List<Long> storeIds);

    List<MaReturnOrderInfo> findMaReturnOrderListByScreen(@Param(value = "storeId") Long storeId, @Param(value = "status") String status,@Param("list") List<Long> storeIds,
                                                          @Param(value = "beginTime") String beginTime, @Param(value = "endTime") String endTime, @Param(value = "memberName") String memberName, @Param(value = "memberPhone") String memberPhone, @Param(value = "creatorName") String creatorName, @Param(value = "queryOrderInfo") String queryOrderInfo);

    List<MaReturnOrderInfo> findMaReturnOrderPageGirdByInfo(@Param("info")String info,@Param("list") List<Long> storeIds);

    MaReturnOrderDetailInfo queryMaReturnOrderByReturnNo(String returnNumber);

    MaReturnOrderLogisticInfo getMaReturnOrderLogisticeInfo(String returnNumber);

    List<MaReturnGoods> getMaReturnOrderGoodsDetails(String returnNumber);

    List<MaReturnOrderProductCouponInfo> getReturnOrderProductCoupon(String returnNumber);

    List<MaReturnOrderBillingDetail> getMaReturnOrderBillingDetails(Long returnBillingID);

    String findReturnOrderTypeByReturnNumber(String returnNumber);

    List<MaReturnOrderGoodsInfo> findMaReturnOrderGoodsInfoByOrderNumber(String returnNumber);

    Long findReturnOrderBillingId(String returnNumber);

    void updateReturnOrderStatus(@Param(value = "returnNumber") String returnNumber,@Param(value = "status")String status);

    List<ReturnOrderGoodsInfo> findReturnOrderGoodsList(String returnNumber);

    MaOrdReturnBilling findReturnOrderBillingList(Long roid);

    void saveAppToEbsReturnOrderInf(MaStoreReturnOrderAppToEbsBaseInfo maStoreReturnOrderAppToEbs);

    MaStoreReturnOrderAppToEbsBaseInfo findMaStoreReturnOrderAppToEbsInfoByReturnNumber(String returnNumber);

    void saveReturnOrderBillingDetail(@Param(value = "maOrdReturnBillingDetailList")List<MaOrdReturnBillingDetail> maOrdReturnBillingDetailList);
}
