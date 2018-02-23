package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.StorePreDepositChangeType;
import cn.com.leyizhuang.app.foundation.pojo.StPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.pojo.StorePreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.response.PreDepositLogResponse;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/8
 */
public interface StorePreDepositLogService {

    PageInfo<PreDepositLogResponse> findByUserIdAndType(Long userId, List<StorePreDepositChangeType> typeList, Integer page, Integer size);
    /**
     * 查询门店预存款信息
     * @param userId    用户id
     * @param typeList  变更类型
     * @return  门店预存款信息
     */
    PageInfo<PreDepositLogResponse> findPreDepositChangeLog(Long userId, List<StorePreDepositChangeType> typeList,Integer page, Integer size);

    StPreDepositLogDO save(StPreDepositLogDO stPreDepositLogDO);
    /**
     * 根据导购id查询门店预存款信息
     * @param userId    用户id
     * @return  门店预存款信息
     */
    StorePreDeposit findStoreByUserId(Long userId);

    /**
     * 修改门店预存款
     * @param money 修改后金额
     * @param userId    用户id
     */
    void updateStPreDepositByUserId(Double money,Long userId);

    Integer updateStPreDepositByStoreIdAndVersion(Double money,Long userId, Date version);
}
