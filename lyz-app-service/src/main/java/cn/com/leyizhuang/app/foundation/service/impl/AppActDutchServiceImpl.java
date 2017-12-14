package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.ActConditionType;
import cn.com.leyizhuang.app.core.constant.AppCustomerType;
import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.ActBaseDAO;
import cn.com.leyizhuang.app.foundation.dao.ActGoodsMappingDAO;
import cn.com.leyizhuang.app.foundation.dao.OrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActBaseDO;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActGoodsMappingDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.GoodsIdQtyParam;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.PromotionSimpleInfo;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.OrderGoodsVO;
import cn.com.leyizhuang.common.util.CountUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by panjie on 2017/12/11.
 */
@Service
public class AppActDutchServiceImpl implements AppActDutchService {

    @Resource
    private ActBaseDAO actBaseDAO;

    @Resource
    private ActGoodsMappingDAO actGoodsMappingDAO;

    @Resource
    private GoodsService goodsService;

    @Resource
    private AppOrderService orderService;

    @Resource
    private AppCustomerService appCustomerService;

    @Resource
    private OrderDAO orderDAO;

    /**
     * 新增明细 分摊金额
     * @param userId
     * @param identityType
     * @param promotionSimpleInfoList
     * @param orderGoodsInfoList
     * @return
     */
    public Boolean addGoodsDetailsAndDutch(Long userId, AppIdentityType identityType,List<PromotionSimpleInfo> promotionSimpleInfoList, List<OrderGoodsInfo> orderGoodsInfoList){

        // 无促销 不用分摊
        if(promotionSimpleInfoList == null || promotionSimpleInfoList.size() == 0){
            return false;
        }

        // 没有本品明细 不用分摊
        if (orderGoodsInfoList == null || orderGoodsInfoList.size() == 0){
            return false;
        }

        // 顾客类型
        AppCustomerType  customerType = null;
        if (identityType.equals(AppIdentityType.CUSTOMER)){
            customerType = appCustomerService.findById(userId).getCustomerType();
        }

        Map<String,OrderGoodsInfo> orderGoodsInfoMap = new HashMap<>();
        for (OrderGoodsInfo goodsInfo : orderGoodsInfoList){
            orderGoodsInfoMap.put(goodsInfo.getSku(),goodsInfo);
        }

        Map<Long,PromotionSimpleInfo> promotionMap = new HashMap<>();
        List<Long> actIds = new ArrayList<>();
        for (PromotionSimpleInfo promotion : promotionSimpleInfoList){
                actIds.add(promotion.getPromotionId());
                promotionMap.put(promotion.getPromotionId(),promotion);
        }

        // 得到促销基础类集合
        List<ActBaseDO> actBaseList = actBaseDAO.queryByIdList(actIds);

        // 算满数量
        for (ActBaseDO act : actBaseList){
            // 前端返回的促销简单信息
            PromotionSimpleInfo promotionSimpleInfo = promotionMap.get(act.getId());
            // 参与此促销的次数
            Integer enjoyTimes = promotionSimpleInfo.getEnjoyTimes();
            // 创建一个新的明细集合
            List<OrderGoodsInfo> newOrderGoodsInfoList = new ArrayList<>();

            if (act.getConditionType().equals(ActConditionType.FQTY)){
               List<ActGoodsMappingDO> goodsMappingList = actGoodsMappingDAO.queryListByActId(act.getId());

               List<GoodsIdQtyParam> goodsIdQtyParams = promotionSimpleInfo.getPresentInfo();
               // 赠品
               if (goodsIdQtyParams != null && goodsIdQtyParams.size() > 0 ){
                   Set<Long> goodsIdSet = new HashSet<>();
                   for (GoodsIdQtyParam param : goodsIdQtyParams){
                       goodsIdSet.add(param.getId());
                   }

                   // 根据用户身份 返回有价格信息的赠品集合
                   List<OrderGoodsVO> giftGoodsVOList = goodsService.findOrderGoodsVOListByUserIdAndIdentityTypeAndGoodsIds(
                           userId, identityType.getValue(), goodsIdSet);

                   // 本品总价
                   Double goodsTotalPrice = 0.00;
                   // 赠品总价
                   Double giftTotalPrice = 0.00;

                   // 设置数量 新增赠品明细
                   for(OrderGoodsVO goods : giftGoodsVOList){
                        for (GoodsIdQtyParam param : goodsIdQtyParams){
                            if (goods.getGid().equals(param.getId())){
                                goods.setQty(param.getQty());
                                continue;
                            }
                        }

                       // 首先生成一条赠品明细行
                       OrderGoodsInfo giftDetailLine = this.createOneLine(orderGoodsInfoList.get(0),goods,act,AppGoodsLineType.PRESENT);
                       newOrderGoodsInfoList.add(giftDetailLine);
                       orderService.saveOrderGoodsInfo(giftDetailLine);

                       giftTotalPrice = CountUtil.add(this.returnCountPrice(giftDetailLine,identityType,customerType),giftTotalPrice);
                   }

                   // 新增一条参与此促销的本品行 并且更新老的行记录的数量
                   for (ActGoodsMappingDO goodsMapping : goodsMappingList){
                        // 参与促销的本品数量
                        Integer num = goodsMapping.getQty() == null ? 0 : goodsMapping.getQty()*enjoyTimes;

                        OrderGoodsInfo oldGoodsInfo = orderGoodsInfoMap.get(goodsMapping.getSku());
                        Integer oldNum = oldGoodsInfo.getOrderQuantity();
                        oldGoodsInfo.setOrderQuantity(oldNum - num*enjoyTimes);
                        orderGoodsInfoMap.put(goodsMapping.getSku(),oldGoodsInfo);

                        // 克隆一个本品对象
                        OrderGoodsInfo newGoodsInfo = oldGoodsInfo.clone();
                        newGoodsInfo.setId(null);
                        newGoodsInfo.setOrderQuantity(goodsMapping.getQty()*enjoyTimes);
                        newGoodsInfo.setPromotionId(act.getId().toString());

                        newOrderGoodsInfoList.add(newGoodsInfo);

                       goodsTotalPrice = CountUtil.add(this.returnCountPrice(newGoodsInfo,identityType,customerType),goodsTotalPrice);
                   }

                   // 计算分担金额 并持久化对象
                    this.countDutchPrice(newOrderGoodsInfoList,CountUtil.add(goodsTotalPrice,giftTotalPrice),giftTotalPrice,identityType,customerType);

               }else if (promotionSimpleInfo.getDiscount() != null && promotionSimpleInfo.getDiscount() > 0.00 && promotionSimpleInfo.getPresentInfo() == null){
                   // 立减优惠
                   Double subPrice = promotionSimpleInfo.getDiscount();
                   // 本品总价
                   Double goodsTotalPrice = 0.00;


                   // 新增一条参与此促销的本品行 并且更新老的行记录的数量
                   for (ActGoodsMappingDO goodsMapping : goodsMappingList){
                       // 参与促销的本品数量
                       Integer num = goodsMapping.getQty() == null ? 0 : goodsMapping.getQty()*enjoyTimes;

                       OrderGoodsInfo oldGoodsInfo = orderGoodsInfoMap.get(goodsMapping.getSku());
                       Integer oldNum = oldGoodsInfo.getOrderQuantity();
                       oldGoodsInfo.setOrderQuantity(oldNum - num);
                       orderGoodsInfoMap.put(goodsMapping.getSku(),oldGoodsInfo);

                       // 克隆一个本品对象
                       OrderGoodsInfo newGoodsInfo = oldGoodsInfo.clone();
                       newGoodsInfo.setId(null);
                       newGoodsInfo.setOrderQuantity(goodsMapping.getQty()*enjoyTimes);
                       newGoodsInfo.setPromotionId(act.getId().toString());

                       newOrderGoodsInfoList.add(newGoodsInfo);

                       goodsTotalPrice = CountUtil.add(this.returnCountPrice(newGoodsInfo,identityType,customerType),goodsTotalPrice);
                   }

                   // 计算分担金额 并持久化对象
                   this.countDutchPrice(newOrderGoodsInfoList,goodsTotalPrice,subPrice,identityType,customerType);

                   // 从促销List中移除
                   promotionMap.remove(act.getId());
               }else{
                    //throw Exception;
               }
            }
        }

        // 再分摊剩余的满金额促销
        for (ActBaseDO act : actBaseList){
            // 前端返回的促销简单信息
            PromotionSimpleInfo promotionSimpleInfo = promotionMap.get(act.getId());
            // 创建一个新的明细集合
            List<OrderGoodsInfo> newOrderGoodsInfoList = new ArrayList<>();

            if (act.getConditionType().equals(ActConditionType.FAMO)){
                List<String> skus = actGoodsMappingDAO.querySkusByActId(act.getId());

                // 本品总价
                Double goodsTotalPrice = 0.00;

                // 获取参与此促销的本品List
                Iterator<Map.Entry<String, OrderGoodsInfo>> it = orderGoodsInfoMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, OrderGoodsInfo> itEntry = it.next();
                    Object itKey = itEntry.getKey();
                    //注意：可以使用这种遍历方式进行删除元素和修改元素

                    OrderGoodsInfo info =  itEntry.getValue();
                    if (info.getOrderQuantity().equals(0)){
                        // 删除此条记录
                        orderDAO.deleteOrderGoodsInfo(info.getId());
                        it.remove();
                    }else if(skus.contains(info.getSku())){
                        newOrderGoodsInfoList.add(info);
                        goodsTotalPrice = CountUtil.add(this.returnCountPrice(info,identityType,customerType),goodsTotalPrice);
                    }
                }

                List<GoodsIdQtyParam> goodsIdQtyParams = promotionSimpleInfo.getPresentInfo();
                // 赠品
                if (goodsIdQtyParams != null && goodsIdQtyParams.size() > 0){
                    Set<Long> goodsIdSet = new HashSet<>();
                    for (GoodsIdQtyParam param : goodsIdQtyParams){
                        goodsIdSet.add(param.getId());
                    }

                    // 根据用户身份 返回有价格信息的赠品集合
                    List<OrderGoodsVO> giftGoodsVOList = goodsService.findOrderGoodsVOListByUserIdAndIdentityTypeAndGoodsIds(
                            userId, identityType.getValue(), goodsIdSet);


                    // 赠品总价
                    Double giftTotalPrice = 0.00;

                    // 设置数量 新增赠品明细
                    for(OrderGoodsVO goods : giftGoodsVOList){
                        for (GoodsIdQtyParam param : goodsIdQtyParams){
                            if (goods.getGid().equals(param.getId())){
                                goods.setQty(param.getQty());
                                continue;
                            }
                        }

                        // 首先生成一条赠品明细行
                        OrderGoodsInfo giftDetailLine = this.createOneLine(orderGoodsInfoList.get(0),goods,act,AppGoodsLineType.PRESENT);
                        newOrderGoodsInfoList.add(giftDetailLine);
                        orderService.saveOrderGoodsInfo(giftDetailLine);

                        giftTotalPrice = CountUtil.add(this.returnCountPrice(giftDetailLine,identityType,customerType),giftTotalPrice);
                    }

                    // 计算分担金额 并持久化对象
                    this.countDutchPrice(newOrderGoodsInfoList,CountUtil.add(goodsTotalPrice,giftTotalPrice),giftTotalPrice,identityType,customerType);

                }else if (promotionSimpleInfo.getDiscount() != null && promotionSimpleInfo.getDiscount() > 0.00){
                    // 立减优惠
                    Double subPrice = promotionSimpleInfo.getDiscount();

                    // 计算分担金额 并持久化对象
                    this.countDutchPrice(newOrderGoodsInfoList,goodsTotalPrice,subPrice,identityType,customerType);

                    // 从促销List中移除
                    promotionMap.remove(act.getId());
                }else{
                    //throw Exception;
                }
            }
        }

        Iterator<Map.Entry<String, OrderGoodsInfo>> it = orderGoodsInfoMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, OrderGoodsInfo> itEntry = it.next();
            Object itKey = itEntry.getKey();
            //注意：可以使用这种遍历方式进行删除元素和修改元素

            OrderGoodsInfo info =  itEntry.getValue();
            if (info.getOrderQuantity().equals(0)){
                it.remove();
                // 删除此条记录
                orderDAO.deleteOrderGoodsInfo(info.getId());
            }else{
                // 更新此条记录
                orderDAO.updateOrderGoodsInfo(info);
            }
        }

        return true;
    }

    /**
     * 计算分摊金额
     * @param orderGoodsInfos
     * @param totalPrice
     * @param subPrice
     * @return
     */
    private List<OrderGoodsInfo> countDutchPrice(List<OrderGoodsInfo> orderGoodsInfos , Double totalPrice , Double subPrice,AppIdentityType type, AppCustomerType customerType){

        for (OrderGoodsInfo info : orderGoodsInfos){
            Double price = this.returnCountPrice(info,type,customerType);
            Double dutchPrice = CountUtil.mul(CountUtil.div(price,totalPrice),subPrice);
            info.setIsPriceShare(true);
            info.setSharePrice(dutchPrice);
            info.setReturnPrice(CountUtil.sub(price,dutchPrice));

            if(info.getId() == null){
                orderDAO.saveOrderGoodsInfo(info);
            }else{
                orderDAO.updateOrderGoodsInfo(info);
            }
        }

        return orderGoodsInfos;
    }

    // 返回结算价格
    private Double returnCountPrice(OrderGoodsInfo info, AppIdentityType type, AppCustomerType customerType){
        Double price = 0.00;
        if (type.equals(AppIdentityType.SELLER) || type.equals(AppIdentityType.DECORATE_MANAGER)){
            // 导购或是装饰公司都是会员价
            price = info.getVIPPrice();
        }else if (type.equals(AppIdentityType.CUSTOMER)){
            if (customerType.equals(AppCustomerType.MEMBER)){
                price = info.getVIPPrice();
            }else{
                price = info.getRetailPrice();
            }
        }
        return CountUtil.mul(price,info.getOrderQuantity());
    }

    private OrderGoodsInfo createOneLine(OrderGoodsInfo info ,OrderGoodsVO goods,ActBaseDO act,AppGoodsLineType lineType){
        OrderGoodsInfo result = new OrderGoodsInfo();

        result.setOid(info.getOid());
        result.setOrderNumber(info.getOrderNumber());
        result.setSku(goods.getSku());
        result.setSkuName(goods.getSkuName());
        result.setRetailPrice(goods.getRetailPrice());
        result.setVIPPrice(goods.getVipPrice());
        result.setWholesalePrice(goods.getWholesalePrice());
        result.setPromotionId(act.getId().toString());
        result.setIsReturnable(act.getIsReturnable());
        result.setOrderQuantity(goods.getQty());
        result.setGoodsLineType(lineType);
        result.setPriceItemId(goods.getPrice_item_id());
        result.setCompanyFlag(goods.getCompanyFlag());

        return result;
    }
}
