package cn.com.leyizhuang.app.foundation.pojo.management.returnOrder;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.foundation.pojo.management.Customer;
import lombok.*;

import java.util.Date;

/**
 * Created by liuh on 2018/1/31.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaReturnOrderInfo {

    private Long roid;
    /**
     * 退单时间
     */
    private Date returnTime;
    /**
     * 退单号
     */
    private String returnNo;
    /**
     * 退单类型（CANCEL_RETURN(1, "取消退货"), REFUSED_RETURN(2, "拒签退货"), NORMAL_RETURN(3, "正常退货")）
     */
    private ReturnOrderType returnType;

    /**
     * 创建人姓名
     */
    private String creatorName;
    /**
     * 下单人门店(装饰公司)
     */
    private String storeName;
    /**
     * 退货方式
     */
    private AppOrderType orderType;
    /**
     * 退单状态
     */
    private AppReturnOrderStatus returnStatus;

    /**
     * 顾客姓名
     */
    private String memberName;


    /**
     * 顾客电话
     */
    private  String memberPhone;






}
