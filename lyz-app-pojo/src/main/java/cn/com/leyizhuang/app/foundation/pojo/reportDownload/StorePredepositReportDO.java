package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

import cn.com.leyizhuang.app.core.constant.StorePreDepositChangeType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2018/3/22
 */
@Getter
@Setter
@ToString
public class StorePredepositReportDO {
    //城市
    private String cityName;
    //门店名称
    private String storeName;
    //门店类型
    private String storeType;
    //变更时间
    private String changeTime;
    //变更类型
    private String changeType;
    //变更金额
    private Double changeMoney;
    //变更后金额
    private Double balance;
    //关联单号
    private String referenceNumber;
    //退单号
    private String returnNo;
    //备注
    private String remarks;


    public void setChangeType(StorePreDepositChangeType changeType) {
        this.changeType = changeType.getDescription();
    }

    public void setStoreType(StoreType storeType) {
        this.storeType = storeType.getDescription();
    }

}
