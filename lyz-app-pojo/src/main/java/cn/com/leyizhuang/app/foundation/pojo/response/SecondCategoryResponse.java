package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * App首页加载二级分类接口响应实体
 *
 * @author Ricahrd
 * Created on 2017-09-25 11:27
 **/
@Getter
@Setter
@ToString
public class SecondCategoryResponse {

    private Long categoryId;

    private String categoryName;
}
