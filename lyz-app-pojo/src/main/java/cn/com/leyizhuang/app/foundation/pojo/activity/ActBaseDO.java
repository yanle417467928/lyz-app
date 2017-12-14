package cn.com.leyizhuang.app.foundation.pojo.activity;

import cn.com.leyizhuang.app.core.constant.ActBaseType;
import cn.com.leyizhuang.app.core.constant.ActConditionType;
import cn.com.leyizhuang.app.core.constant.ActPromotionType;
import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 促销基础实体
 * Created by panjie on 2017/11/22.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActBaseDO{

    private static final long serialVersionUID;

    static {
        serialVersionUID = 1L;
    }

    private Long id;

    // 促销代号
    private String actCode;

    // 创建时间
    private LocalDateTime createTime;

    // 标题
    private String title;

    // 活动开始时间
    private LocalDateTime beginTime;

    // 活动结束时间
    private LocalDateTime endTime;

    // 城市id
    private Long cityId;

    // 城市名称
    private String cityName;

    // 活动目标对象
    private String actTarget;

    // 促销基本类型
    private ActBaseType baseType;

    // 结果类型
    private ActPromotionType promotionType;

    // 条件类型
    private ActConditionType conditionType;

    // 是否叠加
    private Boolean isDouble;

    // 赠品选择最大数量
    private Integer giftChooseNumber;

    // 满足额度
    private Double fullAmount;

    // 满足数量
    private Integer fullNumber;

    // 是否可退货
    private Boolean isReturnable;

    // 排序号
    private Integer sortId;

    /**
     * 获取最终促销类型
     */
    public String getActType(){
        return this.baseType.getValue()+"_"+this.conditionType.getValue()+"_"+this.promotionType.getValue();
    }
}
