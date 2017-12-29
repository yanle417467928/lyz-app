package cn.com.leyizhuang.app.remote.webservice;

import cn.com.leyizhuang.app.foundation.pojo.wms.AtwRequisitionOrder;
import cn.com.leyizhuang.app.foundation.pojo.wms.AtwRequisitionOrderGoods;
import cn.com.leyizhuang.app.foundation.service.AppToWmsOrderService;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Created on 2017-12-19 13:18
 **/
@RestController
public class TestClient {
    @Resource
    private ICallWms iCallWms;

    @Resource
    private AppToWmsOrderService appToWmsOrderService;

    public static void main(String[] args) throws Exception {
        String STRTABLE = "tbw_send_task_m";
        String STRTYPE ="test";
        String XML = "PEVSUD48VEFCTEU+PENfQ09NUEFOWV9JRD4yMDMzPC9DX0NPTVBBTllfSUQ+PENfT1VUX05PPk9VMTEwNDE3MTIyMjAwMDE8L0NfT1VUX05PPjxDX1dIX05PPjExMDQ8L0NfV0hfTk8+PENfV0hfTkFNRT7nmb7lp5PkuK3ovazku5M8L0NfV0hfTkFNRT48Q19JRD4yMTcyNDk8L0NfSUQ+PENfREVTQ1JJUFRJT04+V01T5L2c5Lia5Lu75Yqh54q25oCBVG9FUlA8L0NfREVTQ1JJUFRJT04+PENfREVTVD48L0NfREVTVD48Q19EVD4yMDE3LzEyLzIyIDEyOjA1OjEyPC9DX0RUPjxDX0NPTFVNTjE+Y19pZDwvQ19DT0xVTU4xPjxDX1ZBTFVFMT5aWl9YTjIwMTcxMjIyMTEzMjA3NjI3Njg4PC9DX1ZBTFVFMT48Q19DT0xVTU4yPmNfYmlsbF90eXBlPC9DX0NPTFVNTjI+PENfVkFMVUUyPuaLo+i0p+WNlTwvQ19WQUxVRTI+PENfQ09MVU1OMz5jX3R5cGU8L0NfQ09MVU1OMz48Q19WQUxVRTM+5bey5ouj6LSnPC9DX1ZBTFVFMz48Q19DT0xVTU40PmNfY3VzdG9tZXJfbm88L0NfQ09MVU1OND48Q19WQUxVRTQ+MTAwMDwvQ19WQUxVRTQ+PENfQ09MVU1ONT5jX2djb2RlPC9DX0NPTFVNTjU+PENfVkFMVUU1PkpCQzIzNS0xMDwvQ19WQUxVRTU+PENfQ09MVU1ONj5jX3F0eTwvQ19DT0xVTU42PjxDX1ZBTFVFNj4xLjAwPC9DX1ZBTFVFNj48Q19DT0xVTU43PmNfb3V0X25vPC9DX0NPTFVNTjc+PENfVkFMVUU3Pk9VMTEwNDE3MTIyMjAwMDE8L0NfVkFMVUU3PjxDX0NPTFVNTjg+Y19kX3JlcXVlc3RfcXR5PC9DX0NPTFVNTjg+PENfVkFMVUU4PjEuMDA8L0NfVkFMVUU4PjwvVEFCTEU+PC9FUlA+";
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient("http://localhost:9999/webservice/user?wsdl");
        Object[] objects=client.invoke("GetWMSInfo",STRTABLE,STRTYPE,XML);
        System.out.println("*****"+objects[0].toString());
    }

    @RequestMapping("/test/webservice")
    public void testWebservice() throws Exception {
        AtwRequisitionOrder order = new AtwRequisitionOrder();
        order.setDiySiteId("FZM007");
        order.setDiySiteTel("028-83551646");
        order.setDiySiteTitle("富森富之美");
        order.setCustomerName("测试");
        order.setReceiveTimeQuantum("2017-12-20 14:30");
        order.setOrderNumber("CD_XN20171220102259123465");
        order.setReceiveAddress("成都市新都区大丰街道订单");
        order.setReceiveName("测试");
        order.setReceivePhone("13408698552");
        order.setCity("成都市");
        order.setDetailAddress("订单");
        order.setDisctrict("新都区");
        order.setSubdistrict("大丰街道");
        order.setSellerTel("18280285992");
        order.setGoodsQuantity(1);
        order.setUpstairsAll(0D);
        order.setSellerName("樊云霞");
        order.setDeliveryFee(30.0);
        order.setColorFee(0D);
        order.setDiscount(0D);
        order.setOtherPayed(0D);
        order.setBalanceUsed(60.0);
        order.setMemberReceiver(false);
        order.setUnpayed(0D);
        order.setTotalGoodsPrice(30.0);
        order.setAgencyRefund(0.00);

        AtwRequisitionOrderGoods goods = new AtwRequisitionOrderGoods();
        goods.setCreateTime(new Date());
        goods.setGoodsCode("SJWT4503-20");
        goods.setGoodsTitle("金牛白色PPR45度弯头20");
        goods.setOrderNumber("CD_XN20171220102259123465");
        goods.setPrice(10D);
        goods.setQuantity(1);

        //备注：目前发送订单通了，取消订单wms还在重新做。
        //第一步：将订单信息转化成要货单实体AtwRequisitionOrder然后调用下面service保存
        appToWmsOrderService.saveAtwRequisitionOrder(order);
        //第二步：将订单商品转化成要货商品实体AtwRequisitionOrderGoods然后调用下面service保存
        appToWmsOrderService.saveAtwRequisitionOrderGoods(goods);
        //第三步：调用下面service将订单号发送给wms
        iCallWms.sendToWmsRequisitionOrderAndGoods(order.getOrderNumber());
    }
}
