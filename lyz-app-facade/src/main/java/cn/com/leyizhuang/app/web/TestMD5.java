package cn.com.leyizhuang.app.web;

import cn.com.leyizhuang.common.core.utils.Base64Utils;

import java.util.regex.Pattern;

/**
 * @author Created on 2017-09-19 14:02
 **/
public class TestMD5 {
    public static void main(String[] args) {
        /*String password = DigestUtils.md5DigestAsHex("admin123".getBytes());
        System.out.println(password);*/

//        String a = Base64Utils.encode("<ERP><TABLE><C_COMPANY_ID>2033</C_COMPANY_ID><C_WH_NO>1104</C_WH_NO><C_OWNER_NO>001</C_OWNER_NO><C_TASK_NO>SU11041712220001</C_TASK_NO><C_TASK_TYPE>一般出货</C_TASK_TYPE><C_PRINT_TIMES>0</C_PRINT_TIMES><C_CAR_NO>0024</C_CAR_NO><C_LINE_NO>0001</C_LINE_NO><C_PLAT_NO>0011</C_PLAT_NO><C_OP_USER>000006</C_OP_USER><C_OP_TOOLS>纸单</C_OP_TOOLS><C_OP_STATUS>已出车</C_OP_STATUS><C_CUSTOMER_NO>1000</C_CUSTOMER_NO><C_PROVIDER_NO></C_PROVIDER_NO><C_BEGIN_DT>2017/12/22 12:05:38</C_BEGIN_DT><C_END_DT>2017/12/22 12:06:11</C_END_DT><C_RESERVED1>CD_XN20171213154341701170293</C_RESERVED1><C_RESERVED2>郑州市二七区马寨镇不知</C_RESERVED2><C_RESERVED3></C_RESERVED3><C_RESERVED4></C_RESERVED4><C_RESERVED5></C_RESERVED5><C_NOTE></C_NOTE><C_MK_USERNO>0000</C_MK_USERNO><C_MK_DT>2017/12/22 12:05:38</C_MK_DT><C_MODIFIED_USERNO>0000</C_MODIFIED_USERNO><C_MODIFIED_DT>2017/12/22 12:06:11</C_MODIFIED_DT><C_UPLOAD_STATUS></C_UPLOAD_STATUS><C_UPLOAD_FILENAME></C_UPLOAD_FILENAME><C_LOADING_USER></C_LOADING_USER><C_DRIVER>110401</C_DRIVER><C_CLOSE_NO></C_CLOSE_NO></TABLE></ERP>");
        String a = Base64Utils.encode("<ERP><TABLE><C_OWNER_NO>001</C_OWNER_NO><C_TASK_NO>SU11041712220001</C_TASK_NO><C_TASK_ID>1</C_TASK_ID><C_TASK_TYPE>一般出货</C_TASK_TYPE><C_OP_TYPE>C</C_OP_TYPE><C_S_LOCATION_NO>F1F0111</C_S_LOCATION_NO><C_S_LOCATION_ID>58</C_S_LOCATION_ID><C_S_CONTAINER_SERNO></C_S_CONTAINER_SERNO><C_S_CONTAINER_NO>OU11041712220001</C_S_CONTAINER_NO><C_D_LOCATION_NO></C_D_LOCATION_NO><C_D_CONTAINER_NO></C_D_CONTAINER_NO><C_D_CONTAINER_SERNO></C_D_CONTAINER_SERNO><C_GCODE>DDKOSB-9MM</C_GCODE><C_STOCKATTR_ID>2</C_STOCKATTR_ID><C_PACK_QTY>1</C_PACK_QTY><C_D_REQUEST_QTY>1.00</C_D_REQUEST_QTY><C_D_ACK_BAD_QTY>0.00</C_D_ACK_BAD_QTY><C_D_ACK_QIFT_QTY>0.00</C_D_ACK_QIFT_QTY><C_D_ACK_QTY>1.00</C_D_ACK_QTY><C_OP_USER>0000</C_OP_USER><C_OP_TOOLS>表单</C_OP_TOOLS><C_OP_STATUS>已出车</C_OP_STATUS><C_WAVE_NO>WA11041712220001</C_WAVE_NO><C_SOURCE_NO>OU11041712220001</C_SOURCE_NO><C_RESERVED1>CD_XN20171213154341701170293</C_RESERVED1><C_RESERVED2></C_RESERVED2><C_RESERVED3></C_RESERVED3><C_RESERVED4>ZZ_XN20171222113207627688</C_RESERVED4><C_RESERVED5></C_RESERVED5><C_NOTE></C_NOTE><C_MK_DT>2017/12/22 12:05:38</C_MK_DT><C_MK_USERNO>0000</C_MK_USERNO><C_MODIFIED_DT>2017/12/22 12:06:11</C_MODIFIED_DT><C_MODIFIED_USERNO>0000</C_MODIFIED_USERNO><C_UPLOAD_STATUS></C_UPLOAD_STATUS><C_SEND_FALG>否</C_SEND_FALG></TABLE></ERP>");
        System.out.println(a);

//        String c = Base64Utils.decode("");
//        System.out.println(c);


        System.out.println(Pattern.matches("^(/app/resend/).*", "/app/resend/wms/order/123455"));
    }
}
