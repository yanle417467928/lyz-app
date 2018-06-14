package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;
import cn.com.leyizhuang.common.core.constant.PhotoOrderType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * @author GenerationRoad
 * @date 2018/6/12
 */
@Getter
@Setter
@ToString
public class PhotoOrderCheckDO {
    //城市
    private String cityName;
    //单号
    private String photoOrderNo;
    //下单人姓名
    private String userName;
    //下单人电话
    private String mobile;
    //下单人身份类型
    private String identityType;
    //状态（待处理、已下单、已支付、完成）
    private String status;
    //订单类型
    private String orderType;
    //门店
    private String storeName;
    //备注
    private String remark;
    //创建时间
    private String createTime;
    //处理时间
    private String updateTime;
    //处理人
    private String updateUser;
    //完结时间
    private String finishTime;
    //完结人
    private String operationUser;
    //收货人姓名
    private String receiver;
    //收货人号码
    private String receiverPhone;
    //收货详细地址
    private String detailedAddress;


    public void setIdentityType(AppIdentityType identityType){
        this.identityType = identityType.getDescription();
    }

    public void setStatus(PhotoOrderStatus status){
        this.status = status.getValue();
    }
    public void setOrderType(PhotoOrderType orderType){
        this.orderType = orderType.getValue();
    }

}
