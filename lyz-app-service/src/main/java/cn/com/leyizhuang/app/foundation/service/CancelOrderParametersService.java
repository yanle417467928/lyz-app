package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.CancelOrderParametersDO;
import org.springframework.stereotype.Service;

/**
 * Created by caiyu on 2018/1/29.
 */

public interface CancelOrderParametersService {
    /**
     * 添加保存取消订单参数
     * @param cancelOrderParametersDO
     */
    void addCancelOrderParameters(CancelOrderParametersDO cancelOrderParametersDO);

    /**
     * 根据订单号查询取消订单参数
     * @param orderNumber
     * @return
     */
    CancelOrderParametersDO findCancelOrderParametersByOrderNumber(String orderNumber);

    /**
     * 修改取消订单处理状态
     * @param orderNumber
     */
    void updateCancelStatusByOrderNumber(String orderNumber);
}
