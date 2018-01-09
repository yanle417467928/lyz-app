package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.LeBiVariationLogDAO;
import cn.com.leyizhuang.app.foundation.pojo.CustomerLeBiVariationLog;
import cn.com.leyizhuang.app.foundation.service.LeBiVariationLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public void addCustomerLeBiVariationLog(CustomerLeBiVariationLog customerLeBiVariationLog) {
        leBiVariationLogDAO.addCustomerLeBiVariationLog(customerLeBiVariationLog);
    }

    @Override
    public List<CustomerLeBiVariationLog> queryListBycusID(Long cusId) {
        return leBiVariationLogDAO.queryListBycusID(cusId);
    }

    @Override
    public PageInfo<CustomerLeBiVariationLog> queryListBycusIDAndShowTypeType(Long cusId, Integer showType, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<CustomerLeBiVariationLog> customerLeBiVariationLogList = leBiVariationLogDAO.queryListBycusIDAndShowTypeType(cusId, showType);
        return new PageInfo<>(customerLeBiVariationLogList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLeBiQtyByUserId(Integer quantity, Date lastUpdateTime, Long customerId) {
        leBiVariationLogDAO.updateLeBiQtyByUserId(quantity,lastUpdateTime,customerId);
    }
}
