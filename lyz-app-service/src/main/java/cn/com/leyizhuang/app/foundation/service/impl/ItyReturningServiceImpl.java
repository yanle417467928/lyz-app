package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.AppStoreDAO;
import cn.com.leyizhuang.app.foundation.dao.ReturnOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.inventory.returning.Returning;
import cn.com.leyizhuang.app.foundation.pojo.inventory.returning.ReturningVO;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.service.ItyReturningService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public PageInfo<ReturningVO> queryPage(Integer offset, Integer size, String keywords) {
        PageHelper.startPage(offset, size);

        List<ReturnOrderBaseInfo> returnOrderBaseInfoList = returnOrderDAO.findReturnOrderList(keywords);
        List<ReturningVO> returningVOList = ReturningVO.transform(returnOrderBaseInfoList);
        return new PageInfo<>(returningVOList);
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
        returning.setReturnStatus(baseInfo.getReturnStatus());
        returning.setReturnType(baseInfo.getReturnType());

        AppStore appStore;
        if (AppIdentityType.CUSTOMER.equals(baseInfo.getCreatorIdentityType())) {
            appStore = appStoreDAO.findAppStoreCusId(baseInfo.getCreatorId());
        } else {
            appStore = appStoreDAO.findAppStoreByEmpId(baseInfo.getCreatorId());
        }
        returning.setStoreName(appStore.getStoreName());
        returning.setStoreAddress(appStore.getDetailedAddress());
        returning.setStorePhone(appStore.getPhone());

        List<ReturnOrderGoodsInfo> returnOrderGoodsDetails = returnOrderDAO.findReturnOrderGoodsInfoByOrderNumber(baseInfo.getReturnNo());
        returning.setReturnOrderGoodsList(returnOrderGoodsDetails);

        return returning;
    }
}
