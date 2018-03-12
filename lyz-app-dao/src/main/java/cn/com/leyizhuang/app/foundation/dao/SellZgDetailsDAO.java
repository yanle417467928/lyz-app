package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.SellZgDetailsDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by panjie on 2018/3/12.
 */
@Repository
public interface SellZgDetailsDAO {

    int addOneDetail(SellZgDetailsDO DO);

    List<SellZgDetailsDO> getDetailsByCusIdAndSku(Long cusId,String sku);
}
