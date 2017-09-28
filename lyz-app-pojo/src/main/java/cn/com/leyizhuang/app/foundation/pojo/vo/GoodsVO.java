package cn.com.leyizhuang.app.foundation.pojo.vo;

import cn.com.leyizhuang.app.foundation.pojo.GoodsDO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@NoArgsConstructor
public class GoodsVO {
    private static final Logger LOG = LoggerFactory.getLogger(GoodsVO.class);

    //商品ID
    private Long id;

    //商品名称
    private String goodsName;

    //商品编码
    private String goodsCode;

    //封面图片
    private String coverImageUri;

    //轮播展示图片，多张图片以,隔开
    private String rotationImageUri;

    //商品规格
    private  String goodsSpecification;

    //单位名称
    private String goodsUnit;

    //产品分类id
    private Long categoryId;

    //品牌id
    private Long brandId;

    //类型id
    private Long typeId;

    //会员价
    private Double memberPrice;

    //零售价
    private Double retailPrice;


    public static final GoodsVO transform(GoodsDO goodsDO) {
        if (null != goodsDO) {
            GoodsVO goodsVO = new GoodsVO();
            goodsVO.setGoodsName(goodsDO.getGoodsName());
            goodsVO.setGoodsCode(goodsDO.getGoodsCode());
            goodsVO.setBrandId(goodsDO.getBrandId());
            goodsVO.setCategoryId(goodsDO.getCategoryId());
            goodsVO.setCoverImageUri(goodsDO.getCoverImageUri());
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
