package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.bill.BillRuleDO;

/**
 * @author GenerationRoad
 * @date 2018/6/26
 */
public interface BillRuleService {

    BillRuleDO getBillRuleByStoreId(Long storeId);
}