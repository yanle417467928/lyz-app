package cn.com.leyizhuang.app.remote.webservice.utils;

import cn.com.leyizhuang.app.core.constant.AppReturnOrderStatus;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.*;
import cn.com.leyizhuang.common.core.utils.Base64Utils;
import cn.com.leyizhuang.common.util.CountUtil;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jerry.Ren
 * Notes:主要写生成传送的xml格式数据
 * Created with IntelliJ IDEA.
 * Date: 2017/12/20.
 * Time: 11:38.
 */

public class AppXmlUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppXmlUtil.class);

    /**
     * 获取订单XML数据，相当于发送要货单
     *
     * @param requisitionOrder 要货单
     * @return XML数据
     */
    public static String getRequisitionOrderXml(AtwRequisitionOrder requisitionOrder) {
        String orderTime = null;
        if (requisitionOrder.getOrderTime() != null) {
            orderTime = DateUtil.formatDate(requisitionOrder.getOrderTime(), "yyyy-MM-dd HH:MM:ss");
        }
        String residenceName = requisitionOrder.getResidenceName();
        if(null != residenceName && residenceName.length() > 15){
            requisitionOrder.setResidenceName(residenceName.substring(0,14));
        }
        // 这里是一次临时处理，diy_site_id字面上的意义是门店ID，但因为历史原因这个标签必须传递门店编码
        String xmlStr = "<ERP><TABLE>" +
                "<ID>" + requisitionOrder.getId() + "</ID>" +
                "<CANCEL_TIME></CANCEL_TIME>" +
                "<CHECK_TIME></CHECK_TIME>" +
                "<DIY_SITE_ADDRESS>" + requisitionOrder.getDiySiteAddress() + "</DIY_SITE_ADDRESS>" +
                "<DIY_SITE_ID>" + requisitionOrder.getDiySiteId() + "</DIY_SITE_ID>" +
                "<DIY_SITE_TEL>" + requisitionOrder.getDiySiteTel() + "</DIY_SITE_TEL>" +
                "<DIY_SITE_TITLE>" + requisitionOrder.getDiySiteTitle() + "</DIY_SITE_TITLE>" +
                "<MANAGER_REMARK_INFO></MANAGER_REMARK_INFO>" +
                "<REMARK_INFO>" + requisitionOrder.getRemarkInfo() + "</REMARK_INFO>" +
                "<REQUISITION_NUMBER></REQUISITION_NUMBER>" +
                "<STATUS_ID></STATUS_ID>" +
                "<TYPE_ID></TYPE_ID>" +
                "<CUSTOMER_NAME>" + requisitionOrder.getCustomerName() + "</CUSTOMER_NAME>" +
                "<CUSTOMER_ID></CUSTOMER_ID>" +
                "<DELIVERY_TIME>" + requisitionOrder.getReserveTimeQuantum() + "</DELIVERY_TIME>" +
                "<ORDER_NUMBER>" + requisitionOrder.getOrderNumber() + "</ORDER_NUMBER>" +
                "<RECEIVE_ADDRESS>" + requisitionOrder.getReceiveAddress() + "</RECEIVE_ADDRESS>" +
                "<RECEIVE_NAME>" + requisitionOrder.getReceiveName() + "</RECEIVE_NAME>" +
                "<RECEIVE_PHONE>" + requisitionOrder.getReceivePhone() + "</RECEIVE_PHONE>" +
                "<CITY>" + requisitionOrder.getCity() + "</CITY>" +
                "<DETAIL_ADDRESS>" + requisitionOrder.getDetailAddress() + "</DETAIL_ADDRESS>" +
                "<DISCTRICT>" + requisitionOrder.getDisctrict() + "</DISCTRICT>" +
                "<PROVINCE>" + requisitionOrder.getProvince() + "</PROVINCE>" +
                "<SUBDISTRICT>" + requisitionOrder.getSubdistrict() + "</SUBDISTRICT>" +
                "<RESIDENCE_NAME>" + requisitionOrder.getResidenceName() + "</RESIDENCE_NAME>" +
                "<ORDER_TIME>" + orderTime + "</ORDER_TIME>" +
                "<SUB_ORDER_NUMBER></SUB_ORDER_NUMBER>" +
                "<SELLER_TEL>" + requisitionOrder.getSellerTel() + "</SELLER_TEL>" +
                "<GOODS_QUANTITY>" + requisitionOrder.getGoodsQuantity() + "</GOODS_QUANTITY>" +
                "<UPSTAIRS_ALL>" + CountUtil.HALF_UP_SCALE_2(requisitionOrder.getUpstairsAll()) + "</UPSTAIRS_ALL>" +
                "<UPSTAIRS_LEFT></UPSTAIRS_LEFT>" +
                "<SELLER_NAME>" + requisitionOrder.getSellerName() + "</SELLER_NAME>" +
                "<DELIVERY_FEE>" + requisitionOrder.getDeliveryFee() + "</DELIVERY_FEE>" +
                "<COLOR_FEE>" + CountUtil.HALF_UP_SCALE_2(requisitionOrder.getColorFee()) + "</COLOR_FEE>" +
                "<DISCOUNT>" + CountUtil.HALF_UP_SCALE_2(requisitionOrder.getDiscount()) + "</DISCOUNT>" +
                "<OTHER_PAYED>" + CountUtil.HALF_UP_SCALE_2(requisitionOrder.getOtherPayed()) + "</OTHER_PAYED>" +
                "<BALANCE_USED>" + CountUtil.HALF_UP_SCALE_2(requisitionOrder.getBalanceUsed()) + "</BALANCE_USED>" +
                "<MEMBER_RECEIVER>" + String.valueOf(requisitionOrder.getMemberReceiver()) + "</MEMBER_RECEIVER>" +
                "<UNPAYED>" + CountUtil.HALF_UP_SCALE_2(requisitionOrder.getUnpayed()) + "</UNPAYED>" +
                "<TOTAL_GOODS_PRICE>" + CountUtil.HALF_UP_SCALE_2(requisitionOrder.getTotalGoodsPrice()) +
                "</TOTAL_GOODS_PRICE>" +
                "<AGENCY_FUND>" + CountUtil.HALF_UP_SCALE_2(requisitionOrder.getAgencyFund()) + "</AGENCY_FUND>" +
                "<IS_PRINT>"+ requisitionOrder.getIsPrint() +"</IS_PRINT>"+
                "</TABLE></ERP>";

        xmlStr = xmlStr.replace("null", "");
        LOGGER.info("getRequisitionOrderXml OUT, 拼接订单XML数据, 出参 xmlStr:{}", xmlStr);
        return Base64Utils.encode(xmlStr);
    }

    /**
     * 获取退货单xml
     *
     * @param returnOrder 退单号
     * @return xml
     */
    public static String getReturnOrderXml(AtwReturnOrder returnOrder) {
        String returnTime = null;
        if (returnOrder.getReturnTime() != null) {
            returnTime = DateUtil.formatDate(returnOrder.getReturnTime(), "yyyy-MM-dd HH:MM:ss");
        }

        // 这里是一次临时处理，diy_site_id字面上的意义是门店ID，但因为历史原因这个标签必须传递门店编码
        String xmlStr = "<ERP><TABLE>" +
                "<id>" + returnOrder.getId() + "</id>" +
                "<cancel_time></cancel_time>" +
                "<check_time></check_time>" +
                "<diy_site_address>" + returnOrder.getDiySiteAddress() + "</diy_site_address>" +
                "<diy_site_id>" + returnOrder.getDiySiteId() + "</diy_site_id>" +
                "<diy_site_tel>" + returnOrder.getDiySiteTel() + "</diy_site_tel>" +
                "<diy_site_title>" + returnOrder.getDiySiteTitle() + "</diy_site_title>" +
                "<manager_remark_info></manager_remark_info>" +
                "<order_number>" + returnOrder.getOrderNumber() + "</order_number>" +
                "<order_time></order_time>" +
                "<pay_type_id></pay_type_id>" +
                "<pay_type_title></pay_type_title>" +
                "<remark_info>" + returnOrder.getRemarkInfo() + "</remark_info>" +
                "<return_number>" + returnOrder.getReturnNumber() + "</return_number>" +
                "<return_time>" + returnTime + "</return_time>" +
                "<sort_id></sort_id>" +
                "<status_id>" + returnOrder.getStatusId() + "</status_id>" +
                "<username>" + returnOrder.getDeliveryClerkNo() + "</username>" +
                "<deliver_type_title>" + returnOrder.getDeliverTypeTitle() + "</deliver_type_title>" +
                "<turn_price>" + returnOrder.getReturnPrice() + "</turn_price>" +
                "<turn_type></turn_type>" +
                "<shopping_address>" + returnOrder.getShoppingAddress() + "</shopping_address>" +
                "<province>" + returnOrder.getProvince() + "</province>" +
                "<city>" + returnOrder.getCity() + "</city>" +
                "<disctrict>" + returnOrder.getDisctrict() + "</disctrict>" +
                "<subdistrict>" + returnOrder.getSubdistrict() + "</subdistrict>" +
                "<residence_name>" + returnOrder.getResidenceName() + "</residence_name>" +
                "<seller_real_name>" + returnOrder.getSellerRealName() + "</seller_real_name>" +
                "<goods_line_quantity>" + returnOrder.getGoodsLineQuantity() + "</goods_line_quantity>" +
                "<creator>" + returnOrder.getCreator() + "</creator>" +
                "<creator_phone>" + returnOrder.getCreatorPhone() + "</creator_phone>" +
                "<rejecter>" + returnOrder.getRejecter() + "</rejecter>" +
                "<rejecter_phone>" + returnOrder.getRejecterPhone() + "</rejecter_phone>" +
                "<rejecter_address>" + returnOrder.getRejecterAddress() + "</rejecter_address>" +
                "</TABLE></ERP>";

        xmlStr = xmlStr.replace("null", "");
        LOGGER.info("getRequisitionOrderXml OUT, 拼接订单XML数据, 出参 xmlStr:{}", xmlStr);
        return Base64Utils.encode(xmlStr);
    }

    /**
     * 获取拒签退货和取消订单xml
     *
     * @param cancelOrderRequest 取消订单
     * @return XML数据
     */
    public static String getCancelOrderXml(AtwCancelOrderRequest cancelOrderRequest) {

        String cancelTime = null;
        if (cancelOrderRequest.getCancelTime() != null) {
            cancelTime = DateUtil.formatDate(cancelOrderRequest.getCancelTime(), "yyyy-MM-dd HH:MM:ss");
        }
        String createTime = null;
        if (cancelOrderRequest.getCreateTime() != null) {
            createTime = DateUtil.formatDate(cancelOrderRequest.getCreateTime(), "yyyy-MM-dd HH:MM:ss");
        }
        // diy_site_id字面上的意义是门店ID，但因为历史原因这个标签必须传递门店编码
        String xmlStr = "<ERP><TABLE>" +
                "<id>" + cancelOrderRequest.getId() + "</id>" +
                "<create_time>" + createTime + "</create_time>" +
                "<cancel_time>" + cancelTime + "</cancel_time>" +
                "<cancel_reason>" + cancelOrderRequest.getCancelReason() + "</cancel_reason>" +
                "<order_no>" + cancelOrderRequest.getOrderNo() + "</order_no>" +
                "<order_status>" + cancelOrderRequest.getOrderStatus().getDescription() + "</order_status>" +
                "</TABLE></ERP>";

        xmlStr = xmlStr.replace("null", "");
        LOGGER.info("getCancelOrderXml OUT, 拼接拒签退货和取消订单xml, 出参 xmlStr:{}", xmlStr);
        return Base64Utils.encode(xmlStr);
    }

    /**
     * 取消退货单xml
     *
     * @param returnOrderRequest 取消退货单
     * @return xml
     */
    public static String getCancelReturnOrderXml(AtwCancelReturnOrderRequest returnOrderRequest) {

        String returnTime = null;
        if (returnOrderRequest.getReturnTime() != null) {
            returnTime = DateUtil.formatDate(returnOrderRequest.getReturnTime(), "yyyy-MM-dd HH:MM:ss");
        }
        // diy_site_id字面上的意义是门店ID，但因为历史原因这个标签必须传递门店编码
        String xmlStr = "<ERP><TABLE>" +
                "<id>" + returnOrderRequest.getId() + "</id>" +
                "<c_request_dt>" + returnTime + "</c_request_dt>" +
                "<c_due_dt></c_due_dt>" +
                "<c_customer_no>" + returnOrderRequest.getStoreCode() + "</c_customer_no>" +
                "<c_po_no>" + returnOrderRequest.getReturnNo() + "</c_po_no>" +
                "<c_po_type>" + returnOrderRequest.getReturnType().getDescription() + "</c_po_type>" +
                "</TABLE></ERP>";

        xmlStr = xmlStr.replace("null", "");
        LOGGER.info("getCancelOrderXml OUT, 拼接拒签退货和取消订单xml, 出参 xmlStr:{}", xmlStr);
        return Base64Utils.encode(xmlStr);
    }

    /**
     * 获取退货商品和要货商品XML
     *
     * @param orderGoods 要货商品
     * @return XML数据
     */
    public static String getRequisitionOrderGoodsXml(AtwRequisitionOrderGoods orderGoods) {

        String xmlStr = "<ERP><TABLE>" +
                "<id>" + orderGoods.getId() + "</id>" +
                "<goods_code>" + orderGoods.getGoodsCode() + "</goods_code>" +
                "<goods_title>" + orderGoods.getGoodsTitle() + "</goods_title>" +
                "<price>" + orderGoods.getPrice() + "</price>" +
                "<quantity>" + orderGoods.getQuantity() + "</quantity>" +
                "<td_requisition_id></td_requisition_id>" +
                "<order_number>" + orderGoods.getOrderNumber() + "</order_number>" +
                "<sub_order_number>" + orderGoods.getCompanyFlag() + "</sub_order_number>" +
                "</TABLE></ERP>";

        xmlStr = xmlStr.replace("null", "");
        LOGGER.info("getRequisitionOrderGoodsXml OUT, 拼接退货商品和要货商品XML, 出参 xmlStr:{}", xmlStr);

        return Base64Utils.encode(xmlStr);
    }

    /**
     * 发送退货单收货确认
     *
     * @param atwReturnOrderCheckEnter 发送退货单收货确认
     * @return xml
     */
    public static String getReturnOrderCheckEnterXml(AtwReturnOrderCheckEnter atwReturnOrderCheckEnter) {

        String checkTime = null;
        if (atwReturnOrderCheckEnter.getCheckGoodsTime() != null) {
            checkTime = DateUtil.formatDate(atwReturnOrderCheckEnter.getCheckGoodsTime(), "yyyy-MM-dd HH:MM:ss");
        }
        int returnStatus = 0;
        if (AppReturnOrderStatus.PENDING_REFUND.equals(atwReturnOrderCheckEnter.getReturnStatus())) {
            returnStatus = 4;
        }
        String xmlStr = "<ERP><TABLE>" +
                "<check_goods_time>" + checkTime + "</check_goods_time>" +
                "<return_no>" + atwReturnOrderCheckEnter.getReturnNo() + "</return_no>" +
                "<return_status>" + returnStatus + "</return_status>" +
                "</TABLE></ERP>";

        xmlStr = xmlStr.replace("null", "");
        LOGGER.info("getRequisitionOrderGoodsXml OUT, 拼接发送退货单收货确认XML, 出参 xmlStr:{}", xmlStr);

        return Base64Utils.encode(xmlStr);
    }

    /**
     * 拼接结果信息返回给wms
     *
     * @return xml结果
     */
    public static String resultStrXml(Integer code, String message) {
        String xmlStr = "<RESULTS><STATUS>" +
                "<CODE>" + code + "</CODE>" +
                "<MESSAGE>" + message + "</MESSAGE>" +
                "</STATUS></RESULTS>";

        xmlStr = xmlStr.replace("null", "");
        LOGGER.info("resultStrXml OUT, 拼接结果信息返回给wms, 出参 xmlStr:{}", xmlStr);

        return xmlStr;
    }

    /**
     * 拼接结果信息返回给EBS
     *
     * @return xml结果
     */
    public static String generateResultXmlToEbs(Integer code, String message) {
        LOGGER.info("generateResultXmlToEbs IN, 拼接结果信息返回给 EBS\n, 入参: code:{},message:{}", code, message);
        String xmlStr = "<RESULTS><STATUS>" +
                "<CODE>" + code + "</CODE>" +
                "<MESSAGE>" + message + "</MESSAGE>" +
                "</STATUS></RESULTS>";

        xmlStr = xmlStr.replace("null", "");
        LOGGER.info("generateResultXmlToEbs OUT, 拼接结果信息返回给 EBS, 出参 xmlStr:{}", xmlStr);
        return xmlStr;
    }

    /**
     * 解析wms返回的XML字符串
     *
     * @param xml 主体内容
     * @return 文档主体
     */
    public static Document parseStrXml(String xml) throws ParserConfigurationException, IOException, SAXException {
        xml = xml.trim();

        String strXml = xml.replace("\n", "");

        String decodedXML = Base64Utils.decode(strXml);

        if (StringUtils.isBlank(decodedXML)) {
            LOGGER.info("getWMSInfo, OUT, 解密后XML数据为空");
            return null;
        }
        LOGGER.debug("getWMSInfo, decodedXML=" + decodedXML);

        // 解析XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(decodedXML));
        return builder.parse(is);
    }

    /**
     * 解析返回结果信息
     *
     * @param objects
     * @return
     */
    public static String checkReturnXml(Object[] objects) {
        StringBuilder result = new StringBuilder();
        if (objects != null) {
            for (Object object : objects) {
                result.append(object);
            }
        }
        String resultStr = result.toString();
        if (!resultStr.contains("<CODE>") || !resultStr.contains("</CODE>") || !resultStr.contains("<MESSAGE>")
                || !resultStr.contains("</MESSAGE>")) {
            return "返回XML格式错误错误:" + resultStr;
        }
        String regEx = "<CODE>([\\s\\S]*?)</CODE>";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(resultStr);

        if (mat.find()) {
            System.out.println("CODE is :" + mat.group(0));
            String code = mat.group(0).replace("<CODE>", "");
            code = code.replace("</CODE>", "").trim();
            if (Integer.parseInt(code) == 0) {
                return null;
            } else {
                String errorMsg = "<MESSAGE>([\\s\\S]*?)</MESSAGE>";
                pat = Pattern.compile(errorMsg);
                mat = pat.matcher(resultStr);
                if (mat.find()) {
                    System.out.println("ERRORMSG is :" + mat.group(0));
                    String msg = mat.group(0).replace("<MESSAGE>", "");
                    msg = msg.replace("</MESSAGE>", "").trim();
                    return msg;
                }
            }
        }
        return null;
    }

    public static String format(String unformattedXml) {
        try {
            final Document document = parseXmlFile(unformattedXml);
            if (document == null) {
                return unformattedXml;
            }
            OutputFormat format = new OutputFormat(document);
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);
            return out.toString();
        } catch (IOException e) {
            return unformattedXml;
        }
    }

    private static Document parseXmlFile(String in) throws IOException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException | SAXException e) {
            return null;
        }
    }
}
