package cn.com.leyizhuang.app.quartz;
import cn.com.leyizhuang.app.foundation.service.MaClearTempCreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTaskCommandLineRunner implements CommandLineRunner {

    @Autowired
    private MaClearTempCreditService maClearTempCreditService;

    @Override
    public void run(String... var1) throws Exception{
        String clearTempCreditCron =maClearTempCreditService.getCron((long)1);
        if(null!=clearTempCreditCron){
            QuartzManager.addJob("clearTempCredit","jobGroup","trigger","triggerGroup", ClearTempCreditScheduleTask.class,clearTempCreditCron);
        }else{
            throw new RuntimeException("clearTempCreditCron为空");
        }
    }
}
