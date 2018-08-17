package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.DataTransferExceptionType;
import cn.com.leyizhuang.app.core.exception.DataTransferException;
import cn.com.leyizhuang.app.core.wechat.refund.MaOnlinePayRefundService;
import cn.com.leyizhuang.app.core.wechat.sign.WechatPrePay;
import cn.com.leyizhuang.app.foundation.dao.OrderDAO;
import cn.com.leyizhuang.app.foundation.dao.SellDetailsDAO;
import cn.com.leyizhuang.app.foundation.dao.transferdao.TransferDAO;
import cn.com.leyizhuang.app.foundation.pojo.SellDetailsStatisticErrorLog;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.DataTransferErrorLog;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrder;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderGoodsInf;
import cn.com.leyizhuang.app.foundation.service.ProductCouponService;
import cn.com.leyizhuang.app.foundation.service.StatisticsSellDetailsService;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferService;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferSupportService;
import cn.com.leyizhuang.app.foundation.service.datatransfer.OrderGoodsTransferService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

    @Resource
    private DataTransferSupportService dataTransferSupportService;

    @Resource
    private DataTransferService dataTransferService;

    @Resource
    private SellDetailsDAO sellDetailsDAO;

    @Resource
    private OrderDAO orderDAO;

    @Resource
    private ProductCouponService productCouponService;

    @Resource
    private MaOnlinePayRefundService maOnlinePayRefundService;

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

    @Test
    public void testTransfer(){
        try {
            dataTransferService.transferOrderRelevantInfo("CD_XN20171125122851755751");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runSaleDetils(){

        List<String> list = new ArrayList<>();
        list.add("RCC001");
        list.add("PCC001");
        list.add("BYC001");
        list.add("RDC001");
        list.add("ZZC001");

        statisticsSellDetailsService.statisticsAllSellerSellDetails(list);
    }

    @Test
    public void test(){
        productCouponService.activateCusProductCoupon("CD_XN20180502165632256553");
    }


    @Test
    public void returnMoney(){
        Map<String, Object> resultMap = WechatPrePay.wechatRefundSign(
                "20180811105108711466710", "T20180811150053541", new BigDecimal(2824), new BigDecimal(2824));
        System.out.println(resultMap.get("return_code").toString());
        System.out.println(resultMap.get("result_code").toString());
    }
}
