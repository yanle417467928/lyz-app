package cn.com.leyizhuang.app.foundation.vo.management.customer;

import cn.com.leyizhuang.app.core.constant.LeBiVariationType;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 乐币变动明细类
 * @author GenerationRoad
 * @date 2018/1/13
 */
@Getter
@Setter
@ToString
public class CusLebiLogVO {
    private Long id;
    //变动时间
    private String variationTime;
    //变动类型
    private String leBiVariationType;
    //变动类型说明
    private String variationTypeDesc;
    //变动数量
    private Integer variationQuantity;
    //变动后乐币数量
    private Integer afterVariationQuantity;
    //乐币使用订单号
    private String orderNum;
    //门店名称
    private String storeName;
    //真实姓名
    private String name;
    //手机号码
    private String mobile;
    //操作人id
    private String operatorId;
    //操作人员类型
    private String operatorType;
    //操作人员ip
    private String operatorIp;
    //备注
    private String remarks;

    public void setLeBiVariationType(LeBiVariationType leBiVariationType){
        this.leBiVariationType = leBiVariationType.getDescription();
    }
    public void setVariationTime(LocalDateTime variationTime){
        this.variationTime = TimeTransformUtils.df.format(variationTime);
    }


}
