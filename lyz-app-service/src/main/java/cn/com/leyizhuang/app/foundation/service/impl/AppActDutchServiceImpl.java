package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.DutchException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by panjie on 2017/12/11.
 */
@Service
public class AppActDutchServiceImpl implements AppActDutchService {

    private static final Logger logger = LoggerFactory.getLogger(AppActDutchServiceImpl.class);

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

    @Autowired
    private StatisticsSellDetailsService statisticsSellDetailsService;

    /**
     * 新增明细 分摊金额
     *
     * @param userId
     * @param identityType
     * @param promotionSimpleInfoList
     * @param orderGoodsInfoList
     * @return
     */
    public List<OrderGoodsInfo> addGoodsDetailsAndDutch(Long userId, AppIdentityType identityType, List<PromotionSimpleInfo> promotionSimpleInfoList, List<OrderGoodsInfo> orderGoodsInfoList,Long cusId) throws UnsupportedEncodingException {

        // 最终商品明细集合
        List<OrderGoodsInfo> finallyOrderGoodsInfo = new ArrayList<>();

        // 无促销 不用分摊
        if (promotionSimpleInfoList == null || promotionSimpleInfoList.size() == 0) {
            return orderGoodsInfoList;
        }

        // 没有本品明细 不用分摊
        if (orderGoodsInfoList == null || orderGoodsInfoList.size() == 0) {
            return null;
        }

        // 顾客类型
        AppCustomerType customerType = null;
        if (identityType.equals(AppIdentityType.CUSTOMER)) {
            customerType = appCustomerService.findById(userId).getCustomerType();
        }

        // 本品池
        Map<String, OrderGoodsInfo> orderGoodsInfoMap = new HashMap<>();
        for (OrderGoodsInfo goodsInfo : orderGoodsInfoList) {
            orderGoodsInfoMap.put(goodsInfo.getSku(), goodsInfo);
        }

        Map<Long, PromotionSimpleInfo> promotionMap = new HashMap<>();
        List<Long> actIds = new ArrayList<>();
        for (PromotionSimpleInfo promotion : promotionSimpleInfoList) {
            actIds.add(promotion.getPromotionId());
            promotionMap.put(promotion.getPromotionId(), promotion);
        }

        // 得到促销基础类集合
        List<ActBaseDO> actBaseList = actBaseDAO.queryByIdList(actIds);

        List<ActBaseDO> actBaseListSort = new ArrayList<>();
        /** 排序将专供促销放到前面 优先分摊 否则会分摊出错**/
        for (int i = actBaseList.size()-1 ; i > 0  ; i--){
            ActBaseDO act = actBaseList.get(i);
            if (act.getBaseType().equals(ActBaseType.ACCUMULATE) || act.getBaseType().equals(ActBaseType.ZGFRIST)){
                actBaseListSort.add(act);
                actBaseList.remove(i);
            }
        }
        actBaseListSort.addAll(actBaseList);

        // 算满数量
        for (ActBaseDO act : actBaseListSort) {
            // 前端返回的促销简单信息
            PromotionSimpleInfo promotionSimpleInfo = promotionMap.get(act.getId());
            // 参与此促销的次数
            Integer enjoyTimes = promotionSimpleInfo.getEnjoyTimes();
            // 创建一个新的明细集合
            List<OrderGoodsInfo> newOrderGoodsInfoList = new ArrayList<>();


            /** 分摊专供促销 优先分摊专供促销**/
            if (act.getBaseType().equals(ActBaseType.ZGFRIST) && act.getConditionType().equals(ActConditionType.FQTY)) {
                // 首单促销

                // 本品总价
                Double goodsTotalPrice = 0.00;
                // 赠品总价
                Double giftTotalPrice = 0.00;

                // 促销条件数量
                Integer fullQty = act.getFullNumber();
                // 赠送数量
                Integer sentQty = act.getGiftChooseNumber();

                // 赠品 一般赠品只有一种
                List<GoodsIdQtyParam> giftList = promotionSimpleInfo.getPresentInfo();

                if (giftList != null && giftList.size() > 0){
                    Set<Long> giftIdsList = new HashSet<>();
                    for (GoodsIdQtyParam param : giftList){
                        giftIdsList.add(param.getId());
                    }

                    // 根据用户身份 返回有价格信息的赠品集合
                    List<OrderGoodsVO> giftGoodsVOList = goodsService.findOrderGoodsVOListByUserIdAndIdentityTypeAndGoodsIds(
                            userId, identityType.getValue(), giftIdsList);

                    // 设置数量 新增赠品明细
                    for (OrderGoodsVO goods : giftGoodsVOList) {
                        for (GoodsIdQtyParam param : giftList) {
                            if (goods.getGid().equals(param.getId())) {
                                goods.setQty(param.getQty());
                                break;
                            }
                        }

                        // 首先生成一条赠品明细行
                        OrderGoodsInfo giftDetailLine = this.createOneLine(orderGoodsInfoList.get(0), goods, act, AppGoodsLineType.PRESENT);
                        newOrderGoodsInfoList.add(giftDetailLine);
                        //orderService.saveOrderGoodsInfo(giftDetailLine);

                        giftTotalPrice = CountUtil.add(this.returnCountPrice(giftDetailLine, identityType, customerType), giftTotalPrice);

                        //扣除本品
                        OrderGoodsInfo orderGoodsInfo = orderGoodsInfoMap.get(goods.getSku());

                        if (orderGoodsInfo == null){
                            //  抛异常
                            throw new DutchException("分摊异常，找不到本品");
                        }else {
                            Integer remainQty = orderGoodsInfo.getOrderQuantity() - fullQty;

                            // 克隆一个本品对象
                            OrderGoodsInfo newGoodsInfo = orderGoodsInfo.clone();
                            newGoodsInfo.setId(null);
                            newGoodsInfo.setOrderQuantity(fullQty);
                            newGoodsInfo.setReturnableQuantity(fullQty);
                            newGoodsInfo.setPromotionId(act.getId().toString());
                            newOrderGoodsInfoList.add(newGoodsInfo);

                            goodsTotalPrice = CountUtil.add(CountUtil.mul(orderGoodsInfo.getSettlementPrice(),fullQty),goodsTotalPrice);

                            if (remainQty == 0){
                                orderGoodsInfoMap.remove(goods.getSku());
                            }else if (remainQty > 0){
                                orderGoodsInfo.setOrderQuantity(remainQty);
                                orderGoodsInfo.setReturnableQuantity(remainQty);
                                orderGoodsInfoMap.put(goods.getSku(),orderGoodsInfo);
                            }
                        }

                        /** 记录该顾客参与首单促销参与次数 **/
                        statisticsSellDetailsService.addOrUpdateSellZgCusTimes(userId,goods.getSku(),1,orderGoodsInfo.getOrderQuantity(),ActBaseType.ZGFRIST);
                    }

                    //计算分摊
                    finallyOrderGoodsInfo.addAll(this.countDutchPrice(newOrderGoodsInfoList, CountUtil.add(goodsTotalPrice, giftTotalPrice), giftTotalPrice, identityType, customerType));

                }

            }

            if (act.getBaseType().equals(ActBaseType.ACCUMULATE) && act.getConditionType().equals(ActConditionType.FQTY)) {
                // 累计促销

                // 本品总价
                Double goodsTotalPrice = 0.00;
                // 赠品总价
                Double giftTotalPrice = 0.00;

                // 促销条件数量
                Integer fullQty = act.getFullNumber();
                // 赠送数量
                Integer sentQty = act.getGiftChooseNumber();

                // 赠品 一般赠品只有一种
                List<GoodsIdQtyParam> giftList = promotionSimpleInfo.getPresentInfo();

                if (giftList != null && giftList.size() > 0){
                    Set<Long> giftIdsList = new HashSet<>();
                    for (GoodsIdQtyParam param : giftList){
                        giftIdsList.add(param.getId());
                    }

                    // 根据用户身份 返回有价格信息的赠品集合
                    List<OrderGoodsVO> giftGoodsVOList = goodsService.findOrderGoodsVOListByUserIdAndIdentityTypeAndGoodsIds(
                            userId, identityType.getValue(), giftIdsList);

                    // 设置数量 新增赠品明细
                    for (OrderGoodsVO goods : giftGoodsVOList) {
                        for (GoodsIdQtyParam param : giftList) {
                            if (goods.getGid().equals(param.getId())) {
                                goods.setQty(param.getQty());
                                break;
                            }
                        }

                        // 首先生成一条赠品明细行
                        OrderGoodsInfo giftDetailLine = this.createOneLine(orderGoodsInfoList.get(0), goods, act, AppGoodsLineType.PRESENT);
                        newOrderGoodsInfoList.add(giftDetailLine);
                        //orderService.saveOrderGoodsInfo(giftDetailLine);

                        giftTotalPrice = CountUtil.add(this.returnCountPrice(giftDetailLine, identityType, customerType), giftTotalPrice);

                        //扣除本品
                        OrderGoodsInfo orderGoodsInfo = orderGoodsInfoMap.get(goods.getSku());

                        if (orderGoodsInfo == null){
                            //  抛异常
                            throw new DutchException("分摊异常，找不到本品");
                        }else {
                            // 得到专供产品的桶数
                            Integer saledQty = statisticsSellDetailsService.getZgTsBycusIdAndsku(cusId,orderGoodsInfo.getSku());

                            Integer sumQty = saledQty + orderGoodsInfo.getOrderQuantity();
                            // 向下取整 得出参与次数
                            int zgEnjoyTimes = (int) Math.floor(sumQty / fullQty);

                            if (zgEnjoyTimes > 0){
                                Integer remainQty = sumQty % fullQty;

                                // 克隆一个本品对象
                                OrderGoodsInfo newGoodsInfo = orderGoodsInfo.clone();
                                newGoodsInfo.setId(null);
                                newGoodsInfo.setOrderQuantity(orderGoodsInfo.getOrderQuantity()-remainQty);
                                newGoodsInfo.setReturnableQuantity(orderGoodsInfo.getOrderQuantity()-remainQty);
                                newGoodsInfo.setPromotionId(act.getId().toString());
                                newOrderGoodsInfoList.add(newGoodsInfo);

                                goodsTotalPrice = CountUtil.add(CountUtil.mul(orderGoodsInfo.getSettlementPrice(),orderGoodsInfo.getOrderQuantity()-remainQty),goodsTotalPrice);

                                if (remainQty == 0){
                                    orderGoodsInfoMap.remove(goods.getSku());
                                }else if (remainQty > 0){
                                    orderGoodsInfo.setOrderQuantity(remainQty);
                                    orderGoodsInfo.setReturnableQuantity(remainQty);
                                    orderGoodsInfoMap.put(goods.getSku(),orderGoodsInfo);
                                }

                            }
                        }

                        /** 记录该顾客参累积促销参与次数 **/
                        statisticsSellDetailsService.addOrUpdateSellZgCusTimes(userId,goods.getSku(),1,orderGoodsInfo.getOrderQuantity(),ActBaseType.ACCUMULATE);
                    }

                    //计算分摊
                    finallyOrderGoodsInfo.addAll(this.countDutchPrice(newOrderGoodsInfoList, CountUtil.add(goodsTotalPrice, giftTotalPrice), CountUtil.sub(giftTotalPrice), identityType, customerType));

                }
            }

            if (act.getBaseType().equals(ActBaseType.COMMON) && act.getConditionType().equals(ActConditionType.FQTY)) {
                List<ActGoodsMappingDO> goodsMappingList = actGoodsMappingDAO.queryListByActId(act.getId());

                List<GoodsIdQtyParam> goodsIdQtyParams = promotionSimpleInfo.getPresentInfo();
                // 赠品
                if (goodsIdQtyParams != null && goodsIdQtyParams.size() > 0) {
                    Set<Long> goodsIdSet = new HashSet<>();
                    // 用户选择赠品的数量
                    Integer giftNum = 0;

                    for (GoodsIdQtyParam param : goodsIdQtyParams) {
                        goodsIdSet.add(param.getId());
                        giftNum += param.getQty();
                    }

                    // 根据用户身份 返回有价格信息的赠品集合
                    List<OrderGoodsVO> giftGoodsVOList = goodsService.findOrderGoodsVOListByUserIdAndIdentityTypeAndGoodsIds(
                            userId, identityType.getValue(), goodsIdSet);

                    /** 根据用户所选赠品数量计算参与促销次数*/

                    // 单个促销最大选择赠品数量
                    Integer giftMaxChooseNum = act.getGiftChooseNumber();

                    if(giftNum == 0){
                        enjoyTimes = 0;
                    }else{
                        // 向上取整 得出用户参加次数
                        enjoyTimes = (int) Math.ceil(Double.valueOf(giftNum)/Double.valueOf(giftMaxChooseNum));
                    }

                    // 本品总价
                    Double goodsTotalPrice = 0.00;
                    // 赠品总价
                    Double giftTotalPrice = 0.00;
                    // 加价金额
                    Double addAmount = 0.00;
                    if (act.getActType().contains("ADD")){

                        addAmount = CountUtil.mul(act.getAddAmount(),enjoyTimes);
                    }

                    // 设置数量 新增赠品明细
                    for (OrderGoodsVO goods : giftGoodsVOList) {
                        for (GoodsIdQtyParam param : goodsIdQtyParams) {
                            if (goods.getGid().equals(param.getId())) {
                                goods.setQty(param.getQty());
                                break;
                            }
                        }

                        // 首先生成一条赠品明细行
                        OrderGoodsInfo giftDetailLine = this.createOneLine(orderGoodsInfoList.get(0), goods, act, AppGoodsLineType.PRESENT);
                        newOrderGoodsInfoList.add(giftDetailLine);
                        //orderService.saveOrderGoodsInfo(giftDetailLine);

                        giftTotalPrice = CountUtil.add(this.returnCountPrice(giftDetailLine, identityType, customerType), giftTotalPrice);
                    }

                    if (act.getIsGoodsOptionalQty()){
                        /** 本品为数量任选（非固定数量） 则将促销赠分摊到所有满足本品上，并且从本品池中扣除 （原则上此促销排序在固定数量促销之后）**/

                        List<String> skuList = actGoodsMappingDAO.querySkusByActId(act.getId());

                        Iterator<Map.Entry<String, OrderGoodsInfo>> it = orderGoodsInfoMap.entrySet().iterator();

                        while (it.hasNext()) {
                            Map.Entry<String, OrderGoodsInfo> itEntry = it.next();
                            Object itKey = itEntry.getKey();
                            //注意：可以使用这种遍历方式进行删除元素和修改元素

                            // 此商品在促销范围内
                            if (skuList.contains(itKey.toString()) && enjoyTimes > 0) {
                                // 顾客购买此商品数量
                                OrderGoodsInfo goods = itEntry.getValue();
                                Integer sellQty = goods.getOrderQuantity();

                                if (sellQty >= act.getFullNumber()){
                                    // 满足促销数量条件

                                    // 计算单品参与次数 向下取整
                                    Integer singleEnjoyTimes = (int) Math.floor(Double.valueOf(sellQty)/Double.valueOf(act.getFullNumber()));

                                    if(singleEnjoyTimes >= enjoyTimes){
                                        singleEnjoyTimes = enjoyTimes;
                                    }

                                    // 消耗单品数量
                                    Integer singleQty = act.getFullNumber()*singleEnjoyTimes;

                                    // 克隆一个本品对象
                                    OrderGoodsInfo newGoodsInfo = goods.clone();
                                    newGoodsInfo.setId(null);
                                    newGoodsInfo.setOrderQuantity(singleQty);
                                    newGoodsInfo.setReturnableQuantity(singleQty);
                                    newGoodsInfo.setPromotionId(act.getId().toString());
                                    newOrderGoodsInfoList.add(newGoodsInfo);

                                    //goodsTotalPrice = CountUtil.add(CountUtil.mul(this.returnCountPrice(goods, identityType, customerType) ,singleQty), goodsTotalPrice);
                                    goodsTotalPrice = CountUtil.add(CountUtil.mul(goods.getSettlementPrice() ,singleQty), goodsTotalPrice);

                                    OrderGoodsInfo oldGoodsInfo = orderGoodsInfoMap.get(goods.getSku());
                                    Integer oldNum = oldGoodsInfo.getOrderQuantity();
                                    oldGoodsInfo.setOrderQuantity(oldNum - singleQty);
                                    oldGoodsInfo.setReturnableQuantity(oldNum - singleQty);
                                    orderGoodsInfoMap.put(goods.getSku(), oldGoodsInfo);

                                    enjoyTimes = enjoyTimes - singleEnjoyTimes;
                                }

                                // 从本品池中扣除
                                //it.remove();
                            }
                        }

                    }else{
                        /** 固定数量 **/
                        // 新增一条参与此促销的本品行 并且更新老的行记录的数量
                        for (ActGoodsMappingDO goodsMapping : goodsMappingList) {
                            // 参与促销的本品数量
                            Integer num = goodsMapping.getQty() == null ? 0 : goodsMapping.getQty() * enjoyTimes;

                            OrderGoodsInfo oldGoodsInfo = orderGoodsInfoMap.get(goodsMapping.getSku());
                            Integer oldNum = oldGoodsInfo.getOrderQuantity();
                            oldGoodsInfo.setOrderQuantity(oldNum - num);
                            oldGoodsInfo.setReturnableQuantity(oldNum - num);
                            orderGoodsInfoMap.put(goodsMapping.getSku(), oldGoodsInfo);

                            // 克隆一个本品对象
                            OrderGoodsInfo newGoodsInfo = oldGoodsInfo.clone();
                            newGoodsInfo.setId(null);
                            newGoodsInfo.setOrderQuantity(num);
                            newGoodsInfo.setReturnableQuantity(num);
                            newGoodsInfo.setPromotionId(act.getId().toString());

                            newOrderGoodsInfoList.add(newGoodsInfo);

                            goodsTotalPrice = CountUtil.add(this.returnCountPrice(newGoodsInfo, identityType, customerType), goodsTotalPrice);
                        }
                    }

                    // 计算分担金额 并持久化对象
                    finallyOrderGoodsInfo.addAll(this.countDutchPrice(newOrderGoodsInfoList, CountUtil.add(goodsTotalPrice, giftTotalPrice), CountUtil.sub(giftTotalPrice,addAmount), identityType, customerType));

                } else if (promotionSimpleInfo.getDiscount() != null && promotionSimpleInfo.getDiscount() > 0.00) {
                    // 立减优惠
                    Double subPrice = promotionSimpleInfo.getDiscount();
                    // 本品总价
                    Double goodsTotalPrice = 0.00;

                    if (act.getIsGoodsOptionalQty()){
                        /** 本品为数量任选（非固定数量） 则将促销分摊到所有满足本品上，并且从本品池中扣除 （原则上此促销排序在固定数量促销之后）**/

                        List<String> skuList = actGoodsMappingDAO.querySkusByActId(act.getId());

                        Iterator<Map.Entry<String, OrderGoodsInfo>> it = orderGoodsInfoMap.entrySet().iterator();

//                        while (it.hasNext()) {
//                            Map.Entry<String, OrderGoodsInfo> itEntry = it.next();
//                            Object itKey = itEntry.getKey();
//                            //注意：可以使用这种遍历方式进行删除元素和修改元素
//
//                            // 此商品在促销范围内
//                            if (skuList.contains(itKey.toString())) {
//                                // 顾客购买此商品数量
//                                OrderGoodsInfo goods = itEntry.getValue();
//                                goods.setPromotionId(act.getId().toString());
//                                newOrderGoodsInfoList.add(goods);
//                                goodsTotalPrice = CountUtil.add(this.returnCountPrice(goods, identityType, customerType), goodsTotalPrice);
//
//                                // 从本品池中扣除
//                                it.remove();
//                            }
//                        }

                        while (it.hasNext()) {
                            Map.Entry<String, OrderGoodsInfo> itEntry = it.next();
                            Object itKey = itEntry.getKey();
                            //注意：可以使用这种遍历方式进行删除元素和修改元素

                            // 此商品在促销范围内
                            if (skuList.contains(itKey.toString()) && enjoyTimes > 0) {
                                // 顾客购买此商品数量
                                OrderGoodsInfo goods = itEntry.getValue();
                                Integer sellQty = goods.getOrderQuantity();

                                if (sellQty > act.getFullNumber()){
                                    // 满足促销数量条件

                                    // 计算单品参与次数 向下取整
                                    Integer singleEnjoyTimes = (int) Math.floor(Double.valueOf(sellQty)/Double.valueOf(act.getFullNumber()));;

                                    // 消耗单品数量
                                    Integer singleQty = act.getFullNumber()*singleEnjoyTimes;

                                    // 克隆一个本品对象
                                    OrderGoodsInfo newGoodsInfo = goods.clone();
                                    newGoodsInfo.setId(null);
                                    newGoodsInfo.setOrderQuantity(singleQty);
                                    newGoodsInfo.setReturnableQuantity(singleQty);
                                    newGoodsInfo.setPromotionId(act.getId().toString());
                                    newOrderGoodsInfoList.add(newGoodsInfo);

                                    OrderGoodsInfo oldGoodsInfo = orderGoodsInfoMap.get(goods.getSku());
                                    Integer oldNum = oldGoodsInfo.getOrderQuantity();
                                    oldGoodsInfo.setOrderQuantity(oldNum - singleQty);
                                    oldGoodsInfo.setReturnableQuantity(oldNum - singleQty);
                                    orderGoodsInfoMap.put(goods.getSku(), oldGoodsInfo);

                                    //goodsTotalPrice = CountUtil.add(CountUtil.mul(this.returnCountPrice(goods, identityType, customerType) ,singleQty), goodsTotalPrice);
                                    goodsTotalPrice = CountUtil.add(CountUtil.mul(goods.getSettlementPrice() ,singleQty), goodsTotalPrice);

                                    enjoyTimes = enjoyTimes - singleEnjoyTimes;
                                }

                                // 从本品池中扣除
                                //it.remove();
                            }
                        }

                    }else{
                        /** 固定数量 **/
                        // 新增一条参与此促销的本品行 并且更新老的行记录的数量
                        for (ActGoodsMappingDO goodsMapping : goodsMappingList) {
                            // 参与促销的本品数量
                            Integer num = goodsMapping.getQty() == null ? 0 : goodsMapping.getQty() * enjoyTimes;

                            OrderGoodsInfo oldGoodsInfo = orderGoodsInfoMap.get(goodsMapping.getSku());
                            Integer oldNum = oldGoodsInfo.getOrderQuantity();
                            oldGoodsInfo.setOrderQuantity(oldNum - num);
                            oldGoodsInfo.setReturnableQuantity(oldNum - num);
                            orderGoodsInfoMap.put(goodsMapping.getSku(), oldGoodsInfo);

                            // 克隆一个本品对象
                            OrderGoodsInfo newGoodsInfo = oldGoodsInfo.clone();
                            newGoodsInfo.setId(null);
                            newGoodsInfo.setOrderQuantity(goodsMapping.getQty() * enjoyTimes);
                            newGoodsInfo.setReturnableQuantity(goodsMapping.getQty() * enjoyTimes);
                            newGoodsInfo.setPromotionId(act.getId().toString());

                            newOrderGoodsInfoList.add(newGoodsInfo);

                            goodsTotalPrice = CountUtil.add(this.returnCountPrice(newGoodsInfo, identityType, customerType), goodsTotalPrice);
                        }
                    }

                    // 计算分担金额 并持久化对象
                    finallyOrderGoodsInfo.addAll(this.countDutchPrice(newOrderGoodsInfoList, goodsTotalPrice, subPrice, identityType, customerType));

                    // 从促销List中移除
                    promotionMap.remove(act.getId());
                } else {
                    //throw Exception;
                   throw new DutchException("分摊异常");
                }
            }
        }

        // 再分摊剩余的满金额促销
        for (ActBaseDO act : actBaseList) {
            // 前端返回的促销简单信息
            PromotionSimpleInfo promotionSimpleInfo = promotionMap.get(act.getId());
            // 创建一个新的明细集合
            List<OrderGoodsInfo> newOrderGoodsInfoList = new ArrayList<>();

            if (act.getBaseType().equals(ActBaseType.COMMON) && act.getConditionType().equals(ActConditionType.FAMO)) {
                List<String> skus = actGoodsMappingDAO.querySkusByActId(act.getId());

                // 本品总价
                Double goodsTotalPrice = 0.00;

                // 获取参与此促销的本品List
                Iterator<Map.Entry<String, OrderGoodsInfo>> it = orderGoodsInfoMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, OrderGoodsInfo> itEntry = it.next();
                    //注意：可以使用这种遍历方式进行删除元素和修改元素

                    OrderGoodsInfo info = itEntry.getValue();
                    if (info.getOrderQuantity().equals(0)) {
                        // 删除此条记录
                        //orderDAO.deleteOrderGoodsInfo(info.getId());
                        it.remove();
                    } else if (skus.contains(info.getSku())) {
                        info.setPromotionId(act.getId().toString());
                        newOrderGoodsInfoList.add(info);
                        goodsTotalPrice = CountUtil.add(this.returnCountPrice(info, identityType, customerType), goodsTotalPrice);
                        it.remove();
                    }
                }

                List<GoodsIdQtyParam> goodsIdQtyParams = promotionSimpleInfo.getPresentInfo();
                // 赠品
                if (goodsIdQtyParams != null && goodsIdQtyParams.size() > 0) {
                    Set<Long> goodsIdSet = new HashSet<>();
                    // 用户选择赠品的数量
                    Integer giftNum = 0;
                    for (GoodsIdQtyParam param : goodsIdQtyParams) {
                        goodsIdSet.add(param.getId());
                        giftNum += param.getQty();
                    }

                    // 根据用户身份 返回有价格信息的赠品集合
                    List<OrderGoodsVO> giftGoodsVOList = goodsService.findOrderGoodsVOListByUserIdAndIdentityTypeAndGoodsIds(
                            userId, identityType.getValue(), goodsIdSet);

                    // 赠品总价
                    Double giftTotalPrice = 0.00;

                    // 加价金额
                    Double addAmount = 0.00;
                    if (act.getActType().contains("ADD")){
                        // 单个促销最大选择赠品数量
                        Integer giftMaxChooseNum = act.getGiftChooseNumber();
                        Double addSaleTimes;
                        if(giftNum == 0){
                            addSaleTimes = 0.00;
                        }else{
                            // 向上取整 得出用户加价次数
                            addSaleTimes = Math.ceil(Double.valueOf(giftNum)/Double.valueOf(giftMaxChooseNum));
                        }

                        addAmount = CountUtil.mul(act.getAddAmount(),addSaleTimes);
                    }

                    // 设置数量 新增赠品明细
                    for (OrderGoodsVO goods : giftGoodsVOList) {
                        for (GoodsIdQtyParam param : goodsIdQtyParams) {
                            if (goods.getGid().equals(param.getId())) {
                                goods.setQty(param.getQty());
                                break;
                            }
                        }

                        // 首先生成一条赠品明细行
                        OrderGoodsInfo giftDetailLine = this.createOneLine(orderGoodsInfoList.get(0), goods, act, AppGoodsLineType.PRESENT);
                        newOrderGoodsInfoList.add(giftDetailLine);
                        //orderService.saveOrderGoodsInfo(giftDetailLine);

                        giftTotalPrice = CountUtil.add(this.returnCountPrice(giftDetailLine, identityType, customerType), giftTotalPrice);
                    }

                    // 计算分担金额 并持久化对象
                    this.countDutchPrice(newOrderGoodsInfoList, CountUtil.add(goodsTotalPrice, giftTotalPrice), CountUtil.sub(giftTotalPrice,addAmount), identityType, customerType);

                } else if (promotionSimpleInfo.getDiscount() != null && promotionSimpleInfo.getDiscount() > 0.00) {
                    // 立减优惠
                    Double subPrice = promotionSimpleInfo.getDiscount();

                    // 计算分担金额 并持久化对象
                    this.countDutchPrice(newOrderGoodsInfoList, goodsTotalPrice, subPrice, identityType, customerType);

                    // 从促销List中移除
                    promotionMap.remove(act.getId());
                } else {
                    //throw Exception;
                    throw new DutchException("分摊出错");
                }

                finallyOrderGoodsInfo.addAll(newOrderGoodsInfoList);
            }
        }

        Iterator<Map.Entry<String, OrderGoodsInfo>> it = orderGoodsInfoMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, OrderGoodsInfo> itEntry = it.next();

            //注意：可以使用这种遍历方式进行删除元素和修改元素

            OrderGoodsInfo info = itEntry.getValue();
            if (info.getOrderQuantity().equals(0)) {
                it.remove();
                // 删除此条记录
                //orderDAO.deleteOrderGoodsInfo(info.getId());
            } else {
                // 更新此条记录
                //orderDAO.updateOrderGoodsInfo(info);

                // 将没有参与促销的商品装入
                finallyOrderGoodsInfo.add(info);
            }
        }

        return finallyOrderGoodsInfo;
    }

    /**
     * 计算分摊金额
     *
     * @param orderGoodsInfos 需要参与分摊的商品集合
     * @param totalPrice 总价
     * @param subPrice 分摊金额
     * @return 分摊后的结果
     */
    private List<OrderGoodsInfo> countDutchPrice(List<OrderGoodsInfo> orderGoodsInfos, Double totalPrice, Double subPrice, AppIdentityType type, AppCustomerType customerType) {
        // 记录已经分摊的金额  倒挤算法
        Double dutchedPrice = 0.00;
        for (int i =0 ;i<orderGoodsInfos.size();i++) {
            OrderGoodsInfo info = orderGoodsInfos.get(i);

            if(i < (orderGoodsInfos.size() - 1)){
                Double price = info.getSettlementPrice();
                //四舍五入
                Double dutchPrice = CountUtil.HALF_UP_SCALE_2((price / totalPrice) * subPrice);

                info.setIsPriceShare(true);
                info.setPromotionSharePrice(dutchPrice);
                dutchedPrice += CountUtil.mul(dutchPrice , info.getOrderQuantity());
            }else{
                Double dutchPrice =  subPrice - dutchedPrice;
                info.setIsPriceShare(true);
                info.setPromotionSharePrice(CountUtil.div(dutchPrice,info.getOrderQuantity()));
            }

        }

        return orderGoodsInfos;
    }

    // 返回结算价格
    private Double returnCountPrice(OrderGoodsInfo info, AppIdentityType type, AppCustomerType customerType) {
        Double price = 0.00;
        if (type.equals(AppIdentityType.SELLER) || type.equals(AppIdentityType.DECORATE_MANAGER)) {
            // 导购或是装饰公司都是会员价
            price = info.getVIPPrice();
        } else if (type.equals(AppIdentityType.CUSTOMER)) {
            if (customerType.equals(AppCustomerType.MEMBER)) {
                price = info.getVIPPrice();
            } else {
                price = info.getRetailPrice();
            }
        }
        return CountUtil.mul(price, info.getOrderQuantity());
    }

    private OrderGoodsInfo createOneLine(OrderGoodsInfo info, OrderGoodsVO goods, ActBaseDO act, AppGoodsLineType lineType) {
        OrderGoodsInfo result = new OrderGoodsInfo();

        result.setOid(info.getOid());
        result.setOrderNumber(info.getOrderNumber());
        result.setGid(goods.getGid());
        result.setSku(goods.getSku());
        result.setSkuName(goods.getSkuName());
        result.setRetailPrice(goods.getRetailPrice());
        result.setVIPPrice(goods.getVipPrice());
        result.setWholesalePrice(goods.getWholesalePrice());
        result.setPromotionId(act.getId().toString());
        result.setIsReturnable(act.getIsReturnable());
        result.setOrderQuantity(goods.getQty());
        result.setGoodsLineType(lineType);
        result.setPriceItemId(goods.getPriceItemId());
        result.setCompanyFlag(goods.getCompanyFlag());
        result.setSettlementPrice(info.getSettlementPrice());
        result.setLbSharePrice(0D);
        result.setCashCouponSharePrice(0D);
        result.setCashReturnSharePrice(0D);
        if (lineType.equals(AppGoodsLineType.PRESENT)){
            result.setReturnPriority(2);
        }else {
            result.setReturnPriority(1);
        }
        result.setShippingQuantity(0);
        result.setReturnQuantity(0);
        result.setReturnableQuantity(goods.getQty());
        return result;
    }

    /**
     * 所有分摊完毕后计算退款单价 旧
     * @return
     */
    public List<OrderGoodsInfo> countReturnPrice(List<OrderGoodsInfo> goodsInfoList){

        Double totalDutchPrice = 0.00;
        Double totalReturnPrice = 0.00;
        Double totalPrice = 0.00;


        for (OrderGoodsInfo goodsInfo : goodsInfoList){
            // 促销分摊金额
            Double promotionPrice = goodsInfo.getPromotionSharePrice() == null ? 0.00 : goodsInfo.getPromotionSharePrice();
            // 乐币分摊金额
            Double lbPrice = goodsInfo.getLbSharePrice() == null ? 0.00 : goodsInfo.getLbSharePrice();
            // 现金券分摊金额
            Double cashCouponPrice = goodsInfo.getCashCouponSharePrice() == null ? 0.00 : goodsInfo.getCashCouponSharePrice();
            // 现金返利分摊金额
            Double cashReturnPrice = goodsInfo.getCashReturnSharePrice() == null ? 0.00 : goodsInfo.getCashReturnSharePrice();

            // 商品单价
            Double price = goodsInfo.getSettlementPrice() == null ? 0.00 : goodsInfo.getSettlementPrice();

            if (price.equals(0.00)){
                goodsInfo.setReturnPrice(0.00);
            }else {
                Double returnPrice = CountUtil.sub(price , promotionPrice , lbPrice , cashCouponPrice , cashReturnPrice);
                //goodsInfo.setReturnPrice(returnPrice);
                // 如果推货价出现负数 则设置为0
                if ( returnPrice >= -0.02 && returnPrice < 0){
                    goodsInfo.setReturnPrice(0.00);
                }else{
                    goodsInfo.setReturnPrice(returnPrice);
                }
                totalReturnPrice = CountUtil.add(totalReturnPrice,CountUtil.mul(returnPrice , goodsInfo.getOrderQuantity()));
            }

            totalPrice += goodsInfo.getSettlementPrice() * goodsInfo.getOrderQuantity();
            totalDutchPrice = CountUtil.add(totalDutchPrice , CountUtil.mul((promotionPrice + lbPrice + cashCouponPrice + cashReturnPrice) , goodsInfo.getOrderQuantity()));

        }

        // 打印订单分摊信息
        logger.debug("订单总价值："+ totalPrice);
        logger.debug("退款总额："+ totalReturnPrice);
        logger.debug("分摊总额："+ totalDutchPrice);

        return goodsInfoList;
    }

    /**
     * 新
     * @param goodsInfoList
     * @param totalCashReturnPrice
     * @param totalLeBi
     * @return
     */
    public List<OrderGoodsInfo> countReturnPrice(List<OrderGoodsInfo> goodsInfoList,Double totalCashReturnPrice,Double totalLeBi,Double promotionDiscont){

        if(totalCashReturnPrice == null){
            totalCashReturnPrice = 0.00;
        }
        if (totalLeBi == null){
            totalLeBi = 0.00;
        }
        if(promotionDiscont == null){
            promotionDiscont = 0.00;
        }

        Double totalDutchPrice = 0.00;
        Double totalReturnPrice = 0.00;
        Double totalPrice = 0.00;
        Double totalCashCouponPrice = 0.00;

        for (int i=0 ; i < goodsInfoList.size() ; i++){
            OrderGoodsInfo  goodsInfo = goodsInfoList.get(i);

            // 促销分摊金额
            Double promotionPrice = goodsInfo.getPromotionSharePrice() == null ? 0.00 : goodsInfo.getPromotionSharePrice();
            // 乐币分摊金额
            Double lbPrice = goodsInfo.getLbSharePrice() == null ? 0.00 : goodsInfo.getLbSharePrice();
            // 现金券分摊金额
            Double cashCouponPrice = goodsInfo.getCashCouponSharePrice() == null ? 0.00 : goodsInfo.getCashCouponSharePrice();
            // 现金返利分摊金额
            Double cashReturnPrice = goodsInfo.getCashReturnSharePrice() == null ? 0.00 : goodsInfo.getCashReturnSharePrice();

            // 商品单价
            Double price = goodsInfo.getSettlementPrice() == null ? 0.00 : goodsInfo.getSettlementPrice();

            if (goodsInfo.getGoodsLineType().equals(AppGoodsLineType.GOODS)){
                totalPrice += goodsInfo.getSettlementPrice() * goodsInfo.getOrderQuantity();
            }
            totalCashCouponPrice = CountUtil.add(totalCashCouponPrice,CountUtil.mul(cashCouponPrice,goodsInfo.getOrderQuantity()));

            if (price.equals(0.00)){
                goodsInfo.setReturnPrice(0.00);
            }else {
                Double returnPrice = 0.00;
                if (i != goodsInfoList.size() -1){
                    returnPrice = CountUtil.sub(price , promotionPrice , lbPrice , cashCouponPrice , cashReturnPrice);
                }else{
                    // 最后一个 倒扣  (商品总价 - 所以折扣 - 已退款) / 数量
                    returnPrice = CountUtil.div(CountUtil.sub(totalPrice,totalCashCouponPrice,totalCashReturnPrice,promotionDiscont,totalReturnPrice),goodsInfo.getOrderQuantity());
                    if (returnPrice > goodsInfo.getSettlementPrice()){
                        returnPrice = goodsInfo.getSettlementPrice();
                    }
                }

                // 如果推货价出现负数 则设置为0
                if ( returnPrice >= -0.02 && returnPrice < 0){
                    goodsInfo.setReturnPrice(0.00);
                }else{
                    goodsInfo.setReturnPrice(returnPrice);
                }
                totalReturnPrice = CountUtil.add(totalReturnPrice,CountUtil.mul(returnPrice , goodsInfo.getOrderQuantity()));
            }

            totalDutchPrice = CountUtil.add(totalDutchPrice , CountUtil.mul((promotionPrice + lbPrice + cashCouponPrice + cashReturnPrice) , goodsInfo.getOrderQuantity()));

        }

        // 打印订单分摊信息
        logger.debug("订单总价值："+ totalPrice);
        logger.debug("退款总额："+ totalReturnPrice);
        logger.debug("分摊总额："+ totalDutchPrice);

        return goodsInfoList;
    }
}
