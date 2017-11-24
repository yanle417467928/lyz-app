package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.OrderAgencyFundDAO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderAgencyFundDO;
import cn.com.leyizhuang.app.foundation.service.OrderAgencyFundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author GenerationRoad
 * @date 2017/11/24
 */
@Service
@Transactional
public class OrderAgencyFundServiceImpl implements OrderAgencyFundService {

    @Autowired
    private OrderAgencyFundDAO orderAgencyFundDAO;


    @Override
    public OrderAgencyFundDO save(OrderAgencyFundDO orderAgencyFundDO) {
        this.orderAgencyFundDAO.save(orderAgencyFundDO);
        return orderAgencyFundDO;
    }
}
