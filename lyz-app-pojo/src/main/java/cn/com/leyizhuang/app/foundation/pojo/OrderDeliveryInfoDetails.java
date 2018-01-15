package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.WtaShippingOrderHeader;
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
    /**
     * 图片
     */
    private String picture;

    /**
     * 是否已读
     */
    private Boolean isRead;

    public void setDeliveryInfo(String orderNo,LogisticStatus logisticStatus,String description,
                                String operationType,String operatorNo,String picture,String warehouseNo,String taskNo){
        this.orderNo = orderNo;
        this.logisticStatus = logisticStatus;
        this.description = description;
        this.operationType = operationType;
        this.operatorNo = operatorNo;
        this.picture = picture;
        this.createTime = new Date();
        this.warehouseNo = warehouseNo;
        this.taskNo = taskNo;
    }

    public static OrderDeliveryInfoDetails transform(WtaShippingOrderHeader header) {
        OrderDeliveryInfoDetails deliveryInfoDetails = new OrderDeliveryInfoDetails();

        deliveryInfoDetails.setOrderNo(header.getOrderNo());
        deliveryInfoDetails.setIsRead(false);
        deliveryInfoDetails.setCreateTime(header.getCreateTime());
        deliveryInfoDetails.setLogisticStatus(LogisticStatus.SEALED_CAR);
        String description = "您的订单在乐易装在【" +
                header.getWhNo() +
                "】仓库" +
                "封车完成，开始配送";
        deliveryInfoDetails.setDescription(description);
        deliveryInfoDetails.setOperationType("已封车");
        deliveryInfoDetails.setOperatorNo(header.getDriver());
        deliveryInfoDetails.setWarehouseNo(header.getWhNo());
        deliveryInfoDetails.setTaskNo(header.getTaskNo());
        return deliveryInfoDetails;
    }
}
