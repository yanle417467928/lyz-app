package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 获取商品分类接口返回值
 *
 * @author Richard
 * Created on 2017-09-19 13:57
 **/
@Getter
@Setter
@ToString
public class GoodsCategoryResponse implements Serializable{

    private static final long serialVersionUID = -3385163050087944894L;
    private Long categoryId;

    private String categoryName;
}
