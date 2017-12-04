package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.core.constant.StoreCreditMoneyChangeType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 门店信用金变更日志
 * @author GenerationRoad
 * @date 2017/11/27
 */
@Getter
@Setter
@ToString
public class StoreCreditMoneyLogResponse {
    private Long id;
    //生成时间
    private String createTime;
    //变更金额
    private Double changeMoney;
    //变更类型
    private String type;

    public void setType(StoreCreditMoneyChangeType type) {
        this.type = type.getValue();
    }
}
