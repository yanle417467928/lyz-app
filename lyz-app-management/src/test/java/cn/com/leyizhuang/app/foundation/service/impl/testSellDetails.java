package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.transferdao.TransferDAO;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderGoods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by panjie on 2018/3/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testSellDetails {

    @Autowired
    private StatisticsSellDetailsServiceImpl statisticsSellDetailsService;

    @Autowired
    private TransferDAO transferDAO;

    @Test
    public void testInsert(){
        //statisticsSellDetailsService.addOrderSellDetails("CD_XN20180319095037659959");

        List<TdOrderGoods> list = transferDAO.getTdOrderGoods();

    }
}
