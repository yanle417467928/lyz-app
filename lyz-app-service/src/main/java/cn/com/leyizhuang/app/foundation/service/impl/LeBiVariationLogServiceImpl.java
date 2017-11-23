package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.LeBiVariationLogDAO;
import cn.com.leyizhuang.app.foundation.pojo.CustomerLeBiVariationLog;
import cn.com.leyizhuang.app.foundation.service.LeBiVariationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by caiyu on 2017/11/8.
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
    public List<CustomerLeBiVariationLog> queryListBycusID(Long cusID) {
        return leBiVariationLogDAO.queryListBycusID(cusID);
    }

    @Override
    public List<CustomerLeBiVariationLog> queryListBycusIDAndShowTypeType(Long cusID, Integer showType) {
        return leBiVariationLogDAO.queryListBycusIDAndShowTypeType(cusID, showType);
    }
}
