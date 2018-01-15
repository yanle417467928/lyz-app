package cn.com.leyizhuang.app.foundation.dto;

import cn.com.leyizhuang.app.core.constant.LeBiVariationType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

/**
 * @author GenerationRoad
 * @date 2018/1/15
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CusLebiDTO {

    /**
     * 客户id
     */
    private Long cusId;
    /**
     * 变更类型
     */
    private LeBiVariationType changeType;
    /**
     * 变更数量
     */
    private Integer changeNum;
    /**
     * 备注
     */
    @Length(min = 0, max = 50, message = "'备注'的长度必须在0~50字之间")
    private String remarks;


}
