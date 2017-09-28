package cn.com.leyizhuang.app.web.controller.category;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.response.SecondCategoryResponse;
import cn.com.leyizhuang.app.foundation.pojo.GoodsCategory;
import cn.com.leyizhuang.app.foundation.service.IAppProductCategoryService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类接口
 *
 * @author Richard
 * Created on 2017-09-25 10:17
 **/
@RestController
@RequestMapping(value = "/app/category")
public class CategoryController {

    @Autowired
    private IAppProductCategoryService productCategoryService;

    @RequestMapping(value = "/second/list",method = RequestMethod.POST)
    public ResultDTO<Object> getSecondCategoryListByParentCode(String categoryCode,Long userId,String identityType){
        if (StringUtils.isBlank(categoryCode)){
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,"一级分类编码{categoryCode}不能为空",null);
        }
        if (null == userId){
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,"用户id{userId}不能为空",null);
        }
        if (StringUtils.isBlank(identityType)){
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,"身份类型{categoryCode}不能为空",null);
        }
        List<GoodsCategory> categoryList = productCategoryService.findSecondCategoryByFirstCategoryCodeAndUserIdAndIdentityType(categoryCode,userId,identityType);
        List<SecondCategoryResponse> responseList = new ArrayList<>();
        if (null != categoryList && categoryList.size()>0){
            SecondCategoryResponse secondCategoryResponse = null;
            for (GoodsCategory category:categoryList){
                secondCategoryResponse = new SecondCategoryResponse();
                secondCategoryResponse.setCategoryId(category.getId());
                secondCategoryResponse.setCategoryName(category.getCategoryName());
                responseList.add(secondCategoryResponse);
            }
        }

        return  new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,null,responseList);
    }
}
