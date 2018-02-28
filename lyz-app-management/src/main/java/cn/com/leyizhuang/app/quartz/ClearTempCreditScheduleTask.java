package cn.com.leyizhuang.app.quartz;


import cn.com.leyizhuang.app.core.utils.ApplicationContextUtil;
import cn.com.leyizhuang.app.foundation.service.MaEmpCreditMoneyService;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ClearTempCreditScheduleTask implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(new Date() + ": 执行清除临时额度任务");
        MaEmpCreditMoneyService maEmpCreditMoneyService = (MaEmpCreditMoneyService) ApplicationContextUtil.getBean("maEmpCreditMoneyService");
        maEmpCreditMoneyService.autoClearTempCreditMoney();
        System.out.println(new Date() + ": 执行清除临时额度任务成功");
    }
}
