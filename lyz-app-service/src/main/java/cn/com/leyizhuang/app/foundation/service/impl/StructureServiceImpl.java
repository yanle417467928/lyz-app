package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaGroupStructureDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.structure.Structure;
import cn.com.leyizhuang.app.foundation.service.StructureService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class StructureServiceImpl implements StructureService {

    private final Logger LOG = LoggerFactory.getLogger(StructureServiceImpl.class);

    @Autowired
    private MaGroupStructureDAO maGroupStructureDAO;

    @Override
    @Transactional
    public void SaveStructure(Structure structure) {
        maGroupStructureDAO.SaveStructure(structure);
    }

    @Override
    public void ModifyStructure(Structure structure) {
        maGroupStructureDAO.ModifyStructure(structure);
    }


    @Override
    public void delStructure(String structureNumber) {
        maGroupStructureDAO.delStructure(structureNumber);
    }

    @Override
    public Structure findByStructureNumber(String structureNumber) {
        return maGroupStructureDAO.findByStructureNumber(structureNumber);
    }
}
