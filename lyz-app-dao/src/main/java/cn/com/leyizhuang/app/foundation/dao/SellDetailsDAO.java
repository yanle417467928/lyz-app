package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.SellDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.SellDetailsErrorLogDO;
import cn.com.leyizhuang.app.foundation.pojo.SellZgDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.response.SellDetailsResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellDetailsWeekFinishResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.tomcat.jni.Local;
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
    /**
     * 查询某年某月销售明细
     *
     * @param year
     * @param month
     * @return
     */
    List<SellDetailsDO> queryOneMonthSellDetails(@Param("year") Integer year, @Param("month") Integer month);

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
    Integer countHYS(@Param("startTime") LocalDateTime startTime,@Param("endTime") LocalDateTime endTime,@Param("sellerId") Long sellerId);

    /******              专供销量                *******/

    /**
     * 插入专供销量
     */
    void insertZgSellDetails(SellZgDetailsDO detailsDO);
}
