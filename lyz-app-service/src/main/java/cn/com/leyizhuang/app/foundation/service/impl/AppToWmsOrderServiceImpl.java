package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.AppToWmsOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.wms.AtwCancelOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.wms.AtwRequisitionOrder;
import cn.com.leyizhuang.app.foundation.pojo.wms.AtwRequisitionOrderGoods;
import cn.com.leyizhuang.app.foundation.service.AppToWmsOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2017/12/21.
 * Time: 11:50.
 */
@Service
public class AppToWmsOrderServiceImpl implements AppToWmsOrderService {

    @Resource
    private AppToWmsOrderDAO appToWmsOrderDAO;

    @Override
    @Transactional
    public void save(AtwCancelOrderRequest atwCancelOrderRequest) {
        appToWmsOrderDAO.saveAtwCancelOrderRequest(atwCancelOrderRequest);
    }

    @Override
    @Transactional
    public void save(AtwRequisitionOrder atwRequisitionOrder) {
        appToWmsOrderDAO.saveAtwRequisitionOrder(atwRequisitionOrder);
    }

    @Override
    @Transactional
    public void save(AtwRequisitionOrderGoods atwRequisitionOrderGoods) {
        appToWmsOrderDAO.saveAtwRequisitionOrderGoods(atwRequisitionOrderGoods);
    }

    @Override
    @Transactional
    public void modify(AtwCancelOrderRequest atwCancelOrderRequest) {
        appToWmsOrderDAO.updateAtwCancelOrderRequest(atwCancelOrderRequest);
    }

    @Override
    @Transactional
    public void modify(AtwRequisitionOrder atwRequisitionOrder) {
        appToWmsOrderDAO.updateAtwRequisitionOrder(atwRequisitionOrder);
    }

    @Override
    @Transactional
    public void modify(AtwRequisitionOrderGoods atwRequisitionOrderGoods) {
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
}
