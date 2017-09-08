package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 商品业务实体
 *
 * @author Richard
 *         Created on 2017-07-12 17:26
 **/
@Getter
@Setter
@ToString
public class GoodsDO extends BaseDO {

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
    private LocalDateTime onSaleTime;
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

}
