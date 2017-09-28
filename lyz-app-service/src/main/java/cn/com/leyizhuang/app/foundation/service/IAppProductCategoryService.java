package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.GoodsCategory;

import java.util.List;

/**
 *  商品分类API
 *
 * @author Richard
 * Created on 2017-07-20 10:40
 **/
public interface IAppProductCategoryService{


    List<GoodsCategory> findSecondCategoryByFirstCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, String identityType);
}
