package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import lombok.*;

import java.util.Date;

/**
 * Created by caiyu on 2017/11/20.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDeliveryInfoDetails {

    private Long id;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 操作后物流状态
     */
    private LogisticStatus logisticStatus;
    /**
     * 操作（创建）时间
     */
    private Date createTime;
    /**
     * 操作描述
     */
    private String description;
    /**
     * 操作类型
     */
    private String operationType;
    /**
     * 操作人(配送员)编号
     */
    private String operatorNo;
    /**
     * 仓库编号
     */
    private String warehouseNo;
    /**
     * 物流操作单号
     */
    private String taskNo;
}
