package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderArrearsAuditDO;
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

}
