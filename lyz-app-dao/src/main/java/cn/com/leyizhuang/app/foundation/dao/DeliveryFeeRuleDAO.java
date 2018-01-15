package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.deliveryFeeRule.DeliveryFeeRule;
import cn.com.leyizhuang.app.foundation.pojo.deliveryFeeRule.DeliveryFeeRuleSpecailGoods;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 运费规则
 * Created by panjie on 2018/1/12.
 */
@Repository
public interface DeliveryFeeRuleDAO {

    List<DeliveryFeeRule> findAllDeliveryFeeRule();

    void addDeliveryFeeRule(DeliveryFeeRule deliveryFeeRule);

    void updateDeliveryFeeRule(DeliveryFeeRule deliveryFeeRule);

    void deleteDeliveryFeeRule(@Param("id") Long id);

    DeliveryFeeRule findRuleById (@Param("id") Long id);

    List<DeliveryFeeRule> findRuleByCityId (@Param("cityId") Long cityId);

    void addSpecailGoods(DeliveryFeeRuleSpecailGoods specailGoods);

    List<DeliveryFeeRuleSpecailGoods> findSpecailGoodsByRuleId(@Param("ruleId") Long ruleId);

    void deleteSpecailGoodsByRuleId(@Param("ruleId") Long ruleId);

}
