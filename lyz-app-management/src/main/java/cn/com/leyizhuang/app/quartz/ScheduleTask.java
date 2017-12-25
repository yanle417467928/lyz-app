package cn.com.leyizhuang.app.quartz;


import cn.com.leyizhuang.app.core.utils.ApplicationContextUtil;
import cn.com.leyizhuang.app.foundation.service.MaEmpCreditMoneyService;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class ScheduleTask implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(new Date() + ": 清除零时额度");
       MaEmpCreditMoneyService maEmpCreditMoneyService = (MaEmpCreditMoneyService) ApplicationContextUtil.getBean("maEmpCreditMoneyService");
       maEmpCreditMoneyService.clearAllTempCredit();
    }

}
