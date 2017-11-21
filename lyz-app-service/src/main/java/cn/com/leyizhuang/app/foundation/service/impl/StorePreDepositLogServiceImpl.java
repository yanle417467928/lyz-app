package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.pojo.CusPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.pojo.StPreDepositLogDO;
import cn.com.leyizhuang.common.core.constant.PreDepositChangeType;
import cn.com.leyizhuang.app.foundation.dao.StorePreDepositLogDAO;
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
    public List<PreDepositLogResponse> findByUserIdAndType(Long userId, List<PreDepositChangeType> typeList) {
        return this.storePreDepositLogDAO.findByUserIdAndType(userId, typeList);
    }

    @Override
    public StPreDepositLogDO save(StPreDepositLogDO stPreDepositLogDO) {
        this.storePreDepositLogDAO.save(stPreDepositLogDO);
        return stPreDepositLogDO;
    }
}
