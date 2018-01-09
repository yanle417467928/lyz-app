package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.OrderAgencyFundDAO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderAgencyFundDO;
import cn.com.leyizhuang.app.foundation.pojo.response.DeliveryAgencyFundResponse;
import cn.com.leyizhuang.app.foundation.service.OrderAgencyFundService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public List<DeliveryAgencyFundResponse> findByUserId(Long userId) {
        return this.orderAgencyFundDAO.findByUserId(userId);
    }

    @Override
    public PageInfo<DeliveryAgencyFundResponse> findByUserIdAndCreateTime(Long userId, String startDate, String endDate, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<DeliveryAgencyFundResponse> deliveryAgencyFundResponseList = this.orderAgencyFundDAO.findByUserIdAndCreateTime(userId, startDate, endDate);
         return new PageInfo<>(deliveryAgencyFundResponseList);
    }
}
