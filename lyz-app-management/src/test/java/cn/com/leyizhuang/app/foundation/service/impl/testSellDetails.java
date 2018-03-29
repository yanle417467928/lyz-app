package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.DataTransferExceptionType;
import cn.com.leyizhuang.app.core.exception.DataTransferException;
import cn.com.leyizhuang.app.foundation.dao.transferdao.TransferDAO;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrder;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.service.datatransfer.OrderGoodsTransferService;
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

    @Autowired
    private AppOrderServiceImpl appOrderService;

    @Autowired
    private OrderGoodsTransferService orderGoodsTransferService;

    @Test
    public void testInsert(){
        //statisticsSellDetailsService.addOrderSellDetails("CD_XN20180319095037659959");

        List<OrderBaseInfo> orderBaseInfoList = transferDAO.findNewOrderNumber();
        OrderBaseInfo baseInfo = appOrderService.getOrderByOrderNumber("CD_XN20171001095945519243");

        // 根据主单号 找到旧订单分单
        List<TdOrder> tdOrders = transferDAO.findOrderAllFieldByOrderNumber(baseInfo.getOrderNumber());
        if (tdOrders == null || tdOrders.size() == 0) {
            //throw new Exception("订单商品转行异常，找不到旧订单 订单号："+ orderBaseInfo.getOrderNumber());
            throw new DataTransferException("找不到旧订单 订单号："+ baseInfo.getOrderNumber(), DataTransferExceptionType.NDT);
        }
        try {
            //orderGoodsTransferService.transferOne(orderBaseInfoList.get(0));
            orderGoodsTransferService.transferOne(baseInfo,tdOrders);
        } catch (Exception e) {
            e.printStackTrace();

            if (e.getMessage().contains("订单商品转行异常")){
                System.out.println(e.getMessage());
            }
        }
    }
}
