package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.PhysicalClassifyDAO;
import cn.com.leyizhuang.app.foundation.pojo.PhysicalClassify;
import cn.com.leyizhuang.app.foundation.service.PhysicalClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by caiyu on 2017/11/27.
 */
@Service
public class PhysicalClassifyServiceImpl implements PhysicalClassifyService {
    @Autowired
    private PhysicalClassifyDAO physicalClassifyDAO;

    @Override
    public void saveSynchronize(PhysicalClassify physicalClassify) {
        physicalClassifyDAO.saveSynchronize(physicalClassify);
    }

    @Override
    public void modifySynchronize(PhysicalClassify physicalClassify) {
        physicalClassifyDAO.modifySynchronize(physicalClassify);
    }

    @Override
    public void deleteSynchronize(Long hqId) {
        physicalClassifyDAO.deleteSynchronize(hqId);
    }

    @Override
    public PhysicalClassify findByHqId(Long hqId) {
        return physicalClassifyDAO.findByHqId(hqId);
    }
}
