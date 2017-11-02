package cn.com.leyizhuang.app.foundation.pojo.request;

import cn.com.leyizhuang.app.foundation.pojo.order.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsSimpleRequest;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 物料审核单参数
 * Created by caiyu on 2017/10/17.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaterialAuditSheetRequest {
    //用户id
    private Long userID;
    //用户类型
    private String identityType;
    //商品id+商品数量+是否是赠品
    private String goodsList;
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
    //预约配送时间
    private String reservationDeliveryTime;
    //是否主家收货
    private Boolean isOwnerReceiving;
    //备注
    private String remark;
}
