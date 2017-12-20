package cn.com.leyizhuang.app.remote.webservice;

import cn.com.leyizhuang.common.core.utils.Base64Utils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import javax.xml.namespace.QName;

/**
 * @author Created on 2017-12-19 13:18
 **/
public class ICallWms {

    private static QName WMSName = new QName("http://tempuri.org/", "GetErpInfo");
    public static void main(String[] args) throws Exception {


        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient("http://120.76.214.99:8199/WmsInterServer.asmx?wsdl");
        String xml = "<ERP>\n" +
                "    <TABLE>\n" +
                "        <ID>136992</ID>\n" +
                "        <CANCEL_TIME></CANCEL_TIME>\n" +
                "        <CHECK_TIME></CHECK_TIME>\n" +
                "        <DIY_SITE_ADDRESS></DIY_SITE_ADDRESS>\n" +
                "        <DIY_SITE_ID>FZM007</DIY_SITE_ID>\n" +
                "        <DIY_SITE_TEL>028-83551646</DIY_SITE_TEL>\n" +
                "        <DIY_SITE_TITLE>富森富之美</DIY_SITE_TITLE>\n" +
                "        <MANAGER_REMARK_INFO></MANAGER_REMARK_INFO>\n" +
                "        <REMARK_INFO></REMARK_INFO>\n" +
                "        <REQUISITION_NUMBER></REQUISITION_NUMBER>\n" +
                "        <STATUS_ID></STATUS_ID>\n" +
                "        <TYPE_ID></TYPE_ID>\n" +
                "        <CUSTOMER_NAME>测试</CUSTOMER_NAME>\n" +
                "        <CUSTOMER_ID>32555</CUSTOMER_ID>\n" +
                "        <DELIVERY_TIME>2017-12-20 14:30</DELIVERY_TIME>\n" +
                "        <ORDER_NUMBER>CD_XN20171220102259255115</ORDER_NUMBER>\n" +
                "        <RECEIVE_ADDRESS>成都市新都区大丰街道订单</RECEIVE_ADDRESS>\n" +
                "        <RECEIVE_NAME>测试</RECEIVE_NAME>\n" +
                "        <RECEIVE_PHONE>13408698552</RECEIVE_PHONE>\n" +
                "        <CITY>成都市</CITY>\n" +
                "        <DETAIL_ADDRESS>订单</DETAIL_ADDRESS>\n" +
                "        <DISCTRICT>新都区</DISCTRICT>\n" +
                "        <PROVINCE></PROVINCE>\n" +
                "        <SUBDISTRICT>大丰街道</SUBDISTRICT>\n" +
                "        <ORDER_TIME>2017-12-20 10:22:59.0</ORDER_TIME>\n" +
                "        <SUB_ORDER_NUMBER></SUB_ORDER_NUMBER>\n" +
                "        <SELLER_TEL>18280285992</SELLER_TEL>\n" +
                "        <GOODS_QUANTITY>1</GOODS_QUANTITY>\n" +
                "        <UPSTAIRS_ALL>0.0</UPSTAIRS_ALL>\n" +
                "        <UPSTAIRS_LEFT>0.0</UPSTAIRS_LEFT>\n" +
                "        <SELLER_NAME>樊云霞</SELLER_NAME>\n" +
                "        <DELIVERY_FEE>30.0</DELIVERY_FEE>\n" +
                "        <COLOR_FEE>0.0</COLOR_FEE>\n" +
                "        <DISCOUNT>0.0</DISCOUNT>\n" +
                "        <OTHER_PAYED>0.0</OTHER_PAYED>\n" +
                "        <BALANCE_USED>60.0</BALANCE_USED>\n" +
                "        <MEMBER_RECEIVER>FALSE</MEMBER_RECEIVER>\n" +
                "        <UNPAYED>0.0</UNPAYED>\n" +
                "        <TOTAL_GOODS_PRICE>30.0</TOTAL_GOODS_PRICE>\n" +
                "        <AGENCY_FUND>0.0</AGENCY_FUND>\n" +
                "    </TABLE>\n" +
                "</ERP>";
        String encodedXml = Base64Utils.encode(xml);
       // invoke(WMSName, "td_requisition", "1", xmlEncode);
        Object[] objects=client.invoke(WMSName,"td_requisition","1" ,encodedXml);
        System.out.println("*****"+objects[0].toString());
    }
}
