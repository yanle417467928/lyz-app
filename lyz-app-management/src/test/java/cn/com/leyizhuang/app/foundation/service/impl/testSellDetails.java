package cn.com.leyizhuang.app.foundation.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by panjie on 2018/3/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testSellDetails {

    @Autowired
    private StatisticsSellDetailsServiceImpl statisticsSellDetailsService;

    @Test
    public void testInsert(){
        String orderNo = "XN_123321";

        statisticsSellDetailsService.recordeErrorLog(orderNo);
    }
}
