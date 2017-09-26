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
public class ProductCategory {

    private Long id;

    //分类名称
    private String categoryName;

    //父分类ID
    private Long parentId;

    //分类编码
    private String categoryCode;

    //排序id
    private Long sortId;

    public ProductCategory() {
    }

    public ProductCategory(Long id, String categoryName, Long parentId, String categoryCode, Long sortId) {
        this.id = id;
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.categoryCode = categoryCode;
        this.sortId = sortId;
    }
}
