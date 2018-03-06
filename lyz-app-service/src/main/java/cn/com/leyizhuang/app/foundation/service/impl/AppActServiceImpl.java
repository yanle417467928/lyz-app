package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.ActStatusType;
import cn.com.leyizhuang.app.core.constant.AppCustomerType;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.*;
import cn.com.leyizhuang.app.foundation.pojo.activity.*;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.util.CountUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
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
    private GoodsDAO goodsDAO;

    @Autowired
    private ActGoodsMappingDAO actGoodsMappingDAO;

    @Autowired
    private ActGiftDetailsDAO actGiftDetailsDAO;

    @Autowired
    private ActSubAmountDAO actSubAmountDAO;

    @Autowired
    private ActStoresDAO actStoresDAO;

    @Resource
    private ActAddSaleDAO actAddSaleDAO;

    @Autowired
    private AppCustomerService appCustomerService;

    @Autowired
    private AppEmployeeService appEmployeeService;

    @Autowired
    private GoodsPriceService goodsPriceService;

    @Autowired
    private CityService cityService;

    @Override
    public List<ActBaseDO> queryList() {
        return actBaseDAO.queryList();
    }


    /**
     * 检查促销是否过期 未过期：true; 过期：false
     *
     * @return
     */
    @Override
    public Boolean checkActOutTime(List<Long> actIdList) {
        Boolean flag = true;
        LocalDateTime now = LocalDateTime.now();
        List<ActBaseDO> baseList = actBaseDAO.queryListByActIdsAndEndTime(actIdList, now);

        // 存在过期促销
        if (baseList != null && baseList.size() != 0) {
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
    public List<PromotionsGiftListResponse> countGift(Long userId, AppIdentityType userType, List<OrderGoodsSimpleResponse> goodsInfoList) throws UnsupportedEncodingException {
        List<PromotionsGiftListResponse> giftList = new ArrayList<>();
        PromotionsListResponse actResultInfos = countAct(userId, userType, goodsInfoList);

        if (actResultInfos != null && actResultInfos.getPromotionGiftList() != null) {
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
    public List<PromotionDiscountListResponse> countDiscount(Long userId, AppIdentityType userType, List<OrderGoodsSimpleResponse> goodsInfoList) throws UnsupportedEncodingException {
        List<PromotionDiscountListResponse> discountList = new ArrayList<>();
        PromotionsListResponse actResultInfos = countAct(userId, userType, goodsInfoList);
        if (actResultInfos != null && actResultInfos.getPromotionDiscountList() != null) {
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
    public PromotionsListResponse countAct(Long userId, AppIdentityType userType, List<OrderGoodsSimpleResponse> goodsInfoList) throws UnsupportedEncodingException {
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
            // 把商品注入本品池
            goodsPool.put(goods.getSku(), goods);
        }

        // 顾客身份
        AppCustomerType customerType = null;

        if (userType.getValue() == 6) {
            // 顾客
            AppCustomer customer = appCustomerService.findById(userId);
            if (customer == null) {
                return result;
            }

            actList = this.getActList(customer, skus);
            customerType = customer.getCustomerType();
        } else if (userType.getValue() == 0) {
            // 导购
            AppEmployee employee = appEmployeeService.findById(userId);
            if (employee == null) {
                return result;
            }

            actList = this.getActList(employee, skus);
            customerType = AppCustomerType.MEMBER;
        }

        if (actList == null || actList.size() == 0) {

            logger.info("无促销！");
            return null;
        }

        //******************* 根据促销类型 计算促销 *************************
        for (ActBaseDO act : actList) {

            // 判断当前用户身份
            String target = act.getActTarget();
            if (!target.contains(String.valueOf(userType.getValue()))){
                /**不可参与此促销，计算下一个促销**/
                continue;
            }

            // 促销类型
            String actType = act.getActType();

            //***************** 普通-满金额-立减 ************
            if (actType.equals("COMMON_FAMO_SUB")) {

                // 参与活动商品总额
                Double actualTotalPrice = countActualTotalPrice(goodsPool, act, customerType);

                // 满足促销立减金额
                if (actualTotalPrice >= act.getFullAmount()) {
                    int enjoyTimes = 1;
                    if (act.getIsDouble()) {
                        enjoyTimes = this.countFamoActEnjoyTimes(actualTotalPrice, act.getFullAmount());
                    }
                    // 创建一个促销结果
                    proDiscountList.add(this.getPromotionDiscountResponse(act, enjoyTimes));
                    // 结束本次促销循环
                    continue;
                }
            }
            //***************** 普通-满金额-赠商品 ************
            else if (actType.equals("COMMON_FAMO_GOO")) {
                // 参与活动商品总额
                Double actualTotalPrice = countActualTotalPrice(goodsPool, act, customerType);

                // 满足促销金额
                if (actualTotalPrice >= act.getFullAmount()) {

                    int enjoyTimes = this.countFamoActEnjoyTimes(actualTotalPrice, act.getFullAmount());
                    proGiftList.add(this.getGiftResultByActId(act, userId, userType, enjoyTimes));
                    // 结束本次促销循环
                    continue;
                }
            }
            //***************** 普通-满金额-加价购 ************
            else if (actType.equals("COMMON_FAMO_ADD")) {
                // 参与活动商品总额
                Double actualTotalPrice = countActualTotalPrice(goodsPool, act, customerType);

                // 满足促销金额
                if (actualTotalPrice >= act.getFullAmount()) {

                    int enjoyTimes = this.countFamoActEnjoyTimes(actualTotalPrice, act.getFullAmount());
                    proGiftList.add(this.getGiftResultByActId(act, userId, userType, enjoyTimes));
                    // 结束本次促销循环
                    continue;
                }
            }
            // **************** 普通-满金额-折扣 *************
            else if (actType.equals("COMMON_FAMO_DIS")) {
                // 参与活动商品总额
                Double actualTotalPrice = countActualTotalPrice(goodsPool, act, customerType);

                // 满足促销金额
                if (actualTotalPrice >= act.getFullAmount()) {

                    int enjoyTimes = 1;
                    if (act.getIsDouble()) {
                        enjoyTimes = this.countFamoActEnjoyTimes(actualTotalPrice, act.getFullAmount());
                    }
                    // 创建一个促销结果
                    proDiscountList.add(this.getPromotionDiscountResponse(act, enjoyTimes,actualTotalPrice));
                    // 结束本次促销循环
                    continue;
                }

            }
            //***************** 普通-满数量-赠商品 ************
            else if (actType.equals("COMMON_FQTY_GOO")) {
                // 判断本品是否满足数量要求 得出参与此促销次数
                int enjoyTimes = checkActGoodsNum(goodsPool, act);

                if (enjoyTimes > 0) {
                    proGiftList.add(this.getGiftResultByActId(act, userId, userType, enjoyTimes));
                    // 结束本次促销循环
                    continue;
                }
            }
            //***************** 普通-满数量-加价购 ************
            else if (actType.equals("COMMON_FQTY_ADD")) {
                // 判断本品是否满足数量要求 得出参与此促销次数
                int enjoyTimes = checkActGoodsNum(goodsPool, act);

                if (enjoyTimes > 0) {
                    proGiftList.add(this.getGiftResultByActId(act, userId, userType, enjoyTimes));
                    // 结束本次促销循环
                    continue;
                }
            }
            //***************** 普通-满数量-立减 ************
            else if (actType.equals("COMMON_FQTY_SUB")) {
                // 判断本品是否满足数量要求 得出参与此促销次数
                int enjoyTimes = checkActGoodsNum(goodsPool, act);

                if (enjoyTimes > 0) {
                    proDiscountList.add(this.getPromotionDiscountResponse(act, enjoyTimes));
                    // 结束本次促销循环
                    continue;
                }
            }

        }

        // 最后优惠金额，赠品，等集合装入促销结果类
        result.setPromotionDiscountList(proDiscountList);
        result.setPromotionGiftList(proGiftList);

        return result;
    }

    /*
       返回商品参与的促销 (顾客)
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
        actList = actBaseDAO.queryListBySkus(skus, now, cityId, "6", storeId);
        return actList;
    }


    /*
        返回商品参与的促销 (导购)
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
        actList = actBaseDAO.queryListBySkus(skus, now, cityId, "0", storeId);
        return actList;
    }

    /**
     * 获取参与活动商品金额
     *
     * @return
     */
    private Double countActualTotalPrice(Map<String, OrderGoodsSimpleResponse> goodsPool, ActBaseDO act, AppCustomerType customerType) {

        // 实际支付总额
        Double actualTotalPrice = 0.00;

        // 判断是否满足促销条件
        List<String> skuList = actGoodsMappingDAO.querySkusByActId(act.getId());

        Iterator<Map.Entry<String, OrderGoodsSimpleResponse>> it = goodsPool.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, OrderGoodsSimpleResponse> itEntry = it.next();
            Object itKey = itEntry.getKey();
            //注意：可以使用这种遍历方式进行删除元素和修改元素

            // 此商品在促销范围内
            if (skuList.contains(itKey)) {
                OrderGoodsSimpleResponse goods = itEntry.getValue();
                if (goods != null) {
                    if (customerType.equals(AppCustomerType.MEMBER)) {// 会员
                        actualTotalPrice = CountUtil.add(CountUtil.mul(goods.getVipPrice(), goods.getGoodsQty()), actualTotalPrice);
                    } else if (customerType.equals(AppCustomerType.RETAIL)) {// 零售
                        actualTotalPrice = CountUtil.add(CountUtil.mul(goods.getRetailPrice(), goods.getGoodsQty()), actualTotalPrice);
                    }
                }
            }

        }

        if (actualTotalPrice >= act.getFullAmount()) {
            // 满足金额条件 扣除商品池中商品
            Iterator<Map.Entry<String, OrderGoodsSimpleResponse>> it2 = goodsPool.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<String, OrderGoodsSimpleResponse> itEntry = it2.next();
                Object itKey = itEntry.getKey();
                //注意：可以使用这种遍历方式进行删除元素和修改元素

                // 此商品在促销范围内
                if (skuList.contains(itKey.toString())) {
                    OrderGoodsSimpleResponse goods = itEntry.getValue();
                    if (goods != null) {
                        if (act.getIsDouble()) {
                            // 此可重复参与 直接移除
                            it2.remove();
                        } else {
                            it2.remove();
                        }
                    }
                }

            }
        }
        return actualTotalPrice;
    }

    /**
     * 检查购买商品是否符合促销对于商品数量的要求
     *
     * @param goodsPool
     * @param act
     * @return
     */
    private Integer checkActGoodsNum(Map<String, OrderGoodsSimpleResponse> goodsPool, ActBaseDO act) {
        Boolean falg = true;

        // 参与次数
        Integer enjoyTimes = 0;

        // 促销需要满足总数量
        Integer fullNumber = act.getFullNumber();

        if(fullNumber == null || fullNumber == 0){
            return enjoyTimes;
        }

        // 本品数量是否任选
        Boolean isGoodsOptionalQty = act.getIsGoodsOptionalQty();

        if (isGoodsOptionalQty) {
            /** 本品为数量任选（非固定数量） 则将满足本品从本品池中扣除，不能再参与其他促销计算 （原则上此促销排序在固定数量促销之后）**/

            List<String> skuList = actGoodsMappingDAO.querySkusByActId(act.getId());

            Iterator<Map.Entry<String, OrderGoodsSimpleResponse>> it = goodsPool.entrySet().iterator();

            // 满足条件商品数量
            Integer  matchGoodsQty = 0;
            List<String> matchSku = new ArrayList<>();

            while (it.hasNext()) {
                Map.Entry<String, OrderGoodsSimpleResponse> itEntry = it.next();
                Object itKey = itEntry.getKey();
                //注意：可以使用这种遍历方式进行删除元素和修改元素

                // 此商品在促销范围内
                if (skuList.contains(itKey.toString())) {
                    // 顾客购买此商品数量
                    OrderGoodsSimpleResponse goods = itEntry.getValue();
                    Integer buyNum = goods.getGoodsQty();
                    matchGoodsQty += buyNum;
                    matchSku.add(goods.getSku());
                }
            }

            if (matchGoodsQty >= fullNumber){
                // 满足促销条件 向下取整获取次数
                enjoyTimes = (int) Math.floor(Double.valueOf(matchGoodsQty)/Double.valueOf(fullNumber));

                // 扣掉本品池中所有促销范围内商品

                for (String sku : matchSku){
                    goodsPool.remove(sku);
                }
            }else {
                return 0;
            }

            if (!act.getIsDouble()){
                // 不可重复参与
                enjoyTimes = 1;
            }

            return enjoyTimes;
        } else {
            // 本品数量固定 （组合促销）
            List<ActGoodsMappingDO> goodsMappingList = actGoodsMappingDAO.queryListByActId(act.getId());

            // 总数量是否满足要求 不满足直接返回false
            Integer totalNum = 0;
            for (ActGoodsMappingDO goodsMapping : goodsMappingList) {
                OrderGoodsSimpleResponse goods = goodsPool.get(goodsMapping.getSku());
                if (goods != null) {
                    totalNum += goods.getGoodsQty();
                }
            }
            if (totalNum < fullNumber) {
                return 0;
            }

            // 判断对单品数量要求
            for (ActGoodsMappingDO goodsMapping : goodsMappingList) {

                // 说明对此商品数量有单独要求 必须满足 qty 个；
                if (goodsMapping.getQty() != null) {
                    Integer qty = goodsMapping.getQty();

                    // 顾客购买此商品数量
                    OrderGoodsSimpleResponse goods = goodsPool.get(goodsMapping.getSku());

                    if (goods == null) {
                        return 0;
                    }

                    Integer buyNum = goods.getGoodsQty();

                    if (buyNum < qty) {
                        // 不满足要求 返回false
                        falg = false;
                        return 0;
                    }else{
                        // 向下取整 得出单品参与次数 最终取最小次数
                        Integer singleEnjoyTimes = (int) Math.floor(Double.valueOf(buyNum)/Double.valueOf(qty));
                        if(enjoyTimes == 0){
                            enjoyTimes = singleEnjoyTimes;
                        }else if(singleEnjoyTimes < enjoyTimes){
                            enjoyTimes = singleEnjoyTimes;
                        }
                    }
                }
            }

            // 说明以上条件都满足 扣掉商品池中商品
            if (falg) {

                if (!act.getIsDouble()){
                    // 不可重复参与
                    enjoyTimes = 1;
                }

                for (ActGoodsMappingDO goodsMapping : goodsMappingList) {

                    // 说明对此商品数量有单独要求 必须满足 qty 个；
                    if (goodsMapping.getQty() != null) {
                        Integer qty = goodsMapping.getQty();
                        String key = goodsMapping.getSku();
                        // 顾客购买此商品数量
                        OrderGoodsSimpleResponse goods = goodsPool.get(key);
                        Integer buyNum = goods.getGoodsQty();

                        // 满足单品数量要求 扣除商品池中该商品数量
                        Integer residuNum = buyNum - (qty * enjoyTimes);

                        if (residuNum == 0 || residuNum < 0) {
                            // 说明此商品被消耗完毕
                            goodsPool.remove(key);
                        } else {
                            goods.setGoodsQty(residuNum);
                            goodsPool.put(key, goods);
                        }
                    }
                }
            }

            return enjoyTimes;
        }
    }

    /**
     * 返回赠品结果
     *
     * @param act
     * @param userId
     * @param userType
     * @param enjoyTime
     * @return
     */
    private PromotionsGiftListResponse getGiftResultByActId(ActBaseDO act, Long userId, AppIdentityType userType, int enjoyTime) {

        // 创建一个促销结果
        PromotionsGiftListResponse response = new PromotionsGiftListResponse();

        response.setPromotionId(act.getId());
        response.setPromotionTitle(act.getTitle());
        response.setIsGiftOptionalQty(act.getIsGiftOptionalQty());
        response.setMaxChooseNumber(act.getGiftChooseNumber() * enjoyTime);
        response.setEnjoyTimes(enjoyTime);


        List<Long> giftIdList = new ArrayList<>();
        String actType = act.getActType();
        if (actType.contains("ADD")) {
            List<ActAddSaleDO> giftDetailList = new ArrayList<>();
            giftDetailList = actAddSaleDAO.queryByActId(act.getId());
            for (ActAddSaleDO goods : giftDetailList) {
                giftIdList.add(goods.getGoodsId());
            }
        } else if (actType.contains("GOO")) {
            List<ActGiftDetailsDO> giftDetailList = new ArrayList<>();
            giftDetailList = actGiftDetailsDAO.queryByActId(act.getId());
            for (ActGiftDetailsDO gift : giftDetailList) {
                if (gift.getGiftId() != null) {

                    giftIdList.add(gift.getGiftId());
                }
            }
        }
        logger.info("促销："+act.getTitle()+",包含赠品"+giftIdList.size()+"件");

        List<GiftListResponseGoods> giftListResponseGoods = new ArrayList<>();
        giftListResponseGoods = goodsPriceService.findGoodsPriceListByGoodsIdsAndUserIdAndIdentityType(
                giftIdList, userId, userType);

        if (actType.contains("ADD")) {
            // 加价购 将赠品设置价格
            for (GiftListResponseGoods gift : giftListResponseGoods) {
                gift.setRetailPrice(act.getAddAmount());
            }
        } else if (actType.contains("GOO")) {
            // 赠送 将价格设置为0
            for (GiftListResponseGoods gift : giftListResponseGoods) {
                if (act.getIsGiftOptionalQty()){
                    gift.setQty(0);
                }else{
                    // 设置每个赠品的固定数量
                    List<ActGiftDetailsDO> giftDetailList = new ArrayList<>();
                    giftDetailList = actGiftDetailsDAO.queryByActId(act.getId());

                    for (ActGiftDetailsDO detail : giftDetailList){
                        for(GiftListResponseGoods responseGoods : giftListResponseGoods){
                            if (detail.getGiftId().equals(responseGoods.getGoodsId())){
                                responseGoods.setQty(detail.getGiftFixedQty()*enjoyTime);
                            }
                        }
                    }
                }

                gift.setRetailPrice(0D);
            }
        }

        logger.info("享受："+act.getTitle()+",价目表下存在 有效赠品"+giftListResponseGoods.size()+"件");

        response.setGiftList(giftListResponseGoods);
        return response;
    }

    /**
     * 返回优惠立减结果
     *
     * @param act
     * @return
     */
    private PromotionDiscountListResponse getPromotionDiscountResponse(ActBaseDO act, int enjoyTimes) {

        // 创建一个促销结果
        PromotionDiscountListResponse proDiscount = new PromotionDiscountListResponse();
        ActSubAmountDO sub_amount = actSubAmountDAO.queryByActId(act.getId());

        proDiscount.setPromotionId(act.getId());
        proDiscount.setPromotionTitle(act.getTitle());
        proDiscount.setDiscountPrice(CountUtil.mul(sub_amount.getSubAmount(), enjoyTimes));
        proDiscount.setEnjoyTimes(enjoyTimes);

        logger.info("享受："+act.getTitle());
        return proDiscount;
    }

    private PromotionDiscountListResponse getPromotionDiscountResponse(ActBaseDO act, int enjoyTimes,Double totalPrice) {

        // 创建一个促销结果
        PromotionDiscountListResponse proDiscount = new PromotionDiscountListResponse();
        ActSubAmountDO sub_amount = actSubAmountDAO.queryByActId(act.getId());

        Double discountPrice = 0.00;
        if (act.getActType().contains("DIS")){
            Double discount = sub_amount.getDiscount() == null ? 1 : sub_amount.getDiscount();
            discountPrice = CountUtil.mul(CountUtil.sub(10D,discount),totalPrice);
            discountPrice = CountUtil.mul(0.1,discountPrice);
        }else if (act.getActType().contains("SUB")){
            discountPrice = sub_amount.getSubAmount() == null ? 0 : sub_amount.getSubAmount();
            discountPrice = CountUtil.mul(discountPrice,enjoyTimes);
        }

        proDiscount.setPromotionId(act.getId());
        proDiscount.setPromotionTitle(act.getTitle());
        proDiscount.setDiscountPrice(discountPrice);
        proDiscount.setEnjoyTimes(enjoyTimes);

        logger.info("享受："+act.getTitle());
        return proDiscount;
    }

    /**
     * 计算满金额促销重复参与次数
     *
     * @param actualTotaoPrice
     * @param fullAmount
     * @return
     */
    private int countFamoActEnjoyTimes(Double actualTotaoPrice, Double fullAmount) {
        int num = 1;

        num = (int) CountUtil.div(actualTotaoPrice, fullAmount);
        return num;
    }

    private int countFqtyActEnjoyTimes(Integer num1, Integer fullNumber) {
        int num = 1;

        return num;
    }

    /**
     * 批量插入
     *
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


    /******************************************************************** 华丽的分割线 ****************************************************************/

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @param keywords
     * @return
     */
    @Override
    public PageInfo<ActBaseDO> queryPageVO(Integer page, Integer size, String keywords, String status) {

        PageHelper.startPage(page, size);
        List<ActBaseDO> actBaseDOList = actBaseDAO.queryByKeywords(keywords, status);
        return new PageInfo<>(actBaseDOList);
    }

    /**
     * 保存促销
     *
     * @param baseDO
     * @param goodsList
     * @param giftList
     * @param subAmount
     */
    @Transactional
    public void save(ActBaseDO baseDO, List<ActGoodsMappingDO> goodsList, List<ActGiftDetailsDO> giftList, Double subAmount, List<ActStoreDO> storeDOList,Double discount) {
        String cityName = cityService.findById(baseDO.getCityId()).getName();
        baseDO.setCityName(cityName);

        if (baseDO.getIsGoodsOptionalQty()) {
            // 本品数量任选

        } else {
            // 固定数量 则将每个本品数量只和设置为fullNumber
            if (goodsList != null && goodsList.size() > 0) {
                Integer totalQty = 0;
                for (ActGoodsMappingDO item : goodsList) {
                    totalQty += item.getQty();
                }
                baseDO.setFullNumber(totalQty);
            }
        }

        if (baseDO.getIsGiftOptionalQty()) {
            // 赠品数量任选
        } else {
            // 固定数量 则将每个赠品数量只和设置为giftChooseNumber
            if (giftList != null && giftList.size() > 0) {
                Integer totalQty = 0;
                for (ActGiftDetailsDO item : giftList) {
                    totalQty += item.getGiftFixedQty();
                }
                baseDO.setGiftChooseNumber(totalQty);
            }

        }

        baseDO.setStatus(ActStatusType.NEW);
        baseDO.setCreateTime(LocalDateTime.now());
        // 生成编码
        String code = baseDO.createCode();
        baseDO.setActCode(code);
        actBaseDAO.save(baseDO);

        // 门店和促销映射
        for (ActStoreDO store : storeDOList) {
            store.setActId(baseDO.getId());
            store.setActCode(baseDO.getActCode());
            actStoresDAO.save(store);
        }

        //创建本品与促销的映射
        for (ActGoodsMappingDO item : goodsList) {
            item.setActId(baseDO.getId());
            item.setActCode(baseDO.getActCode());
            actGoodsMappingDAO.save(item);
        }

        String act_type = baseDO.getActType();
        if (act_type.contains("SUB")) {
            ActSubAmountDO actSubAmountDO = new ActSubAmountDO();
            actSubAmountDO.setActId(baseDO.getId());
            actSubAmountDO.setActCode(baseDO.getActCode());
            actSubAmountDO.setSubAmount(subAmount);

            actSubAmountDAO.save(actSubAmountDO);
        } else if (act_type.contains("GOO")) {
            // 创建赠品与促销的映射
            if (giftList != null && giftList.size() > 0) {
                for (ActGiftDetailsDO item : giftList) {
                    item.setActId(baseDO.getId());
                    item.setActCode(baseDO.getActCode());

                    actGiftDetailsDAO.save(item);
                }
            }
        } else if (act_type.contains("ADD")) {
            // 加价购
            if (giftList != null && giftList.size() > 0) {
                for (ActGiftDetailsDO item : giftList) {
                    ActAddSaleDO actAddSaleDO = new ActAddSaleDO();
                    actAddSaleDO.setGoodsId(item.getGiftId());
                    actAddSaleDO.setGoodsSku(item.getGiftSku());
                    actAddSaleDO.setGoodsTitle(item.getGiftTitle());
                    actAddSaleDO.setActId(baseDO.getId());
                    actAddSaleDO.setActCode(baseDO.getActCode());

                    actAddSaleDAO.save(actAddSaleDO);
                }
            }
        } else if (act_type.contains("DIS")) {
            // 折扣

            ActSubAmountDO actSubAmountDO = new ActSubAmountDO();
            actSubAmountDO.setActId(baseDO.getId());
            actSubAmountDO.setActCode(baseDO.getActCode());
            actSubAmountDO.setDiscount(discount);

            actSubAmountDAO.save(actSubAmountDO);
        }
    }

    /**
     * 更新
     *
     * @param baseDO
     * @param goodsList
     * @param giftList
     * @param subAmount
     * @param storeDOList
     */
    @Transactional
    public void edit(ActBaseDO baseDO, List<ActGoodsMappingDO> goodsList, List<ActGiftDetailsDO> giftList, Double subAmount, List<ActStoreDO> storeDOList,Double discount) {
        String cityName = cityService.findById(baseDO.getCityId()).getName();
        baseDO.setCityName(cityName);

        if (baseDO.getIsGoodsOptionalQty()) {
            // 本品数量任选

        } else {
            // 固定数量 则将每个本品数量只和设置为fullNumber
            if (goodsList != null && goodsList.size() > 0) {
                Integer totalQty = 0;
                for (ActGoodsMappingDO item : goodsList) {
                    totalQty += item.getQty();
                }
                baseDO.setFullNumber(totalQty);
            }
        }

        if (baseDO.getIsGiftOptionalQty()) {
            // 赠品数量任选
        } else {
            // 固定数量 则将每个赠品数量只和设置为giftChooseNumber
            if (giftList != null && giftList.size() > 0) {
                Integer totalQty = 0;
                for (ActGiftDetailsDO item : giftList) {
                    totalQty += item.getGiftFixedQty();
                }
                baseDO.setGiftChooseNumber(totalQty);
            }

        }

        actBaseDAO.update(baseDO);

        // 修改门店 先删除旧的记录
        actStoresDAO.deleteByActBaseId(baseDO.getId());
        for (ActStoreDO store : storeDOList) {
            store.setActId(baseDO.getId());
            store.setActCode(baseDO.getActCode());
            actStoresDAO.save(store);
        }

        //修改本品 先删除旧的记录
        actGoodsMappingDAO.deleteByActBaseId(baseDO.getId());
        for (ActGoodsMappingDO item : goodsList) {
            item.setActId(baseDO.getId());
            item.setActCode(baseDO.getActCode());
            actGoodsMappingDAO.save(item);
        }

        String act_type = baseDO.getActType();
        if (act_type.contains("SUB")) {
            // 删除旧记录
            actSubAmountDAO.deleteByActBaseId(baseDO.getId());
            ActSubAmountDO actSubAmountDO = new ActSubAmountDO();
            actSubAmountDO.setActId(baseDO.getId());
            actSubAmountDO.setActCode(baseDO.getActCode());
            actSubAmountDO.setSubAmount(subAmount);

            actSubAmountDAO.save(actSubAmountDO);
        } else if (act_type.contains("GOO")) {
            // 修改赠品 先删除旧记录
            actGiftDetailsDAO.deleteByActBaseId(baseDO.getId());
            if (giftList != null && giftList.size() > 0) {
                for (ActGiftDetailsDO item : giftList) {
                    item.setActId(baseDO.getId());
                    item.setActCode(baseDO.getActCode());

                    actGiftDetailsDAO.save(item);
                }
            }
        } else if (act_type.contains("ADD")) {
            // 加价购
            actAddSaleDAO.deleteByActBaseId(baseDO.getId());
            if (giftList != null && giftList.size() > 0) {
                for (ActGiftDetailsDO item : giftList) {
                    ActAddSaleDO actAddSaleDO = new ActAddSaleDO();
                    actAddSaleDO.setGoodsId(item.getGiftId());
                    actAddSaleDO.setGoodsSku(item.getGiftSku());
                    actAddSaleDO.setGoodsTitle(item.getGiftTitle());
                    actAddSaleDO.setActId(baseDO.getId());
                    actAddSaleDO.setActCode(baseDO.getActCode());

                    actAddSaleDAO.save(actAddSaleDO);
                }
            }
        }else if (act_type.contains("DIS")) {
            // 折扣
            actSubAmountDAO.deleteByActBaseId(baseDO.getId());
            ActSubAmountDO actSubAmountDO = new ActSubAmountDO();
            actSubAmountDO.setActId(baseDO.getId());
            actSubAmountDO.setActCode(baseDO.getActCode());
            actSubAmountDO.setDiscount(discount);

            actSubAmountDAO.save(actSubAmountDO);
        }
    }

    public ModelMap getModelMapByActBaseId(ModelMap map, Long id) {

        ActBaseDO ActBaseDO = actBaseDAO.queryById(id);
        List<ActStoreDO> ActStoreDOList = actStoresDAO.queryListByActBaseId(id);
        List<ActGoodsMappingDO> ActGoodsMappingDOList = actGoodsMappingDAO.queryListByActId(id);
        List<ActGiftDetailsDO> ActGiftDetailsDOLsit = actGiftDetailsDAO.queryByActId(id);
        ActSubAmountDO ActSubAmountDOList = actSubAmountDAO.queryByActId(id);
        List<ActAddSaleDO> ActAddSaleDOList = actAddSaleDAO.queryByActId(id);

        if (ActAddSaleDOList != null && ActAddSaleDOList.size() > 0) {
            for (ActAddSaleDO item : ActAddSaleDOList) {
                ActGiftDetailsDO giftDetailsDO = new ActGiftDetailsDO();
                giftDetailsDO.setActId(item.getActId());
                giftDetailsDO.setActCode(item.getActCode());
                giftDetailsDO.setGiftId(item.getGoodsId());
                giftDetailsDO.setGiftSku(item.getGoodsSku());
                giftDetailsDO.setGiftTitle(item.getGoodsTitle());
                giftDetailsDO.setGiftFixedQty(0);

                ActGiftDetailsDOLsit.add(giftDetailsDO);
            }
        }

        map.addAttribute("actBaseDO", ActBaseDO);
        map.addAttribute("actStoresDO", ActStoreDOList);
        map.addAttribute("actGoodsMappingDO", ActGoodsMappingDOList);
        map.addAttribute("actGiftDetailsDO", ActGiftDetailsDOLsit);
        map.addAttribute("actSubAmountDO", ActSubAmountDOList);
        return map;
    }

    /**
     * 发布促销
     *
     * @param id
     */
    public void publishAct(Long id) {
        ActBaseDO baseDO = actBaseDAO.queryById(id);
        baseDO.setStatus(ActStatusType.PUBLISH);

        actBaseDAO.update(baseDO);
    }

    /**
     * 失效促销
     *
     * @param id
     */
    public void inValid(Long id) {
        ActBaseDO baseDO = actBaseDAO.queryById(id);
        baseDO.setStatus(ActStatusType.INVALID);

        actBaseDAO.update(baseDO);
    }

    @Override
    public ActBaseDO findById(Long promotionId) {
        if (null != promotionId) {
            return actBaseDAO.queryById(promotionId);
        }
        return null;
    }
}
