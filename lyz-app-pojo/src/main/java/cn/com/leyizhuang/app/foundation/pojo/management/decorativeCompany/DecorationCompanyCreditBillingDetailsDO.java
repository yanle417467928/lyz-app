package cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany;

import lombok.*;

import java.io.Serializable;

/**
 * @author GenerationRoad
 * @date 2018/3/19
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DecorationCompanyCreditBillingDetailsDO implements Serializable{
    private Long id;
    //装饰公司信用金账单单号
    private String creditBillingNo;
    //封车时间、反配上架时间
    private String orderTime;
    //订单号、退单号
    private String orderNumber;
    //使用、返还信用金金额
    private Double creditMoney;
    //下单人姓名
    private String creatorName;
    //送货、收货地址
    private String deliveryAddress;
    //下单人所在装饰公司id
    private Long storeId;
    //创建者id
    private Long creatorId;
    //收货人
    private String receiver;
    //商品数量
    private Integer goodsQty;
    //备注
    private String remark;
}
