package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.bill.BillRuleDO;
import cn.com.leyizhuang.app.foundation.vo.management.BillRuleVO;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/6/26
 */
public interface BillRuleService {

    BillRuleDO getBillRuleByStoreId(Long storeId);

    List<BillRuleDO> findAllBillRule();

    List<BillRuleVO> findAllBillRuleVO();

}
