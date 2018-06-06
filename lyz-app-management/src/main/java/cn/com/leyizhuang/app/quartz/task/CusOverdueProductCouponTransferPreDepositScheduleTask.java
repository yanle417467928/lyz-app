package cn.com.leyizhuang.app.quartz.task;


import cn.com.leyizhuang.app.core.utils.ApplicationContextUtil;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.CommonService;
import cn.com.leyizhuang.app.foundation.service.MaEmpCreditMoneyService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
@Slf4j
public class CusOverdueProductCouponTransferPreDepositScheduleTask implements Job {



    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("开始执行过期顾客产品券转化顾客预存款定时任务,当前时间:{}", new Date());
        CommonService commonService = (CommonService) ApplicationContextUtil.getBean("commonServiceImpl");
        try {
            commonService.cusProductCouponTransferPreDeposit();
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("{},e");
        }

        log.info("过期顾客产品券转化顾客预存款定时任务执行完成,当前时间:{}", new Date());
    }
}
