package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.AdminUserStoreDO;
import cn.com.leyizhuang.app.foundation.vo.management.AdminUserStoreVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/2/26
 */
@Repository
public interface AdminUserStoreDAO {

    int batchSave(@Param("list") List<AdminUserStoreDO> adminUserStoreDOList);

    List<AdminUserStoreVO> findByUid(Long uid);

    List<AdminUserStoreDO> findAdminUserStoreDOByUid(Long uid);

    int batchDelete(@Param("list") List<AdminUserStoreDO> adminUserStoreDOList);

}
