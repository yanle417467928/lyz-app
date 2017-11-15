package cn.com.leyizhuang.app.foundation.pojo.goods;

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
public class GoodsCategory {

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

}
