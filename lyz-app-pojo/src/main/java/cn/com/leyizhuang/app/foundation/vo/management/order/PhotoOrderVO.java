package cn.com.leyizhuang.app.foundation.vo.management.order;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author GenerationRoad
 * @date 2018/1/15
 */
@Getter
@Setter
@ToString
public class PhotoOrderVO {
    private Long id;
    //下单人ID
    private Long userId;
    //下单人真实姓名
    private String username;
    //下单人手机号码
    private String userMobile;
    //下单人身份类型
    private String identityType;
    //下单照片，多张照片用逗号分隔
    private String photos;
    //收货人地址
    private Long deliveryId;
    //是否主家收货
    private Boolean isOwnerReceiving;
    //备注
    private String remark;
    //导购代下单时填写的客户信息
    private Long customerId;
    //客户真实姓名
    private String customerName;
    //客户手机号码
    private String customerMobile;
    //状态（待处理、已下单、已支付、完成）
    private String status;
    //创建时间
    private String createTime;
    //关联单号
    private Long ordId;

    public void setStatus(PhotoOrderStatus status){
        this.status = status.getValue();
    }

    public void setIdentityType(AppIdentityType identityType){
        this.identityType = identityType.getDescription();
    }

    public void setCreateTime(LocalDateTime createTime){
        this.createTime = TimeTransformUtils.df.format(createTime);
    }
}
