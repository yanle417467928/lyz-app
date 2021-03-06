package cn.com.leyizhuang.app.foundation.pojo.management.webservice.ebs;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import lombok.*;

import java.util.Date;

/**
 * 订单发货信息
 *
 * @author liuh
 * Created on 2018-01-20 13:39
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaOrderReceiveInf {

    private Long id;

    /**
     * 创建时间
     */
    private Date initDate;

    /**
     * 配送方式名称
     */
    private AppDeliveryType deliverTypeTitle;

    /**
     * 订单头id
     */
    private Long headerId;

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 自提单门店发货时间
     */
    private Date receiveDate;

    /**
     * 分公司id
     */
    private Long sobId;

    /**
     * 接口错误信息
     */
    private String errorMsg;

    /**
     * 发送成功标识
     */
    private AppWhetherFlag sendFlag;

    /**
     * 接口发送时间
     */
    private Date sendTime;


    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    private String attribute5;


}
