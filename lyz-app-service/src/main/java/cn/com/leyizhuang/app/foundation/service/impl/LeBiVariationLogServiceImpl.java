package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.LeBiVariationLogDAO;
import cn.com.leyizhuang.app.foundation.pojo.CustomerLeBiVariationLog;
import cn.com.leyizhuang.app.foundation.service.LeBiVariationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *
 * @author caiyu
 * @date 2017/11/8
 */
@Service
public class LeBiVariationLogServiceImpl implements LeBiVariationLogService {
    @Autowired
    private LeBiVariationLogDAO leBiVariationLogDAO;

    @Override
    public void addCustomerLeBiVariationLog(CustomerLeBiVariationLog customerLeBiVariationLog) {
        leBiVariationLogDAO.addCustomerLeBiVariationLog(customerLeBiVariationLog);
    }

    @Override
    public List<CustomerLeBiVariationLog> queryListBycusID(Long cusId) {
        return leBiVariationLogDAO.queryListBycusID(cusId);
    }

    @Override
    public List<CustomerLeBiVariationLog> queryListBycusIDAndShowTypeType(Long cusId, Integer showType) {
        return leBiVariationLogDAO.queryListBycusIDAndShowTypeType(cusId, showType);
    }

    @Override
    public void updateLeBiQtyByUserId(Integer quantity, Date lastUpdateTime, Long customerId) {
        leBiVariationLogDAO.updateLeBiQtyByUserId(quantity,lastUpdateTime,customerId);
    }
}
