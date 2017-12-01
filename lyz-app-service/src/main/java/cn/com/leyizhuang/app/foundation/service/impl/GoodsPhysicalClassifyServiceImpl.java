package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.GoodsPhysicalClassifyDAO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPhysicalClassify;
import cn.com.leyizhuang.app.foundation.service.GoodsPhysicalClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by caiyu on 2017/11/27.
 */
@Service
public class GoodsPhysicalClassifyServiceImpl implements GoodsPhysicalClassifyService {
    @Autowired
    private GoodsPhysicalClassifyDAO goodsPhysicalClassifyDAO;

    @Override
    public void saveSynchronize(GoodsPhysicalClassify goodsPhysicalClassify) {
        goodsPhysicalClassifyDAO.saveSynchronize(goodsPhysicalClassify);
    }

    @Override
    public void modifySynchronize(GoodsPhysicalClassify goodsPhysicalClassify) {
        goodsPhysicalClassifyDAO.modifySynchronize(goodsPhysicalClassify);
    }

    @Override
    public void deleteSynchronize(Long hqId) {
        goodsPhysicalClassifyDAO.deleteSynchronize(hqId);
    }

    @Override
    public GoodsPhysicalClassify findByHqId(Long hqId) {
        return goodsPhysicalClassifyDAO.findByHqId(hqId);
    }
}
