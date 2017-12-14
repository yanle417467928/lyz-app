package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.StorePreDepositChangeType;
import cn.com.leyizhuang.app.foundation.dao.StorePreDepositLogDAO;
import cn.com.leyizhuang.app.foundation.pojo.StPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.pojo.StorePreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.response.PreDepositLogResponse;
import cn.com.leyizhuang.app.foundation.service.StorePreDepositLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/8
 */
@Service
@Transactional
public class StorePreDepositLogServiceImpl implements StorePreDepositLogService {

    @Autowired
    private StorePreDepositLogDAO storePreDepositLogDAO;

    @Override
    public List<PreDepositLogResponse> findByUserIdAndType(Long userId, List<StorePreDepositChangeType> typeList) {
        return this.storePreDepositLogDAO.findByUserIdAndType(userId, typeList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StPreDepositLogDO save(StPreDepositLogDO stPreDepositLogDO) {
        this.storePreDepositLogDAO.save(stPreDepositLogDO);
        return stPreDepositLogDO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StorePreDeposit findStoreByUserId(Long userId) {
        return this.storePreDepositLogDAO.findStoreByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStPreDepositByUserId(Double money, Long userId) {
        this.storePreDepositLogDAO.updateStPreDepositByUserId(money,userId);
    }
}
