package cn.com.leyizhuang.app.remote.webservice.utils;

import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.foundation.pojo.wms.AtwCancelOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.wms.AtwRequisitionOrder;
import cn.com.leyizhuang.app.foundation.pojo.wms.AtwRequisitionOrderGoods;
import cn.com.leyizhuang.common.core.utils.Base64Utils;
import cn.com.leyizhuang.common.util.CountUtil;

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


    /**
     * 获取订单XML数据，相当于发送要货单
     *
     * @param requisitionOrder 要货单
     * @return XML数据
     */
    public static String getRequisitionOrderXml(AtwRequisitionOrder requisitionOrder) {
        String createTime = null;
        if (requisitionOrder.getCreateTime() != null) {
            createTime = DateUtil.formatDate(requisitionOrder.getCreateTime(), "yyyy-MM-dd HH:MM:ss");
        }
        String orderTime = null;
        if (requisitionOrder.getOrderTime() != null) {
            orderTime = DateUtil.formatDate(requisitionOrder.getOrderTime(), "yyyy-MM-dd HH:MM:ss");
        }
        // 这里是一次临时处理，diy_site_id字面上的意义是门店ID，但因为历史原因这个标签必须传递门店编码
        String xmlStr = "<ERP><TABLE>" +
                "<ID>" + requisitionOrder.getId() + "</ID>" +
                "<CANCEL_TIME>" + createTime + "</CANCEL_TIME>" +
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
                "<DELIVERY_TIME>" + requisitionOrder.getReceiveTimeQuantum() + "</DELIVERY_TIME>" +
                "<ORDER_NUMBER>" + requisitionOrder.getOrderNumber() + "</ORDER_NUMBER>" +
                "<RECEIVE_ADDRESS>" + requisitionOrder.getReceiveAddress() + "</RECEIVE_ADDRESS>" +
                "<RECEIVE_NAME>" + requisitionOrder.getReceiveName() + "</RECEIVE_NAME>" +
                "<RECEIVE_PHONE>" + requisitionOrder.getReceivePhone() + "</RECEIVE_PHONE>" +
                "<CITY>" + requisitionOrder.getCity() + "</CITY>" +
                "<DETAIL_ADDRESS>" + requisitionOrder.getDetailAddress() + "</DETAIL_ADDRESS>" +
                "<DISCTRICT>" + requisitionOrder.getDisctrict() + "</DISCTRICT>" +
                "<PROVINCE>" + requisitionOrder.getProvince() + "</PROVINCE>" +
                "<SUBDISTRICT>" + requisitionOrder.getSubdistrict() + "</SUBDISTRICT>" +
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
                "<MEMBER_RECEIVER>" + requisitionOrder.getMemberReceiver() + "</MEMBER_RECEIVER>" +
                "<UNPAYED>" + CountUtil.HALF_UP_SCALE_2(requisitionOrder.getUnpayed()) + "</UNPAYED>" +
                "<TOTAL_GOODS_PRICE>" + CountUtil.HALF_UP_SCALE_2(requisitionOrder.getTotalGoodsPrice()) +
                "</TOTAL_GOODS_PRICE>" +
                "<AGENCY_FUND>" + CountUtil.HALF_UP_SCALE_2(requisitionOrder.getAgencyRefund()) + "</AGENCY_FUND>" +
                "</TABLE></ERP>";

        xmlStr = xmlStr.replace("null", "");
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
                "<order_status>" + cancelOrderRequest.getOrderStatus() + "</order_status>" +
                "</TABLE></ERP>";

        xmlStr = xmlStr.replace("null", "");
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
                "<sub_order_number></sub_order_number>" +
                "</TABLE></ERP>";

        xmlStr = xmlStr.replace("null", "");
        return Base64Utils.encode(xmlStr);
    }

    public static String checkReturnXml(Object[] objects) {
        StringBuilder result = new StringBuilder();
        if (objects != null) {
            for (Object object : objects) {
                result.append(object);
            }
        }
        String resultStr = result.toString();
        // "<RESULTS><STATUS><CODE>1</CODE><MESSAGE>XML参数错误</MESSAGE></STATUS></RESULTS>";
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
}
