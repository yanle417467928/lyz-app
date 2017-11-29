package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.ArrearsAuditDAO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderArrearsAuditDO;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearageListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerArrearsAuditResponse;
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
    public List<ArrearsAuditResponse> findByUserIdAndStatus(Long userId, List<ArrearsAuditStatus> arrearsAuditStatusList) {
        return this.arrearsAuditDAO.findByUserIdAndStatus(userId, arrearsAuditStatusList);
    }

    @Override
    public List<ArrearsAuditResponse> findByUserIdAndOrderNoAndStatus(Long userId, String orderNo, List<ArrearsAuditStatus> arrearsAuditStatusList) {
        return this.arrearsAuditDAO.findByUserIdAndOrderNoAndStatus(userId, orderNo, arrearsAuditStatusList);
    }

    @Override
    public OrderArrearsAuditDO save(OrderArrearsAuditDO orderArrearsAuditDO) {
        this.arrearsAuditDAO.save(orderArrearsAuditDO);
        return orderArrearsAuditDO;
    }

    @Override
    public List<SellerArrearsAuditResponse> findBySellerIdAndStatus(Long sellerId, List<ArrearsAuditStatus> arrearsAuditStatusList) {
        return this.arrearsAuditDAO.findBySellerIdAndStatus(sellerId, arrearsAuditStatusList);
    }

    @Override
    public List<ArrearageListResponse> findArrearsListByUserId(Long userID) {
        return arrearsAuditDAO.findArrearsListByUserId(userID);
    }

    @Override
    public OrderArrearsAuditDO findById(Long id) {
        return this.arrearsAuditDAO.findById(id);
    }

    @Override
    public void updateStatusById(OrderArrearsAuditDO orderArrearsAuditDO) {
        this.arrearsAuditDAO.updateStatusById(orderArrearsAuditDO);
    }
}
