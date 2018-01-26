package cn.com.leyizhuang.app.quartz;

import cn.com.leyizhuang.app.core.utils.ApplicationContextUtil;
import cn.com.leyizhuang.app.foundation.pojo.SellDetailsDO;
import cn.com.leyizhuang.app.foundation.service.StatisticsSellDetailsService;
import cn.com.leyizhuang.app.remote.queue.MaSellDetailsSender;
import cn.com.leyizhuang.common.util.CountUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
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
        List<List<SellDetailsDO>> lists = this.splitList(sellDetailsDOList,100);
        for (List<SellDetailsDO> list : lists){
            String data = JSON.toJSONString(list);
            maSellDetailsSender.sendSellDetailsToHQ(data);
        }

    }

    /**
     * @author panjie
     * 2018.1.24
     * 将传入的List按照给定的size拆分成多个子List    <br>
     * 例如 list=[1, 2, 3, 4, 5] , per=3    <br>
     * 则会得到 : [[1, 2, 3],[4, 5]]    <br>
     * list=[1, 2, 3, 4, 5]  , per=2    <br>
     * 则会得到 : [[1, 2], [3, 4], [5]]    <br>
     * */
    public static <T> List<List<T>> splitList(List<T> list,int per){
        List<List<T>> returnList = new ArrayList<List<T>>();
        int count = list.size()/per;
        int yu = list.size() % per;
        for (int i = 0; i <= count; i++) {
            List<T> subList = new ArrayList<T>();
            if (i == count) {
                subList = list.subList(i * per, i * per + yu);
            } else {
                subList = list.subList(i * per, per * (i + 1));
            }
            returnList.add(subList);
        }
        return returnList;
    }
}
