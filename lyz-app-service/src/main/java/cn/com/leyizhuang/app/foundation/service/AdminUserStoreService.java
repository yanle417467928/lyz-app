package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.foundation.pojo.management.AdminUserStoreDO;
import cn.com.leyizhuang.app.foundation.vo.management.AdminUserStoreVO;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/2/27
 */
public interface AdminUserStoreService {

    int batchSave(List<AdminUserStoreDO> adminUserStoreDOList);

    /**
     * @title   通过userid获取权限门店
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/2/28
     */
    List<AdminUserStoreVO> findByUid(Long uid);

    void batchUpdateAndSave(List<AdminUserStoreDO> adminUserStoreDOList, Long uid);

    int batchDelete(List<AdminUserStoreDO> adminUserStoreDOList);

    /**
     * @title   通过userId查询权限门店ID列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/2/28
     */
    List<Long> findStoreIdByUid(Long uid);

    /**
     * @title   查询当前登录用户权限门店ID列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/2/28
     */
    List<Long> findStoreIdList();

    /**
     * @title   查询当前登录用户权限直营门店ID列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/2/28
     */
    List<Long> findZYStoreIdList();

    List<Long> findStoreIdByUidAndStoreType(List<StoreType> storeTypes);

    List<Long>  findStoreIdByAdminAndStoreType(List<StoreType> storeTypes);

}
