package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.SellDetailsDAO;
import cn.com.leyizhuang.app.foundation.pojo.SellDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.remote.queue.MqSellDetailsChannel;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.MaOrderVO;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private AppOrderService orderService;

    @Resource
    private ReturnOrderService returnOrderService;

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
           List<OrderGoodsInfo> orderGoodsInfos = orderService.getOrderGoodsInfoByOrderNumber(orderNumber);

           if (orderBaseInfo != null && orderBaseInfo != null && orderGoodsInfos.size() > 0){
               Date createDate = orderBaseInfo.getCreateTime();
               //date 转 localDateTime
               Instant instant = createDate.toInstant();
               ZoneId zoneId = ZoneId.systemDefault();
               LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

               Integer year = localDateTime.getYear();
               Integer month = localDateTime.getMonthValue();

               for (OrderGoodsInfo goodsInfo : orderGoodsInfos){
                   // 检查明细是否已经记录
                   Boolean isExit = sellDetailsDAO.isExitByNumberAndGoodsLineId(orderNumber,goodsInfo.getId());

                   if (!isExit){
                       SellDetailsDO detailsDO = new SellDetailsDO();

                       detailsDO.setCompanyId(orderBaseInfo.getSobId());
                       detailsDO.setYear(year);
                       detailsDO.setMonth(month);
                       detailsDO.setCityId(orderBaseInfo.getCityId());
                       detailsDO.setStoreId(orderBaseInfo.getStoreOrgId());
                       detailsDO.setSellerId(orderBaseInfo.getSalesConsultId());
                       detailsDO.setCustomerPhone(orderBaseInfo.getCustomerPhone());
                       detailsDO.setCustomerName(orderBaseInfo.getCustomerName());
                       detailsDO.setNumber(orderBaseInfo.getOrderNumber());
                       detailsDO.setSku(goodsInfo.getSku());
                       detailsDO.setQuantity(goodsInfo.getOrderQuantity());
                       detailsDO.setAmount(goodsInfo.getSettlementPrice());
                       detailsDO.setGoodsLineId(goodsInfo.getId());

                       sellDetailsDAO.addOneDetail(detailsDO);
                   }
               }

           }else{
               logger.info("订单："+orderNumber+"数据异常，生成销量明细失败！");
               // TODO 记录错误日志
           }
        }
    }

    @Override
    @Transactional
    public void addReturnOrderSellDetails(String returnOrderNumber){
        if (StringUtils.isNotBlank(returnOrderNumber)){
            ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(returnOrderNumber);
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
                        detailsDO.setCityId(orderBaseInfo.getCityId());
                        detailsDO.setStoreId(orderBaseInfo.getStoreOrgId());
                        detailsDO.setSellerId(orderBaseInfo.getSalesConsultId());
                        detailsDO.setCustomerPhone(orderBaseInfo.getCustomerPhone());
                        detailsDO.setCustomerName(orderBaseInfo.getCustomerName());
                        detailsDO.setNumber(orderBaseInfo.getOrderNumber());
                        detailsDO.setSku(goodsInfo.getSku());
                        detailsDO.setQuantity(goodsInfo.getReturnQty());
                        detailsDO.setAmount(-goodsInfo.getSettlementPrice());
                        detailsDO.setGoodsLineId(goodsInfo.getId());

                        sellDetailsDAO.addOneDetail(detailsDO);
                    }
                }
            }else{
                logger.info("订单："+returnOrderNumber+"数据异常，生成销量明细失败！");
                // TODO 记录错误日志
            }
        }
    }
}
