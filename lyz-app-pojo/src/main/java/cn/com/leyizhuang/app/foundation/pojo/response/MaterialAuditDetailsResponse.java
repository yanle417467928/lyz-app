package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditGoodsInfo;
import lombok.*;

import java.util.List;

/**
 * 物料审核单详情返回值
 * Created by caiyu on 2017/10/20.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaterialAuditDetailsResponse {
    //物料单编号
    private String auditNo;
    //物料单商品list
    private List<MaterialAuditGoodsInfo> goodsList;
    //物料单提交时间
    private String createTime;
    //物料单总额（零售）
    private Double totalPrice;
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
    //备注
    private String remark;
    //是否主家收货
    private Boolean isOwnerReceiving;
    //预约配送时间
    private String reservationDeliveryTime;
    //物料审核单头id
    private Long auditHeaderID;

}
