package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.AreaManagementDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/4/8
 */
@Repository
public interface AreaManagementDAO {

    List<String> findAreaManagementByParentCodeAndLevelIsFive(String parentCode);

    List<AreaManagementDO> findAreaManagementByCityId(Long cityId);

}
