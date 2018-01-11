package cn.com.leyizhuang.app.foundation.vo.management.customer;

import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2018/1/8
 */
@Getter
@Setter
@ToString
public class CustomerPreDepositVO {

    private  Long id;
    //顾客ID
    private Long cusId;
    //门店名称
    private String storeName;
    //真实姓名
    private String name;
    //手机号码
    private String mobile;
    //当前余额
    private String balance = "0.00";

    public void setBalance(String balance){
        if (null != balance){
            this.balance = balance;
        }
    }

}
