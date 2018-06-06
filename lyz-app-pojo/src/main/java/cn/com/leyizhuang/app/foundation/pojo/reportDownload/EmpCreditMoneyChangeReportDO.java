package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

import cn.com.leyizhuang.app.core.constant.EmpCreditMoneyChangeType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jerry.Ren
 * create 2018-06-05 11:51
 * desc:导购信用额度变更报表实体
 **/

@Getter
@Setter
@ToString
public class EmpCreditMoneyChangeReportDO {

    private Long id;
    //城市
    private String cityName;
    //门店名称
    private String storeName;
    //门店类型
    private String storeType;
    //导购姓名
    private String employeeName;
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
    //订单号
    private String orderNumber;
    //退单号
    private String returnNumber;

    public void setChangeType(EmpCreditMoneyChangeType changeType) {
        this.changeType = changeType.getDescription();
    }

    public void setStoreType(StoreType storeType) {
        this.storeType = storeType.getDescription();
    }

}
