package cn.com.leyizhuang.app.foundation.service;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/4/8
 */
public interface AreaManagementService {
    List<String> findAreaManagementByParentCodeAndLevelIsFive(String parentCode);
}
