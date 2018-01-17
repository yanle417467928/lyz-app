package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 拍照下单类
 * @author GenerationRoad
 * @date 2017/10/24
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PhotoOrderDO {
    private Long id;
    //下单人ID
    private Long userId;
    //下单人身份类型
    private AppIdentityType identityType;
    //下单照片，多张照片用逗号分隔
    private String photos;
    //收货人地址
//    private Long deliveryId;
    //是否主家收货
//    private Boolean isOwnerReceiving;
    //联系人电话
    private String contactPhone;
    //备注
    private String remark;
    //导购代下单时填写的客户信息
//    private Long customerId;
    //状态（待处理、已下单、已支付、完成）
    private PhotoOrderStatus status;
    //创建时间
    private LocalDateTime createTime;
    //拍照下单单号
    private String photoOrderNo;
}
