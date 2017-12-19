package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** 查询订单代收款金额
 * @author GenerationRoad
 * @date 2017/12/19
 */
@Getter
@Setter
@ToString
public class OrderArrearageInfoResponse {

    /**
     * 代收款金额
     */
    private Double arrearageMoney;

}
