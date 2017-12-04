package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.CustomerPreDepositChangeType;
import cn.com.leyizhuang.app.foundation.dao.CusPreDepositLogDAO;
import cn.com.leyizhuang.app.foundation.pojo.CusPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.pojo.response.PreDepositLogResponse;
import cn.com.leyizhuang.app.foundation.service.CusPreDepositLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/7
 */
@Service
@Transactional
public class CusPreDepositLogServiceImpl implements CusPreDepositLogService {

    @Autowired
    private CusPreDepositLogDAO cusPreDepositLogDAO;

    @Override
    public List<PreDepositLogResponse> findByUserIdAndType(Long userId, List<CustomerPreDepositChangeType> typeList) {
        return this.cusPreDepositLogDAO.findByUserIdAndType(userId, typeList);
    }

    @Override
    public CusPreDepositLogDO save(CusPreDepositLogDO cusPreDepositLogDO) {
        this.cusPreDepositLogDAO.save(cusPreDepositLogDO);
        return cusPreDepositLogDO;
    }


}
