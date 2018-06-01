package cn.com.leyizhuang.app.foundation.service.impl;


import cn.com.leyizhuang.app.foundation.dao.MaGroupStructureDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.structure.SimpaleGroupStructureParam;
import cn.com.leyizhuang.app.foundation.service.MaGroupStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaGroupStructureServiceImpl implements MaGroupStructureService {

    @Autowired
    private MaGroupStructureDAO maGroupStructureDAO;


    @Override
    public List<SimpaleGroupStructureParam> querySimpaleStructureList() {
        List<SimpaleGroupStructureParam> simpaleStructureList = this.maGroupStructureDAO.querySimpaleStructureList();
        return simpaleStructureList;
    }


}