package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.SellDetailsDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by panjie on 2018/1/24.
 */
@Repository
public interface SellDetailsDAO {

    int addOneDetail(SellDetailsDO DO);

    /**
     * 查询某年某月销售明细
     * @param year
     * @param month
     * @return
     */
    List<SellDetailsDO> queryOneMonthSellDetails(@Param("year") Integer year, @Param("month") Integer month);

    /**
     * 根据单号和明细行判断，是否已经记录此销售明细
     * @param number
     * @param lineId
     * @return
     */
    Boolean isExitByNumberAndGoodsLineId(@Param("number") String number ,@Param("lineId") Long lineId);
}
