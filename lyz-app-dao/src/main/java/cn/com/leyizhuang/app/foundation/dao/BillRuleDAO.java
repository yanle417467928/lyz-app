package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.bill.BillRuleDO;
import cn.com.leyizhuang.app.foundation.vo.management.BillRuleVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/6/26
 */
@Repository
public interface BillRuleDAO {

    BillRuleDO getBillRuleByStoreId(Long storeId);

    List<BillRuleDO> findAllBillRule();

    List<BillRuleVO> findAllBillRuleVO(Long storeId);
}
