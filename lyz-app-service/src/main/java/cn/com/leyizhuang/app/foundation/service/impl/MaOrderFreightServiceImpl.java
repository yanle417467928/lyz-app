package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.MaOrderFreightDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.order.OrderFreightChange;
import cn.com.leyizhuang.app.foundation.service.MaOrderFreightService;
import cn.com.leyizhuang.app.foundation.vo.management.freight.OrderFreightChangeVO;
import cn.com.leyizhuang.app.foundation.vo.management.freight.OrderFreightDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.freight.OrderFreightVO;
import cn.com.leyizhuang.common.util.CountUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class MaOrderFreightServiceImpl implements MaOrderFreightService {

    @Autowired
    private MaOrderFreightDAO maOrderFreightDAO;

    @Override
    public PageInfo<OrderFreightVO> queryPageVO(Integer page, Integer size, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<OrderFreightVO> orderFreightPageList = this.maOrderFreightDAO.findAllOrderFreight(storeIds);
        return new PageInfo<>(orderFreightPageList);
    }


    @Override
    public PageInfo<OrderFreightVO> queryOrderFreightVOByStoreId(Integer page, Integer size, Long storeId) {
        PageHelper.startPage(page, size);
        List<OrderFreightVO> orderFreightPageList = this.maOrderFreightDAO.queryOrderFreightVOByStoreId(storeId);
        return new PageInfo<>(orderFreightPageList);
    }

    @Override
    public PageInfo<OrderFreightVO> queryOrderFreightVOByCityId(Integer page, Integer size, Long cityId) {
        PageHelper.startPage(page, size);
        List<OrderFreightVO> orderFreightPageList = this.maOrderFreightDAO.queryOrderFreightVOByCityId(cityId);
        return new PageInfo<>(orderFreightPageList);
    }

    @Override
    public PageInfo<OrderFreightVO> queryOrderFreightVOByInfo(Integer page, Integer size, String queryOrderInfo) {
        PageHelper.startPage(page, size);
        List<OrderFreightVO> orderFreightPageList = this.maOrderFreightDAO.queryOrderFreightVOByInfo(queryOrderInfo);
        return new PageInfo<>(orderFreightPageList);
    }

    @Override
    public OrderFreightVO queryOrderFreightVOById(Long id) {
        OrderFreightVO orderFreightVO = this.maOrderFreightDAO.queryOrderFreightVOById(id);
        return orderFreightVO;
    }

    @Override
    public OrderFreightDetailVO queryOrderFreightDetailVOById(Long id) {
        if (null != id) {
            OrderFreightDetailVO orderFreightPage = this.maOrderFreightDAO.queryOrderFreightDetailVOById(id);
            List<MaOrderGoodsInfo> goodsInfoList = orderFreightPage.getOrderGoodsInfoList();
            if (null != goodsInfoList) {
                //算出单种商品的总金额
                for (MaOrderGoodsInfo maOrderGoodsInfo : goodsInfoList) {
                    if (null != maOrderGoodsInfo.getOrderQty() && null != maOrderGoodsInfo.getReturnPrice()) {
                        maOrderGoodsInfo.setSettlementPrice(maOrderGoodsInfo.getReturnPrice().multiply(new BigDecimal(maOrderGoodsInfo.getOrderQty())));
                    }
                }
                //得到所有商品总价格
                BigDecimal totalPrice = BigDecimal.ZERO;
                for (MaOrderGoodsInfo maOrderGoodsInfo : goodsInfoList) {
                    totalPrice = totalPrice.add(maOrderGoodsInfo.getSettlementPrice());
                }
                orderFreightPage.setTotalGoodsPrice(totalPrice);
                return orderFreightPage;
            }
        }
        return null;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(OrderFreightChange orderFreightChange) {
        if (null != orderFreightChange) {
            Long orid = orderFreightChange.getOrderId();
            OrderFreightVO orderFreightVO = this.maOrderFreightDAO.queryOrderFreightVOById(orid);
            Double freight = orderFreightVO.getSimpleOrderBillingDetails().getFreight();
            //修改订单金额小计和运费
            BigDecimal changAmount = orderFreightChange.getChangeAmount();
            orderFreightChange.setFreightChangeAfter(BigDecimal.valueOf(CountUtil.add(freight, changAmount.doubleValue())));
            this.updateOrderBillingPrice(orderFreightChange.getOrderId(),orderFreightChange.getFreightChangeAfter(),changAmount);
            orderFreightChange.setModifyTime(new Date());
            orderFreightChange.setModifier(AppIdentityType.ADMINISTRATOR.toString());
            //保存记录
            this.saveOrderFreightChange(orderFreightChange);
        }
    }

    @Override
    public PageInfo<OrderFreightChangeVO> queryOrderFreightChangeList(Integer page, Integer size, String keywords) {
        PageHelper.startPage(page, size);
        List<OrderFreightChangeVO> orderFreightChangePageList = this.maOrderFreightDAO.queryOrderFreightChangeList(keywords);
        return new PageInfo<>(orderFreightChangePageList);
    }

    @Override
    public void updateOrderBillingPrice(Long orderId, BigDecimal freight,BigDecimal changAmount) {
        this.maOrderFreightDAO.updateOrderBillingPrice(orderId, freight,changAmount);
    }

    @Override
    public void saveOrderFreightChange(OrderFreightChange orderFreightChange) {
        //获取登录用户ID
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (null != shiroUser){
            orderFreightChange.setModifier(shiroUser.getName());
        } else {
            orderFreightChange.setModifier("admin");
        }
        orderFreightChange.setModifyTime(new Date());
        this.maOrderFreightDAO.saveOrderFreightChange(orderFreightChange);
    }

    @Override
    public List<OrderFreightChange> queryOrderFreightChangeLogListByOid(Long oid) {
        return this.maOrderFreightDAO.queryOrderFreightChangeLogListByOid(oid);
    }

    @Override
    public OrderFreightChange queryOrderFreightChangeLogFirstByOid(Long oid) {
        return this.maOrderFreightDAO.queryOrderFreightChangeLogFirstByOid(oid);
    }
}
