package cn.com.leyizhuang.app.foundation.pojo.returnorder;

import cn.com.leyizhuang.app.core.constant.ReturnLogisticStatus;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.WtaReturningOrderHeader;
import lombok.*;

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
    private Long pickersId;
    /**
     * 收货人编码
     */
    private String pickersNumber;
    /**
     * 物流操作编码
     */
    private String taskNo;
    /**
     * 图片
     */
    private String picture;

    public static ReturnOrderDeliveryDetail transform(WtaReturningOrderHeader header) {
        ReturnOrderDeliveryDetail returnOrderDeliveryDetail = new ReturnOrderDeliveryDetail();


        return returnOrderDeliveryDetail;
    }
}
