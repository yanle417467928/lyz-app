package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.MaReturnGoods;
import cn.com.leyizhuang.app.foundation.pojo.management.returnOrder.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderGoodsInfo;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.map.HashedMap;

import java.util.List;

public interface MaReturnOrderService {

    PageInfo<MaReturnOrderInfo> findMaReturnOrderList(Integer page, Integer size, List<Long> storeIds);


    PageInfo<MaReturnOrderInfo> findMaReturnOrderListByScreen(Integer page, Integer size, Long storeId, String status, List<Long> storeIds);

    PageInfo<MaReturnOrderInfo> findMaReturnOrderPageGirdByInfo(Integer page, Integer size,String info, List<Long> storeIds);

    MaReturnOrderDetailInfo queryMaReturnOrderByReturnNo(String returnNumber);

    MaReturnOrderLogisticInfo getMaReturnOrderLogisticeInfo(String returnNumber);

    List<MaReturnGoods> getMaReturnOrderGoodsDetails(String returnNumber);

    List<MaReturnOrderProductCouponInfo> getReturnOrderProductCoupon(String returnNumber);

    List<MaReturnOrderBillingDetail> getMaReturnOrderBillingDetails(Long returnBillingID);

    String findReturnOrderTypeByReturnNumber(String returnNumber);

    List<MaReturnOrderGoodsInfo> findMaReturnOrderGoodsInfoByOrderNumber(String returnNumber);

    Long findReturnOrderBillingId(String returnNumber);

    HashedMap returnOrderReceive(String returnNumber, MaReturnOrderDetailInfo maReturnOrderDetailInfo, MaOrdReturnBilling maOrdReturnBillingList, ShiroUser shiroUser);
    /**
     * 更新退单状态
     */
    void updateReturnOrderStatus(String returnNumber, String status);

    /**
     * 发送自提单退单收货接口表数据到ebs
     */
    void sendReturnOrderReceiptInfAndRecord(String returnNumber);

    List<ReturnOrderGoodsInfo> findReturnOrderGoodsList(String returnNumber);

    MaOrdReturnBilling findReturnOrderBillingList(Long roid);

    void saveReturnOrderBillingDetail(List<MaOrdReturnBillingDetail> maOrdReturnBillingDetailList);

}
