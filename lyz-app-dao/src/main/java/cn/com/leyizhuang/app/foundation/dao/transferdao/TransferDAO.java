package cn.com.leyizhuang.app.foundation.dao.transferdao;


import cn.com.leyizhuang.app.core.constant.OrderCouponType;
import cn.com.leyizhuang.app.foundation.pojo.CashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.CashCouponCompany;
import cn.com.leyizhuang.app.foundation.pojo.CustomerCashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.*;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsType;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsBrand;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsCategory;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreDetailVO;
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

    List<TdOrderGoods> findTdorderGoodsByPresentId(@Param("presentId") Long presentId);

    Boolean isExitTdOrderGoodsLine(@Param("orderNo") String orderNo,@Param("gid") Long gid,@Param("goodsLineType") String goodsLineType);

    TdOrderLogistics queryOrderLogistcsByOrderNumber(String orderNumber);

    void saveOrderLogisticsInfo(OrderLogisticsInfo orderLogisticsInfo);

    List<TdDeliveryInfoDetails> queryDeliveryTimeSeqByOrderNo(String orderNO);

    List<TdDeliveryInfoDetails> queryDeliveryInfoDetailByOrderNumber(String orderNo);

    List<TdDeliveryInfoDetails> queryTdOrderListByOrderNo(String orderNo);

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

    AppCustomer findCustomerByCustomerMobile(String realUserUsername);

    List<TdOrderSmall> getPendingTransferOrder(Date startTime, Date endTime);

    List<TdReturnSmall> getPendingTransferReturnOrder(Date startTime, Date endTime);

    List<TdOrderSmall> getPendingTransferOrderByOrderNo(Date startTime, Date endTime ,@Param("orderNo") String orderNO);

    List<TdOrderData> queryTdOrderDataListByOrderNo(String orderNo);

    List<OrderBaseInfo> findNewOrderNumberByDeliveryType();

    List<OrderBaseInfo> queryOrderBaseInfoBySize(int size);

    List<TdOrder> queryTdOrderByOrderNumber(String orderNumber);

    void saveDataTransferErrorLogList(@Param(value = "errorLogList") List<DataTransferErrorLog> errorLogList);

    void saveDataTransferErrorLog(DataTransferErrorLog errorLogList);

    void updateTransferDate(@Param("date") Date date , @Param("orderNo") String orderNo);

    List<GoodsDO> queryAllGoodsTrans();

    void updateGoodsTrans(GoodsDO goodsDO);

    List<GoodsType> queryAllGoodsType();

    List<GoodsBrand> queryAllGoodsBrand();

    List<GoodsCategory> queryAllGoodsCategory();

    List<StoreDetailVO> findStorehasInventory();

    void saveStoreInventory(StoreInventory storeInventory);

    List<TdOrder> findOrderAllFieldBySubOrderNumber(String orderNo);

    List<TdReturnSmall> getTdReturnSmallBySize(int size);
}
