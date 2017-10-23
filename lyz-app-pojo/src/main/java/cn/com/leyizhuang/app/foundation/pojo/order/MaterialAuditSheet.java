package cn.com.leyizhuang.app.foundation.pojo.order;

import cn.com.leyizhuang.app.foundation.pojo.response.MaterialAuditDetailsResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialAuditSheetResponse;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 装饰公司物料审核
 * Created by caiyu on 2017/10/16.
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaterialAuditSheet {
    //物料审核单头id
    private Long auditHeaderID;
    //工人id
    private Long employeeID;
    //工人姓名
    private String employeeName;
    //配送方式
    private String deliveryType;
    //门店id
    private Long storeID;
    //收货人姓名
    private String receiver;
    //收货人电话号码
    private String receiverPhone;
    //收货市
    private String deliveryCity;
    //收货区
    private String deliveryCounty;
    //收货街道
    private String deliveryStreet;
    //收货小区
    private String residenceName;
    //收货详细地址
    private String detailedAddress;
    //是否主家收货
    private Boolean isOwnerReceiving;
    //备注
    private String remark;
    //状态 1.待审核2.已审核3.已取消
    private int status;
    //预约配送时间
    private LocalDateTime reservationDeliveryTime;
    //物料审核单创建时间
    private LocalDateTime createTime;
    //物料审核单号
    private String auditNo;
    //审核人id
    private Long auditorID;
    //审核是否通过
    private Boolean isAudited;

    /**
     * 转换为返回值类型
     * @return
     */
    public MaterialAuditDetailsResponse getMaterialAuditDetailsResponse(){
        MaterialAuditDetailsResponse materialAuditDetailsResponse = new MaterialAuditDetailsResponse();
        materialAuditDetailsResponse.setAuditNo(this.getAuditNo());
        materialAuditDetailsResponse.setDeliveryCity(this.getDeliveryCity());
        materialAuditDetailsResponse.setDeliveryCounty(this.getDeliveryCounty());
        materialAuditDetailsResponse.setDeliveryStreet(this.getDeliveryStreet());
        materialAuditDetailsResponse.setResidenceName(this.getResidenceName());
        materialAuditDetailsResponse.setDetailedAddress(this.getDetailedAddress());
        materialAuditDetailsResponse.setAuditHeaderID(this.getAuditHeaderID());
        materialAuditDetailsResponse.setIsOwnerReceiving(this.getIsOwnerReceiving());
        materialAuditDetailsResponse.setRemark(this.getRemark());
        return materialAuditDetailsResponse;
    }
}
