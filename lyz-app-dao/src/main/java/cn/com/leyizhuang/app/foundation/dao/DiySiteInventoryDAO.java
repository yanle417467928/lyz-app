package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.EtaReturnAndRequireGoodsInf;
import org.springframework.stereotype.Repository;

@Repository
public interface DiySiteInventoryDAO {

    EtaReturnAndRequireGoodsInf findByTransId(String transId);

    void saveReturnAndRequireGoodsInf(EtaReturnAndRequireGoodsInf etaReturnAndRequireGoodsInf);

}
