package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppReturnOrderStatus;
import cn.com.leyizhuang.app.foundation.dao.AppStoreDAO;
import cn.com.leyizhuang.app.foundation.dao.ReturnOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.inventory.returning.Returning;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.service.ItyReturningService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2018/1/16.
 * Time: 18:23.
 */

@Service
public class ItyReturningServiceImpl implements ItyReturningService {

    @Resource
    private ReturnOrderDAO returnOrderDAO;
    @Resource
    private AppStoreDAO appStoreDAO;

    @Override
    public PageInfo<ReturnOrderBaseInfo> queryPage(Integer offset, Integer size, String keywords) {
        PageHelper.startPage(offset, size);

        List<ReturnOrderBaseInfo> returnOrderBaseInfoList = returnOrderDAO.findReturnOrderList(keywords);

        return new PageInfo<>(returnOrderBaseInfoList);
    }

    @Override
    public PageInfo<ReturnOrderBaseInfo> getReturningVOByQueryParam(Integer offset, Integer size, Integer status, Long store) {

        PageHelper.startPage(offset, size);
        List<ReturnOrderBaseInfo> returnOrderBaseInfoList = new ArrayList<>();
        if (null != status) {
            returnOrderBaseInfoList = returnOrderDAO.findReturnOrderListByStatus(AppReturnOrderStatus.getAppOrderReturnStatusByValue(status));
        } else if (null != store) {
            returnOrderBaseInfoList = returnOrderDAO.findReturnOrderListByStroe(store);
        }
        return new PageInfo<>(returnOrderBaseInfoList);
    }

    @Override
    public Returning queryReturningById(Long id) {

        ReturnOrderBaseInfo baseInfo = returnOrderDAO.queryReturnOrderByRoid(id);
        Returning returning = new Returning();
        returning.setId(id);
        returning.setCreatorPhone(baseInfo.getCreatorPhone());
        returning.setCustomerName(baseInfo.getCustomerName());
        returning.setOrderNo(baseInfo.getOrderNo());
        returning.setRemarksInfo(baseInfo.getRemarksInfo());
        returning.setReturnNo(baseInfo.getReturnNo());
        returning.setReturnPrice(baseInfo.getReturnPrice());
        returning.setReturnStatus(baseInfo.getReturnStatus().getDescription());
        returning.setReturnType(baseInfo.getReturnType().getDescription());

        AppStore appStore = appStoreDAO.findByStoreCode(baseInfo.getStoreCode());
        returning.setStoreName(appStore.getStoreName());
        returning.setStoreAddress(appStore.getDetailedAddress());
        returning.setStorePhone(appStore.getPhone());

        List<ReturnOrderGoodsInfo> returnOrderGoodsDetails = returnOrderDAO.findReturnOrderGoodsInfoByOrderNumber(baseInfo.getReturnNo());
        returning.setReturnOrderGoodsList(returnOrderGoodsDetails);

        return returning;
    }
}
