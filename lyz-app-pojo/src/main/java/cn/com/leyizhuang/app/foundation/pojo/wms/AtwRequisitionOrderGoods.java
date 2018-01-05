package cn.com.leyizhuang.app.foundation.pojo.wms;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * Notes:APP~WMS订单产品信息下传接口表
 * Created with IntelliJ IDEA.
 * Date: 2017/12/21.
 * Time: 10:46.
 */
@Getter
@Setter
@ToString
public class AtwRequisitionOrderGoods {

    private Long id;

    /**
     * 创建时间（本条数据创建时间）
     */
    private Date createTime;
    /**
     * 主单号
     */
    private String orderNumber;
    /**
     * 商品编码
     */
    private String goodsCode;
    /**
     * 商品名称
     */
    private String goodsTitle;
    /**
     * 商品价格
     */
    private Double price;
    /**
     * 商品数量
     */
    private Integer quantity;
    /**
     * 接口传输标识
     */
    private Boolean sendFlag;
    /**
     * 接口错误信息
     */
    private String errorMessage;
    /**
     * wms收到信息时间
     */
    private Date sendTime;

    public static AtwRequisitionOrderGoods transform(OrderGoodsInfo goodsInfo) {
        AtwRequisitionOrderGoods requisitionOrderGoods = new AtwRequisitionOrderGoods();
        requisitionOrderGoods.setCreateTime(new Date());
        requisitionOrderGoods.setOrderNumber(goodsInfo.getOrderNumber());
        requisitionOrderGoods.setGoodsCode(goodsInfo.getSku());
        requisitionOrderGoods.setGoodsTitle(goodsInfo.getSkuName());
        requisitionOrderGoods.setPrice(goodsInfo.getRetailPrice());
        requisitionOrderGoods.setQuantity(goodsInfo.getOrderQuantity());
        return requisitionOrderGoods;
    }
}
