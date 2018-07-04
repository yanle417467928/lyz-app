package cn.com.leyizhuang.app.quartz.task;

import cn.com.leyizhuang.app.core.constant.BillStatusEnum;
import cn.com.leyizhuang.app.core.utils.ApplicationContextUtil;
import cn.com.leyizhuang.app.core.utils.DateUtils;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRuleDO;
import cn.com.leyizhuang.app.foundation.service.BillInfoService;
import cn.com.leyizhuang.app.foundation.service.BillRuleService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 账单日定时任务
 * @author GenerationRoad
 * @date 2018/7/4
 */
@Component
public class BillDateTask implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(new Date() + "：开始扫描账单规则");
        BillInfoService billInfoService = (BillInfoService) ApplicationContextUtil.getBean("billInfoService");

        BillRuleService billRuleService = (BillRuleService) ApplicationContextUtil.getBean("billRuleService");

        List<BillRuleDO> billRuleList = billRuleService.findAllBillRule();

        Integer nowDate = DateUtils.getDate();
        System.out.println(new Date() + "：开始处理账单规则： "+ billRuleList);
        if (null != billRuleList && billRuleList.size() > 0) {
            for (BillRuleDO billRule : billRuleList) {
                Integer billDate = billRule.getBillDate();
                Integer repaymentDeadlineDate = billRule.getRepaymentDeadlineDate();
                System.out.println(new Date() + "：开始处理账单规则： "+ billDate + "," + repaymentDeadlineDate);
                if (null != billDate && nowDate.equals(billDate)) {
                    billInfoService.handleBillInfoInBillDate(billRule.getStoreId());
                    billInfoService.createBillInfo(billRule.getStoreId());
                }
                if (null != repaymentDeadlineDate && nowDate.equals(repaymentDeadlineDate)) {
                    billInfoService.updateBillStatus(billRule.getStoreId(), BillStatusEnum.ALREADY_OUT, BillStatusEnum.HISTORY);
                }
                System.out.println(new Date() + "：处理账单规则完毕，门店ID： "+ billRule.getStoreId());
            }
        }



    }
}
