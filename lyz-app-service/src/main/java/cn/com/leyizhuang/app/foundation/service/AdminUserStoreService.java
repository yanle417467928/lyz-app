package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.AdminUserStoreDO;
import cn.com.leyizhuang.app.foundation.vo.management.AdminUserStoreVO;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/2/27
 */
public interface AdminUserStoreService {

    int batchSave(List<AdminUserStoreDO> adminUserStoreDOList);

    List<AdminUserStoreVO> findByUid(Long uid);

}
