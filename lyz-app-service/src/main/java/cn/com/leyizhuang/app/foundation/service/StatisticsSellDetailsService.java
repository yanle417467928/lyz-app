package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.core.constant.ActBaseType;
import cn.com.leyizhuang.app.foundation.pojo.SellDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.SellZgCusTimes;
import cn.com.leyizhuang.app.foundation.pojo.SellZgDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.response.SellDetailsResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import com.github.pagehelper.PageInfo;

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
    void addOrderSellDetails(String orderNumber) throws Exception;

    /**
     * 记录退单销量明细
     * returnOrderNo 退单号
     */
    void addReturnOrderSellDetails(String returnOrderNo) throws Exception;

    /**
     * 查询用户近期90天内的4单 单号
     * @param cusId
     * @return
     */
    List<String> getCustomerSellDetailsOrderByCreateTimeDescLimit4(Long cusId, LocalDateTime dateTime,Long sellerId);

    /**
     * 查询某导购三个月未下单自动分配到默认门店的会员
     *
     * @param sellerId
     * @param sellerId
     * @return
     */
    PageInfo<AppCustomer> getCustomerIsDefaultStoreAndNoSellDetailsOrder(Long sellerId, Integer page, Integer size);

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
    void recordeErrorLog(String orderNo,String msg);

    /****** 专供 ********/

    /**
     * 新增
     * @param detailsDOS
     */
    void addZgDetailsList(List<SellZgDetailsDO> detailsDOS);

    /**
     * 返回会员专供产品累计桶数
     * @return
     */
    Integer getZgTsBycusIdAndsku(Long cusId,String sku);

    /**
     * 根据专供会员id和返回专供销量结果结果
     * @param cusId
     * @param sku
     * @return
     */
    List<SellZgDetailsDO> getZgDetailsByCusIdAndSku(Long cusId, String sku);

    List<SellZgDetailsDO> getZgDetailsByCusId(Long cusId);

    SellZgCusTimes getTimesByCusIdAndSku(Long cusId, String sku, ActBaseType actBaseType);

    void updateSellZgCusTimes(SellZgCusTimes sellZgCusTimes);

    void addSellZgCusTimes(SellZgCusTimes sellZgCusTimes);

    void addOrUpdateSellZgCusTimes(Long cusId,String sku,Integer times,Integer qty,ActBaseType type);

    void statisticsAllSellerSellDetails(List<String> structureCode);

    void statisticOneSeller(Long empId);

    SellDetailsResponse currentTsSellDetails(Long empId);

    SellDetailsResponse currentHYS(Long empId);

    SellDetailsResponse currentXKF(Long empId);

    List<SellDetailsResponse> getFgsRank(Long empId,String flag);

    List<SellDetailsResponse> getJtRank(String flag);

    void createAllOrderDetails() ;

    void createAllreturnOrderDetails() ;

    void repairAllOrderDetails();
}
