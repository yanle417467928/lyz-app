package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.ActBaseType;
import cn.com.leyizhuang.app.foundation.pojo.SellZgCusTimes;
import cn.com.leyizhuang.app.foundation.pojo.SellZgDetailsDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by panjie on 2018/3/12.
 */
@Repository
public interface SellZgDetailsDAO {

    int addOneDetail(SellZgDetailsDO DO);

    void addSellZgCusTimes(SellZgCusTimes  sellZgCusTimes);

    List<SellZgDetailsDO> getDetailsByCusId(@Param("cusId") Long cusId);

    List<SellZgDetailsDO> getDetailsByCusIdAndSku(@Param("cusId") Long cusId, @Param("sku") String sku);

    Integer getQtyByCusIdAndSku(@Param("cusId") Long cusId, @Param("sku") String sku);

    /**
     * 检查是否存在
     * @param number
     * @param lineId
     * @return
     */
    Boolean isExitByNumberAndGoodsLineId(@Param("number") String number, @Param("lineId") Long lineId);

    /**
     * 获取某人某专供产品的 累积促销参与次数
     * @param cusId
     * @param sku
     * @return
     */
    SellZgCusTimes getTimesByCusIdAndSku(@Param("cusId") Long cusId, @Param("sku") String sku, @Param("actBaseType") ActBaseType actBaseType);

    /**
     * 更新用户参与促销次数
     */
    void updateSellZgCusTimes(SellZgCusTimes sellZgCusTimes);
}
