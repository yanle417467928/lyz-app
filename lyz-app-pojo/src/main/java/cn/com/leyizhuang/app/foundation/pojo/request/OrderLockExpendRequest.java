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

    //身份信息：

    private Long userId;            //用户id
    private Integer identityType;   //身份类型

    //所有款项消费相关：

    private Double customerDeposit;     //顾客预存款
    private Double guideCredit;         //导购信用金
    private Double storeDeposit;        //门店预存款
    private Double storeCredit;         //门店信用金
    private Double storeSubvention;     //门店现金返利

    //所有物品消费相关：

    private Integer lebiQty;                    //乐币
//    private List goodsList;
    private Integer storeInventory;             //门店库存
    private Integer cityInventory;              //城市库存
    private Integer productCoupon;  //产品券
    private Integer cashCoupon;        //现金券

}
