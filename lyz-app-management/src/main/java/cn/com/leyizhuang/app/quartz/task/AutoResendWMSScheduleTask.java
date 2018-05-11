package cn.com.leyizhuang.app.quartz.task;

import cn.com.leyizhuang.app.core.utils.ApplicationContextUtil;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrder;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwReturnOrder;
import cn.com.leyizhuang.app.foundation.service.AppToWmsOrderService;
import cn.com.leyizhuang.app.remote.wms.MaICallWms;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Jerry.Ren
 * create 2018-05-10 9:13
 * desc:wms定时扫描传输失败的接口再重传
 **/
@Component
public class AutoResendWMSScheduleTask implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(new Date() + ": 执行job重传WMS接口任务");
        AppToWmsOrderService appToWmsOrderService = (AppToWmsOrderService) ApplicationContextUtil.getBean("appToWmsOrderService");
        MaICallWms iCallWms = (MaICallWms) ApplicationContextUtil.getBean("maICallWms");
        //先查出今昨天所有传输失败的单子(因为是每天的定时任务,取两天的数据足够)
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        System.out.println(yesterday);
        List<AtwRequisitionOrder> failOrderList = appToWmsOrderService.findFailResendWmsOrder(yesterday);

        for (AtwRequisitionOrder atwRequisitionOrder : failOrderList) {
            if (atwRequisitionOrder.getErrorMessage().startsWith("违反了 PRIMARY KEY 约束")) {
                continue;
            }
            iCallWms.sendToWmsRequisitionOrderAndGoods(atwRequisitionOrder.getOrderNumber());
        }

        List<AtwReturnOrder> failReturnOrderList = appToWmsOrderService.findFailResendWmsReturnOrder(yesterday);

        for (AtwReturnOrder atwReturnOrder : failReturnOrderList) {
            if (atwReturnOrder.getErrorMessage().contains("违反了 PRIMARY KEY 约束")) {
                continue;
            }
            iCallWms.sendToWmsReturnOrderAndGoods(atwReturnOrder.getReturnNumber());
        }
        System.out.println(new Date() + ": 执行job重传WMS接口任务成功");
    }
}
