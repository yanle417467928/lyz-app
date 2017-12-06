package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.ReturnOrderDeliveryInfoDetailsDAO;
import cn.com.leyizhuang.app.foundation.service.ReturnOrderDeliveryInfoDetailsService;
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
public class ReturnOrderDeliveryInfoDetailsServiceImpl implements ReturnOrderDeliveryInfoDetailsService {

    @Resource
    private ReturnOrderDeliveryInfoDetailsDAO returnDeliveryInfoDetailsDAO;
}
