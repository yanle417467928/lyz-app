package cn.com.leyizhuang.app.quartz.task;

import cn.com.leyizhuang.app.core.utils.ApplicationContextUtil;
import cn.com.leyizhuang.app.foundation.service.StatisticsSellDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 12421 on 2018/7/16.
 */
@Component
@Slf4j
public class CountSellerSalesTask implements Job {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        StatisticsSellDetailsService statisticsSellDetailsService = (StatisticsSellDetailsService) ApplicationContextUtil.getBean("statisticsSellDetailsService");
        System.out.println("计算导购销量定时器开始执行");

        List<String> list = new ArrayList<>();
        list.add("RCC001");
        list.add("PCC001");
        list.add("BYC001");
        list.add("RDC001");
        list.add("ZZC001");

        statisticsSellDetailsService.statisticsAllSellerSellDetails(list);

    }
}
