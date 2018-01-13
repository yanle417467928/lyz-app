package cn.com.leyizhuang.app.foundation.dto;

import cn.com.leyizhuang.app.core.constant.CustomerPreDepositChangeType;
import cn.com.leyizhuang.app.core.constant.StorePreDepositChangeType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

/**
 * @author GenerationRoad
 * @date 2018/1/12
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StorePreDepositDTO {

    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 变更类型
     */
    private StorePreDepositChangeType changeType;
    /**
     * 变更金额
     */
    private Double changeMoney;
    /**
     * 到账时间
     */
    private String transferTime;
    /**
     * 商户订单号
     */
    @Length(min = 0, max = 6, message = "请输入6位的'商户订单号'")
    private String merchantOrderNumber;
    /**
     * 备注
     */
    @Length(min = 0, max = 50, message = "'备注'的长度必须在0~50字之间")
    private String remarks;
}
