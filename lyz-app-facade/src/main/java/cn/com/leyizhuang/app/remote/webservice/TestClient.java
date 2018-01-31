package cn.com.leyizhuang.app.remote.webservice;

import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwCancelOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrder;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwReturnOrder;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.AppToWmsOrderService;
import cn.com.leyizhuang.app.foundation.service.WmsToAppOrderService;
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

    @Resource
    private WmsToAppOrderService WmsToAppOrderService;
    @Resource
    private AppStoreService storeService;

    public static void main(String[] args) throws Exception {
//        String STRTABLE = "tbw_send_task_m";
        String STRTABLE = "tbw_send_task_d";
        String STRTYPE ="test";
        String XML = "PEVSUD48VEFCTEU+PENfQ09NUEFOWV9JRD4yMDMzPC9DX0NPTVBBTllfSUQ+PENfV0hfTk8+MTEwNDwvQ19XSF9OTz48Q19PV05FUl9OTz4wMDE8L0NfT1dORVJfTk8+PENfVEFTS19OTz5TVTExMDQxNzEyMjIwMDAxPC9DX1RBU0tfTk8+PENfVEFTS19UWVBFPuS4gOiIrOWHuui0pzwvQ19UQVNLX1RZUEU+PENfUFJJTlRfVElNRVM+MDwvQ19QUklOVF9USU1FUz48Q19DQVJfTk8+MDAyNDwvQ19DQVJfTk8+PENfTElORV9OTz4wMDAxPC9DX0xJTkVfTk8+PENfUExBVF9OTz4wMDExPC9DX1BMQVRfTk8+PENfT1BfVVNFUj4wMDAwMDY8L0NfT1BfVVNFUj48Q19PUF9UT09MUz7nurjljZU8L0NfT1BfVE9PTFM+PENfT1BfU1RBVFVTPuW3suWHuui9pjwvQ19PUF9TVEFUVVM+PENfQ1VTVE9NRVJfTk8+MTAwMDwvQ19DVVNUT01FUl9OTz48Q19QUk9WSURFUl9OTz48L0NfUFJPVklERVJfTk8+PENfQkVHSU5fRFQ+MjAxNy8xMi8yMiAxMjowNTozODwvQ19CRUdJTl9EVD48Q19FTkRfRFQ+MjAxNy8xMi8yMiAxMjowNjoxMTwvQ19FTkRfRFQ+PENfUkVTRVJWRUQxPkNEX1hOMjAxNzEyMTMxNTQzNDE3MDExNzAyOTM8L0NfUkVTRVJWRUQxPjxDX1JFU0VSVkVEMj7pg5Hlt57luILkuozkuIPljLrpqazlr6jplYfkuI3nn6U8L0NfUkVTRVJWRUQyPjxDX1JFU0VSVkVEMz48L0NfUkVTRVJWRUQzPjxDX1JFU0VSVkVEND48L0NfUkVTRVJWRUQ0PjxDX1JFU0VSVkVENT48L0NfUkVTRVJWRUQ1PjxDX05PVEU+PC9DX05PVEU+PENfTUtfVVNFUk5PPjAwMDA8L0NfTUtfVVNFUk5PPjxDX01LX0RUPjIwMTcvMTIvMjIgMTI6MDU6Mzg8L0NfTUtfRFQ+PENfTU9ESUZJRURfVVNFUk5PPjAwMDA8L0NfTU9ESUZJRURfVVNFUk5PPjxDX01PRElGSUVEX0RUPjIwMTcvMTIvMjIgMTI6MDY6MTE8L0NfTU9ESUZJRURfRFQ+PENfVVBMT0FEX1NUQVRVUz48L0NfVVBMT0FEX1NUQVRVUz48Q19VUExPQURfRklMRU5BTUU+PC9DX1VQTE9BRF9GSUxFTkFNRT48Q19MT0FESU5HX1VTRVI+PC9DX0xPQURJTkdfVVNFUj48Q19EUklWRVI+MTEwNDAxPC9DX0RSSVZFUj48Q19DTE9TRV9OTz48L0NfQ0xPU0VfTk8+PC9UQUJMRT48L0VSUD4=";
        String XMLGOODS = "PEVSUD48VEFCTEU+PENfT1dORVJfTk8+MDAxPC9DX09XTkVSX05PPjxDX1RBU0tfTk8+U1UxMTA0MTcxMjIyMDAwMTwvQ19UQVNLX05PPjxDX1RBU0tfSUQ+MTwvQ19UQVNLX0lEPjxDX1RBU0tfVFlQRT7kuIDoiKzlh7rotKc8L0NfVEFTS19UWVBFPjxDX09QX1RZUEU+QzwvQ19PUF9UWVBFPjxDX1NfTE9DQVRJT05fTk8+RjFGMDExMTwvQ19TX0xPQ0FUSU9OX05PPjxDX1NfTE9DQVRJT05fSUQ+NTg8L0NfU19MT0NBVElPTl9JRD48Q19TX0NPTlRBSU5FUl9TRVJOTz48L0NfU19DT05UQUlORVJfU0VSTk8+PENfU19DT05UQUlORVJfTk8+T1UxMTA0MTcxMjIyMDAwMTwvQ19TX0NPTlRBSU5FUl9OTz48Q19EX0xPQ0FUSU9OX05PPjwvQ19EX0xPQ0FUSU9OX05PPjxDX0RfQ09OVEFJTkVSX05PPjwvQ19EX0NPTlRBSU5FUl9OTz48Q19EX0NPTlRBSU5FUl9TRVJOTz48L0NfRF9DT05UQUlORVJfU0VSTk8+PENfR0NPREU+RERLT1NCLTlNTTwvQ19HQ09ERT48Q19TVE9DS0FUVFJfSUQ+MjwvQ19TVE9DS0FUVFJfSUQ+PENfUEFDS19RVFk+MTwvQ19QQUNLX1FUWT48Q19EX1JFUVVFU1RfUVRZPjEuMDA8L0NfRF9SRVFVRVNUX1FUWT48Q19EX0FDS19CQURfUVRZPjAuMDA8L0NfRF9BQ0tfQkFEX1FUWT48Q19EX0FDS19RSUZUX1FUWT4wLjAwPC9DX0RfQUNLX1FJRlRfUVRZPjxDX0RfQUNLX1FUWT4xLjAwPC9DX0RfQUNLX1FUWT48Q19PUF9VU0VSPjAwMDA8L0NfT1BfVVNFUj48Q19PUF9UT09MUz7ooajljZU8L0NfT1BfVE9PTFM+PENfT1BfU1RBVFVTPuW3suWHuui9pjwvQ19PUF9TVEFUVVM+PENfV0FWRV9OTz5XQTExMDQxNzEyMjIwMDAxPC9DX1dBVkVfTk8+PENfU09VUkNFX05PPk9VMTEwNDE3MTIyMjAwMDE8L0NfU09VUkNFX05PPjxDX1JFU0VSVkVEMT5DRF9YTjIwMTcxMjEzMTU0MzQxNzAxMTcwMjkzPC9DX1JFU0VSVkVEMT48Q19SRVNFUlZFRDI+PC9DX1JFU0VSVkVEMj48Q19SRVNFUlZFRDM+PC9DX1JFU0VSVkVEMz48Q19SRVNFUlZFRDQ+WlpfWE4yMDE3MTIyMjExMzIwNzYyNzY4ODwvQ19SRVNFUlZFRDQ+PENfUkVTRVJWRUQ1PjwvQ19SRVNFUlZFRDU+PENfTk9URT48L0NfTk9URT48Q19NS19EVD4yMDE3LzEyLzIyIDEyOjA1OjM4PC9DX01LX0RUPjxDX01LX1VTRVJOTz4wMDAwPC9DX01LX1VTRVJOTz48Q19NT0RJRklFRF9EVD4yMDE3LzEyLzIyIDEyOjA2OjExPC9DX01PRElGSUVEX0RUPjxDX01PRElGSUVEX1VTRVJOTz4wMDAwPC9DX01PRElGSUVEX1VTRVJOTz48Q19VUExPQURfU1RBVFVTPjwvQ19VUExPQURfU1RBVFVTPjxDX1NFTkRfRkFMRz7lkKY8L0NfU0VORF9GQUxHPjwvVEFCTEU+PC9FUlA+";
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient("http://localhost:9999/services/webservice?wsdl");
        Object[] objects = client.invoke("GetWMSInfo", STRTABLE, STRTYPE, XMLGOODS);
        System.out.println("*****"+objects[0].toString());
    }

    @RequestMapping("/test/webservice")
    public void testWebservice() {
        AtwRequisitionOrder order = new AtwRequisitionOrder();
        order.setDiySiteId("FZM007");
        order.setDiySiteTel("028-83551646");
        order.setDiySiteTitle("富森富之美");
        order.setCustomerName("测试");
        order.setReserveTimeQuantum("2017-12-20 14:30");
        order.setOrderNumber("CD_XN20180109112029019999");
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
        order.setAgencyFund(0.00);

        AtwRequisitionOrderGoods goods = new AtwRequisitionOrderGoods();
        goods.setCreateTime(new Date());
        goods.setGoodsCode("SJWT4503-20");
        goods.setGoodsTitle("金牛白色PPR45度弯头20");
        goods.setOrderNumber("CD_XN20180109112029018888");
        goods.setPrice(10D);
        goods.setQuantity(1);

        //备注：目前发送订单通了，取消订单wms还在重新做。
        //第一步：将订单信息转化成要货单实体AtwRequisitionOrder然后调用下面service保存
//        appToWmsOrderService.saveAtwRequisitionOrder(order);
        //第二步：将订单商品转化成要货商品实体AtwRequisitionOrderGoods然后调用下面service保存
//        appToWmsOrderService.saveAtwRequisitionOrderGoods(goods);
        //第三步：调用下面service将订单号发送给wms
//        iCallWms.sendToWmsRequisitionOrderAndGoods("CD_XN20180109112029019999");
        AtwRequisitionOrderGoods goods1 = new AtwRequisitionOrderGoods();
        goods1.setGoodsCode("SJWT4503-25");
        goods1.setOrderNumber("T201801311631638");
        goods1.setCompanyFlag("LYZ");
        goods1.setCreateTime(new Date());
        goods1.setPrice(10D);
        goods1.setQuantity(1);
        goods1.setGoodsTitle("金牛白色PPR45度弯头25");
        goods1.setId(259L);
//        appToWmsOrderService.modifyAtwRequisitionOrderGoods(goods1);

        AtwReturnOrder returnOrder = new AtwReturnOrder();
        returnOrder.setCreateTime(new Date());
        returnOrder.setCreator("测试导购");
        returnOrder.setCreatorPhone("18108654255");
        returnOrder.setDeliverTypeTitle("送货上门");
        returnOrder.setDiySiteAddress("富森美家居城");
        returnOrder.setDiySiteId("FZM007");
        returnOrder.setDiySiteTel("123456678");
        returnOrder.setDiySiteTitle("富之美");
        returnOrder.setOrderNumber("CD_XN20180131161050468455");
        returnOrder.setRemarkInfo("下单下多了");
        returnOrder.setReturnNumber("T201801311631638");
        returnOrder.setReturnPrice(10D);
        returnOrder.setReturnTime(new Date());
        returnOrder.setSellerRealName("测试导购");
        returnOrder.setShoppingAddress("成都市新都区大丰街道订单");
        returnOrder.setStatusId(3);
        returnOrder.setRejecter("游先生");
        returnOrder.setRejecterPhone("18108654255");
        returnOrder.setRejecterAddress("成都市锦江区督院街街道我的小区详细地址");
        returnOrder.setGoodsLineQuantity(1);
//        appToWmsOrderService.saveAtwReturnOrder(returnOrder);
//        appToWmsOrderService.saveAtwRequisitionOrderGoods(goods1);
//        iCallWms.sendToWmsReturnOrderAndGoods("T201801311631638");

//        AtwCancelReturnOrderRequest atwReturnOrderCheckEnter = new AtwCancelReturnOrderRequest();
//        atwReturnOrderCheckEnter.setStoreCode("FZM007");
//        atwReturnOrderCheckEnter.setReturnTime(new Date());
//        atwReturnOrderCheckEnter.setReturnType(ReturnOrderType.NORMAL_RETURN);
//        atwReturnOrderCheckEnter.setCreateTime(new Date());
//        atwReturnOrderCheckEnter.setReturnNo("T201801121041875");
//        appToWmsOrderService.saveAtwCancelReturnOrderRequest(atwReturnOrderCheckEnter);
//
//
//        iCallWms.sendToWmsCancelReturnOrder("T201801121041875");
//        iCallWms.sendToWmsReturnOrderCheck("T201801121041875");

//        WtaCancelOrderResultEnter orderResultEnter = new WtaCancelOrderResultEnter();
//        orderResultEnter.setCreateTime(new Date());
//        orderResultEnter.setOrderNo("CD_XN20180109112029014872");
//        orderResultEnter.setIsCancel(true);
//        WtaCancelReturnOrderResultEnter returnOrderResultEnter = new WtaCancelReturnOrderResultEnter();
//        returnOrderResultEnter.setReturnNumber("T201801121041875");
//        returnOrderResultEnter.setCreateTime(new Date());
//        returnOrderResultEnter.setIsCancel(true);
//
//        WmsToAppOrderService.saveWtaCancelOrderResultEnter(orderResultEnter);
//        WmsToAppOrderService.saveWtaCancelReturnOrderResultEnter(returnOrderResultEnter);

        AtwCancelOrderRequest orderRequest = new AtwCancelOrderRequest();
        orderRequest.setOrderStatus(AppOrderStatus.CANCELING);
        orderRequest.setOrderNo("CD_XN20180131184544890515");
        orderRequest.setCreateTime(new Date());
        orderRequest.setCancelTime(new Date());
        orderRequest.setCancelReason("下错误了");
        appToWmsOrderService.saveAtwCancelOrderRequest(orderRequest);

        iCallWms.sendToWmsCancelOrder("CD_XN20180131184544890515");

    }
}
