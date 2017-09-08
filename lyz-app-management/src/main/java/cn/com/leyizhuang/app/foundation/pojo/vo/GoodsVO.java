package cn.com.leyizhuang.app.foundation.pojo.vo;

import cn.com.leyizhuang.app.foundation.pojo.GoodsDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/6
 */
@Getter
@Setter
@ToString
public class GoodsVO implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(GoodsVO.class);

    private Long id;
    //商品名称
    private String goodsName;
    //商品编码
    private String goodsCode;
    //是否为小辅料
    private Boolean isGift;
    //商品标题
    private String title;
    //副标题
    private String subTitle;
    //封面图片
    private String coverImageUri;
    //轮播展示图片，多张图片以,隔开
    private String showPictures;
    //商品详情
    private String detail;
    //是否上架
    private Boolean isOnSale;
    //是否首页推荐
    private Boolean isRecommendIndex;
    //是否分类推荐
    private Boolean isRecommendType;
    //是否热销
    private Boolean isHot;
    //是否新品
    private Boolean isNew;
    //是否特价
    private Boolean isSpecialPrice;
    //商品类型
    private Long categoryId;
    //商品类型名称
    private String categoryTitle;
    //商品所有类型
    private String categoryIdTree;
    //市场价
    private Double marketPrice;
    //销售价
    private Double salePrice;
    //库存数量
    private Long leftNumber;
    //商品价格单位
    private String priceUnit;
    //上架时间
    private String onSaleTime;
    //创建日期
    private String createTime;
    //排序号
    private Long sortId;
    //品牌
    private String brandTitle;
    //品牌ID
    private Long brandId;
    //品牌Flag
    private String productFlag;
    //商品返现金额
    private Double returnPrice;
    //是否是调色包
    private Boolean isColorPackage;
    //是否调色产品
    private Boolean isColorful;
    //可调色的调色包SKU编号（多个调色包以","分割）
    private String colorPackageSku;
    //物料id（唯一标识）
    private Long inventoryItemId;
    //库存分类ID
    private Long invCategoryId;
    //物料类型名称
    private String itemTypeName;
    //物料类型CODE
    private String itemTypeCode;
    //物料状态 0 失效，1 有效
    private Boolean inventoryItemStatus;
    //产品条码
    private String itemBarcode;
    //单位名称
    private String unitName;

    public static final GoodsVO transform(GoodsDO goodsDO) {
        if (null != goodsDO) {
            GoodsVO goodsVO = new GoodsVO();
            goodsVO.setId(goodsDO.getId());
            goodsVO.setGoodsName(goodsDO.getGoodsName());
            goodsVO.setGoodsCode(goodsDO.getGoodsCode());
            goodsVO.setBrandId(goodsDO.getBrandId());
            goodsVO.setBrandTitle(goodsDO.getBrandTitle());
            goodsVO.setCategoryId(goodsDO.getCategoryId());
            goodsVO.setCategoryIdTree(goodsDO.getCategoryIdTree());
            goodsVO.setCategoryTitle(goodsDO.getCategoryTitle());
            goodsVO.setColorPackageSku(goodsDO.getColorPackageSku());
            goodsVO.setCoverImageUri(goodsDO.getCoverImageUri());
            if (null != goodsDO.getCreateTime()) {
                String createTime = goodsDO.getCreateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                goodsVO.setCreateTime(createTime);
            }
            goodsVO.setDetail(goodsDO.getDetail());
            goodsVO.setInvCategoryId(goodsDO.getInvCategoryId());
            goodsVO.setInventoryItemId(goodsDO.getInventoryItemId());
            goodsVO.setInventoryItemStatus(goodsDO.getInventoryItemStatus());
            goodsVO.setIsColorful(goodsDO.getIsColorful());
            goodsVO.setIsColorPackage(goodsDO.getIsColorPackage());
            goodsVO.setIsGift(goodsDO.getIsGift());
            goodsVO.setIsHot(goodsDO.getIsHot());
            goodsVO.setIsNew(goodsDO.getIsNew());
            goodsVO.setIsOnSale(goodsDO.getIsOnSale());
            goodsVO.setIsRecommendIndex(goodsDO.getIsRecommendIndex());
            goodsVO.setIsRecommendType(goodsDO.getIsRecommendType());
            goodsVO.setIsSpecialPrice(goodsDO.getIsSpecialPrice());
            goodsVO.setItemBarcode(goodsDO.getItemBarcode());
            goodsVO.setItemBarcode(goodsDO.getItemBarcode());
            goodsVO.setItemTypeCode(goodsDO.getItemTypeCode());
            goodsVO.setItemTypeName(goodsDO.getItemTypeName());
            goodsVO.setLeftNumber(goodsDO.getLeftNumber());
            goodsVO.setMarketPrice(goodsDO.getMarketPrice());
            if (null != goodsDO.getOnSaleTime()) {
                String onSaleTime = goodsDO.getOnSaleTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                goodsVO.setOnSaleTime(onSaleTime);
            }
            goodsVO.setPriceUnit(goodsDO.getPriceUnit());
            goodsVO.setProductFlag(goodsDO.getProductFlag());
            goodsVO.setReturnPrice(goodsDO.getReturnPrice());
            goodsVO.setSalePrice(goodsDO.getSalePrice());
            goodsVO.setShowPictures(goodsDO.getShowPictures());
            goodsVO.setSortId(goodsDO.getSortId());
            goodsVO.setSubTitle(goodsDO.getSubTitle());
            goodsVO.setTitle(goodsDO.getTitle());
            goodsVO.setUnitName(goodsDO.getUnitName());
            return goodsVO;
        } else {
            return null;
        }
    }

    public static final List<GoodsVO> transform(List<GoodsDO> goodsDOList) {
        List<GoodsVO> goodsVOList;
        if (null != goodsDOList && goodsDOList.size() > 0) {
            goodsVOList = new ArrayList<>(goodsDOList.size());
            goodsDOList.forEach(goodsDO -> goodsVOList.add(transform(goodsDO)));
        } else {
            goodsVOList = new ArrayList<>(0);
        }
        return goodsVOList;
    }

}
