package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.ArrearsAuditDAO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderArrearsAuditDO;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.service.ArrearsAuditService;
import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/15
 */
@Service
@Transactional
public class ArrearsAuditServiceImpl implements ArrearsAuditService {

    @Autowired
    private ArrearsAuditDAO arrearsAuditDAO;

    @Override
    public List<ArrearsAuditResponse> findByUserId(Long userId, List<ArrearsAuditStatus> arrearsAuditStatusList) {
        return this.arrearsAuditDAO.findByUserId(userId, arrearsAuditStatusList);
    }

    @Override
    public List<ArrearsAuditResponse> findByUserIdAndOrderNo(Long userId, String orderNo, List<ArrearsAuditStatus> arrearsAuditStatusList) {
        return null;
    }

    @Override
    public OrderArrearsAuditDO save(OrderArrearsAuditDO orderArrearsAuditDO) {
        this.arrearsAuditDAO.save(orderArrearsAuditDO);
        return orderArrearsAuditDO;
    }
}
