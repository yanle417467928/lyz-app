package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.pojo.SellDetailsDO;

import java.util.List;

/**
 * 统计销量明细服务
 * Created by panjie on 2018/1/24.
 */
public interface StatisticsSellDetailsService {

    /**
     * 统计上月 1号0点 到 月末最后一天 23点59分 的销量明细
     */
    List<SellDetailsDO> statisticsCurrentDetails();

    /**
     * 新增明细
     * @param sellDetailsDOS
     */
    void addSellDetails(List<SellDetailsDO> sellDetailsDOS);

    /**
     * 记录下单销量明细
     */
    void addOrderSellDetails(String orderNumber);

    /**
     * 记录退单销量明细
     */
    void addReturnOrderSellDetails(String returnOrderNo);
}
