package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.LeBiVariationType;
import lombok.*;

import java.util.Date;

/**
 * 乐币变动明细
 *
 * @author caiyu
 * @date 2017/11/8
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLeBiVariationLog {

    private Long id;
    /**
     * 顾客id
     */
    private Long cusId;
    /**
     * 变动时间
     */
    private Date variationTime;
    /**
     * 变动类型
     */
    private LeBiVariationType leBiVariationType;
    /**
     * 变动类型说明
     */
    private String variationTypeDesc;
    /**
     * 变动数量
     */
    private int variationQuantity;
    /**
     * 变动后乐币数量
     */
    private int afterVariationQuantity;
    /**
     * 乐币使用订单号
     */
    private String orderNum;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 操作人员
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
}