package cn.com.leyizhuang.app.foundation.pojo.request;

import lombok.*;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/10/24.
 * Time: 17:32.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderLockExpendRequest implements Serializable {

    //所有款项消费相关：

    private Double userDeposit;  //个人预存款
    private Double userCredit;      //个人信用金
    private Double storeDeposit;//
    private Double storeCredit;
    private Double storeSubvention;

    //所有物品消费相关：
    private Integer lebiQty;
//    private
}
