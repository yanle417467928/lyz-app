package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.EtaReturnAndRequireGoodsInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.EtaReturnAndRequireGoodsInfLog;

public interface DiySiteInventoryEbsService {
    EtaReturnAndRequireGoodsInf findByTransId(String transId);

    void saveReturnAndRequireGoodsInf(EtaReturnAndRequireGoodsInf etaReturnAndRequireGoodsInf);


    void saveReturnAndRequireGoodsInfLog(EtaReturnAndRequireGoodsInfLog etaReturnAndRequireGoodsInfLog);

}
