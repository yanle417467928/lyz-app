package cn.com.leyizhuang.app.timedTask;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TemporaryLimitClear {
    public final static long ONE_Minute =  60 * 1000;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Scheduled(fixedDelay=ONE_Minute)
    public void fixedDelayJob(){
        System.out.println("fixedDelay执行...."+df.format(new Date()));
    }

    @Scheduled(fixedRate=ONE_Minute)
    public void fixedRateJob(){
        System.out.println("fixedRate执行...."+df.format(new Date()));
    }

    @Scheduled(cron="0 0 0 1 * ?")
    public void cronJob(){
        System.out.println("cron执行...."+df.format(new Date()));
    }

}
