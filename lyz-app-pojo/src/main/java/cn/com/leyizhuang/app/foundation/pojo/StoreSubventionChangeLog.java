package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.StoreSubventionChangeType;
import lombok.*;

import java.util.Date;

/**
 * 门店现金返利变更日志
 *
 * @author GenerationRoad
 * @date 2017/11/27
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StoreSubventionChangeLog {

    private Long id;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 变更金额
     */
    private Double changeAmount;

    /**
     * 现金返利变更后金额
     */
    private Double balance;
    /**
     * 相关单号
     */
    private String referenceNumber;
    /**
     * 变更类型
     */
    private StoreSubventionChangeType changeType;
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
