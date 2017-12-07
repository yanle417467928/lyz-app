package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.CustomerCashCouponChangeType;
import cn.com.leyizhuang.app.core.constant.StoreCreditMoneyChangeType;
import lombok.*;

import java.util.Date;

/**
 * 顾客优惠券变更日志
 *
 * @author Richard
 * @date 2017/12/01
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCashCouponChangeLog {

    private Long id;
    /**
     * 门店id
     */
    private Long cusId;
    /**
     * 创建时间
     */
    private Date useTime;
    /**
     * 券id
     */
    private Long couponId;
    /**
     * 相关单号
     */
    private String referenceNumber;
    /**
     * 变更类型
     */
    private CustomerCashCouponChangeType changeType;
    /**
     * 变更类型描述
     */
    private String changeTypeDesc;
    /**
     * 操作人员id
     */
    private Long operatorId;
    /**
     * 操作人员类型
     */
    private AppIdentityType operatorType;
    /**
     * 操作人员ip
     */
    private String operatorIp;
    /**
     * 备注
     */
    private String remark;
}
