package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.deliveryFeeRule.DeliveryFeeRule;
import cn.com.leyizhuang.app.foundation.pojo.deliveryFeeRule.DeliveryFeeRuleSpecailGoods;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by panjie on 2018/1/12.
 */
@Service
@Transactional
public interface DeliveryFeeRuleService {

    PageInfo<DeliveryFeeRule> queryPageDate(Integer page, Integer size);

    void addRule(DeliveryFeeRule deliveryFeeRule , List<DeliveryFeeRuleSpecailGoods> specailGoods);

    void updateRule(DeliveryFeeRule deliveryFeeRule , List<DeliveryFeeRuleSpecailGoods> specailGoods);

    void deleteRule(List<Long> ruleIdList);

    List<DeliveryFeeRule> findRuleByCityId(Long cityId);

    DeliveryFeeRule findRuleById(Long id);

    List<DeliveryFeeRuleSpecailGoods> findSpecailGoodsByRuleId(Long ruleId);
}
