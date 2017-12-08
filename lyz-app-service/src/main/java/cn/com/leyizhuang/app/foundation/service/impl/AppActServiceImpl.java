package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.*;
import cn.com.leyizhuang.app.foundation.pojo.activity.*;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppActService;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.GoodsPriceService;
import cn.com.leyizhuang.common.util.CountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 促销服务类
 * Created by panjie on 2017/11/22.
 */
@Service
public class AppActServiceImpl implements AppActService {

    private static final Logger logger = LoggerFactory.getLogger(AppActServiceImpl.class);
    @Autowired
    private ActBaseDAO actBaseDAO;

    @Autowired
    private ActGoodsMappingDAO actGoodsMappingDAO;

    @Autowired
    private ActGiftDetailsDAO actGiftDetailsDAO;

    @Autowired
    private ActSubAmountDAO actSubAmountDAO;

    @Autowired
    private ActStoresDAO actStoresDAO;

    @Autowired
    private AppCustomerService appCustomerService;

    @Autowired
    private AppEmployeeService appEmployeeService;

    @Autowired
    private GoodsPriceService goodsPriceService;

    @Override
    public List<ActBaseDO> queryList() {
        return actBaseDAO.queryList();
    }

    /**
     * 检查促销是否过期 未过期：true; 过期：false
     * @return
     */
    public Boolean checkActOutTime(List<Long> actIdList){
        Boolean flag = true;
        LocalDateTime now = LocalDateTime.now();
        List<ActBaseDO> baseList = actBaseDAO.queryListByActIdsAndEndTime(actIdList,now);

        // 存在过期促销
        if(baseList != null && baseList.size() != 0){
            flag = false;
        }
        return flag;
    }

    /**
     * 计算促销 只返回赠品
     *
     * @param userId
     * @param userType
     * @param goodsInfoList
     * @return
     */
    @Override
    public List<PromotionsGiftListResponse> countGift(Long userId, AppIdentityType userType, List<OrderGoodsSimpleResponse> goodsInfoList) {
        List<PromotionsGiftListResponse> giftList = new ArrayList<>();
        PromotionsListResponse actResultInfos = countAct(userId, userType, goodsInfoList);

        if(actResultInfos != null){
            giftList = actResultInfos.getPromotionGiftList();
        }

        return giftList;
    }
    /**
     * 计算促销 只返回优惠立减
     *
     * @param userId
     * @param userType
     * @param goodsInfoList
     * @return
     */
    public List<PromotionDiscountListResponse> countDiscount(Long userId, AppIdentityType userType, List<OrderGoodsSimpleResponse> goodsInfoList){
        List<PromotionDiscountListResponse> discountList = new ArrayList<>();
        PromotionsListResponse actResultInfos = countAct(userId, userType, goodsInfoList);
        if(actResultInfos != null){
            discountList = actResultInfos.getPromotionDiscountList();
        }

        return discountList;
    }

    /**
     * 计算促销
     *
     * @param userId        用户id
     * @param userType      用户类型
     * @param goodsInfoList 本品集合
     * @return
     */
    public PromotionsListResponse countAct(Long userId, AppIdentityType userType, List<OrderGoodsSimpleResponse> goodsInfoList) {
        //*****促销结果*****
        PromotionsListResponse result = new PromotionsListResponse();
        List<PromotionDiscountListResponse> proDiscountList = new ArrayList<>();
        List<PromotionsGiftListResponse> proGiftList = new ArrayList<>();

        // 创建一个本品池，计算一次促销消耗掉对应商品数量
        Map<String, OrderGoodsSimpleResponse> goodsPool = new HashMap<>();

        // 本品集合为空 不用计算促销了
        if (goodsInfoList == null || goodsInfoList.size() == 0) {
            return result;
        }

        // 创建一个促销 集合
        List<ActBaseDO> actList = new ArrayList<>();

        List<String> skus = new ArrayList<>();
        for (int i = 0; i < goodsInfoList.size(); i++) {
            OrderGoodsSimpleResponse goods = goodsInfoList.get(i);

            skus.add(goods.getSku());
            // 把商品及数量注入本品池
            goodsPool.put(goods.getSku(), goods);
        }

        if (userType.getValue() == 6) {
            // 顾客
            AppCustomer customer = appCustomerService.findById(userId);
            if (customer == null) {
                return result;
            }

            actList = this.getActList(customer, skus);

            //******************* 根据促销类型 计算促销 *************************

            if (actList == null || actList.size() == 0) {

                logger.info("无促销！");
                return null;
            }
            for (ActBaseDO act : actList) {
                //***************** 普通-满金额-立减 ************
                if (act.getActType().equals("COMMON_FAMO_SUB")) {

                    // 参与活动商品总额
                    Double actualTotalPrice = countActualTotalPrice(goodsPool, act.getId(), customer.getCustomerType().getValue());

                    // 满足促销立减金额
                    if (actualTotalPrice >= act.getFullAmount()) {

                        // 创建一个促销结果
                        proDiscountList.add(this.getPromotionDiscountResponse(act,actualTotalPrice));
                        // 结束本次促销循环
                        continue;
                    }
                }
                //***************** 普通-满金额-赠商品 ************
                else if (act.getActType().equals("COMMON_FAMO_GOO")) {
                    // 参与活动商品总额
                    Double actualTotalPrice = countActualTotalPrice(goodsPool, act.getId(), customer.getCustomerType().getValue());

                    // 满足促销立减金额
                    if (actualTotalPrice >= act.getFullAmount()) {

                        int enjoyTimes = this.countFamoActEnjoyTimes(actualTotalPrice,act.getFullAmount());
                        proGiftList.add(this.getGiftResultByActId(act,userId,userType,enjoyTimes));
                        // 结束本次促销循环
                        continue;
                    }
                }
                //***************** 普通-满数量-赠商品 ************
                else if (act.getActType().equals("COMMON_FQTY_GOO")) {
                    // 判断本品是否满足数量要求 返回参与此促销次数
                    Boolean flag = true;
                    int enjoyTimes = 0;
                    while(flag){
                        flag = checkActGoodsNum(goodsPool, act.getId(), act.getFullNumber());
                        if(flag){
                            enjoyTimes++;
                        }
                    }
                    if (flag) {
                        proGiftList.add(this.getGiftResultByActId(act,userId,userType,enjoyTimes));
                        // 结束本次促销循环
                        continue;
                    }
                }

            }
        } else if (userType.getValue() == 0) {
            // 导购
            AppEmployee employee = appEmployeeService.findById(userId);
            if (employee == null) {
                return result;
            }

            actList = this.getActList(employee, skus);
            if (actList == null || actList.size() == 0) {

                logger.info("无促销！");
                return null;
            }
            //******************* 根据促销类型 计算促销 *************************

            for (ActBaseDO act : actList) {
                //***************** 普通-满金额-立减 ************
                if (act.getActType().equals("COMMON_FAMO_SUB")) {

                    // 参与活动商品总额 导购以会员价计算
                    Double actualTotalPrice = countActualTotalPrice(goodsPool, act.getId(), "MEMBER");

                    // 满足促销立减金额
                    if (actualTotalPrice >= act.getFullAmount()) {

                        // 创建一个促销结果
                        proDiscountList.add(this.getPromotionDiscountResponse(act,actualTotalPrice));
                        // 结束本次促销循环
                        continue;
                    }
                }
                //***************** 普通-满金额-赠商品 ************
                else if (act.getActType().equals("COMMON_FAMO_GOO")) {
                    // 参与活动商品总额
                    Double actualTotalPrice = countActualTotalPrice(goodsPool, act.getId(), "MEMBER");

                    // 满足促销立减金额
                    if (actualTotalPrice >= act.getFullAmount()) {
                        int enjoyTimes = this.countFamoActEnjoyTimes(actualTotalPrice,act.getFullAmount());
                        proGiftList.add(this.getGiftResultByActId(act,userId,userType,enjoyTimes));
                        // 结束本次促销循环
                        continue;
                    }
                }
                //***************** 普通-满数量-赠商品 ************
                else if (act.getActType().equals("COMMON_FQTY_GOO")) {
                    // 判断本品是否满足数量要求 返回参与此促销次数
                    Boolean flag = true;
                    int enjoyTimes = 0;
                    while(flag){
                        flag = checkActGoodsNum(goodsPool, act.getId(), act.getFullNumber());
                        if(flag){
                            enjoyTimes++;
                        }
                    }

                    if (enjoyTimes > 0) {
                        proGiftList.add(this.getGiftResultByActId(act,userId,userType,enjoyTimes));
                        // 结束本次促销循环
                        continue;
                    }
                }
            }
        }


        // 最后优惠金额，赠品，等集合装入促销结果类
        result.setPromotionDiscountList(proDiscountList);
        result.setPromotionGiftList(proGiftList);

        return result;
    }

    /*
       返回商品 参与的促销 (顾客)
     */
    private List<ActBaseDO> getActList(AppCustomer cus, List<String> skus) {
        // 创建一个促销 集合
        List<ActBaseDO> actList = new ArrayList<>();
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 城市id
        Long cityId = cus.getCityId();
        // 门店id
        Long storeId = cus.getStoreId();

        // 返回用户购买商品参与的未过期的活动
        actList = actBaseDAO.queryListBySkus(skus, now, cityId, "顾客", storeId);
        return actList;
    }


    /*
        返回商品 参与的促销 (导购)
     */
    private List<ActBaseDO> getActList(AppEmployee employee, List<String> skus) {
        // 创建一个促销 集合
        List<ActBaseDO> actList = new ArrayList<>();
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 城市id
        Long cityId = employee.getCityId();
        // 门店id
        Long storeId = employee.getStoreId();

        // 返回用户购买商品参与的未过期的活动
        actList = actBaseDAO.queryListBySkus(skus, now, cityId, "导购", storeId);
        return actList;
    }

    /**
     * 获取参与活动商品金额
     *
     * @return
     */
    private Double countActualTotalPrice(Map<String, OrderGoodsSimpleResponse> goodsPool, Long actId, String priceType) {

        // 实际支付总额
        Double actualTotalPrice = 0.00;

        // 判断是否满足促销条件
        List<String> skuList = actGoodsMappingDAO.querySkusByActId(actId);

        Iterator<Map.Entry<String, OrderGoodsSimpleResponse>> it = goodsPool.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, OrderGoodsSimpleResponse> itEntry = it.next();
            Object itKey = itEntry.getKey();
            //注意：可以使用这种遍历方式进行删除元素和修改元素

            // 此商品在促销范围内
            if (skuList.contains(itKey)) {
                OrderGoodsSimpleResponse goods = itEntry.getValue();
                if (goods != null) {
                    if (priceType.equals("MEMBER")) {// 会员
                        actualTotalPrice = CountUtil.add(CountUtil.mul(goods.getVipPrice(),goods.getGoodsQty()),actualTotalPrice);
                    } else if (priceType.equals("MEMBER")) {// 零售
                        actualTotalPrice = CountUtil.add(CountUtil.mul(goods.getVipPrice(),goods.getGoodsQty()),actualTotalPrice);
                    }

                    // 该商品参与此促销后 将不能参与其他促销
                    it.remove();
                }
            }

        }
        return actualTotalPrice;
    }

    /**
     * 检查购买商品是否符合促销对于商品数量的要求
     *
     * @param goodsPool
     * @param actId
     * @param fullNumber
     * @return
     */
    private Boolean checkActGoodsNum(Map<String, OrderGoodsSimpleResponse> goodsPool, Long actId, Integer fullNumber) {
        Boolean falg = true;

        List<ActGoodsMappingDO> goodsMappingList = actGoodsMappingDAO.queryListByActId(actId);

        // 总数量是否满足要求 不满足直接返回false
        Integer totalNum = 0;
        for (ActGoodsMappingDO goodsMapping : goodsMappingList) {
            OrderGoodsSimpleResponse goods = goodsPool.get(goodsMapping.getSku());
            if (goods != null) {
                totalNum += goods.getGoodsQty();
            }
        }
        if (totalNum < fullNumber) {
            return false;
        }

        // 判断对单品数量要求
        for (ActGoodsMappingDO goodsMapping : goodsMappingList) {

            // 说明对此商品数量有单独要求 必须满足 qty 个；
            if (goodsMapping.getQty() != null) {
                Integer qty = goodsMapping.getQty();

                // 顾客购买此商品数量
                OrderGoodsSimpleResponse goods = goodsPool.get(goodsMapping.getSku());

                if(goods == null){
                    return false;
                }

                Integer buyNum = goods.getGoodsQty();

                if (buyNum < qty) {
                    // 不满足要求 返回false
                    falg = false;
                    return falg;
                }
            }
        }

        // 说明以上条件都满足 扣掉商品池中商品
        if (falg) {
            for (ActGoodsMappingDO goodsMapping : goodsMappingList) {

                // 说明对此商品数量有单独要求 必须满足 qty 个；
                if (goodsMapping.getQty() != null) {
                    Integer qty = goodsMapping.getQty();
                    String key = goodsMapping.getSku();
                    // 顾客购买此商品数量
                    OrderGoodsSimpleResponse goods = goodsPool.get(key);
                    Integer buyNum = goods.getGoodsQty();

                    // 满足单品数量要求 扣除商品池中该商品数量
                    Integer residuNum = buyNum - qty;

                    if (residuNum == 0) {
                        // 说明此商品被消耗完毕
                        goodsPool.remove(key);
                    } else {
                        goods.setGoodsQty(residuNum);
                        goodsPool.put(key, goods);
                    }
                }
            }
        }

        return falg;
    }

    /**
     * 返回赠品结果
     * @param act
     * @param userId
     * @param userType
     * @param enjoyTime
     * @return
     */
    private PromotionsGiftListResponse getGiftResultByActId(ActBaseDO act,Long userId, AppIdentityType userType,int enjoyTime) {

        // 创建一个促销结果
        PromotionsGiftListResponse response = new PromotionsGiftListResponse();
        List<ActGiftDetailsDO> giftDetailList = actGiftDetailsDAO.queryByActId(act.getId());

        response.setPromotionId(act.getId());
        response.setPromotionTitle(act.getTitle());
        response.setMaxChooseNumber(act.getGiftChooseNumber()*enjoyTime);


        List<Long> giftIdList = new ArrayList<>();
        for (ActGiftDetailsDO gift : giftDetailList) {
            if (gift.getGiftId() != null) {
                // 赠品为商品
                giftIdList.add(gift.getGiftId());
            }

        }
        List<GiftListResponseGoods> giftListResponseGoods = new ArrayList<>();
        giftListResponseGoods = goodsPriceService.findGoodsPriceListByGoodsIdsAndUserIdAndIdentityType(
                giftIdList, userId, userType);
        response.setGiftList(giftListResponseGoods);

        return response;
    }

    /**
     * 返回优惠立减结果
     * @param act
     * @param actualTotalPrice
     * @return
     */
    private PromotionDiscountListResponse getPromotionDiscountResponse(ActBaseDO act,Double actualTotalPrice){
        int enjoyTimes = 1;

        // 此促销是否可以重复参与
        if(act.getIsDouble()){
            enjoyTimes = this.countFamoActEnjoyTimes(actualTotalPrice,act.getFullAmount());
        }

        // 创建一个促销结果
        PromotionDiscountListResponse proDiscount = new PromotionDiscountListResponse();
        ActSubAmountDO sub_amount = actSubAmountDAO.queryByActId(act.getId());

        proDiscount.setPromotionId(act.getId());
        proDiscount.setPromotionTitle(act.getTitle());
        proDiscount.setDiscountPrice(CountUtil.mul(sub_amount.getSubAmount(),enjoyTimes));

        return proDiscount;
    }

    /**
     * 计算满金额促销重复参与次数
     * @param actualTotaoPrice
     * @param fullAmount
     * @return
     */
    private int countFamoActEnjoyTimes(Double actualTotaoPrice,Double fullAmount){
        int num = 1;

        num = (int) CountUtil.div(actualTotaoPrice,fullAmount);
        return num;
    }

    private int countFqtyActEnjoyTimes(Integer num1,Integer fullNumber){
        int num = 1;

        return num;
    }

    /**
     * 批量插入
     * @return
     */
    public int insertBatch() {
//        List<ActBaseDO> list = new ArrayList<ActBaseDO>();

//        for(int i=0;i<100;i++){
//
//            ActBaseDO act = new ActBaseDO();
//            act.setActCode("123"+i);
//            act.setActTarget("会员");
//            act.setBaseType("COMMON");
//            act.setConditionType("FQTY");
//            act.setPromotionType("SUB");
//            act.setBeginTime(LocalDateTime.now());
//            act.setCityId(1L);
//            act.setCityName("成都市");
//            act.setCreateTime(LocalDateTime.now());
//            act.setEndTime(LocalDateTime.of(2018,11,12,11,11,11));
//            act.setIsDouble(true);
//            act.setTitle("123"+i);
//            list.add(act);
//        }
        List<ActGoodsMappingDO> goodsList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ActGoodsMappingDO goods = new ActGoodsMappingDO();
            goods.setActCode("123" + i);
            goods.setActId(1L);
            goods.setGid(Long.valueOf(i));
            goods.setSku("CX_SUB00" + i);
            goods.setQty(0);
            goods.setGoodsTitile("满减");
            goodsList.add(goods);
        }

        return actGoodsMappingDAO.insertBatch(goodsList);
    }

}
