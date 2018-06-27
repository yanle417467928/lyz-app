package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.bill.BillRuleDO;
import org.springframework.stereotype.Repository;

/**
 * @author GenerationRoad
 * @date 2018/6/26
 */
@Repository
public interface BillRuleDAO {

    BillRuleDO getBillRuleByStoreId(Long storeId);

}
