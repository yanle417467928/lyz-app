package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.bill.BillRuleDO;
import cn.com.leyizhuang.app.foundation.vo.management.BillRuleLogVO;
import cn.com.leyizhuang.app.foundation.vo.management.BillRuleVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/6/26
 */
public interface BillRuleService {

    BillRuleDO getBillRuleByStoreId(Long storeId);

    BillRuleVO getBillRuleById(Long storeId);

    List<BillRuleDO> findAllBillRule();

    PageInfo<BillRuleVO> findAllBillRuleVO(Long storeId, Integer page, Integer size);

    PageInfo<BillRuleLogVO> findBillRuleLogVOById(Integer page, Integer size, Long id,String startTime,String endTime ,String changeUser);

}
