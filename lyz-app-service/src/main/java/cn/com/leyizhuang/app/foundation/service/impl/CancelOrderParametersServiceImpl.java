package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.CancelOrderParametersDAO;
import cn.com.leyizhuang.app.foundation.pojo.CancelOrderParametersDO;
import cn.com.leyizhuang.app.foundation.service.CancelOrderParametersService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by caiyu on 2018/1/29.
 */
@Service("cancelOrderParametersService")
public class CancelOrderParametersServiceImpl implements CancelOrderParametersService {
    @Resource
    private CancelOrderParametersDAO cancelOrderParametersDAO;
    @Override
    public void addCancelOrderParameters(CancelOrderParametersDO cancelOrderParametersDO) {
        this.cancelOrderParametersDAO.addCancelOrderParameters(cancelOrderParametersDO);
    }

    @Override
    public CancelOrderParametersDO findCancelOrderParametersByOrderNumber(String orderNumber) {
        return this.cancelOrderParametersDAO.findCancelOrderParametersByOrderNumber(orderNumber);
    }

    @Override
    public void updateCancelStatusByOrderNumber(String orderNumber) {
        this.cancelOrderParametersDAO.updateCancelStatusByOrderNumber(orderNumber);
    }
}
