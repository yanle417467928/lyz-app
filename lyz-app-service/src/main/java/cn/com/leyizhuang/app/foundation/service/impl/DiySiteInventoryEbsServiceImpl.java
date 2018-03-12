package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.DiySiteInventoryDAO;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.EtaReturnAndRequireGoodsInf;
import cn.com.leyizhuang.app.foundation.service.DiySiteInventoryEbsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DiySiteInventoryEbsServiceImpl implements DiySiteInventoryEbsService {
    @Resource
    DiySiteInventoryDAO diySiteInventoryDAO;
    @Override
    public EtaReturnAndRequireGoodsInf findByTransId(String transId) {
        return diySiteInventoryDAO.findByTransId(transId);
    }

    @Override
    public void saveReturnAndRequireGoodsInf(EtaReturnAndRequireGoodsInf etaReturnAndRequireGoodsInf) {
         diySiteInventoryDAO.saveReturnAndRequireGoodsInf(etaReturnAndRequireGoodsInf);
    }
}
