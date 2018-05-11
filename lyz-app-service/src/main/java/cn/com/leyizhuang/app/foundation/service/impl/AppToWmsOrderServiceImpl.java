package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.AppToWmsOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.*;
import cn.com.leyizhuang.app.foundation.service.AppToWmsOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2017/12/21.
 * Time: 11:50.
 */
@Service("appToWmsOrderService")
public class AppToWmsOrderServiceImpl implements AppToWmsOrderService {

    @Resource
    private AppToWmsOrderDAO appToWmsOrderDAO;

    @Override
    public void saveAtwCancelOrderRequest(AtwCancelOrderRequest atwCancelOrderRequest) {
        appToWmsOrderDAO.saveAtwCancelOrderRequest(atwCancelOrderRequest);
    }

    @Override
    public void saveAtwRequisitionOrder(AtwRequisitionOrder atwRequisitionOrder) {
        appToWmsOrderDAO.saveAtwRequisitionOrder(atwRequisitionOrder);
    }

    @Override
    public void saveAtwRequisitionOrderGoods(AtwRequisitionOrderGoods atwRequisitionOrderGoods) {
        appToWmsOrderDAO.saveAtwRequisitionOrderGoods(atwRequisitionOrderGoods);
    }

    @Override
    public void modifyAtwCancelOrderRequest(AtwCancelOrderRequest atwCancelOrderRequest) {
        appToWmsOrderDAO.updateAtwCancelOrderRequest(atwCancelOrderRequest);
    }

    @Override
    public void modifyAtwRequisitionOrder(AtwRequisitionOrder atwRequisitionOrder) {
        appToWmsOrderDAO.updateAtwRequisitionOrder(atwRequisitionOrder);
    }

    @Override
    public void modifyAtwRequisitionOrderGoods(AtwRequisitionOrderGoods atwRequisitionOrderGoods) {
        appToWmsOrderDAO.updateAtwRequisitionOrderGoods(atwRequisitionOrderGoods);
    }

    @Override
    public AtwCancelOrderRequest findAtwCancelOrderByOrderNo(String orderNo) {
        if (StringUtils.isNotBlank(orderNo)) {
            return appToWmsOrderDAO.findAtwCancelOrderByOrderNo(orderNo);
        }
        return null;
    }

    @Override
    public AtwRequisitionOrder findAtwRequisitionOrderByOrderNo(String orderNo) {
        if (StringUtils.isNotBlank(orderNo)) {
            return appToWmsOrderDAO.findAtwRequisitionOrderByOrderNo(orderNo);
        }
        return null;
    }

    @Override
    public List<AtwRequisitionOrderGoods> findAtwRequisitionOrderGoodsByOrderNo(String orderNo) {
        if (StringUtils.isNotBlank(orderNo)) {
            return appToWmsOrderDAO.findAtwRequisitionOrderGoodsByOrderNo(orderNo);
        }
        return null;
    }

    @Override
    public void saveAtwReturnOrder(AtwReturnOrder returnOrder) {
        appToWmsOrderDAO.saveAtwReturnOrder(returnOrder);
    }

    @Override
    public void modifyAtwReturnOrder(AtwReturnOrder returnOrder) {
        appToWmsOrderDAO.modifyAtwReturnOrder(returnOrder);
    }

    @Override
    public AtwReturnOrder findReturnOrderByReturnOrderNo(String returnNumber) {
        if (StringUtils.isNotBlank(returnNumber)) {
            return appToWmsOrderDAO.findReturnOrderByReturnOrderNo(returnNumber);
        }
        return null;
    }

    @Override
    public List<AtwRequisitionOrder> findRequiringOrderList(String keywords) {
        return appToWmsOrderDAO.findRequiringOrderList(keywords);
    }

    @Override
    public AtwRequisitionOrder findAtwRequisitionOrderById(Long id) {
        return appToWmsOrderDAO.findAtwRequisitionOrderById(id);
    }

    @Override
    public void saveAtwCancelReturnOrderRequest(AtwCancelReturnOrderRequest returnOrderRequest) {
        appToWmsOrderDAO.saveAtwCancelReturnOrderRequest(returnOrderRequest);
    }

    @Override
    public AtwCancelReturnOrderRequest findAtwCancelReturnOrderByReturnNo(String returnNumber) {
        if (StringUtils.isNotBlank(returnNumber)) {
            return appToWmsOrderDAO.findAtwCancelReturnOrderByReturnNo(returnNumber);
        }
        return null;
    }

    @Override
    public void modifyAtwCancelReturnOrderRequest(AtwCancelReturnOrderRequest returnOrderRequest) {
        appToWmsOrderDAO.updateAtwCancelReturnOrderRequest(returnOrderRequest);
    }

    @Override
    public void saveAtwReturnOrderCheckEnter(AtwReturnOrderCheckEnter atwReturnOrderCheckEnter) {
        appToWmsOrderDAO.saveAtwReturnOrderCheckEnter(atwReturnOrderCheckEnter);
    }

    @Override
    public AtwReturnOrderCheckEnter findAtwReturnOrderCheckEnterByReturnNo(String returnNumber) {
        if (StringUtils.isNotBlank(returnNumber)) {
            return appToWmsOrderDAO.findAtwReturnOrderCheckEnterByReturnNo(returnNumber);
        }
        return null;
    }

    @Override
    public void modifyAtwReturnOrderCheckEnterRequest(AtwReturnOrderCheckEnter atwReturnOrderCheckEnter) {
        appToWmsOrderDAO.updateAtwReturnOrderCheckEnterRequest(atwReturnOrderCheckEnter);
    }

    @Override
    public List<AtwRequisitionOrder> findFailResendWmsOrder(LocalDateTime yesterday) {
        if (yesterday != null) {
            return appToWmsOrderDAO.findFailResendWmsOrder(yesterday);
        }
        return null;
    }

    @Override
    public List<AtwReturnOrder> findFailResendWmsReturnOrder(LocalDateTime yesterday) {
        if (yesterday != null) {
            return appToWmsOrderDAO.findFailResendWmsReturnOrder(yesterday);
        }
        return null;
    }
}
