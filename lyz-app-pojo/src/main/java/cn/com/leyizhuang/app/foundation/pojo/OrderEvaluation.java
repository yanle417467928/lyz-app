package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import java.util.Date;

/**
 * Created by caiyu on 2017/11/16.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvaluation {
    /**
     * 自增id
     */
    private Long id;
    /**
     * 产品星级
     */
    private int productStar;
    /**
     * 物流星级
     */
    private int logisticsStar;
    /**
     * 服务星级
     */
    private int serviceStars;
    /**
     * 订单编号
     */
    private String orderNumber;
    /**
     * 评价时间
     */
    private Date evaluationTime;
}
