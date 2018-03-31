package cn.com.leyizhuang.app.foundation.service.datatransfer;

import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.*;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsType;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsBrand;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsCategory;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderArrearsAuditDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLogisticsInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreDetailVO;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

/**
 * @author Created on 2018-03-24 15:15
 **/
public interface DataTransferService {

    TdOrderLogistics queryOrderLogistcsByOrderNumber(String orderNumber);

    List<String> getTransferStoreMainOrderNumber(Date startTime, Date endTime);

    TdOrder getMainOrderInfoByMainOrderNumber(String mainOrderNumber);

    OrderArrearsAuditDO transferArrearsAudit(String orderNumber, List<AppEmployee> employeeList);

    List<TdDeliveryInfoDetails> queryDeliveryTimeSeqByOrderNo(String orderNO);

    List<TdDeliveryInfoDetails> queryDeliveryInfoDetailByOrderNumber(String orderNo);

    List<TdDeliveryInfoDetails> queryTdOrderListByOrderNo(String orderNo);

    List<TdDeliveryInfoDetails> queryOrderGoodsListByOrderNumber(Long id);

    Map<String, Object> transferCoupon(OrderBaseInfo baseInfo);

    OrderBillingDetails transferOrderBillingDetails(OrderBaseInfo orderBaseInfo);

    AppEmployee findFitEmployeeInfoById(Long userId);

    AppEmployee findStoreEmployeeById(Long sellerId);

    AppCustomer findCustomerById(Long userId);

    OrderLogisticsInfo transferOrderLogisticsInfo(TdOrderSmall tdOrder, List<AppEmployee> employeeList,List<AppStore> storeList);

    TdOrderDeliveryTimeSeqDetail findDeliveryStatusByMainOrderNumber(String mainOrderNumber);

    List<OrderBaseInfo> findNewOrderNumber();

    AppCustomer findCustomerByCustomerMobile(String realUserUsername);

    List<TdOrderSmall> getPendingTransferOrder(Date startTime, Date endTime);

    List<TdOrderData> queryTdOrderDataListByOrderNo(String orderNo);

    List<OrderBaseInfo> findNewOrderNumberByDeliveryType();

    List<OrderBaseInfo> queryOrderBaseInfoBySize(int size);

    List<TdOrder> queryTdOrderByOrderNumber(String orderNumber);

    public OrderBaseInfo transferOrderBaseInfo(TdOrderSmall tdOrder, List<AppEmployee> employeeList,
                                               List<AppCustomer> customerList, List<AppStore> storeList);

    public Queue<DataTransferErrorLog> transferOrderRelevantInfo() throws ExecutionException, InterruptedException;

    List<GoodsDO> queryAllGoodsTrans();

    void goodsInfoTransfer();

    void updateGoodsTrans(GoodsDO goodsDO);

    List<GoodsType> queryAllGoodsType();

    List<GoodsBrand> queryAllGoodsBrand();

    List<GoodsCategory> queryAllGoodsCategory();

    void storeInventoryInfoTransfer();

    List<StoreDetailVO> findStorehasInventory();
}
