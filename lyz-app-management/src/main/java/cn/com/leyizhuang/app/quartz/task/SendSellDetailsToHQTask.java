package cn.com.leyizhuang.app.quartz.task;

import cn.com.leyizhuang.app.core.utils.ApplicationContextUtil;
import cn.com.leyizhuang.app.core.utils.ArrayListUtils;
import cn.com.leyizhuang.app.foundation.pojo.SellDetailsDO;
import cn.com.leyizhuang.app.foundation.service.StatisticsSellDetailsService;
import cn.com.leyizhuang.app.remote.queue.MaSellDetailsSender;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 每个月1号给hq 发送销量数据
 * Created by panjie on 2018/1/24.
 */
@Component
@Slf4j
public class SendSellDetailsToHQTask implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        MaSellDetailsSender maSellDetailsSender = (MaSellDetailsSender) ApplicationContextUtil.getBean("maSellDetailsSender");
        StatisticsSellDetailsService statisticsSellDetailsService = (StatisticsSellDetailsService) ApplicationContextUtil.getBean("statisticsSellDetailsService");


        LocalDate date = LocalDate.now();
        date = date.minusMonths(1);
        log.info("《《《《《《《《《《《《《《《《 开始发送"+date.getYear()+"年"+date.getMonthValue()+"月的销量明细 》》》》》》》》》》》》》》》》》》》》");

        List<SellDetailsDO> sellDetailsDOList = statisticsSellDetailsService.statisticsCurrentDetails();

        //将list按100长度拆分
        List<List<SellDetailsDO>> lists = ArrayListUtils.splitList(sellDetailsDOList,100);
        for (List<SellDetailsDO> list : lists){
            String data = JSON.toJSONString(list);
            maSellDetailsSender.sendSellDetailsToHQ(data);
        }

    }

}
