package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

import cn.com.leyizhuang.app.core.constant.StoreType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2018/4/3
 */
@Getter
@Setter
@ToString
public class AgencyFundDO {
    //城市
    private String cityName;
    //仓库名称
    private String warehouse;
    //门店名称
    private String storeName;
    //门店类型
    private String storeType;
    //订单号
    private String orderNumber;
    //下单时间
    private String orderTime;
    //收货地址
    private String shippingAddress;
    //导购姓名
    private String sellerName;
    //配送员姓名
    private String deliveryName;
    //订单代收金额
    private Double agencyMoney;
    //实收现金金额
    private Double cashMoney;
    //实收pos金额
    private Double posMoney;
    //配送员备注
    private String remarks;
    //应退门店金额
    private Double returnMoney;
    //仓库应存回公司货款
    private Double realMoney;
    //订单备注
    private String orderRemark;

    public void setStoreType(StoreType storeType){
        this.storeType = storeType.getDescription();
    }

}
