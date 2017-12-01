package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.LeBiVariationType;
import lombok.*;

import java.util.Date;

/**
 * 乐币变动明细
 * Created by caiyu on 2017/11/8.
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
    private Long cusID;
    /**
     * 变动时间
     */
    private Date variationTime;
    /**
     * 变动类型
     */
    private LeBiVariationType leBiVariationType;
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
}