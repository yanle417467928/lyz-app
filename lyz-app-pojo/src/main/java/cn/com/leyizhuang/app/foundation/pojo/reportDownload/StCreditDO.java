package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 12421 on 2018/7/11.
 */
@Getter
@Setter
@ToString
public class StCreditDO {

    // 城市
    private String city = "";
    // 门店
    private String storeName = "";
    // 门店电话
    private String storeMobile = "";
    // 信用额度
    private Double maxCreditMoney = 0D;
    // 可用余额
    private Double avaliableCreditMoney = 0D;
    // 最后一次变更时间
    private String lastChangeTime = "";
}
