package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.BillRuleDAO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRuleDO;
import cn.com.leyizhuang.app.foundation.service.BillRuleService;
import cn.com.leyizhuang.app.foundation.vo.management.BillRuleVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/6/26
 */
@Service("billRuleService")
public class BillRuleServiceImpl implements BillRuleService {

    @Autowired
    private BillRuleDAO billRuleDAO;

    @Override
    public BillRuleDO getBillRuleByStoreId(Long storeId) {
        return this.billRuleDAO.getBillRuleByStoreId(storeId);
    }

    @Override
    public List<BillRuleDO> findAllBillRule() {
        return this.billRuleDAO.findAllBillRule();
    }

    @Override
    public PageInfo<BillRuleVO> findAllBillRuleVO(Long storeId,Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<BillRuleVO> list = this.billRuleDAO.findAllBillRuleVO(storeId);
        return  new PageInfo<>(list);
    }
}
