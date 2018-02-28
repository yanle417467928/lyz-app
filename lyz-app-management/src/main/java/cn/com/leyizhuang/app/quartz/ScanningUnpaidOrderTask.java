package cn.com.leyizhuang.app.quartz;

import cn.com.leyizhuang.app.core.utils.ApplicationContextUtil;
import cn.com.leyizhuang.app.foundation.service.MaOrderService;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 扫描待付款订单是否过期
 * Created by caiyu on 2018/1/30.
 */
@Component
public class ScanningUnpaidOrderTask implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(new Date() + "：开始扫描待付款订单");
        MaOrderService MaOrderService = (MaOrderService) ApplicationContextUtil.getBean("maOrderService");
        MaOrderService.findScanningUnpaidOrder();
    }
}
