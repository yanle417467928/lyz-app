package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderArrearsAuditDO;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearageListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerArrearsAuditResponse;
import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/15
 */
public interface ArrearsAuditService {

    List<ArrearsAuditResponse> findByUserIdAndStatus(Long userId, List<ArrearsAuditStatus> arrearsAuditStatusList);

    List<ArrearsAuditResponse> findByUserIdAndOrderNoAndStatus(Long userId, String orderNo, List<ArrearsAuditStatus> arrearsAuditStatusList);

    OrderArrearsAuditDO save(OrderArrearsAuditDO orderArrearsAuditDO);

    List<SellerArrearsAuditResponse>findBySellerIdAndStatus(Long sellerId, List<ArrearsAuditStatus> arrearsAuditStatusList);

    /**
     * 查询欠款单
     * @param userID    用户id
     * @return  欠款列表
     */
    List<ArrearageListResponse> findArrearsListByUserId(Long userID);
}
