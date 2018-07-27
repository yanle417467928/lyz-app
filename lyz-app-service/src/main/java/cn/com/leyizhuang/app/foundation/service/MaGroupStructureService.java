package cn.com.leyizhuang.app.foundation.service;





import cn.com.leyizhuang.app.foundation.pojo.management.structure.SimpaleGroupStructureParam;
import cn.com.leyizhuang.app.foundation.pojo.management.structure.Structure;

import java.util.List;

public interface MaGroupStructureService {

    List<SimpaleGroupStructureParam> querySimpaleStructureList();

    List<SimpaleGroupStructureParam> querySimpaleStructureListByFilter(List<String> structureCodeList);

    Structure querySimpaleStructureByStructureCode(String structureCode);
}
