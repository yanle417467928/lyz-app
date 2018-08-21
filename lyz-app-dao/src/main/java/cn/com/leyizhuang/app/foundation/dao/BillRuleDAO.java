package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.bill.BillRuleDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRuleLogDO;
import cn.com.leyizhuang.app.foundation.vo.management.BillRuleLogVO;
import cn.com.leyizhuang.app.foundation.vo.management.BillRuleVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/6/26
 */
@Repository
public interface BillRuleDAO {

    BillRuleDO getBillRuleByStoreId(Long storeId);

    BillRuleVO getBillRuleById(@Param(value = "id") Long id);

    List<BillRuleDO> findAllBillRule();

    List<BillRuleVO> findAllBillRuleVO(@Param(value = "storeId")Long storeId);

    void saveBillRuleLog(BillRuleLogDO billRuleLogDO);

    Integer updateBillRule(BillRuleDO billRuleDO);

    List<BillRuleLogVO> findBillRuleLogVOById(@Param(value = "id")Long id,@Param(value = "startTime")String startTime,@Param(value = "endTime") String endTime ,@Param(value = "changeUser") String changeUser);
}
