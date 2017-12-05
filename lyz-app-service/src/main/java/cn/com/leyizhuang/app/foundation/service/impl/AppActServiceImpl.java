package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.*;
import cn.com.leyizhuang.app.foundation.pojo.activity.*;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.PromotionSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderGoodsSimpleResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppActService;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.common.util.CountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by panjie on 2017/11/22.
 */
@Service
public class AppActServiceImpl implements AppActService{

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

    @Override
    public List<ActBaseDO> queryList() {
        return actBaseDAO.queryList();
    }

    /**
     * 计算促销
     * @param userId 用户id
     * @param userType 用户类型
     * @param goodsInfoList 本品集合
     * @return
     */
    public List<ActResultInfo> countAct(Long userId , AppIdentityType userType, List<OrderGoodsSimpleResponse> goodsInfoList){
        List<ActResultInfo> result = null;
        // 创建一个本品池，计算一次促销消耗掉对应商品数量
        Map<String,OrderGoodsSimpleResponse> goodsPool = new HashMap<>();

        if(goodsInfoList == null || goodsInfoList.size() == 0){
            return result;
        }

        // 创建一个促销 集合
        List<ActBaseDO> actList = new ArrayList<>();

        StringBuffer skus = new StringBuffer();
        for (int i = 0 ; i< goodsInfoList.size() ; i++) {
            OrderGoodsSimpleResponse goods = goodsInfoList.get(i);

            if(i == 0){
                skus.append(goods.getSku());
            }else{
                skus.append(","+goods.getSku());
            }

            // 把商品及数量注入本品池
            goodsPool.put(goods.getGoodsName(),goods);
        }

        if(userType.getValue() == 6){
            // 顾客
            AppCustomer customer = appCustomerService.findById(userId);
            if (customer == null){
                return result;
            }

            actList = this.getActList(customer , skus.toString());

            //******************* 根据促销类型 计算促销 *************************

            for (ActBaseDO act : actList ){

                //***************** 普通-满金额-立减 ************
                if( act.getActType().equals("COMMON_FAMO_SUB")){

                    // 参与活动商品总额
                    Double actualTotalPrice = countActualTotalPrice(goodsPool,act.getId(),customer.getCustomerType().getValue());

                    // 满足促销立减金额
                    if(actualTotalPrice >= act.getFullAmount()){

                        // 创建一个促销结果
                        ActResultInfo info = new ActResultInfo();
                        ActSubAmountDO sub_amount = actSubAmountDAO.queryByActId(act.getId());
                        info.setDiscount(sub_amount.getSubAmount());
                        info.setActId(act.getId());
                        result.add(info);

                        // 结束本次促销循环
                        continue;
                    }
                }
                //***************** 普通-满金额-赠商品 ************
                else if(act.getActType().equals("COMMON_FAMO_GOO")){
                    // 参与活动商品总额
                    Double actualTotalPrice = countActualTotalPrice(goodsPool,act.getId(),customer.getCustomerType().getValue());

                    // 满足促销立减金额
                    if(actualTotalPrice >= act.getFullAmount()){

                        result.add(this.getGiftResultByActId(act.getId()));
                        // 结束本次促销循环
                        continue;
                    }
                }
                //***************** 普通-满数量-赠商品 ************
                else if(act.getActType().equals("COMMON_FAMO_GOO")){
                    // 判断本品是否满足数量要求
                    Boolean flag = checkActGoodsNum(goodsPool,act.getId(),act.getFullNumber());

                    if(flag){
                        result.add(this.getGiftResultByActId(act.getId()));
                        // 结束本次促销循环
                        continue;
                    }
                }

            }
        }else if(userType.getValue() == 0 ){
            // 导购
            AppEmployee employee = appEmployeeService.findById(userId);
            if (employee == null){
                return result;


            }

            actList = this.getActList(employee , skus.toString());

            //******************* 根据促销类型 计算促销 *************************

            for (ActBaseDO act : actList ){
                if( act.getActType().equals("COMMON_FQTY_SUB")){
                    //**** 普通-满金额-立减 ****



                }
            }
        }



        return null;
    }

    /*
       返回商品 参与的促销 (顾客)
     */
    private List<ActBaseDO> getActList(AppCustomer cus , String skus){
        // 创建一个促销 集合
        List<ActBaseDO> actList = new ArrayList<>();
        // 当前时间
        LocalDateTime now = LocalDateTime.now();


        // 返回用户购买商品参与的未过期的活动
        actList = actBaseDAO.queryListBySkus(skus,now,cus.getCityId(),"顾客",cus.getStoreId());
        return actList;
    }

    /*
        返回商品 参与的促销 (导购)
     */
    private List<ActBaseDO> getActList(AppEmployee employee, String skus){
        // 创建一个促销 集合
        List<ActBaseDO> actList = new ArrayList<>();
        // 当前时间
        LocalDateTime now = LocalDateTime.now();


        // 返回用户购买商品参与的未过期的活动
        actList = actBaseDAO.queryListBySkus(skus,now,employee.getCityId(),"导购",employee.getStoreId());
        return actList;
    }

    /**
     * 获取参与活动商品金额
     * @return
     */
    private Double countActualTotalPrice(Map<String,OrderGoodsSimpleResponse> goodsPool,Long actId,String priceType){

        // 实际支付总额
        Double actualTotalPrice = 0.00;

        // 判断是否满足促销条件
        List<String> skuList = actGoodsMappingDAO.querySkusByActId(actId);

        Iterator<Map.Entry<String, OrderGoodsSimpleResponse>> it = goodsPool.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, OrderGoodsSimpleResponse> itEntry = it.next();
            Object itKey = itEntry.getKey();
            //注意：可以使用这种遍历方式进行删除元素和修改元素

            // 此商品在促销范围内
            if(skuList.contains(itKey)){
                OrderGoodsSimpleResponse goods = itEntry.getValue();
                if (goods != null){
                    if(priceType.equals("MEMBER")){// 会员
                        actualTotalPrice = CountUtil.add(goods.getVipPrice());
                    }else if(priceType.equals("MEMBER")){// 零售
                        actualTotalPrice = CountUtil.add(goods.getRetailPrice());
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
     * @param goodsPool
     * @param actId
     * @param fullNumber
     * @return
     */
    private Boolean checkActGoodsNum(Map<String,OrderGoodsSimpleResponse> goodsPool,Long actId,Integer fullNumber){
        Boolean falg = true;

        List<ActGoodsMappingDO> goodsMappingList =  actGoodsMappingDAO.queryListByActId(actId);

        // 总数量是否满足要求 不满足直接返回false
        Integer totalNum = 0;
        for (ActGoodsMappingDO goodsMapping : goodsMappingList){
            OrderGoodsSimpleResponse goods = goodsPool.get(goodsMapping.getSku());
            totalNum += goods.getGoodsQty();
        }
        if(totalNum < fullNumber){
            return false;
        }

        // 创建一个数组 记录需要

        // 判断对单品数量要求
        for (ActGoodsMappingDO goodsMapping : goodsMappingList){

            // 说明对此商品数量有单独要求 必须满足 qty 个；
            if(goodsMapping.getQty() != null){
                Integer qty = goodsMapping.getQty();

                // 顾客购买此商品数量
                OrderGoodsSimpleResponse goods = goodsPool.get(goodsMapping.getSku());
                Integer buyNum = goods.getGoodsQty();

                if(buyNum <= qty){
                    // 不满足要求 返回false
                    falg = false;
                    return falg;
                }
            }
        }

        // 说明以上条件都满足 扣掉商品池中商品
        if(falg){
            for (ActGoodsMappingDO goodsMapping : goodsMappingList){

                // 说明对此商品数量有单独要求 必须满足 qty 个；
                if(goodsMapping.getQty() != null){
                    Integer qty = goodsMapping.getQty();
                    String key = goodsMapping.getSku();
                    // 顾客购买此商品数量
                    OrderGoodsSimpleResponse goods = goodsPool.get(key);
                    Integer buyNum = goods.getGoodsQty();

                    // 满足单品数量要求 扣除商品池中该商品数量
                    Integer residuNum = buyNum - qty;

                    if(residuNum == 0){
                        // 说明此商品被消耗完毕
                        goodsPool.remove(key);
                    }else{
                        goods.setGoodsQty(residuNum);
                        goodsPool.put(key,goods);
                    }
                }
            }
        }

        return false;
    }

    private ActResultInfo getGiftResultByActId(Long actId){
        // 创建一个促销结果
        ActResultInfo info = new ActResultInfo();
        List<ActGiftDetailsDO> giftDetailList = actGiftDetailsDAO.queryByActId(actId);

        info.setActId(actId);
        info.setGiftDetails(giftDetailList);
        return info;
    }





    /*
        批量插入
     */
    public int insertBatch(){
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
        for (int i=0;i<1000;i++){
            ActGoodsMappingDO goods = new ActGoodsMappingDO();
            goods.setActCode("123"+i);
            goods.setActId(1L);
            goods.setGid(Long.valueOf(i));
            goods.setSku("CX_SUB00"+i);
            goods.setQty(0);
            goods.setGoodsTitile("满减");
            goodsList.add(goods);
        }

        return actGoodsMappingDAO.insertBatch(goodsList);
    }

}
