package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.AreaManagementDO;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/4/8
 */
public interface AreaManagementService {
    List<String> findAreaManagementByParentCodeAndLevelIsFive(String parentCode);

    List<AreaManagementDO> findAreaManagementByCityId(Long cityId);

}
