package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.pojo.SellDetailsDO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
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
     * orderNumeber 订单号
     */
    void addOrderSellDetails(String orderNumber);

    /**
     * 记录退单销量明细
     * returnOrderNo 退单号
     */
    void addReturnOrderSellDetails(String returnOrderNo);

    /**
     * 查询用户近期90天内的4单 单号
     * @param cusId
     * @return
     */
    List<String> getCustomerSellDetailsOrderByCreateTimeDescLimit4(Long cusId, LocalDateTime dateTime,Long sellerId);

    /**
     * 查询一段时间内 一个顾客在一个导购下产生销量的月份频次 不能超过365天
     * @param cusId
     * @param dateTime
     * @param sellerId
     * @return
     */
    List<String> getSellDetailsFrequencyBycusIdAndSellerIdAndCreateTime(Long cusId,  LocalDateTime dateTime,  Long sellerId);

    /**
     * 记录错误日志
     * @param orderNo
     */
    void recordeErrorLog(String orderNo);
}
