package cn.com.leyizhuang.app.foundation.pojo.deliveryFeeRule;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 运费规则
 * Created by panjie on 2018/1/12.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryFeeRule {

    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 城市Id
     */
    private Long cityId;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 区县名称
     */
    private String countyName;
    /**
     * 条件
     */
    private Double condition;

    /**
     * 运费金额
     */
    private Double deliveryFee;

    /**
     * 收费对象
     */
    private String tollObject;

    /**
     * 特殊商品标志 （代表包含不收费的特殊商品，计算运费时排除此范围下商品）
     */
    private Boolean includeSpecialGoods;

    /**
     * 状态 true: 启用； false: 停用；
     */
    private Boolean status;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateUser;
    /**
     * 修改原因
     */
    private String updateReason;
}
