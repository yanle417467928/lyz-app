package cn.com.leyizhuang.app.foundation.pojo.goods;


import cn.com.leyizhuang.app.foundation.vo.management.goods.MaGoodsVO;
import lombok.*;

import java.util.Date;

/**
 * 商品业务实体
 *
 * @author Richard
 * Created on 2017-07-12 17:26
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDO {

    private Long gid;

    //商品名称
    private String skuName;

    //商品编码
    private String sku;

    //封面图片
    private String coverImageUri;

    //轮播展示图片，多张图片以,隔开
    private String rotationImageUri;

    //商品详情图片，多张以，隔开
    private String detailsImageUri;

    //商品规格
    private String goodsSpecification;

    //单位单位
    private String goodsUnit;

    //产品分类id
    private Long cid;

    //产品分类名称
    private String categoryName;

    //品牌id
    private Long brdId;

    //品牌名称
    private String brandName;

    //类型id
    private Long gtid;

    //类型名称
    private String typeName;

    //是否首页推荐
    private Boolean isIndexRecommend;

    //是否热销
    private Boolean isHot;

    //是否新品
    private Boolean isNew;

    //排序号
    private Long sortId;

    //是否调色产品
    private Boolean isColorMixing;

    //创建时间
    private Date createTime;

    //商品公司标识
    private String companyFlag;

    //产品档次
    private String productGrade;

    //搜索关键字
    private String searchKeyword;

    //商品详情
    private String goodsDetial;

    //物料状态
    private Boolean materialsEnable;

    //物理分类
    private Long physicalClassify;

    //物料条码
    private String materialsCode;

    //物料名称
    private String materialsName;

    // 施工面积
    private String workArea;

    // 施工成不
    private String workCost;

    public static final GoodsDO transform(MaGoodsVO goodsVO) {
        if (null != goodsVO) {
            GoodsDO goodsDO = new GoodsDO();
            goodsDO.setGid(goodsVO.getId());
            goodsDO.setSkuName(goodsVO.getSkuName());
            goodsDO.setSku(goodsVO.getSku());
            goodsDO.setBrdId(goodsVO.getBrdId());
            goodsDO.setBrandName(goodsVO.getBrdName());
            goodsDO.setGoodsUnit(goodsVO.getGoodsUnit());
            goodsDO.setCategoryName(goodsVO.getCategoryName());
            goodsDO.setCoverImageUri(goodsVO.getCoverImageUri());
            goodsDO.setGoodsSpecification(goodsVO.getGoodsSpecification());
            goodsDO.setCreateTime(goodsVO.getCreateTime());
            goodsDO.setIsHot(goodsVO.getIsHot());
            goodsDO.setRotationImageUri(goodsVO.getRotationImageUri());
            goodsDO.setSortId(goodsVO.getSortId());
            goodsDO.setCompanyFlag(goodsVO.getCompanyFlag());
            goodsDO.setGoodsDetial(goodsVO.getGoodsDetail());
            goodsDO.setIsColorMixing(goodsVO.getIsColorMixing());
            goodsDO.setMaterialsName(goodsVO.getMaterialsName());
            goodsDO.setMaterialsCode(goodsVO.getMaterialsCode());
            goodsDO.setMaterialsEnable(goodsVO.getMaterialsEnable());
            goodsDO.setPhysicalClassify(goodsVO.getPhysicalClassify());
            goodsDO.setProductGrade(goodsVO.getProductGrade());
            goodsDO.setSearchKeyword(goodsVO.getSearchKeyword());
            goodsDO.setTypeName(goodsVO.getTypeName());
            return goodsDO;
        } else {
            return null;
        }
    }
}
