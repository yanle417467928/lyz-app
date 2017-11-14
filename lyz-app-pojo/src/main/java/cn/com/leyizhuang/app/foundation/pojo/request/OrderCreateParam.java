package cn.com.leyizhuang.app.foundation.pojo.request;

import lombok.*;

import java.io.Serializable;

/**
 * 创建订单接口入参
 *
 * @author Ricahrd
 * Created on 2017-10-25 9:17
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateParam implements Serializable{
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 身份类型
     */
    private Integer identityType;
    /**
     * 总金额
     */
    private Double totalPrice;
    /**
     * 应付金额
     */
    private Double amountPayable;
    /**
     * 会员折扣
     */
    private Double memberDiscount;
    /**
     * 订单折扣
     */
    private Double orderDiscount;
    /**
     * 运费
     */
    private Double freight;
    /**
     * 乐币折算
     */
    private Double lbRebate;
    /**
     * 现金券面额
     */
    private Double ccpDenomination;
    /**
     * 顾客预存款
     */
    private Double preDeposit;
    /**
     * 导购信用金
     */
    private Double creditMoney;
    /**
     * 门店信用金
     */
    private Double storeCreditMoney;
    /**
     * 门店预存款
     */
    private Double storePreDeposit;
    /**
     * 门店现金返利
     */
    private Double storeSubvention;

}
