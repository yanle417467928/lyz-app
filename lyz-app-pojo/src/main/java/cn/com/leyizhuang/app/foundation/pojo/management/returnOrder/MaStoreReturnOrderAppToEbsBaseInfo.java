package cn.com.leyizhuang.app.foundation.pojo.management.returnOrder;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.core.constant.ReturnOrderType;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 门店退货接口基本信息类
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaStoreReturnOrderAppToEbsBaseInfo {

    private Long id;

    /**
     * 分公司id
     */
    private Long sobId;

    /**
     * 原订单号
     */
    private String mainOrderNumber;

    /**
     * 退货单号
     */
    private String returnNumber;

    /**
     * 到店退货日期
     */
    private Date returnDate;

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
