package cn.com.leyizhuang.app.foundation.dao;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/4/8
 */
@Repository
public interface AreaManagementDAO {

    List<String> findAreaManagementByParentCodeAndLevelIsFive(String parentCode);

}
