package cn.com.leyizhuang.app.foundation.vo;

import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsCategory;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsCategoryDO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类
 *
 * @author Richard
 * Created on 2017-09-25 9:54
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCategoryVO {

    private Long id;

    //分类名称
    private String categoryName;

    //父分类code

    private String paCategoryCode;

    //排序id
    private Long sortId;

    public static final GoodsCategoryVO transform(GoodsCategoryDO goodsCategoryDO) {
        if (null != goodsCategoryDO) {
            GoodsCategoryVO goodsCategoryVO = new GoodsCategoryVO();
            goodsCategoryVO.setId(goodsCategoryDO.getCid());
            goodsCategoryVO.setCategoryName(goodsCategoryDO.getCategoryName());
            if("WATER".equals(goodsCategoryDO.getPCategoryCode())){
                goodsCategoryVO.setPaCategoryCode("水");
            }else if("TILE".equals(goodsCategoryDO.getPCategoryCode())){
                goodsCategoryVO.setPaCategoryCode("瓦");
            }else if("WOOD".equals(goodsCategoryDO.getPCategoryCode())){
                goodsCategoryVO.setPaCategoryCode("木");
            }else if("OIL".equals(goodsCategoryDO.getPCategoryCode())){
                goodsCategoryVO.setPaCategoryCode("油");
            }else if("ELECTRIC".equals(goodsCategoryDO.getPCategoryCode())){
                goodsCategoryVO.setPaCategoryCode("电");
            }else{
                goodsCategoryVO.setPaCategoryCode("-");
            }
            goodsCategoryVO.setSortId(goodsCategoryDO.getSortId());
            return goodsCategoryVO;
        } else {
            return null;
        }
    }

    public static final List<GoodsCategoryVO> transform(List<GoodsCategoryDO> goodsCategoryDOList) {
        List<GoodsCategoryVO> GoodsCategoryVOList;
        if (null != goodsCategoryDOList && goodsCategoryDOList.size() > 0) {
            GoodsCategoryVOList = new ArrayList<>(goodsCategoryDOList.size());
            goodsCategoryDOList.forEach(goodsCategoryDO -> GoodsCategoryVOList.add(transform(goodsCategoryDO)));
        } else {
            GoodsCategoryVOList = new ArrayList<>(0);
        }
        return GoodsCategoryVOList;
    }

}
