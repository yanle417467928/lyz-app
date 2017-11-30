package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLogisticsInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.BillingSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.DeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.vo.UserVO;

import java.util.List;
import java.util.Map;

/**
 * 通用方法
 *
 * @author Richard
 * Created on 2017-09-12 15:42
 **/
public interface CommonService {

    void saveUserAndUserRoleByUserVO(UserVO userVO);

    void updateUserAndUserRoleByUserVO(UserVO userVO);

    void deleteUserAndUserRoleByUserId(Long uid);

    AppCustomer saveCustomerInfo(AppCustomer customer, CustomerLeBi leBi, CustomerPreDeposit preDeposit);

    void updateCustomerSignTimeAndCustomerLeBiByUserId(Long userId, Integer identityType);


    void saveAndUpdateMaterialList(List<MaterialListDO> materialListSave, List<MaterialListDO> materialListUpdate);

    /**
     * 创建订单基础信息 OrderBaseInfo
     *
     * @param cityId       城市id
     * @param userId       用户id
     * @param identityType 用户身份类型
     * @param customerId   顾客id
     * @param deliveryType 配送方式
     * @return OrderBaseInfo 订单基础信息
     */
    OrderBaseInfo createOrderBaseInfo(Long cityId, Long userId, Integer identityType, Long customerId, String deliveryType, String remark);

    /**
     * 生成订单物流信息 OrderLogisticInfo
     *
     * @param deliverySimpleInfo 页面填写的物流相关信息
     * @return OrderLogisticInfo 订单物流信息
     */
    OrderLogisticsInfo createOrderLogisticInfo(DeliverySimpleInfo deliverySimpleInfo);

    OrderBillingDetails createOrderBillingDetails(OrderBillingDetails orderBillingDetails, Long userId, Integer identityType, BillingSimpleInfo billing, List<Long> cashCouponIds);

    void reduceInventoryAndMoney(DeliverySimpleInfo deliverySimpleInfo, Map<Long, Integer> inventoryCheckMap, Long cityId, Integer identityType,
                                 Long userId, Long customerId, List<Long> cashCouponIds, OrderBillingDetails billingDetails,String orderNumber);
}
