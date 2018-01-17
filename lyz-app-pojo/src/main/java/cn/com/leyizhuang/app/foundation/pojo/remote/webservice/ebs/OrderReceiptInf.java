package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import lombok.*;

import java.util.Date;

/**
 * 订单收款信息
 *
 * @author Richard
 * Created on 2018-01-02 13:39
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderReceiptInf {

    private Long receiptId;

    /**
     * 分公司id
     */
    private Long sobId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 发送成功标识
     */
    private AppWhetherFlag sendFlag;
    /**
     * 接口错误信息
     */
    private String errorMsg;
    /**
     * 发送成功时间
     */
    private Date sendTime;

    /**
     * 订单（主单）号
     */
    private String mainOrderNumber;

    /**
     * 收款单号
     */
    private String receiptNumber;

    /**
     * 收款方式
     */
    private OrderBillingPaymentType receiptType;

    /**
     * 门店组织id
     */
    private Long storeOrgId;

    /**
     * 门店编码
     */
    private String diySiteCode;

    /**
     * 收款时间
     */
    private Date receiptDate;


    /**
     * 收款金额
     */
    private Double amount;

    /**
     * 收款方式说明
     */
    private String description;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 导购id
     */
    private Long guideId;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    private String attribute5;


}
