package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;

import java.util.List;

/**
 * 顾客等级服务接口
 * Created by panjie on 2018/1/26.
 */
public interface CustomerLevelService {

    /**
     * 计算灯号 多个顾客
     * @param customers
     * @param sellerId
     * @return
     */
    List<AppCustomer> countCustomerListLightlevel(List<AppCustomer> customers, Long sellerId);

    /**
     * 计算灯号 单个顾客
     * @param customer
     * @param sellerId
     * @return
     */
    AppCustomer countCustomerLightLevel(AppCustomer customer, Long sellerId);
}
