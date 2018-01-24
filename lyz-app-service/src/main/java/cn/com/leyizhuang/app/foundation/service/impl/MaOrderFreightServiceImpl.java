package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaOrderFreightDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.order.OrderFreightChange;
import cn.com.leyizhuang.app.foundation.pojo.management.order.SimpleOrderBillingDetails;
import cn.com.leyizhuang.app.foundation.service.MaOrderFreightService;
import cn.com.leyizhuang.app.foundation.vo.management.freight.OrderFreightChangeVO;
import cn.com.leyizhuang.app.foundation.vo.management.freight.OrderFreightDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.freight.OrderFreightVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
  public PageInfo<OrderFreightVO> queryPageVO(Integer page, Integer size){
       PageHelper.startPage(page, size);
       List<OrderFreightVO> orderFreightPageList = this.maOrderFreightDAO.findAllOrderFreight();
       return new PageInfo<>(orderFreightPageList);
  }


    @Override
    public PageInfo<OrderFreightVO> queryOrderFreightVOByStoreId(Integer page, Integer size,Long storeId){
        PageHelper.startPage(page, size);
            List<OrderFreightVO> orderFreightPageList = this.maOrderFreightDAO.queryOrderFreightVOByStoreId(storeId);
            return new PageInfo<>(orderFreightPageList);
    }

    @Override
    public PageInfo<OrderFreightVO> queryOrderFreightVOByCityId(Integer page, Integer size,Long cityId){
        PageHelper.startPage(page, size);
        List<OrderFreightVO> orderFreightPageList = this.maOrderFreightDAO.queryOrderFreightVOByCityId(cityId);
        return new PageInfo<>(orderFreightPageList);
    }

    @Override
    public PageInfo<OrderFreightVO> queryOrderFreightVOByInfo(Integer page, Integer size,String queryOrderInfo){
        PageHelper.startPage(page, size);
        List<OrderFreightVO> orderFreightPageList = this.maOrderFreightDAO.queryOrderFreightVOByInfo(queryOrderInfo);
        return new PageInfo<>(orderFreightPageList);
    }

    @Override
    public OrderFreightVO queryOrderFreightVOById(Long id){
        OrderFreightVO orderFreightVO = this.maOrderFreightDAO.queryOrderFreightVOById(id);
        return orderFreightVO;
    }

    @Override
    public OrderFreightDetailVO queryOrderFreightDetailVOById(Long id){
        if(null!=id){
            OrderFreightDetailVO orderFreightPage = this.maOrderFreightDAO.queryOrderFreightDetailVOById(id);
            List<MaOrderGoodsInfo> goodsInfoList =orderFreightPage.getOrderGoodsInfoList();
            if(null!=goodsInfoList){
                //算出单种商品的总金额
                for (MaOrderGoodsInfo maOrderGoodsInfo:goodsInfoList) {
                    if(null!=maOrderGoodsInfo.getOrderQty()&&null!=maOrderGoodsInfo.getReturnPrice()){
                        maOrderGoodsInfo.setSettlementPrice(maOrderGoodsInfo.getReturnPrice().multiply(new BigDecimal(maOrderGoodsInfo.getOrderQty())));
                    }
                }
                //得到所有商品总价格
                BigDecimal totalPrice = BigDecimal.ZERO;
                for(MaOrderGoodsInfo maOrderGoodsInfo:goodsInfoList){
                    totalPrice=totalPrice.add(maOrderGoodsInfo.getSettlementPrice());
                }
                orderFreightPage.setTotalGoodsPrice(totalPrice);
                return orderFreightPage;
             }
        }
        return  null;
    }

    @Transactional
    @Override
    public void update(SimpleOrderBillingDetails simpleOrderBillingDetails,OrderFreightChange orderFreightChange){
        if(null !=simpleOrderBillingDetails){
            this.maOrderFreightDAO.update(simpleOrderBillingDetails);
            this.updateOrderPrice(orderFreightChange.getOrderId(),BigDecimal.valueOf(simpleOrderBillingDetails.getFreight()));
            this.saveOrderFreightChange(orderFreightChange);
        }
    }

    @Override
    public PageInfo<OrderFreightChangeVO> queryOrderFreightChangeList(Integer page, Integer size){
        PageHelper.startPage(page, size);
        List<OrderFreightChangeVO> orderFreightChangePageList = this.maOrderFreightDAO.queryOrderFreightChangeList();
        return new PageInfo<>(orderFreightChangePageList);
    }

    @Override
    public void updateOrderPrice(Long orderId, BigDecimal freight){
       this.maOrderFreightDAO.updateOrderPrice(orderId, freight);
    }

    @Override
    public void saveOrderFreightChange(OrderFreightChange orderFreightChange){
        orderFreightChange.setFreightChangeAmount(orderFreightChange.getFreightChangeAfter().subtract(orderFreightChange.getFreightChangeBefore()));
        orderFreightChange.setModifier("admin");
        orderFreightChange.setModifyTime(new Date());
        this.maOrderFreightDAO.saveOrderFreightChange(orderFreightChange);
    }
}
