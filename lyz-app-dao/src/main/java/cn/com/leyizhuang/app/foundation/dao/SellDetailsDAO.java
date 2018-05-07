package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.response.SellDetailsResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellDetailsWeekFinishResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by panjie on 2018/1/24.
 */
@Repository
public interface SellDetailsDAO {

    int addOneDetail(SellDetailsDO DO);

    void update(SellDetailsDO DO);

    void addSellDetailsSingle(SellDetailsSingleDO DO);

    void updateSellDetailsSingle(SellDetailsSingleDO DO);

    void addSellDetailsWeekFinish(SellDetailsWeekFinishResponse response);

    void updateSellDetailsWeekFinish(SellDetailsWeekFinishResponse response);

    void addEmpPerformanceStatisticErrorLog(SellDetailsStatisticErrorLog log);

    Long isExitSellDetailsSingle(@Param("year") Integer year,@Param("month") Integer month,@Param("sellerId") Long sellerId,@Param("flag") String flag);

    Long isExitSellDetailsWeek(@Param("headId") Long headId , @Param("week") Integer week);
    /**
     * 查询某年某月销售明细
     *
     * @param year
     * @param month
     * @return
     */
    List<SellDetailsDO> queryOneMonthSellDetails(@Param("year") Integer year, @Param("month") Integer month);

    /**
     * 查询未结清的销量数据 id
     */
    List<Long> getNotPayUpSellDetails();

    /**
     * 查询产品券 销量金额为0数据
     */
    List<SellDetailsDO> getProductSellDetails();

    /**
     * 删除销量数据
     * @param ids
     */
    void deleteSellDetailsByIdList(@Param("ids") List<Long> ids);

    void deleteSellDetailsById(Long id);
    /**
     * 根据单号和明细行判断，是否已经记录此销售明细
     *
     * @param number
     * @param lineId
     * @return
     */
    Boolean isExitByNumberAndGoodsLineId(@Param("number") String number, @Param("lineId") Long lineId);

    /**
     * 根据顾客id 查询顾客销量记录 倒叙排列取四条
     *
     * @param cusId
     * @param dateTime **天前的日期
     * @param sellerId 导购id
     * @return
     */
    List<String> getCustomerSellDetailsOrderByCreateTimeDescLimit4(@Param("cusId") Long cusId, @Param("dateTime") LocalDateTime dateTime, @Param("sellerId") Long sellerId);

    /**
     * 查询某导购三个月未下单自动分配到默认门店的会员
     *
     * @param sellerId
     * @param sellerId
     * @return
     */
    List<AppCustomer> getCustomerIsDefaultStoreAndNoSellDetailsOrder(Long sellerId);
    /**
     * 查询一段时间内 一个顾客在一个导购下产生销量的月份频次
     * @param cusId
     * @param dateTime
     * @param sellerId
     * @return
     */
    List<String> getSellDetailsFrequencyBycusIdAndSellerIdAndCreateTime(@Param("cusId") Long cusId, @Param("dateTime") LocalDateTime dateTime, @Param("sellerId") Long sellerId);

    /**
     * 销量生成失败日志记录
     * @param logDO
     */
    void recordeErrorLog(SellDetailsErrorLogDO logDO);

    /**
     * 统计某导购高端桶数
     * @param flag TS桶数；HYS活跃数； XKF新开发活跃会员数；
     * @param sellerId
     */
    SellDetailsResponse statisticsSellDetailsSingle(@Param("year") Integer year ,@Param("month") Integer month,@Param("sellerId") Long sellerId,@Param("flag") String flag);

    /**
     * 查询周完成情况
     */
    List<SellDetailsWeekFinishResponse> getWeekFinishDetails(Long headId);

    /**
     * 返回导购一段时间内的高端桶数
     */
    Integer countGDTS(@Param("startTime") LocalDateTime startTime,@Param("endTime") LocalDateTime endTime,@Param("sellerId") Long sellerId,@Param("structureCode") String structureCode,@Param("flag") String flag);

    /**
     * 返回导购下活跃会员数
     * @param startTime
     * @param endTime
     * @param sellerId
     * @return
     */
    List<Long> countHYS(@Param("startTime") LocalDateTime startTime,@Param("endTime") LocalDateTime endTime,@Param("sellerId") Long sellerId,@Param("structureCode") String structureCode);


    List<Long> countXKF(@Param("firstDay") LocalDateTime firstDay,@Param("startTime") LocalDateTime startTime,@Param("endTime") LocalDateTime endTime,@Param("halfYearAgoDate") LocalDateTime halfYearAgoDate,@Param("sellerId") Long sellerId,@Param("structureCode") String structureCode);

    List<SellDetailsResponse> getFgsRank(@Param("year") Integer year ,@Param("month") Integer month,@Param("structureCode") String structureCode,@Param("flag") String flag);

    List<SellDetailsResponse> getJtRank(@Param("year") Integer year ,@Param("month") Integer month,@Param("flag") String flag);

    List<Long> cusIdFiltration(@Param("list") List<Long> ids,@Param("empId") Long empId);
    /******              专供销量                *******/

    /**
     * 插入专供销量
     */
    void insertZgSellDetails(SellZgDetailsDO detailsDO);

    /**
     * 将未结清订单 销量记录标志
     */
    void updateOrderRecordFlagFalse();
}
