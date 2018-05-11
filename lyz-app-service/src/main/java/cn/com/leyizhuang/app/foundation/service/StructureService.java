package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.pojo.management.structure.Structure;

public interface StructureService {

    Structure findByStructureNumber(String structureNumber);

    void SaveStructure(Structure structure);

    void ModifyStructure(Structure structure);

    void delStructure(String structureNumber);
}
