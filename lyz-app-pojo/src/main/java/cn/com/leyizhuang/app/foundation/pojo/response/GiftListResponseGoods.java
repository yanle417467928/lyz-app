package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Richard
 * @date 2017/11/06
 */
@Getter
@Setter
@ToString
public class GiftListResponseGoods{

    //商品id
    private Long goodsId;
    //商品名称
    private String skuName;
    private String sku;
    //商品规格
    private String goodsSpecification;
    //封面图片路径
    private String coverImageUri;
    //商品单位
    private String goodsUnit;
    //商品数量
    private Integer qty;
    //商品单价
    private Double retailPrice;
    //商品会员价
    private Double vipPrice;
    //是否赠品
    private Boolean isGift;
    //公司类型
    private String companyFlag;
    /**
     * 商品类型
     */
    private AppGoodsLineType goodsType;

    public Integer getQty() {
        return qty;
    }

}
