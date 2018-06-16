package cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 装饰公司用信用金支付的订单
 * @author GenerationRoad
 * @date 2018/3/15
 */
@Getter
@Setter
@ToString
public class DecorationCompanyCreditBillingDetailsVO {

    //封车时间、反配上架时间
    private String createTime = "";
    //订单号、退单号
    private String orderNumber = "";
    //使用、返还信用金金额
    private Double creditMoney = 0.00;
    //装饰公司名称
    private String storeName = "";
    //下单人姓名
    private String creatorName = "";
    //送货、收货地址
    private String deliveryAddress = "";
    //收货人
    private String receiver = "";
    //商品数量
    private Integer goodsQty = 0;
    //备注
    private String remark = "";
    //装饰公司经理名称
    private String salesManagerName = "";

    // 与装饰工结账总价
    private Double accountMoney = 0.00;

    //楼盘信息
    private String estateInfo = "";
}
