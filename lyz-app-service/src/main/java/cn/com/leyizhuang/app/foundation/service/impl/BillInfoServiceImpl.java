package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentGoodsDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRuleDO;
import cn.com.leyizhuang.app.foundation.service.BillInfoService;
import cn.com.leyizhuang.app.foundation.service.BillRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/6/26
 */
@Service
public class BillInfoServiceImpl implements BillInfoService {

    @Autowired
    private BillRuleService billRuleService;

    @Override
    public List<BillRepaymentGoodsDetailsDO> computeInterestAmount(Long storeId, List<BillRepaymentGoodsDetailsDO> goodsDetailsDOList) {

        if (null == goodsDetailsDOList || goodsDetailsDOList.size() == 0) {
            return goodsDetailsDOList;
        }

        if (null == storeId){
            return goodsDetailsDOList;
        }
        //根据门店ID查询账单规则
        BillRuleDO billRuleDO = this.billRuleService.getBillRuleByStoreId(storeId);
        if (null == billRuleDO || null == billRuleDO.getInterestRate() || billRuleDO.getInterestRate().equals(0D)){
            return goodsDetailsDOList;
        }
        for (BillRepaymentGoodsDetailsDO goodsDetailsDO: goodsDetailsDOList) {

        }


        return null;
    }
}
