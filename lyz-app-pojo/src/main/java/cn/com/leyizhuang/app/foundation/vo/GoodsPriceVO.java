package cn.com.leyizhuang.app.foundation.vo;

import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/30
 */
@Getter
@Setter
@ToString
public class GoodsPriceVO {

    private Long id;

    //门店id
    private Long storeId;

    //商品id
    private Long gid;

    //商品编码
    private String sku;

    //价目表行id，来自hq系统
    private Long priceLineId;

    //会员价
    private Double vipPrice;

    //零售价
    private Double retailPrice;

    //经销价
    private Double wholesalePrice;

    // 生效起始时间
    private String startTime;

    // 生效结束时间
    private String endTime;

    private String skuName;

    private String goodsSpecification;


    public static final List<GoodsPriceVO> transform(List<GoodsPrice> goodsPriceList) {
        List<GoodsPriceVO> goodsPriceVOList;
        if (null != goodsPriceList && goodsPriceList.size() > 0) {
            goodsPriceVOList = new ArrayList<>(goodsPriceList.size());
            goodsPriceList.forEach(goodsPrice -> goodsPriceVOList.add(transform(goodsPrice)));
        } else {
            goodsPriceVOList = new ArrayList<>(0);
        }
        return goodsPriceVOList;
    }

    public static final GoodsPriceVO transform(GoodsPrice goodsPrice) {
        if (null != goodsPrice) {
            GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
            goodsPriceVO.setId(goodsPrice.getGpid());
            goodsPriceVO.setGid(goodsPrice.getGid());
            goodsPriceVO.setStoreId(goodsPrice.getStoreId());
            goodsPriceVO.setPriceLineId(goodsPrice.getPriceLineId());
            goodsPriceVO.setSku(goodsPrice.getSku());
//            goodsPriceVO.setSkuName(goodsPrice.getGoodsDO().getSkuName());
//            goodsPriceVO.setGoodsSpecification(goodsPrice.getGoodsDO().getGoodsSpecification());
            goodsPriceVO.setRetailPrice(goodsPrice.getRetailPrice());
            goodsPriceVO.setVipPrice(goodsPrice.getVIPPrice());
            goodsPriceVO.setWholesalePrice(goodsPrice.getWholesalePrice());
//            goodsPriceVO.setStartTime(goodsPrice.getStartTime());
//            goodsPriceVO.setEndTime(goodsPrice.getEndTime());
            return goodsPriceVO;
        } else {
            return null;
        }
    }
}
