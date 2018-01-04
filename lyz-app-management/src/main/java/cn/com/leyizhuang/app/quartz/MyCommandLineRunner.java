package cn.com.leyizhuang.app.quartz;
import cn.com.leyizhuang.app.foundation.service.MaClearTempCreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    @Autowired
    private MaClearTempCreditService maClearTempCreditService;

    @Override
    public void run(String... var1) throws Exception{
        String cron =maClearTempCreditService.getCron();
        if(null!=cron){
            QuartzManager.addJob("clearTempCredit","jobGroup","trigger","triggerGroup", ScheduleTask.class,cron);
        }else{
            throw new RuntimeException("cron为空");
        }
    }
}
