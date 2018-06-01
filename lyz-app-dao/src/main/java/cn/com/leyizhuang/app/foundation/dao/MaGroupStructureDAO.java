package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.structure.SimpaleGroupStructureParam;
import cn.com.leyizhuang.app.foundation.pojo.management.structure.Structure;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaGroupStructureDAO {

    List<SimpaleGroupStructureParam> querySimpaleStructureList();

    Structure findByStructureNumber(String structureNumber);

    void SaveStructure(Structure structure);

    void ModifyStructure(Structure structure);

    void delStructure(String structureNumber);
}