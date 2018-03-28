package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.ArrearsAuditDAO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderArrearsAuditDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingPaymentDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearageListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.DeliveryArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.service.ArrearsAuditService;
import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
    @Transactional(rollbackFor = ExecutionException.class)
    public OrderArrearsAuditDO save(OrderArrearsAuditDO orderArrearsAuditDO) {
        this.arrearsAuditDAO.save(orderArrearsAuditDO);
        return orderArrearsAuditDO;
    }

    @Override
    public PageInfo<SellerArrearsAuditResponse> findBySellerIdAndStatus(Long sellerId, List<ArrearsAuditStatus> arrearsAuditStatusList, Integer page, Integer size) {
        if (arrearsAuditStatusList.contains(ArrearsAuditStatus.AUDITING)) {
            List<SellerArrearsAuditResponse> sellerArrearsAuditResponseList = this.arrearsAuditDAO.findBySellerIdAndStatus(sellerId, arrearsAuditStatusList);
            return new PageInfo<>(sellerArrearsAuditResponseList);
        } else {
            PageHelper.startPage(page, size);
            List<SellerArrearsAuditResponse> sellerArrearsAuditResponseList = this.arrearsAuditDAO.findBySellerIdAndStatus(sellerId, arrearsAuditStatusList);
            return new PageInfo<>(sellerArrearsAuditResponseList);
        }
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

    @Override
    public OrderArrearsAuditDO findArrearsByUserIdAndOrderNumber(Long userID, String orderNumber) {
        return arrearsAuditDAO.findArrearsByUserIdAndOrderNumber(userID, orderNumber);
    }

    @Override
    public PageInfo<OrderBillingPaymentDetails> getRepaymentMondyList(Long userID, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<OrderBillingPaymentDetails> orderBillingPaymentDetailsList = arrearsAuditDAO.getRepaymentMondyList(userID);
        return new PageInfo<>(orderBillingPaymentDetailsList);
    }

    @Override
    public DeliveryArrearsAuditResponse getArrearsAuditInfo(Long id) {
        return this.arrearsAuditDAO.getArrearsAuditInfo(id);
    }
}
