package cn.com.leyizhuang.app.foundation.dao.transferdao;


import cn.com.leyizhuang.app.core.constant.OrderCouponType;
import cn.com.leyizhuang.app.foundation.pojo.CashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.CashCouponCompany;
import cn.com.leyizhuang.app.foundation.pojo.CustomerCashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.*;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by panjie on 2018/3/24.
 */
@Repository
public interface TransferDAO {

    List<TdOrderGoods> getTdOrderGoods();

    List<String> getTransferStoreMainOrderNumber(Date startTime, Date endTime);

    TdOrder getMainOrderInfoByMainOrderNumber(@Param(value = "mainOrderNumber") String mainOrderNumber);

    List<OrderBaseInfo> findNewOrderNumber();

    List<TdOwnMoneyRecord> findOwnMoneyRecordByOrderNumber(String orderNumber);

    List<TdOrder> findOrderByOrderNumber(String orderNumber);

    List<TdOrder> findOrderAllFieldByOrderNumber(String orderNumber);

    int insertArrearsAudit(OrderArrearsAuditDO orderArrearsAuditDO);

    Long findEmployeeByMobile(String phone);

    TdOrderData findOrderDataByOrderNumber(String orderNumber);

    Boolean existArrearsAudit(String orderNumber);

    String findDeliveryInfoByOrderNumber(String orderNumber);

    Long findDeliveryInfoByClerkNo(String clerkNo);

    List<TdOrderGoods> findTdorderGoodsByTdOrderId(@Param("tdOrderId") Long tdOrderId);

    Boolean isExitTdOrderGoodsLine(@Param("orderNo") String orderNo,@Param("gid") Long gid,@Param("goodsLineType") String goodsLineType);

    List<TdOrderLogistics> queryOrderLogistcs(int size);

    void saveOrderLogisticsInfo(OrderLogisticsInfo orderLogisticsInfo);

    List<TdDeliveryInfoDetails> queryDeliveryTimeSeqBySize(int size);

    List<TdDeliveryInfoDetails> queryDeliveryInfoDetailByOrderNumber(String orderNo);

    List<TdDeliveryInfoDetails> queryTdOrderListBySize(int size);

    List<TdDeliveryInfoDetails> queryOrderGoodsListByOrderNumber(Long id);

    List<OrderBaseInfo> findNewOrderNumberTest();

    AppEmployee findFitEmployeeInfoById(Long userId);

    AppEmployee findStoreEmployeeById(Long sellerId);

    AppCustomer findCustomerById(Long userId);
    OrderBaseInfo findNewOrderByOrderNumber(String orderNumber);

    void addCashCoupon(CashCoupon cashCoupon);

    void addCustomerCashCoupon(CustomerCashCoupon customerCashCoupon);

    void addCashCouponCompany(CashCouponCompany cashCouponCompany);

    void saveOrderCouponInfo(OrderCouponInfo orderCouponInfo);


    TdOwnMoneyRecord getOwnMoneyRecordByOrderNumber(String orderNumber);

    void saveOrderBillingDetails(OrderBillingDetails orderBillingDetails);
    List<TdOrder> findOrderInfoByOrderNumber(String orderNumber);

    List<TdOrderGoods> getTdOrderGoodsByOrderNumber(Long id);

    GoodsDO getGoodsBySku(String sku);

    List<TdCoupon> getCouponListBySkuAndOrderNumber(@Param("sku")String sku, @Param("orderNumber") String orderNumber);

    void addCustomerProductCoupon(CustomerProductCoupon customerProductCoupon);


    Boolean existOrderBillingDetails(String orderNumber);
    List<OrderCouponInfo> findCouponInfoListByType(@Param("orderNumber")String orderNumber, @Param("type") OrderCouponType type);

    TdOrderDeliveryTimeSeqDetail findDeliveryStatusByMainOrderNumber(String mainOrderNumber);

    List<TdOrderData> queryTdOrderDataListBySize(int size);
}
