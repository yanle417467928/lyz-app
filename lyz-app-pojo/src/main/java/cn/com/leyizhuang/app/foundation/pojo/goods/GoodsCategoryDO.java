package cn.com.leyizhuang.app.foundation.pojo.goods;

import cn.com.leyizhuang.app.foundation.vo.GoodsCategoryVO;
import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.*;

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
public class GoodsCategoryDO {

    private Long cid;

    //分类名称
    private String categoryName;

    //父分类ID
    private Long pCid;

    //父分类code

    private String pCategoryCode;

    //分类编码 目前只有一级分类（水电木瓦油）有分类编码
    private String categoryCode;

    //排序id
    private Long sortId;

    public static final GoodsCategoryDO transform(GoodsCategoryVO goodsCategoryVO) {
        if (null != goodsCategoryVO) {
            GoodsCategoryDO goodsCategoryDO = new GoodsCategoryDO();
            goodsCategoryDO.setCid(goodsCategoryVO.getId());
            goodsCategoryDO.setCategoryName(goodsCategoryVO.getCategoryName());
            goodsCategoryDO.setSortId(goodsCategoryVO.getSortId());
            if("水".equals(goodsCategoryVO.getPaCategoryCode())){
                goodsCategoryDO.setPCid(1L);
                goodsCategoryDO.setPCategoryCode("WATER");
            }else if("瓦".equals(goodsCategoryVO.getPaCategoryCode())){
                goodsCategoryDO.setPCid(4L);
                goodsCategoryDO.setPCategoryCode("TILE");
            }else if("木".equals(goodsCategoryVO.getPaCategoryCode())){
                goodsCategoryDO.setPCid(3L);
                goodsCategoryDO.setPCategoryCode("WOOD");
            }else if("油".equals(goodsCategoryVO.getPaCategoryCode())){
                goodsCategoryDO.setPCid(5L);
                goodsCategoryDO.setPCategoryCode("OIL");
            }else if("电".equals(goodsCategoryVO.getPaCategoryCode())){
                goodsCategoryDO.setPCid(2L);
                goodsCategoryDO.setPCategoryCode("ELECTRIC");
            }
            return goodsCategoryDO;
        } else {
            return null;
        }
    }

}
