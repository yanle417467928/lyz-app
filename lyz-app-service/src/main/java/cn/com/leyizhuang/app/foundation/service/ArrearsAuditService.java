package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderArrearsAuditDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingPaymentDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearageListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.DeliveryArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerArrearsAuditResponse;
import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/15
 */
public interface ArrearsAuditService {

    List<ArrearsAuditResponse> findByUserIdAndStatus(Long userId, List<ArrearsAuditStatus> arrearsAuditStatusList);

    List<ArrearsAuditResponse> findByUserIdAndOrderNoAndStatus(Long userId, String orderNo, List<ArrearsAuditStatus> arrearsAuditStatusList);

    OrderArrearsAuditDO save(OrderArrearsAuditDO orderArrearsAuditDO);

    PageInfo<SellerArrearsAuditResponse> findBySellerIdAndStatus(Long sellerId, List<ArrearsAuditStatus> arrearsAuditStatusList,Integer page, Integer size);

    /**
     * 查询欠款单
     *
     * @param userID 用户id
     * @return 欠款列表
     */
    List<ArrearageListResponse> findArrearsListByUserId(Long userID);

    OrderArrearsAuditDO findById(Long id);

    void updateStatusById(OrderArrearsAuditDO orderArrearsAuditDO);

    /**
     * 根据导购id和订单号查询欠款
     *
     * @param userID      用户id
     * @param orderNumber 订单号
     * @return 返回欠款信息
     */
    OrderArrearsAuditDO findArrearsByUserIdAndOrderNumber(Long userID, String orderNumber);

    /**
     * 获取还款记录
     *
     * @param userID 用户id
     * @return 返回还款记录列表
     */
    PageInfo<OrderBillingPaymentDetails> getRepaymentMondyList(Long userID,Integer page, Integer size);

    DeliveryArrearsAuditResponse getArrearsAuditInfo(Long id);

}
