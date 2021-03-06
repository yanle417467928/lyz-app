package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

import cn.com.leyizhuang.app.core.constant.AppCustomerType;
import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuh
 * @date 2018/4/3
 */
@Getter
@Setter
@ToString
public class SalesReportDO {
    //城市
    private String cityName = "";
    //storeId
    private Long storeId = 0L;
    //门店编码
    private String storeCode = "";
    //门店名称
    private String storeName = "";
    //顾客名称
    private String name = "";
    //会员名称
    private String customerName = "";
    //订单号
    private String ordNo = "";
    //订单类型
    private String orderType = "";
    //订单状态
    private String orderStatus = "";
    //自提提货日期
    private String selfTakeOrderTime = "";
    //订单日期
    private String createTime = "";
    //出货日期
   private String shippingDate = "";
   //是否结清
    private String isPayUp = "";
    //订单还清日期
    private String payUpTime = "";
    //编号
    private String sku = "";
    //商品名称
    private String skuName = "";
   //产品类型
    private String goodsType = "";
    //品牌
    private String companyFlag = "";
    //财务销量
    private String  financialSales = "0";
    //经销财务销量
    private String distributionSales = "0";
    //经销单价
    private Double wholesalePrice = 0.00;
    //原单价
    private Double retailPrice = 0.00;
    //结算单价
    private Double settlementPrice = 0.00;
    //会员折扣
    private Double memberDiscount = 0.00;
    //折扣或者赠品分摊
    private Double promotionSharePrice = 0.00;
    //现金券
    private Double cashCouponSharePrice = 0.00;
    //下单数量
    private Double orderQty = 0.00;
    //产品券类型
    private String couponType = "";
    //门店类型
    private String storeType = "";
    //退单号
    private String returnNo = "";
    //分销门店编码
    private String fxStoreCode = "";
    // 顾客id
    private Long cusId = 0L;
    //顾客类型
    private String cusType;
    // 分析仓库名称
    private String fxCkName;
    // 是否为工程单
    private String isGcOrder;
}
