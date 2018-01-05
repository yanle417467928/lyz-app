package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppSeparateOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.service.AppSeparateOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 拆单服务实现
 *
 * @author Ricahrd
 * Created on 2018-01-04 10:49
 **/
@Service
public class AppSeparateOrderServiceImpl implements AppSeparateOrderService {

    @Resource
    private AppSeparateOrderDAO separateOrderDAO;

    @Override
    public Boolean isOrderExist(String orderNumber) {
        if (null != orderNumber) {
            return separateOrderDAO.isOrderExist(orderNumber);
        }
        return null;
    }

    @Override
    public void saveOrderBaseInf(OrderBaseInf baseInf) {
        if (null != baseInf && null != baseInf.getOrderNumber()) {
            separateOrderDAO.saveOrderBaseInf(baseInf);
        }
    }
}
