package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaCompanyOrderVORequest;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaOrderVORequest;
import cn.com.leyizhuang.app.foundation.service.MaOrderService;
import cn.com.leyizhuang.app.foundation.vo.MaOrderVO;
import cn.com.leyizhuang.app.foundation.vo.management.order.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by caiyu on 2017/12/16.
 */
@Service
public class MaOrderServiceImpl implements MaOrderService{

    @Resource
    private MaOrderDAO maOrderDAO;
    @Override
    public List<MaOrderVO> findMaOrderVOAll() {
        return maOrderDAO.findMaOrderVOAll();
    }

    @Override
    public List<MaOrderVO> findMaOrderVOByCityId(Long cityId) {
        return maOrderDAO.findMaOrderVOByCityId(cityId);
    }

    @Override
    public List<MaOrderVO> findMaOrderVOByStoreId(Long storeId) {
        return maOrderDAO.findMaOrderVOByStoreId(storeId);
    }

    @Override
    public List<MaOrderVO> findMaOrderVOByStoreIdAndCityIdAndDeliveryType(String deliveryType, Long cityId, Long storeId) {
        return maOrderDAO.findMaOrderVOByStoreIdAndCityIdAndDeliveryType(deliveryType, cityId, storeId);
    }

    @Override
    public List<MaOrderVO> findMaOrderVOByOrderNumber(String orderNumber) {
        return maOrderDAO.findMaOrderVOByOrderNumber(orderNumber);
    }

    @Override
    public List<MaOrderVO> findMaOrderVOByCondition(MaOrderVORequest maOrderVORequest) {
        return maOrderDAO.findMaOrderVOByCondition(maOrderVORequest);
    }

    @Override
    public List<MaOrderVO> findCompanyOrderAll() {
        return maOrderDAO.findCompanyOrderAll();
    }

    @Override
    public List<MaOrderVO> findCompanyOrderByOrderNumber(String orderNumber) {
        return maOrderDAO.findCompanyOrderByOrderNumber(orderNumber);
    }

    @Override
    public List<MaOrderVO> findCompanyOrderByCondition(MaCompanyOrderVORequest maCompanyOrderVORequest) {
        return maOrderDAO.findCompanyOrderByCondition(maCompanyOrderVORequest);
    }

    @Override
    public List<MaOrderVO> findPendingShipmentOrder() {
        return maOrderDAO.findPendingShipmentOrder();
    }

    @Override
    public List<MaOrderVO> findPendingShipmentOrderByCondition(MaCompanyOrderVORequest maCompanyOrderVORequest) {
        return maOrderDAO.findPendingShipmentOrderByCondition(maCompanyOrderVORequest);
    }

    @Override
    public List<MaOrderVO> findPendingShipmentOrderByOrderNumber(String orderNumber) {
        return maOrderDAO.findPendingShipmentOrderByOrderNumber(orderNumber);
    }

    @Override
    public MaOrderDetailResponse findMaOrderDetailByOrderNumber(String orderNmber) {
        return maOrderDAO.findMaOrderDetailByOrderNumber(orderNmber);
    }

    @Override
    public MaOrderBillingDetailResponse getMaOrderBillingDetailByOrderNumber(String orderNmber) {
        return maOrderDAO.getMaOrderBillingDetailByOrderNumber(orderNmber);
    }

    @Override
    public List<MaOrderBillingPaymentDetailResponse> getMaOrderBillingPaymentDetailByOrderNumber(String orderNmber) {
        return maOrderDAO.getMaOrderBillingPaymentDetailByOrderNumber(orderNmber);
    }

    @Override
    public MaOrderDeliveryInfoResponse getDeliveryInfoByOrderNumber(String orderNmber) {
        return maOrderDAO.getDeliveryInfoByOrderNumber(orderNmber);
    }

    @Override
    public PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderShippingList(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<MaSelfTakeOrderVO> maSelfTakeOrderVOList = maOrderDAO.findSelfTakeOrderShippingList();
        return  new PageInfo<>(maSelfTakeOrderVOList);
    }

    @Override
    public PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderShippingListByCityId(Integer page, Integer size,Long cityId) {
        PageHelper.startPage(page, size);
        List<MaSelfTakeOrderVO> maSelfTakeOrderVOList = maOrderDAO.findSelfTakeOrderShippingListByCityId(cityId);
        return  new PageInfo<>(maSelfTakeOrderVOList);
    }

    @Override
    public PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderShippingListByStoreId(Integer page, Integer size,Long storeId) {
        PageHelper.startPage(page, size);
        List<MaSelfTakeOrderVO> maSelfTakeOrderVOList = maOrderDAO.findSelfTakeOrderShippingListByStoreId(storeId);
        return  new PageInfo<>(maSelfTakeOrderVOList);
    }


    @Override
    public PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderShippingListByInfo(Integer page, Integer size,String info) {
        PageHelper.startPage(page, size);
        List<MaSelfTakeOrderVO> maSelfTakeOrderVOList = maOrderDAO.findSelfTakeOrderShippingListByInfo(info);
        return  new PageInfo<>(maSelfTakeOrderVOList);
    }

    @Override
    public PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderByCondition(Integer page, Integer size,MaOrderVORequest maOrderVORequest) {
        PageHelper.startPage(page, size);
        List<MaSelfTakeOrderVO> maSelfTakeOrderVOList = maOrderDAO.findSelfTakeOrderByCondition(maOrderVORequest);
        return  new PageInfo<>(maSelfTakeOrderVOList);
    }
}
