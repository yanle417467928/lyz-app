package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.DeliveryFeeRuleDAO;
import cn.com.leyizhuang.app.foundation.pojo.deliveryFeeRule.DeliveryFeeRule;
import cn.com.leyizhuang.app.foundation.pojo.deliveryFeeRule.DeliveryFeeRuleSpecailGoods;
import cn.com.leyizhuang.app.foundation.service.DeliveryFeeRuleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by panjie on 2018/1/12.
 */
@Service
@Transactional
public class DeliveryFeeRuleServiceImpl implements DeliveryFeeRuleService{

    @Resource
    private DeliveryFeeRuleDAO deliveryFeeRuleDAO;

    public void initRule(){
        /**清除所有规则数据**/
    }

    /**
     * 查询分页数据
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<DeliveryFeeRule> queryPageDate(Integer page, Integer size){
        PageHelper.startPage(page, size);
        List<DeliveryFeeRule> list = deliveryFeeRuleDAO.findAllDeliveryFeeRule();
        for (DeliveryFeeRule rule : list){
            String target = rule.getTollObject();
            target = target.replace("0", "导购")
                    .replace("2", "装饰公司经理")
                    .replace("6", "顾客");
            rule.setTollObject(target);
        }
        return new PageInfo<>(list);
    }

    /**
     * 新增规则
     * @param deliveryFeeRule
     * @param specailGoods
     */
    @Override
    public void addRule(DeliveryFeeRule deliveryFeeRule , List<DeliveryFeeRuleSpecailGoods> specailGoods){

        deliveryFeeRuleDAO.addDeliveryFeeRule(deliveryFeeRule);
        for (DeliveryFeeRuleSpecailGoods goods : specailGoods) {
                goods.setRuleId(deliveryFeeRule.getId());
                deliveryFeeRuleDAO.addSpecailGoods(goods);
        }
    }

    /**
     * 更新
     * @param deliveryFeeRule
     * @param specailGoods
     */
    @Override
    public void updateRule(DeliveryFeeRule deliveryFeeRule , List<DeliveryFeeRuleSpecailGoods> specailGoods){

        deliveryFeeRuleDAO.updateDeliveryFeeRule(deliveryFeeRule);

        deliveryFeeRuleDAO.deleteSpecailGoodsByRuleId(deliveryFeeRule.getId());
        for (DeliveryFeeRuleSpecailGoods goods : specailGoods) {
            goods.setRuleId(deliveryFeeRule.getId());
            deliveryFeeRuleDAO.addSpecailGoods(goods);
        }
    }

    /**
     * 删除
     * @param ruleIdList
     */
    @Override
    public void deleteRule(List<Long> ruleIdList){
        for (Long id : ruleIdList){
            deliveryFeeRuleDAO.deleteSpecailGoodsByRuleId(id);
            deliveryFeeRuleDAO.deleteDeliveryFeeRule(id);
        }
    }


    @Override
    public List<DeliveryFeeRule> findRuleByCityId(Long cityId){
        return deliveryFeeRuleDAO.findRuleByCityId(cityId);
    }

    @Override
    public DeliveryFeeRule findRuleById(Long id){
        DeliveryFeeRule deliveryFeeRule = deliveryFeeRuleDAO.findRuleById(id);
        return deliveryFeeRule;
    }

    @Override
    public List<DeliveryFeeRuleSpecailGoods> findSpecailGoodsByRuleId(Long ruleId){
        return deliveryFeeRuleDAO.findSpecailGoodsByRuleId(ruleId);
    }

    public Double countDeliveryFee(){
       return null;
    }
}
