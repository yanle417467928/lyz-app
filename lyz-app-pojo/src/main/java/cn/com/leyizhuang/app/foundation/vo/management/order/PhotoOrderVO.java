package cn.com.leyizhuang.app.foundation.vo.management.order;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/15
 */
@Getter
@Setter
@ToString
public class PhotoOrderVO {
    private Long id;
    //下单人门店
    private String storeName;
    //下单人id
    private Long userId;
    //下单人真实姓名
    private String username;
    //下单人手机号码
    private String userMobile;
    //下单人身份类型
    private String identityType;
    //下单照片，多张照片用逗号分隔
    private List<String> photos;
    //备注
    private String remark;
    //状态（待处理、已下单、已支付、完成）
    private String status;
    private String statusEnum;
    //创建时间
    private String createTime;
    //拍照下单单号
    private String photoOrderNo;
    //联系人姓名
    private String contactName;
    //联系人电话
    private String contactPhone;
    //下单人身份类型
    private AppIdentityType identityTypeValue;
    //代下单人id
    private Long proxyId;

    public void setStatus(PhotoOrderStatus status){
        this.status = status.getValue();
    }

    public void setIdentityType(AppIdentityType identityType){
        this.identityType = identityType.getDescription();
        this.identityTypeValue = identityType;
    }

    public void setCreateTime(LocalDateTime createTime){
        this.createTime = TimeTransformUtils.df.format(createTime);
    }

    public void setPhotos(String photos){
        this.photos = new ArrayList<>();
        String uri[] = photos.split(",");
        for (int i = 0; i < uri.length; i++) {
            this.photos.add(uri[i]);
        }
    }
}
