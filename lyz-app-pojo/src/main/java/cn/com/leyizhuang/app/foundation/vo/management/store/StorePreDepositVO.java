package cn.com.leyizhuang.app.foundation.vo.management.store;

import cn.com.leyizhuang.app.core.constant.StoreType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2018/1/12
 */
@Getter
@Setter
@ToString
public class StorePreDepositVO {
    private  Long id;
    private Long storeId;
    //门店名称
    private String storeName;
    //门店编码
    private String storeCode;
    //市
    private String city;
    //当前余额
    private String balance = "0.00";
    /**
     * 门店类型
     */
    private String storeType;

    public void setBalance(String balance){
        if (null != balance){
            this.balance = balance;
        }
    }

    public void setStoreType(StoreType storeType){
        this.storeType = storeType.getDescription();
    }
}
