package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.DeliveryFeeRuleDAO;
import cn.com.leyizhuang.app.foundation.pojo.deliveryFeeRule.DeliveryFeeRule;
import cn.com.leyizhuang.app.foundation.pojo.deliveryFeeRule.DeliveryFeeRuleSpecailGoods;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderGoodsSimpleResponse;
import cn.com.leyizhuang.app.foundation.service.DeliveryFeeRuleService;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
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
public class DeliveryFeeRuleServiceImpl implements DeliveryFeeRuleService {

    @Resource
    private DeliveryFeeRuleDAO deliveryFeeRuleDAO;

    @Resource
    private GoodsService goodsService;

    public void initRule() {
        /**清除所有规则数据**/
    }

    /**
     * 查询分页数据
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<DeliveryFeeRule> queryPageDate(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<DeliveryFeeRule> list = deliveryFeeRuleDAO.findAllDeliveryFeeRule();
        for (DeliveryFeeRule rule : list) {
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
     *
     * @param deliveryFeeRule
     * @param specailGoods
     */
    @Override
    public void addRule(DeliveryFeeRule deliveryFeeRule, List<DeliveryFeeRuleSpecailGoods> specailGoods) {

        deliveryFeeRuleDAO.addDeliveryFeeRule(deliveryFeeRule);
        for (DeliveryFeeRuleSpecailGoods goods : specailGoods) {
            goods.setRuleId(deliveryFeeRule.getId());
            deliveryFeeRuleDAO.addSpecailGoods(goods);
        }
    }

    /**
     * 更新
     *
     * @param deliveryFeeRule
     * @param specailGoods
     */
    @Override
    public void updateRule(DeliveryFeeRule deliveryFeeRule, List<DeliveryFeeRuleSpecailGoods> specailGoods) {

        deliveryFeeRuleDAO.updateDeliveryFeeRule(deliveryFeeRule);

        deliveryFeeRuleDAO.deleteSpecailGoodsByRuleId(deliveryFeeRule.getId());
        for (DeliveryFeeRuleSpecailGoods goods : specailGoods) {
            goods.setRuleId(deliveryFeeRule.getId());
            deliveryFeeRuleDAO.addSpecailGoods(goods);
        }
    }

    /**
     * 删除
     *
     * @param ruleIdList
     */
    @Override
    public void deleteRule(List<Long> ruleIdList) {
        for (Long id : ruleIdList) {
            deliveryFeeRuleDAO.deleteSpecailGoodsByRuleId(id);
            deliveryFeeRuleDAO.deleteDeliveryFeeRule(id);
        }
    }


    @Override
    public List<DeliveryFeeRule> findRuleByCityId(Long cityId) {
        return deliveryFeeRuleDAO.findRuleByCityId(cityId);
    }

    @Override
    public DeliveryFeeRule findRuleById(Long id) {
        DeliveryFeeRule deliveryFeeRule = deliveryFeeRuleDAO.findRuleById(id);
        return deliveryFeeRule;
    }

    @Override
    public List<DeliveryFeeRuleSpecailGoods> findSpecailGoodsByRuleId(Long ruleId) {
        return deliveryFeeRuleDAO.findSpecailGoodsByRuleId(ruleId);
    }

    public Double countDeliveryFee(Integer identityType, Long cityId, Double totalPrice, List<OrderGoodsSimpleResponse> goodsInfoList) {
        // 如果商品为服务类商品不收取运费
        for (OrderGoodsSimpleResponse goodsSimpleResponse : goodsInfoList){
            if (goodsService.isFWGoods(goodsSimpleResponse.getId())){
                return 0.00;
            }else {
                break;
            }
        }

        List<DeliveryFeeRule> ruleList = deliveryFeeRuleDAO.findRuleByCityId(cityId);
        if (null == ruleList || ruleList.size() == 0){
            return 0.00;
        }
        DeliveryFeeRule deliveryFeeRule = ruleList.get(0);
        if (deliveryFeeRule == null) {
            return 0.00;
        } else {
            String tollObject = deliveryFeeRule.getTollObject();

            if (identityType == null || identityType.equals("")){
                throw new RuntimeException("计算运费，用户身份信息为null");
            }

            if (!tollObject.contains(String.valueOf(identityType))){
                return 0.00;
            }

            // 是否包含特殊商品
            if (deliveryFeeRule.getIncludeSpecialGoods()) {
                // 包含

                //计算排除特殊商品后的价格
                List<Long> specialGoodsIds = deliveryFeeRuleDAO.findSpecialGoodsIdByRuleId(deliveryFeeRule.getId());

                Boolean flag = true;

                if (specialGoodsIds == null || specialGoodsIds.size() == 0){
                    flag = false;
                }else{
                    for (OrderGoodsSimpleResponse goods : goodsInfoList) {

                        if (!specialGoodsIds.contains(goods.getId())) {
                            // 有包含特殊商品以外的商品
                            flag = false;
                            break;
                        }
                    }
                }

                if (flag) {
                    // 如果商品全为特殊商品 则不收取运费
                    return 0.00;
                } else {
                    //条件
                    Double condition = deliveryFeeRule.getCondition() == null ? 0.00 : deliveryFeeRule.getCondition();
                    Double deliveryFee = deliveryFeeRule.getDeliveryFee() == null ? 0.00 : deliveryFeeRule.getDeliveryFee();

                    if (totalPrice >= condition) {
                        deliveryFee = 0.00;
                    }

                    if (deliveryFee < 0.00) {
                        deliveryFee = 0.00;
                    }
                    return deliveryFee;
                }

            } else {
                // 不包含

                //条件
                Double condition = deliveryFeeRule.getCondition() == null ? 0.00 : deliveryFeeRule.getCondition();
                Double deliveryFee = deliveryFeeRule.getDeliveryFee() == null ? 0.00 : deliveryFeeRule.getDeliveryFee();
                if (totalPrice >= condition) {
                    deliveryFee = 0.00;
                }

                if (deliveryFee < 0.00) {
                    deliveryFee = 0.00;
                }

                return deliveryFee;
            }
        }

    }

    @Override
    public Double countDeliveryFeeNew(Integer identityType, Long cityId, Double totalPrice, List<OrderGoodsSimpleResponse> goodsInfoList, String countyName) {
        // 如果商品为服务类商品不收取运费
        for (OrderGoodsSimpleResponse goodsSimpleResponse : goodsInfoList){
            if (goodsService.isFWGoods(goodsSimpleResponse.getId())){
                return 0.00;
            }else {
                break;
            }
        }

        List<DeliveryFeeRule> ruleList = deliveryFeeRuleDAO.findRuleByCityIdAndCountyName(cityId, countyName);
        if (null == ruleList || ruleList.size() == 0){
            return 0.00;
        }
        DeliveryFeeRule deliveryFeeRule = ruleList.get(0);
        if (deliveryFeeRule == null) {
            return 0.00;
        } else {
            String tollObject = deliveryFeeRule.getTollObject();

            if (identityType == null || identityType.equals("")){
                throw new RuntimeException("计算运费，用户身份信息为null");
            }

            if (!tollObject.contains(String.valueOf(identityType))){
                return 0.00;
            }

            // 是否包含特殊商品
            if (deliveryFeeRule.getIncludeSpecialGoods()) {
                // 包含

                //计算排除特殊商品后的价格
                List<Long> specialGoodsIds = deliveryFeeRuleDAO.findSpecialGoodsIdByRuleId(deliveryFeeRule.getId());

                Boolean flag = true;

                if (specialGoodsIds == null || specialGoodsIds.size() == 0){
                    flag = false;
                }else{
                    for (OrderGoodsSimpleResponse goods : goodsInfoList) {

                        if (!specialGoodsIds.contains(goods.getId())) {
                            // 有包含特殊商品以外的商品
                            flag = false;
                            break;
                        }
                    }
                }

                if (flag) {
                    // 如果商品全为特殊商品 则不收取运费
                    return 0.00;
                } else {
                    //条件
                    Double condition = deliveryFeeRule.getCondition() == null ? 0.00 : deliveryFeeRule.getCondition();
                    Double deliveryFee = deliveryFeeRule.getDeliveryFee() == null ? 0.00 : deliveryFeeRule.getDeliveryFee();

                    if (totalPrice >= condition) {
                        deliveryFee = 0.00;
                    }

                    if (deliveryFee < 0.00) {
                        deliveryFee = 0.00;
                    }
                    return deliveryFee;
                }

            } else {
                // 不包含

                //条件
                Double condition = deliveryFeeRule.getCondition() == null ? 0.00 : deliveryFeeRule.getCondition();
                Double deliveryFee = deliveryFeeRule.getDeliveryFee() == null ? 0.00 : deliveryFeeRule.getDeliveryFee();
                if (totalPrice >= condition) {
                    deliveryFee = 0.00;
                }

                if (deliveryFee < 0.00) {
                    deliveryFee = 0.00;
                }

                return deliveryFee;
            }
        }
    }

    @Override
    public List<DeliveryFeeRule> findRuleByCityIdAndCountyName(Long cityId, String countyName) {
        return this.deliveryFeeRuleDAO.findRuleByCityIdAndCountyName(cityId, countyName);
    }
}
