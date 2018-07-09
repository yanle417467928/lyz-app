package cn.com.leyizhuang.app.quartz;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.service.MaClearTempCreditService;
import cn.com.leyizhuang.app.quartz.task.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleTaskCommandLineRunner implements CommandLineRunner {

    @Autowired
    private MaClearTempCreditService maClearTempCreditService;

    @Override
    public void run(String... var1) throws Exception{
        String clearTempCreditCron =maClearTempCreditService.getCron(1L);
        if(StringUtils.isNotBlank(clearTempCreditCron)){
            QuartzManager.addJob("clearTempCredit","jobGroup","trigger","triggerGroup", ClearTempCreditScheduleTask.class,clearTempCreditCron);
        }else{
            log.info("clearTempCreditCron为空");
        }

        // 获取定时器规则
        String cron = maClearTempCreditService.getCron(2L);
        if(StringUtils.isNotBlank(cron)){
            try {
                QuartzManager.addJob("sendSellDetails","jobGroup2","trigger2","triggerGroup2",SendSellDetailsToHQTask.class,cron);
            }catch (Exception e){
                log.info("销售明细传输到HQ任务未加入任务池！！！");
            }
        }else{
            log.info("销售明细传输到HQ任务未加入任务池！！！");
        }


        String scanningUnpaidOrderCron =maClearTempCreditService.getCron(3L);
        if(null!=scanningUnpaidOrderCron){
            QuartzManager.addJob("scanningUnpaidOrder","jobGroup3","trigger3","triggerGroup3", ScanningUnpaidOrderTask.class,scanningUnpaidOrderCron);

        }else{
            throw new RuntimeException("scanningUnpaidOrderCron为空");
        }

        String autoResendWMSCron = maClearTempCreditService.getCron(4L);
        if (StringUtils.isNotBlank(autoResendWMSCron)) {

            QuartzManager.addJob("autoResendWMS", "jobGroup4", "trigger4", "triggerGroup4", AutoResendWMSScheduleTask.class, autoResendWMSCron);
        } else {
            throw new RuntimeException("autoResendWMS为空");
        }

     /*   String sendMsgForExpiringSoonProductCouponCron = maClearTempCreditService.getCron(6L);
        if (StringUtils.isNotBlank(sendMsgForExpiringSoonProductCouponCron)) {
            QuartzManager.addJob("sendMsgForExpiringSoonProductCoupon", "jobGroup5", "trigger5", "triggerGroup5", sendProductSMSTask.class, sendMsgForExpiringSoonProductCouponCron);
        } else {
           // throw new RuntimeException("sendMsgForExpiringSoonProductCouponCron为空");
        }*/

        //账期定时任务
        String billDateTaskCron = maClearTempCreditService.getCron(6L);
        if (StringUtils.isNotBlank(autoResendWMSCron)) {
            QuartzManager.addJob("billDateTask", "jobGroup6", "trigger6", "triggerGroup6", BillDateTask.class, billDateTaskCron);
        } else {
            throw new RuntimeException("billDateTask为空");
        }
    }
}
