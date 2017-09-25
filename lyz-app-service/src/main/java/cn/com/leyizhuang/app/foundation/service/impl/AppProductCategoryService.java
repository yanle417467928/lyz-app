package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppProductCategoryDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.vo.ProductCategory;
import cn.com.leyizhuang.app.foundation.service.IAppCustomerService;
import cn.com.leyizhuang.app.foundation.service.IAppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.IAppProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 门店服务实现类
 *
 * @author Richard
 * Created on 2017-07-20 10:42
 **/
@Service
public class AppProductCategoryService  implements IAppProductCategoryService {

    @Autowired
    private AppProductCategoryDAO productCategoryDAO;


    @Autowired
    private IAppEmployeeService employeeService;

    @Override
    public List<ProductCategory> findSecondCategoryByFirstCategoryCodeAndUserIdAndIdentityType(String categoryCode, Long userId, String identityType) {
        if(null != categoryCode && null != userId && null != identityType){
            if (identityType.equalsIgnoreCase("CUSTOMER")){
                AppEmployee employee = employeeService.findByUserId(userId);
                if (null != employee){
                    return productCategoryDAO.findSecondCategoryByFirstCategoryCodeAndStoreId(categoryCode,employee.getStoreId());
                }
            }else if (identityType.equalsIgnoreCase("EMPLOYEE")){
                AppEmployee employee = employeeService.findById(userId);
                if(null != employee){
                    return productCategoryDAO.findSecondCategoryByFirstCategoryCodeAndStoreId(categoryCode,employee.getStoreId());
                }
            }
        }
        return null;
    }
}
