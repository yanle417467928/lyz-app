package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.common.core.constant.StoreSubventionChangeType;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 门店现金返利变更日志
 * @author GenerationRoad
 * @date 2017/11/27
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StoreSubventionLogDO {

    private Long id;
    //生成时间
    private LocalDateTime createTime;
    //变更金额
    private Double changeMoney;
    //使用订单号
    private String orderNumber;
    //变更类型
    private StoreSubventionChangeType type;
    //客户id
    private Long storeId;
    //操作人员
    private Long operatorId;
    //操作人员类型
    private AppIdentityType operatorType;
    //操作人员ip
    private String operatorIp;
    //变更后预存款
    private Double balance;
    //备注
    private String remarks;
}
