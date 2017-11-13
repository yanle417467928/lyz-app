package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.core.constant.PreDepositChangeType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2017/11/7
 */
@Getter
@Setter
@ToString
public class PreDepositLogResponse {
    private Long id;
    //生成时间
    private String createTime;
    //变更金额
    private Double changeMoney;
    //使用订单号
    private String orderNumber;
    //变更类型
    private String type;

    public void setType(PreDepositChangeType type){
        this.type = type.getValue();
    }


}