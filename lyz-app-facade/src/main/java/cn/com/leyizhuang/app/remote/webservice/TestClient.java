package cn.com.leyizhuang.app.remote.webservice;

import cn.com.leyizhuang.app.core.constant.ReturnOrderType;
import cn.com.leyizhuang.app.core.pay.wechat.refund.OnlinePayRefundService;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwCancelReturnOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrder;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwReturnOrder;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderDeliveryDetail;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.core.utils.Base64Utils;
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

    @Resource
    private AppCustomerService appCustomerService;
    @Resource
    private CityService cityService;
    @Resource
    private OnlinePayRefundService onlinePayRefundService;
    @Resource
    private AppSeparateOrderService separateOrderService;
    @Resource
    private ReturnOrderDeliveryDetailsService returnOrderDeliveryDetailsService;

    public static void main(String[] args) throws Exception {
//       String STRTABLE = "tbw_send_task_m";
//       String STRTABLE = "tbw_om_d";
         String STRTABLE = "CUXAPP_INV_STORE_TRANS_OUT";
         String STRTYPE = "test";
//       String XML = "<ERP><TABLE><C_OWNER_NO>001</C_OWNER_NO><C_OM_NO>OM11051802240001</C_OM_NO><C_OM_ID>1</C_OM_ID><C_OM_TYPE>一般调拨</C_OM_TYPE><C_GCODE>WY2580T-17L</C_GCODE><C_OWNER_GCODE>WY2580T-17L</C_OWNER_GCODE><C_PACK_QTY>1</C_PACK_QTY><C_QTY>76.00</C_QTY><C_WAVE_QTY>76.00</C_WAVE_QTY><C_ACK_QTY>76.00</C_ACK_QTY><C_CHECK_QTY>76.00</C_CHECK_QTY><C_PRICE></C_PRICE><C_TAX_RATE></C_TAX_RATE><C_OP_STATUS>已定位</C_OP_STATUS><C_ITEM_TYPE>良品</C_ITEM_TYPE><C_RESERVED1>76.00</C_RESERVED1><C_RESERVED2></C_RESERVED2><C_RESERVED3></C_RESERVED3><C_RESERVED4></C_RESERVED4><C_RESERVED5></C_RESERVED5><C_NOTE></C_NOTE><C_OUT_USERNO>000003</C_OUT_USERNO><C_OUT_DT>2018/2/24 9:56:13</C_OUT_DT><C_CHECK_USERNO>000002</C_CHECK_USERNO><C_CHECK_DT>2018/2/24 11:28:22</C_CHECK_DT><C_PRODUCE_DT></C_PRODUCE_DT><C_MK_USERNO>000002</C_MK_USERNO><C_MK_DT>2018/2/24 9:51:19</C_MK_DT><C_MODIFIED_USERNO></C_MODIFIED_USERNO><C_MODIFIED_DT></C_MODIFIED_DT><C_UPLOAD_STATUS>初始</C_UPLOAD_STATUS></TABLE></ERP>";
//       String XMLGOODS = "PEVSUD48VEFCTEU+PENfT1dORVJfTk8+MDAxPC9DX09XTkVSX05PPjxDX1RBU0tfTk8+U1UxMTA0MTcxMjIyMDAwMTwvQ19UQVNLX05PPjxDX1RBU0tfSUQ+MTwvQ19UQVNLX0lEPjxDX1RBU0tfVFlQRT7kuIDoiKzlh7rotKc8L0NfVEFTS19UWVBFPjxDX09QX1RZUEU+QzwvQ19PUF9UWVBFPjxDX1NfTE9DQVRJT05fTk8+RjFGMDExMTwvQ19TX0xPQ0FUSU9OX05PPjxDX1NfTE9DQVRJT05fSUQ+NTg8L0NfU19MT0NBVElPTl9JRD48Q19TX0NPTlRBSU5FUl9TRVJOTz48L0NfU19DT05UQUlORVJfU0VSTk8+PENfU19DT05UQUlORVJfTk8+T1UxMTA0MTcxMjIyMDAwMTwvQ19TX0NPTlRBSU5FUl9OTz48Q19EX0xPQ0FUSU9OX05PPjwvQ19EX0xPQ0FUSU9OX05PPjxDX0RfQ09OVEFJTkVSX05PPjwvQ19EX0NPTlRBSU5FUl9OTz48Q19EX0NPTlRBSU5FUl9TRVJOTz48L0NfRF9DT05UQUlORVJfU0VSTk8+PENfR0NPREU+RERLT1NCLTlNTTwvQ19HQ09ERT48Q19TVE9DS0FUVFJfSUQ+MjwvQ19TVE9DS0FUVFJfSUQ+PENfUEFDS19RVFk+MTwvQ19QQUNLX1FUWT48Q19EX1JFUVVFU1RfUVRZPjEuMDA8L0NfRF9SRVFVRVNUX1FUWT48Q19EX0FDS19CQURfUVRZPjAuMDA8L0NfRF9BQ0tfQkFEX1FUWT48Q19EX0FDS19RSUZUX1FUWT4wLjAwPC9DX0RfQUNLX1FJRlRfUVRZPjxDX0RfQUNLX1FUWT4xLjAwPC9DX0RfQUNLX1FUWT48Q19PUF9VU0VSPjAwMDA8L0NfT1BfVVNFUj48Q19PUF9UT09MUz7ooajljZU8L0NfT1BfVE9PTFM+PENfT1BfU1RBVFVTPuW3suWHuui9pjwvQ19PUF9TVEFUVVM+PENfV0FWRV9OTz5XQTExMDQxNzEyMjIwMDAxPC9DX1dBVkVfTk8+PENfU09VUkNFX05PPk9VMTEwNDE3MTIyMjAwMDE8L0NfU09VUkNFX05PPjxDX1JFU0VSVkVEMT5DRF9YTjIwMTcxMjEzMTU0MzQxNzAxMTcwMjkzPC9DX1JFU0VSVkVEMT48Q19SRVNFUlZFRDI+PC9DX1JFU0VSVkVEMj48Q19SRVNFUlZFRDM+PC9DX1JFU0VSVkVEMz48Q19SRVNFUlZFRDQ+WlpfWE4yMDE3MTIyMjExMzIwNzYyNzY4ODwvQ19SRVNFUlZFRDQ+PENfUkVTRVJWRUQ1PjwvQ19SRVNFUlZFRDU+PENfTk9URT48L0NfTk9URT48Q19NS19EVD4yMDE3LzEyLzIyIDEyOjA1OjM4PC9DX01LX0RUPjxDX01LX1VTRVJOTz4wMDAwPC9DX01LX1VTRVJOTz48Q19NT0RJRklFRF9EVD4yMDE3LzEyLzIyIDEyOjA2OjExPC9DX01PRElGSUVEX0RUPjxDX01PRElGSUVEX1VTRVJOTz4wMDAwPC9DX01PRElGSUVEX1VTRVJOTz48Q19VUExPQURfU1RBVFVTPjwvQ19VUExPQURfU1RBVFVTPjxDX1NFTkRfRkFMRz7lkKY8L0NfU0VORF9GQUxHPjwvVEFCTEU+PC9FUlA+";
         String XML = "<ERP><TABLE><SOB_ID>2121</SOB_ID><TRANS_ID>OM11051802240001</TRANS_ID><TRANS_TYPE>1</TRANS_TYPE><TRANS_NUMBER>CD_XN20171205183251592719394</TRANS_NUMBER><CUSTOMER_ID>41</CUSTOMER_ID><CUSTOMER_NUMBER>18108117918</CUSTOMER_NUMBER><DIY_SITE_CODE>FZM007</DIY_SITE_CODE><SHIP_DATE>2017-12-06 16:03:39</SHIP_DATE><ITEM_CODE>TJM9715X-9</ITEM_CODE><QUANTITY>1</QUANTITY><ATTRIBUTE1>11</ATTRIBUTE1><ATTRIBUTE2>11</ATTRIBUTE2><ATTRIBUTE3>11</ATTRIBUTE3><ATTRIBUTE4>11</ATTRIBUTE4><ATTRIBUTE5>11</ATTRIBUTE5></TABLE></ERP>";
         JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
         //Client client = dcf.createClient("http://localhost:9999/services/webservice?wsdl");s
         Client client = dcf.createClient("http://localhost:9999/ebs/services/webservice?wsdl");
         //Object[] objects = client.invoke("GetWMSInfo", STRTABLE, STRTYPE, Base64Utils.encode(XML));
         Object[] objects = client.invoke("GetEBSInfo", STRTABLE, STRTYPE, Base64Utils.encode
                (XML));
        System.out.println("*****" + objects[0].toString());
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

        AtwCancelReturnOrderRequest orderRequest = new AtwCancelReturnOrderRequest();
        orderRequest.setReturnNo("T20180222113753087");
        orderRequest.setCreateTime(new Date());
        orderRequest.setReturnType(ReturnOrderType.NORMAL_RETURN);
        orderRequest.setReturnTime(new Date());
        orderRequest.setStoreCode("FZM007");

//        appToWmsOrderService.saveAtwCancelReturnOrderRequest(orderRequest);
//
//        iCallWms.sendToWmsCancelReturnOrder("T20180222113753087");
//
//        AtwReturnOrderCheckEnter checkEnter = new AtwReturnOrderCheckEnter();

//        checkEnter.setCreateTime(new Date());
//        checkEnter.setReturnStatus(AppReturnOrderStatus.PENDING_REFUND);
//        checkEnter.setCheckGoodsTime(new Date());
//        checkEnter.setReturnNo("T20180206175111713");

//        appToWmsOrderService.saveAtwReturnOrderCheckEnter(checkEnter);
//        iCallWms.sendToWmsReturnOrderCheck("T20180206175111713");


//        AppCustomer newUser = new AppCustomer();
//        List<CustomerProfession> professions = appCustomerService.getCustomerProfessionListByStatus(AppWhetherFlag.Y.toString());
//        newUser.setCustomerProfessionDesc(null != professions ? professions.stream().filter(p -> p.getTitle().equals("DG")).collect(Collectors.toList()).get(0).getDescription() : "");
//
//        System.out.println(newUser);
//        onlinePayRefundService.wechatReturnMoney(1L, 6, 0.01D, "CD_XN20180111095243410278", "T201803101111512678");
//
//
//        separateOrderService.separateOrderRefund("TK_20180311115824685");
        ReturnOrderDeliveryDetail returnOrderDeliveryDetail = new ReturnOrderDeliveryDetail();
        returnOrderDeliveryDetail.setReturnNo("123123");
        returnOrderDeliveryDetail.setPickersId(2L);
        returnOrderDeliveryDetail.setCreateTime(new Date());
        returnOrderDeliveryDetail.setWarehouseNo("123");
        returnOrderDeliveryDetail.setPickersNumber("11111");
//        returnOrderDeliveryDetailsService.addReturnOrderDeliveryInfoDetails(returnOrderDeliveryDetail);
    }
}
