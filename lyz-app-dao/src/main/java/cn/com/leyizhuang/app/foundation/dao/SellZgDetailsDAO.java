package cn.com.leyizhuang.app.foundation.dao;

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

    List<SellZgDetailsDO> getDetailsByCusId(@Param("cusId") Long cusId);

    List<SellZgDetailsDO> getDetailsByCusIdAndSku(@Param("cusId") Long cusId, @Param("sku") String sku);
}
