package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.io.Serializable;

/**
 * 获取顾客签到概况接口返回对象
 *
 * @author Richard
 * Created on 2017-12-14 17:32
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSignOverviewResponse implements Serializable{


    private static final long serialVersionUID = -6631708612837160074L;

    /**
     * 是否可以签到
     */
    private Boolean canSign;

    /**
     * 连续签到天数
     */
    private Integer consecutiveSignDays;

    /**
     * 当月签到天数
     */
    private Integer monthlySignDays;

    /**
     * 全部签到天数
     */
    private Integer totalSignDays;

    /**
     * 累计奖励乐币数量
     */
    private Integer totalAwardsQty;
}
