package cn.com.leyizhuang.app.foundation.pojo.activity;

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
public class ActBaseDO implements Serializable{

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
    private String baseType;

    // 结果类型
    private String promotionType;

    // 条件类型
    private String conditionType;

    // 是否叠加
    private Boolean isDouble;

    // 赠品选择最大数量
    private Integer giftChooseNumber;

    // 满足额度
    private Double fullAmount;

    // 满足数量
    private Integer fullNumber;

    /**
     * 获取最终促销类型
     */
    public String getActType(){
        return this.baseType+"_"+this.conditionType+"_"+this.promotionType;
    }
}
