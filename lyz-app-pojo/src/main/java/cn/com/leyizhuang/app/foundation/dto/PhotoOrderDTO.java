package cn.com.leyizhuang.app.foundation.dto;

import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.PromotionSimpleInfo;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/31
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PhotoOrderDTO implements Serializable {
    //拍照下单ID
    private Long photoId;

    //门店id
    private Long storeId;

    //下单导购id
    private Long guideId;

    //下单导购身份
    private String peopleIdentityType;

    //收货人姓名
    private String receiverName;

    //收货人电话
    private String receiverPhone;

    //省
    private String province;

    //市
    private String city;

    //区/县
    private String county;

    //街道
    private String street;

    //小区名
    private String residenceName;

    //楼盘名
    private String estateInfo;

    //详细地址
    private String detailedAddress;

    //地址id
    private Long deliveryId;

    //商品列表
    private List<MaterialListDO> combList;

    //是否填写收货地址
    private Long goAddDeliveryAddressType;

    //备注
    private String remark;

    //联系人姓名
    private String contactName;

    //联系人电话
    private String contactPhone;

    //拍照下单图片
    private String photoImgs;

    //代下单人id
    private Long proxyId;

    //来源
    private String source;

    //促销信息
    private String giftDetails;

    //配送方式
    private String sysDeliveryType;

    //配送时间
    private String pointDistributionTime;

    //收款信息
    private String billingMsg;
}
