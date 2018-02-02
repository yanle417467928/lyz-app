package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.ReturnOrderDeliveryInfoDetailsDAO;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderDeliveryDetail;
import cn.com.leyizhuang.app.foundation.service.ReturnOrderDeliveryDetailsService;
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
public class ReturnOrderDeliveryDetailsServiceImpl implements ReturnOrderDeliveryDetailsService {

    @Resource
    private ReturnOrderDeliveryInfoDetailsDAO returnDeliveryInfoDetailsDAO;

    @Override
    public ReturnOrderDeliveryDetail getReturnLogisticStatusDetail(String returnNumber) {
        if (StringUtils.isNotBlank(returnNumber)) {
            return returnDeliveryInfoDetailsDAO.getReturnLogisticStatusDetail(returnNumber);
        }
        return null;
    }

    @Override
    public void addReturnOrderDeliveryInfoDetails(ReturnOrderDeliveryDetail returnOrderDeliveryDetail) {
        returnDeliveryInfoDetailsDAO.save(returnOrderDeliveryDetail);
    }
}
