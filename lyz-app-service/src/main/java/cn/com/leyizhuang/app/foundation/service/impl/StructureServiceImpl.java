package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.StructureDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.structure.Structure;
import cn.com.leyizhuang.app.foundation.service.StructureService;
import cn.com.leyizhuang.common.core.exception.biz.UnauthorisedException;
import cn.com.leyizhuang.common.core.exception.data.InvalidDataException;
import cn.com.leyizhuang.common.foundation.pojo.dto.HqAppStructureDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class StructureServiceImpl implements StructureService {

    private final Logger LOG = LoggerFactory.getLogger(StructureServiceImpl.class);

    @Autowired
    private StructureDAO structureDAO;

    @Override
    @Transactional
    public void SaveStructure(Structure structure) {
        structureDAO.SaveStructure(structure);
    }

    @Override
    public void ModifyStructure(Structure structure) {
        structureDAO.ModifyStructure(structure);
    }


    @Override
    public void delStructure(String structureNumber) {
        structureDAO.delStructure(structureNumber);
    }

    @Override
    public Structure findByStructureNumber(String structureNumber) {
        return structureDAO.findByStructureNumber(structureNumber);
    }
}
