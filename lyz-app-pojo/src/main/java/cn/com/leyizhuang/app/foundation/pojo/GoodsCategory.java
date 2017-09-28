package cn.com.leyizhuang.app.foundation.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 商品分类
 *
 * @author Richard
 * Created on 2017-09-25 9:54
 **/
@Getter
@Setter
@ToString
public class GoodsCategory {

    private Long id;

    //分类名称
    private String categoryName;

    //父分类ID
    private Long parentId;

    //父分类code

    private String parentCode;

    //分类编码 目前只有一级分类（水电木瓦油）有分类编码
    private String categoryCode;

    //排序id
    private Long sortId;

    public GoodsCategory() {
    }

    public GoodsCategory(Long id, String categoryName, Long parentId, String categoryCode, Long sortId) {
        this.id = id;
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.categoryCode = categoryCode;
        this.sortId = sortId;
    }
}
