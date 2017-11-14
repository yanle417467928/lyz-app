package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.AppStoreDAO;
import cn.com.leyizhuang.app.foundation.dao.CityDAO;
import cn.com.leyizhuang.app.foundation.dao.OrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderLockExpendRequest;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * @author Jerry.Ren
 * Date: 2017/10/30.
 * Time: 18:17.
 */
@Service
public class AppOrderServiceImpl implements AppOrderService {


    @Autowired
    private AppStoreDAO appStoreDAO;

    @Autowired
    private CityDAO cityDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Override
    public int lockUserExpendOfOrder(OrderLockExpendRequest lockExpendRequest) {
        return 0;
    }

    @Override
    public Boolean existGoodsStoreInventory(Long storeId,Long gid,Integer qty) {
        return  appStoreDAO.existGoodsStoreInventory(storeId,gid,qty);
    }

    @Override
    public Boolean existGoodsCityInventory(Long cityId, Long gid, Integer qty) {
        return cityDAO.existGoodsCityInventory(cityId,gid,qty);
    }

    @Override
    public List<OrderBaseInfo> getOrderListByUserIDAndIdentityType(Long userID, Integer identityType) {
        return orderDAO.getOrderListByUserIDAndIdentityType(userID, AppIdentityType.getAppIdentityTypeByValue(identityType));
    }

    @Override
    public List<OrderGoodsInfo> getOrderGoodsInfoByOrderNumber(String orderNumber) {
        return orderDAO.getOrderGoodsInfoByOrderNumber(orderNumber);
    }

    @Override
    public Double getAmountPayableByOrderNumber(String orderNumber) {
        return orderDAO.getAmountPayableByOrderNumber(orderNumber);
    }

    @Override
    public int querySumQtyByOrderNumber(String orderNumber) {
        return orderDAO.querySumQtyByOrderNumber(orderNumber);
    }
}
