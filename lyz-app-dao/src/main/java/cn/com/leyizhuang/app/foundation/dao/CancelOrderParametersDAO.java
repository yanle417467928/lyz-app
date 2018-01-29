package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.CancelOrderParametersDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by caiyu on 2018/1/29.
 */
@Repository
public interface CancelOrderParametersDAO {
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
    CancelOrderParametersDO findCancelOrderParametersByOrderNumber(@Param("orderNumber") String orderNumber);

    /**
     * 修改取消订单处理状态
     * @param orderNumber
     */
    void updateCancelStatusByOrderNumber(@Param("orderNumber") String orderNumber);
}
