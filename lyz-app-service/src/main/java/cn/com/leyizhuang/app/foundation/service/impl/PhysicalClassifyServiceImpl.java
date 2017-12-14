package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaPhysicalClassifyDAO;
import cn.com.leyizhuang.app.foundation.pojo.goods.PhysicalClassify;
import cn.com.leyizhuang.app.foundation.service.MaPhysicalClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PhysicalClassifyServiceImpl implements MaPhysicalClassifyService {

    @Autowired
    private MaPhysicalClassifyDAO physicalClassifyDAO;

    @Override
    public List<PhysicalClassify> findPhysicalClassifyList(){
        List<PhysicalClassify> physicalClassifyList = physicalClassifyDAO.findPhysicalClassifyList();
        return physicalClassifyList;
    }
}
