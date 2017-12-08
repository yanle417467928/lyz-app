package cn.com.leyizhuang.app.foundation.pojo.returnorder;

import cn.com.leyizhuang.app.core.constant.ReturnLogisticStatus;
import lombok.*;

import java.util.Date;

/**
 * 退货地址详情
 * Created by caiyu on 2017/12/2.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReturnOrderDeliveryDetail {
    private Long id;
    /**
     * 退单id
     */
    private Long roid;
    /**
     * 退单物流状态
     */
    private ReturnLogisticStatus returnLogisticStatus;
    /**
     * 描述
     */
    private String description;
    /**
     * 上门收货人id
     */
    private Long consigneeId;
    /**
     * 收货人编码
     */
    private String consigneeNumber;
    /**
     * 门店编码
     */
    private String storeNo;
    /**
     * 仓库编码
     */
    private String warehouseNo;
    /**
     * 收货地址
     */
    private String pickUpAddress;
    /**
     * 收货时间
     */
    private Date pickUpTime;
    /**
     * 物流操作编码
     */
    private String taskNo;
    /**
     * 图片
     */
    private String picture;
}
