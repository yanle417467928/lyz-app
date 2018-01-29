package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.WmsToAppOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.*;
import cn.com.leyizhuang.app.foundation.service.WmsToAppOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2017/12/26.
 * Time: 18:09.
 */
@Service
public class WmsToAppOrderServiceImpl implements WmsToAppOrderService {

    @Resource
    private WmsToAppOrderDAO wmsToAppOrderDAO;

    @Override
    public void saveWtaShippingOrderHeader(WtaShippingOrderHeader header) {
        wmsToAppOrderDAO.saveWtaShippingOrderHeader(header);
    }

    @Override
    public void saveWtaShippingOrderGoods(WtaShippingOrderGoods goods) {
        wmsToAppOrderDAO.saveWtaShippingOrderGoods(goods);
    }

    @Override
    public void saveWtaReturningOrderHeader(WtaReturningOrderHeader header) {
        wmsToAppOrderDAO.saveWtaReturningOrderHeader(header);
    }

    @Override
    public void saveWtaReturningOrderGoods(WtaReturningOrderGoods goods) {
        wmsToAppOrderDAO.saveWtaReturningOrderGoods(goods);
    }

    @Override
    public void saveWtaReturnOrderDeliveryClerk(WtaReturnOrderDeliveryClerk deliveryClerk) {
        wmsToAppOrderDAO.saveWtaReturnOrderDeliveryClerk(deliveryClerk);
    }

    @Override
    public void saveWtaCancelOrderResultEnter(WtaCancelOrderResultEnter orderResultEnter) {
        wmsToAppOrderDAO.saveWtaCancelOrderResultEnter(orderResultEnter);
    }

    @Override
    public void saveWtaCancelReturnOrderResultEnter(WtaCancelReturnOrderResultEnter returnOrderResultEnter) {
        wmsToAppOrderDAO.saveWtaCancelReturnOrderResultEnter(returnOrderResultEnter);
    }
}
