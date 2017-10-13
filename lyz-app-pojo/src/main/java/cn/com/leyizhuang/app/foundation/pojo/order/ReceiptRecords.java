package cn.com.leyizhuang.app.foundation.pojo.order;

import lombok.*;

import java.util.Date;

/**
 * 收款记录
 *
 * @author Richard
 * Created on 2017-10-10 12:00
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptRecords {

    private Long id;

    //收款类型：订单收款、充值
    private String receiptType;

    //收款单号
    private String receiptNumber;

    //相关单号：订单号、充值单号
    private String relevantNumber;

    //收款时间
    private Date receiptTime;

    //收款金额
    private Double receiptAmount;
}
