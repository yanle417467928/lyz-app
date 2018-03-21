package cn.com.leyizhuang.app.foundation.vo.management.goods;

import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/6
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaGoodsVO {

    private Long id;

    //商品名称
    private String skuName;

    //商品编码
    private String sku;

    //封面图片
    private String coverImageUri;

    //轮播展示图片，多张图片以,隔开
    private String rotationImageUri;

    //商品规格
    private String goodsSpecification;

    //单位单位
    private String goodsUnit;

    //产品分类名称
    private String categoryName;

    //品牌id
    private Long brdId;

    //品牌名称
    private String brdName;

    //类型名称
    private String typeName;

    //是否热销
    private Boolean isHot;

    //排序号
    private Long sortId;

    //是否调色产品
    private Boolean isColorMixing;

    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //商品公司标识
    private String companyFlag;

    //产品档次
    private String productGrade;

    //搜索关键字
    private String searchKeyword;

    //商品详情
    private String goodsDetail;

    //物料状态
    private Boolean materialsEnable;

    //物理分类
    private Long physicalClassify;

    //物料条码
    private String materialsCode;

    //物料名称
    private String materialsName;


    public static final MaGoodsVO transform(GoodsDO goodsDO) {
        if (null != goodsDO) {
            MaGoodsVO goodsVO = new MaGoodsVO();
            goodsVO.setId(goodsDO.getGid());
            goodsVO.setBrdId(goodsDO.getBrdId());
            goodsVO.setSkuName(goodsDO.getSkuName());
            goodsVO.setSku(goodsDO.getSku());
            goodsVO.setBrdName(goodsDO.getBrandName());
            goodsVO.setGoodsUnit(goodsDO.getGoodsUnit());
            goodsVO.setCategoryName(goodsDO.getCategoryName());
            goodsVO.setCoverImageUri(goodsDO.getCoverImageUri());
            goodsVO.setGoodsSpecification(goodsDO.getGoodsSpecification());
            goodsVO.setGoodsDetail(goodsDO.getGoodsDetial());
            goodsVO.setCreateTime(goodsDO.getCreateTime());
            goodsVO.setIsHot(goodsDO.getIsHot());
            goodsVO.setRotationImageUri(goodsDO.getRotationImageUri());
            goodsVO.setSortId(goodsDO.getSortId());
            goodsVO.setCompanyFlag(goodsDO.getCompanyFlag());
            goodsVO.setGoodsDetail(goodsDO.getGoodsDetial());
            goodsVO.setIsColorMixing(goodsDO.getIsColorMixing());
            goodsVO.setMaterialsName(goodsDO.getMaterialsName());
            goodsVO.setMaterialsCode(goodsDO.getMaterialsCode());
            goodsVO.setMaterialsEnable(goodsDO.getMaterialsEnable());
            goodsVO.setPhysicalClassify(goodsDO.getPhysicalClassify());
            goodsVO.setProductGrade(goodsDO.getProductGrade());
            goodsVO.setSearchKeyword(goodsDO.getSearchKeyword());
            goodsVO.setTypeName(goodsDO.getTypeName());
            return goodsVO;
        } else {
            return null;
        }
    }

    public static final List<MaGoodsVO> transform(List<GoodsDO> goodsDOList) {
        List<MaGoodsVO> goodsVOList;
        if (null != goodsDOList && goodsDOList.size() > 0) {
            goodsVOList = new ArrayList<>(goodsDOList.size());
            goodsDOList.forEach(goodsDO -> goodsVOList.add(transform(goodsDO)));
        } else {
            goodsVOList = new ArrayList<>(0);
        }
        return goodsVOList;
    }

}
