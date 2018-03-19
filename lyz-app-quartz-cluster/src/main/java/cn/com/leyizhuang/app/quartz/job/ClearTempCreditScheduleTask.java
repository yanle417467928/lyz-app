package cn.com.leyizhuang.app.quartz.job;



import cn.com.leyizhuang.app.foundation.service.MaEmpCreditMoneyService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class ClearTempCreditScheduleTask extends QuartzJobBean {

    @Resource
    private MaEmpCreditMoneyService maEmpCreditMoneyService;

    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println(new Date() + ": 执行清除临时额度任务");
        //MaEmpCreditMoneyService maEmpCreditMoneyService = (MaEmpCreditMoneyService) ApplicationContextUtil.getBean("maEmpCreditMoneyService");
        maEmpCreditMoneyService.autoClearTempCreditMoney();
        System.out.println(new Date() + ": 执行清除临时额度任务成功");
    }
}
