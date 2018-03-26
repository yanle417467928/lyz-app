package cn.com.leyizhuang.app.foundation.dao.transferdao;


import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrder;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdDeliveryInfoDetails;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrder;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderData;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import org.apache.ibatis.annotations.Param;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOwnMoneyRecord;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderArrearsAuditDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
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

    int insertArrearsAudit(OrderArrearsAuditDO orderArrearsAuditDO);

    Long findEmployeeByMobile(String phone);

    TdOrderData findOrderDataByOrderNumber(String orderNumber);

    Boolean existArrearsAudit(String orderNumber);

    String findDeliveryInfoByOrderNumber(String orderNumber);

    Long findDeliveryInfoByClerkNo(String clerkNo);

    List<TdDeliveryInfoDetails> queryDeliveryTimeSeqBySize(int size);

    TdDeliveryInfoDetails queryDeliveryInfoDetailByOrderNumber(String orderNo);

    List<TdDeliveryInfoDetails> queryTdOrderListBySize(int size);

    List<TdDeliveryInfoDetails> queryOrderGoodsListByOrderNumber(Long id);

    AppEmployee findFitEmployeeInfoById(Long userId);

    AppEmployee findStoreEmployeeById(Long sellerId);

    AppCustomer findCustomerById(Long userId);
}
