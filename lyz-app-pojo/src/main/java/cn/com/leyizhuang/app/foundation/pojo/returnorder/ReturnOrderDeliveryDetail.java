package cn.com.leyizhuang.app.foundation.pojo.returnorder;

import cn.com.leyizhuang.app.core.constant.ReturnLogisticStatus;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.WtaReturningOrderHeader;
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
     * 创建时间
     */
    private Date createTime;
    /**
     * 退单id
     */
    private Long roid;
    /**
     * 退单号
     */
    private String returnNo;
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
     * 仓库编号
     */
    private String warehouseNo;
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
        returnOrderDeliveryDetail.setCreateTime(header.getCreateTime());
        returnOrderDeliveryDetail.setWarehouseNo(header.getWhNo());
        returnOrderDeliveryDetail.setTaskNo(header.getRecNo());
        returnOrderDeliveryDetail.setReturnNo(header.getPoNo());
        returnOrderDeliveryDetail.setReturnLogisticStatus(ReturnLogisticStatus.AGAIN_ON_SALE);
        returnOrderDeliveryDetail.setDescription("退单商品已返配上架");
        return returnOrderDeliveryDetail;
    }
}
