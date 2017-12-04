package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.OrderReturnDeliveryInfoDetailsDAO;
import cn.com.leyizhuang.app.foundation.service.OrderReturnDeliveryInfoDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Jerry.Ren
 * Notes: 退单物流详情实现
 * Created with IntelliJ IDEA.
 * Date: 2017/12/4.
 * Time: 14:02.
 */
@Service
public class OrderReturnDeliveryInfoDetailsServiceImpl implements OrderReturnDeliveryInfoDetailsService {

    @Resource
    private OrderReturnDeliveryInfoDetailsDAO returnDeliveryInfoDetailsDAO;
}
