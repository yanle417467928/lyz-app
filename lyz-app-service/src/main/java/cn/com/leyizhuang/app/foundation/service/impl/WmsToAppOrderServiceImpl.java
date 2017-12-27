package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.WmsToAppOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.wms.WtaShippingOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.wms.WtaShippingOrderHeader;
import cn.com.leyizhuang.app.foundation.service.WmsToAppOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void saveWtaShippingOrderHeader(WtaShippingOrderHeader header) {
        wmsToAppOrderDAO.saveWtaShippingOrderHeader(header);
    }

    @Override
    @Transactional
    public void saveWtaShippingOrderGoods(WtaShippingOrderGoods goods) {
        wmsToAppOrderDAO.saveWtaShippingOrderGoods(goods);
    }
}
