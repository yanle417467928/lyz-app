package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

import cn.com.leyizhuang.app.core.constant.EmpCreditMoneyChangeType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import lombok.*;

/**
 * desc:导购信用额度变更报表实体
 **/

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCreditMoneyChangeReportDO {

    private Long id;
    //城市
    private String cityName;
    //门店名称
    private String storeName;
    //门店类型
    private String storeType;
    //变更时间
    private String changeTime;
    //可用额度改变总量
    private Double changeMoney;
    //可用额度改变后余量
    private Double balance;
    //变更类型
    private String changeType;
    //变更类型描述
    private String changeTypeDesc;
    //相关单号
    private String referenceNumber;

    public void setStoreType(StoreType storeType) {
        this.storeType = storeType.getDescription();
    }

}
