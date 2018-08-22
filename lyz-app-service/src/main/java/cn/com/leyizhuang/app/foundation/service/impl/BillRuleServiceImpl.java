package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.BillRuleDAO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRuleDO;
import cn.com.leyizhuang.app.foundation.service.BillRuleService;
import cn.com.leyizhuang.app.foundation.vo.management.BillRuleLogVO;
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
    public BillRuleVO getBillRuleById(Long id) {
        return this.billRuleDAO.getBillRuleById(id);
    }

    @Override
    public List<BillRuleDO> findAllBillRule() {
        return this.billRuleDAO.findAllBillRule();
    }

    @Override
    public PageInfo<BillRuleVO> findAllBillRuleVO(Long storeId,Long cityId,String storeType,List<Long> storeIds,Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<BillRuleVO> list = this.billRuleDAO.findAllBillRuleVO(storeId,cityId,storeType,storeIds);
        return  new PageInfo<>(list);
    }

    @Override
    public PageInfo<BillRuleLogVO> findBillRuleLogVOById(Integer page, Integer size,Long id,String startTime,String endTime ,String changeUser){
        PageHelper.startPage(page, size);
        if(null != startTime && !"".equals(startTime)){
            startTime = startTime +" 00:00:00";
        }
        if(null != endTime && !"".equals(endTime)){
            endTime = endTime +" 23:59:59";
        }
        List<BillRuleLogVO> list = this.billRuleDAO.findBillRuleLogVOById(id,startTime,endTime,changeUser);
        return  new PageInfo<>(list);
    }
}
