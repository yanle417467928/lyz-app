package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.ActBaseType;
import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.SellDetailsDAO;
import cn.com.leyizhuang.app.foundation.dao.SellZgDetailsDAO;
import cn.com.leyizhuang.app.foundation.pojo.SellDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.SellDetailsErrorLogDO;
import cn.com.leyizhuang.app.foundation.pojo.SellZgCusTimes;
import cn.com.leyizhuang.app.foundation.pojo.SellZgDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.CustomerRankInfoResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by panjie on 2018/1/24.
 */
@Service("statisticsSellDetailsService")
public class StatisticsSellDetailsServiceImpl implements StatisticsSellDetailsService {
    private final Logger logger = LoggerFactory.getLogger(StatisticsSellDetailsServiceImpl.class);

    @Resource
    private SellDetailsDAO sellDetailsDAO;

    @Resource
    private SellZgDetailsDAO sellZgDetailsDAO;

    @Resource
    private AppOrderService orderService;

    @Resource
    private ReturnOrderService returnOrderService;

    @Resource
    private AppCustomerService appCustomerService;

    @Resource
    private GoodsPriceService goodsPriceService;

    @Override
    public List<SellDetailsDO> statisticsCurrentDetails() {
        // 当前时间
        LocalDate now = LocalDate.now();
        // 当前时间减一个月
        now = now.minusMonths(1);
        // 年
        Integer year = now.getYear();
        // 月
        Integer month = now.getMonthValue();

        logger.info(year+"年"+month+"月"+"，开始查询当月销量明细");
        List<SellDetailsDO> sellDetailsDOList = sellDetailsDAO.queryOneMonthSellDetails(year,month);

        return sellDetailsDOList;
    }

    public void addSellDetails(List<SellDetailsDO> sellDetailsDOS){
        if (sellDetailsDOS != null){
            for (SellDetailsDO detailsDO : sellDetailsDOS){
                sellDetailsDAO.addOneDetail(detailsDO);
            }
        }
    }

    @Override
    @Transactional
    public void addOrderSellDetails(String orderNumber){
        if (StringUtils.isNotBlank(orderNumber)){
           OrderBaseInfo orderBaseInfo = orderService.getOrderByOrderNumber(orderNumber);

           /**打上标记 标识已经记录销量**/
           orderBaseInfo.setIsRecordSales(true);
           orderService.updateOrderBaseInfo(orderBaseInfo);

           List<OrderGoodsInfo> orderGoodsInfos = orderService.getOrderGoodsInfoByOrderNumber(orderNumber);

           if (orderBaseInfo != null && orderBaseInfo != null && orderGoodsInfos.size() > 0){
               Date createDate = orderBaseInfo.getCreateTime();
               //date 转 localDateTime
               Instant instant = createDate.toInstant();
               ZoneId zoneId = ZoneId.systemDefault();
               LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

               Integer year = localDateTime.getYear();
               Integer month = localDateTime.getMonthValue();

               List<Long> goodsIds = new ArrayList<>();
               for (OrderGoodsInfo goodsInfo : orderGoodsInfos){
                   // 检查明细是否已经记录
                   Boolean isExit = sellDetailsDAO.isExitByNumberAndGoodsLineId(orderNumber,goodsInfo.getId());

                   if (!isExit){
                       SellDetailsDO detailsDO = new SellDetailsDO();

                       detailsDO.setCompanyId(orderBaseInfo.getSobId());
                       detailsDO.setYear(year);
                       detailsDO.setMonth(month);
                       detailsDO.setCreateTime(new Date());
                       detailsDO.setCityId(orderBaseInfo.getCityId());
                       detailsDO.setStoreId(orderBaseInfo.getStoreOrgId());
                       detailsDO.setSellerId(orderBaseInfo.getSalesConsultId());
                       detailsDO.setSellerName(orderBaseInfo.getSalesConsultName());
                       detailsDO.setCustomerId(orderBaseInfo.getCustomerId());
                       detailsDO.setCustomerPhone(orderBaseInfo.getCustomerPhone());
                       detailsDO.setCustomerName(orderBaseInfo.getCustomerName());
                       detailsDO.setNumber(orderBaseInfo.getOrderNumber());
                       detailsDO.setSku(goodsInfo.getSku());
                       detailsDO.setQuantity(goodsInfo.getOrderQuantity());
                       detailsDO.setAmount(goodsInfo.getReturnPrice());
                       detailsDO.setGoodsLineId(goodsInfo.getId());
                       detailsDO.setOrderSubjectType(orderBaseInfo.getOrderSubjectType());
                       detailsDO.setCreatorIdentityType(orderBaseInfo.getCreatorIdentityType());
                       detailsDO.setCreatorId(orderBaseInfo.getCreatorId());
                       detailsDO.setCreatorName(orderBaseInfo.getCreatorName());
                       detailsDO.setSellDetalsFlag(0);
                       detailsDO.setCompanyFlag(goodsInfo.getCompanyFlag());
                       detailsDO.setGoodsLineType(goodsInfo.getGoodsLineType());

                       sellDetailsDAO.addOneDetail(detailsDO);

                   }
                   goodsIds.add(goodsInfo.getGid());
               }

               /*** 记录专供销量 ****/
               this.recordZgSales(orderBaseInfo,goodsIds,orderGoodsInfos);

           }else{
               logger.info("订单："+orderNumber+"数据异常，生成销量明细失败！");
               //  记录错误日志
               this.recordeErrorLog(orderNumber);
           }
        }
    }

    @Override
    @Transactional
    public void addReturnOrderSellDetails(String returnOrderNumber){
        if (StringUtils.isNotBlank(returnOrderNumber)){
            ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(returnOrderNumber);

            returnOrderBaseInfo.setIsRecordSales(true);
            returnOrderService.modifyReturnOrderBaseInfo(returnOrderBaseInfo);

            List<ReturnOrderGoodsInfo> returnOrderGoodsInfos = returnOrderService.findReturnOrderGoodsInfoByOrderNumber(returnOrderNumber);
            // 订单信息
            OrderBaseInfo orderBaseInfo = orderService.getOrderByOrderNumber(returnOrderBaseInfo.getOrderNo());

            if (orderBaseInfo != null && returnOrderBaseInfo != null && returnOrderGoodsInfos != null && returnOrderGoodsInfos.size() != 0){

                Date returnTime = returnOrderBaseInfo.getReturnTime();
                //date 转 localDateTime
                Instant instant = returnTime.toInstant();
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

                Integer year = localDateTime.getYear();
                Integer month = localDateTime.getMonthValue();

                for (ReturnOrderGoodsInfo goodsInfo : returnOrderGoodsInfos){
                    // 检查明细是否已经记录
                    Boolean isExit = sellDetailsDAO.isExitByNumberAndGoodsLineId(returnOrderNumber,goodsInfo.getId());

                    if (!isExit){
                        SellDetailsDO detailsDO = new SellDetailsDO();

                        detailsDO.setCompanyId(orderBaseInfo.getSobId());
                        detailsDO.setYear(year);
                        detailsDO.setMonth(month);
                        detailsDO.setCreateTime(new Date());
                        detailsDO.setCityId(orderBaseInfo.getCityId());
                        detailsDO.setStoreId(orderBaseInfo.getStoreOrgId());
                        detailsDO.setSellerId(orderBaseInfo.getSalesConsultId());
                        detailsDO.setSellerName(orderBaseInfo.getSalesConsultName());
                        detailsDO.setCustomerId(orderBaseInfo.getCustomerId());
                        detailsDO.setCustomerPhone(orderBaseInfo.getCustomerPhone());
                        detailsDO.setCustomerName(orderBaseInfo.getCustomerName());
                        detailsDO.setNumber(returnOrderNumber);
                        detailsDO.setSku(goodsInfo.getSku());
                        detailsDO.setQuantity(goodsInfo.getReturnQty());
                        detailsDO.setAmount(-goodsInfo.getReturnPrice());
                        detailsDO.setGoodsLineId(goodsInfo.getId());
                        detailsDO.setOrderSubjectType(orderBaseInfo.getOrderSubjectType());
                        detailsDO.setCreatorIdentityType(orderBaseInfo.getCreatorIdentityType());
                        detailsDO.setCreatorId(orderBaseInfo.getCreatorId());
                        detailsDO.setCreatorName(orderBaseInfo.getCreatorName());
                        detailsDO.setSellDetalsFlag(1);
                        detailsDO.setCompanyFlag(goodsInfo.getCompanyFlag());
                        detailsDO.setGoodsLineType(goodsInfo.getGoodsLineType());

                        sellDetailsDAO.addOneDetail(detailsDO);
                    }
                }
            }else{
                logger.info("订单："+returnOrderNumber+"数据异常，生成销量明细失败！");
                // 记录错误日志
                this.recordeErrorLog(returnOrderNumber);
            }
        }
    }

    /**
     * 根据销量时间倒叙取四条记录单号
     * @return
     */
    @Override
    public List<String> getCustomerSellDetailsOrderByCreateTimeDescLimit4(Long cusId,LocalDateTime dateTime,Long sellerId){
        if (cusId == null || sellerId == null || dateTime == null){
            return  null;
        }

        return sellDetailsDAO.getCustomerSellDetailsOrderByCreateTimeDescLimit4(cusId,dateTime,sellerId);
    }

    @Override
    public List<String> getSellDetailsFrequencyBycusIdAndSellerIdAndCreateTime(Long cusId,LocalDateTime dateTime,Long sellerId){
        if (cusId == null || sellerId == null || dateTime == null){
            return  null;
        }

        return sellDetailsDAO.getSellDetailsFrequencyBycusIdAndSellerIdAndCreateTime(cusId,dateTime,sellerId);
    }

    @Override
    public void recordeErrorLog(String orderNo){
        if (orderNo != null || !orderNo.equals("")){
            SellDetailsErrorLogDO log = new SellDetailsErrorLogDO();
            log.setOrderNo(orderNo);
            log.setRecordTime(new Date());
            log.setStatus(false);

            sellDetailsDAO.recordeErrorLog(log);
        }
    }

    /******************************** 专供销量 *********************************************/

    /**
     * 新增
     * @param detailsDOS
     */
    public void addZgDetailsList(List<SellZgDetailsDO> detailsDOS){
        if (detailsDOS != null || detailsDOS.size() > 0){
            for (SellZgDetailsDO details : detailsDOS) {
                sellZgDetailsDAO.addOneDetail(details);
            }
        }
    }

    private Boolean recordZgSales(OrderBaseInfo orderBaseInfo,List<Long> goodsIds,List<OrderGoodsInfo> orderGoodsInfoList){
        Long cusId = orderBaseInfo.getCustomerId();
        //判断会员身份
        CustomerRankInfoResponse customerRankInfoResponse = appCustomerService.findCusRankinfoByCusId(cusId);
        AppCustomer appCustomer = null;
        try {
            appCustomer = appCustomerService.findById(cusId);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.info("计算专供促销，顾客id:"+cusId+"找不到顾客信息");
        }

        if (customerRankInfoResponse == null){
            // 非专供会员 不能参与专供促销
            return false;
        }

        // 根据用户购买产品id 返回专供产品
        List<GiftListResponseGoods> goodsZGList = goodsPriceService.findGoodsPriceListByGoodsIdsAndUserId(goodsIds,cusId, AppIdentityType.CUSTOMER);

        if (goodsZGList == null || goodsZGList.size() == 0){
            // 无专供产品
            return false;
        }

        Date createDate = orderBaseInfo.getCreateTime();
        //date 转 localDateTime
        Instant instant = createDate.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

        Integer year = localDateTime.getYear();
        Integer month = localDateTime.getMonthValue();

        for ( GiftListResponseGoods zgGoods : goodsZGList){
            for (OrderGoodsInfo goodsInfo : orderGoodsInfoList){
                if (zgGoods.getSku().equals(goodsInfo.getSku())){

                    if (goodsInfo.getGoodsLineType().equals(AppGoodsLineType.PRESENT)){
                        // 赠品则不记录销量
                        continue;
                    }

                    SellZgDetailsDO detailsDO = new SellZgDetailsDO();

                    detailsDO.setCompanyId(orderBaseInfo.getSobId());
                    detailsDO.setYear(year);
                    detailsDO.setMonth(month);
                    detailsDO.setCreateTime(new Date());
                    detailsDO.setCityId(orderBaseInfo.getCityId());
                    detailsDO.setStoreId(orderBaseInfo.getStoreOrgId());
                    detailsDO.setSellerId(orderBaseInfo.getSalesConsultId());
                    detailsDO.setSellerName(orderBaseInfo.getSalesConsultName());
                    detailsDO.setCustomerId(orderBaseInfo.getCustomerId());
                    detailsDO.setCustomerPhone(orderBaseInfo.getCustomerPhone());
                    detailsDO.setCustomerName(orderBaseInfo.getCustomerName());
                    detailsDO.setNumber(orderBaseInfo.getOrderNumber());
                    detailsDO.setGoodsId(goodsInfo.getGid());
                    detailsDO.setSku(goodsInfo.getSku());
                    detailsDO.setQuantity(goodsInfo.getOrderQuantity());
                    detailsDO.setAmount(goodsInfo.getReturnPrice());
                    detailsDO.setGoodsLineId(goodsInfo.getId());

                    // 检查是否已经记录

                    Boolean exitFlag = sellZgDetailsDAO.isExitByNumberAndGoodsLineId(orderBaseInfo.getOrderNumber(),goodsInfo.getId());
                    if(!exitFlag){
                        sellZgDetailsDAO.addOneDetail(detailsDO);
                    }

                }
            }
        }

        return true;
    }

    /**
     * 返回会员专供产品累计桶数
     * @return
     */
    public Integer getZgTsBycusIdAndsku(Long cusId,String sku){
        Integer totalQty = 0;

        if (cusId == null || sku == null || sku.equals("")){

        }else{
            totalQty = sellZgDetailsDAO.getQtyByCusIdAndSku(cusId,sku);

            if (totalQty == null){
                totalQty = 0;
            }
        }
        return  totalQty;
    }

    /**
     * 根据专供会员id和返回专供销量结果结果
     * @param cusId
     * @param sku
     * @return
     */
    public List<SellZgDetailsDO> getZgDetailsByCusIdAndSku(Long cusId,String sku){
        if (cusId == null || sku == null || sku.equals("")){
            return null;
        }

        return sellZgDetailsDAO.getDetailsByCusIdAndSku(cusId,sku);
    }

    public List<SellZgDetailsDO> getZgDetailsByCusId(Long cusId){
        if (cusId == null){
            return null;
        }

        return sellZgDetailsDAO.getDetailsByCusId(cusId);
    }

    public SellZgCusTimes getTimesByCusIdAndSku(Long cusId,String sku,ActBaseType actBaseType){
        if (cusId == null){
            return null;
        }

        return sellZgDetailsDAO.getTimesByCusIdAndSku(cusId,sku,actBaseType);
    }

    public void updateSellZgCusTimes(SellZgCusTimes sellZgCusTimes){
        if (sellZgCusTimes != null){
            sellZgDetailsDAO.updateSellZgCusTimes(sellZgCusTimes);
        }
    }

    public void addSellZgCusTimes(SellZgCusTimes sellZgCusTimes){
        if (sellZgCusTimes != null){
            sellZgDetailsDAO.addSellZgCusTimes(sellZgCusTimes);
        }
    }


    /**
     * 统计个人总销量
     */
}
