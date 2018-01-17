package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppToWmsOrderDAO;
import cn.com.leyizhuang.app.foundation.dao.ItyRequiringDAO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.requiring.Requiring;
import cn.com.leyizhuang.app.foundation.pojo.inventory.requiring.RequiringVO;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrder;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.response.StoreResponse;
import cn.com.leyizhuang.app.foundation.service.ItyRequiringService;
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
 * Time: 18:29.
 */

@Service
public class ItyRequiringServiceImpl implements ItyRequiringService {

    @Resource
    private ItyRequiringDAO ityRequiringDAO;

    @Resource
    private AppToWmsOrderDAO appToWmsOrderDAO;

    @Override
    public PageInfo<RequiringVO> queryPage(Integer offset, Integer size, String keywords) {
        PageHelper.startPage(offset, size);
        List<AtwRequisitionOrder> requiringOrderList;
        requiringOrderList = appToWmsOrderDAO.findRequiringOrderList(keywords);
        List<RequiringVO> requiringVOList = RequiringVO.transform(requiringOrderList);
        return new PageInfo<>(requiringVOList);
    }

    @Override
    public Requiring queryRequiringById(Long id) {

        Requiring requiring = new Requiring();
        AtwRequisitionOrder atwRequisitionOrder = appToWmsOrderDAO.findAtwRequisitionOrderById(id);
        List<AtwRequisitionOrderGoods> goods = appToWmsOrderDAO.findAtwRequisitionOrderGoodsByOrderNo(atwRequisitionOrder.getOrderNumber());

        requiring.setId(id);
        requiring.setOrderNumber(atwRequisitionOrder.getOrderNumber());
        requiring.setRemarkInfo(atwRequisitionOrder.getRemarkInfo());
        requiring.setGoodsList(goods);

        StoreResponse storeResponse = new StoreResponse();
        storeResponse.setStorePhone(atwRequisitionOrder.getDiySiteTel());
        storeResponse.setStoreName(atwRequisitionOrder.getDiySiteTitle());
        storeResponse.setStoreAddress(atwRequisitionOrder.getDiySiteAddress());
        requiring.setDetailAddress(storeResponse);

        return requiring;
    }
}
