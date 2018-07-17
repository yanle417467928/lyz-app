package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.structure.SimpaleGroupStructureParam;
import cn.com.leyizhuang.app.foundation.pojo.management.structure.Structure;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaGroupStructureDAO {

    List<SimpaleGroupStructureParam> querySimpaleStructureList();

    List<SimpaleGroupStructureParam> querySimpaleStructureListByFilter(@Param("list") List<String> structureCodeList);

    Structure findByStructureNumber(@Param(value = "structureNumber") String structureNumber);

    void SaveStructure(Structure structure);

    void ModifyStructure(Structure structure);

    void delStructure( @Param(value = "structureNumber") String structureNumber);
}
