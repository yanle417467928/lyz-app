package cn.com.leyizhuang.app.quartz.task;

import cn.com.leyizhuang.app.core.utils.ApplicationContextUtil;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.service.MaOrderService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 扫描待付款订单是否过期
 * Created by caiyu on 2018/1/30.
 */
@Component
public class ScanningUnpaidOrderTask implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(new Date() + "：开始扫描待付款订单");
        MaOrderService maOrderService = (MaOrderService) ApplicationContextUtil.getBean("maOrderService");



        Date date = new Date();
        //获取当前时间5小时前时间
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.HOUR_OF_DAY, calendar.get(java.util.Calendar.HOUR_OF_DAY) - 5);
        String findDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

        //获取所有待付款订单
        List<OrderBaseInfo> orderBaseInfoList = maOrderService.scanningUnpaidOrder(findDate);
        if (null != orderBaseInfoList && orderBaseInfoList.size() > 0) {
            for (OrderBaseInfo orderBaseInfo : orderBaseInfoList) {
                if (date.after(orderBaseInfo.getEffectiveEndTime())) {
                    System.out.println(new Date() + "：开始处理超时待付款订单： "+orderBaseInfo.getOrderNumber());
                    maOrderService.scanningUnpaidOrder(orderBaseInfo);
                    System.out.println(new Date() + "：处理超时待付款订单完毕： "+orderBaseInfo.getOrderNumber());
                } else {
                    System.out.println(new Date() + "：未查询到待付款超时订单，订单号：");
                }
            }
        }

    }
}
