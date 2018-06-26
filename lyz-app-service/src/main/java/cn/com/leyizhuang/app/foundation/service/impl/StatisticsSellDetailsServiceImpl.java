package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.ActBaseType;
import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.ArrayListUtils;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.SellDetailsDAO;
import cn.com.leyizhuang.app.foundation.dao.SellZgDetailsDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.util.CountUtil;
import cn.com.leyizhuang.common.util.WeekDateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Resource
    private AppEmployeeService appEmployeeService;

    @Resource
    private AppStoreService appStoreService;

    @Resource
    private WmsToAppOrderService wmsToAppOrderService;

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

        logger.info(year + "年" + month + "月" + "，开始查询当月销量明细");
        List<SellDetailsDO> sellDetailsDOList = sellDetailsDAO.queryOneMonthSellDetails(year, month);

        return sellDetailsDOList;
    }

    /**
     * 以为单位组织计算 并统计所有导购销量情况 持久化至sell_details_single
     */
    public void statisticsAllSellerSellDetails(List<String> structureCode){
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 当月1号 0 点 0 分 0 秒
        LocalDateTime firstDay = LocalDateTime.of(now.getYear(),now.getMonth(),1,0,0,0);


        for (String code : structureCode){
            // 获取组织下的导购
            List<SellerResponse> employeeList = appEmployeeService.querySellerByStructureCode(code);

            // 循环遍历
            for (SellerResponse employee : employeeList) {

                try{
                    if (employee.getStatus().equals(false)){
                        continue;
                    }
                    Long empId = employee.getSellerId();
                    String empName = employee.getSellerName();
                    if (empId.equals(205L)){
                        System.out.println("123");
                    }
                    // 统计高端桶数
                    Integer gdts = this.countGDTS(firstDay,now,empId,code);

                    SellDetailsSingleDO gdtsDO = new SellDetailsSingleDO();

                    gdtsDO.setCreateTime(now);
                    gdtsDO.setUpdateTime(now);
                    gdtsDO.setYear(now.getYear());
                    gdtsDO.setMonth(now.getMonthValue());
                    gdtsDO.setFinishQty(gdts);
                    gdtsDO.setStructureCode(code);
                    gdtsDO.setFlag("TS");
                    gdtsDO.setSellerId(empId);
                    gdtsDO.setSellerName(empName);

                    Long id1 = sellDetailsDAO.isExitSellDetailsSingle(now.getYear(),now.getMonthValue(),empId,"TS");
                    if (id1 != null){
                        gdtsDO.setId(id1);
                        sellDetailsDAO.updateSellDetailsSingle(gdtsDO);
                    }else {
                        sellDetailsDAO.addSellDetailsSingle(gdtsDO);
                    }

                    // 统计活跃会员数
                    Integer hys = this.countHYS(firstDay,now,employee.getSellerId(),code);
                    SellDetailsSingleDO hysDO = new SellDetailsSingleDO();

                    hysDO.setCreateTime(now);
                    hysDO.setUpdateTime(now);
                    hysDO.setYear(now.getYear());
                    hysDO.setMonth(now.getMonthValue());
                    hysDO.setFinishQty(hys);
                    hysDO.setStructureCode(code);
                    hysDO.setFlag("HYS");
                    hysDO.setSellerId(empId);
                    hysDO.setSellerName(empName);

                    Long id2 = sellDetailsDAO.isExitSellDetailsSingle(now.getYear(),now.getMonthValue(),empId,"HYS");
                    if (id2 != null){
                        hysDO.setId(id2);
                        sellDetailsDAO.updateSellDetailsSingle(hysDO);
                    }else {
                        sellDetailsDAO.addSellDetailsSingle(hysDO);
                    }

                    if (empId.equals(386L)){
                        System.out.println("123");
                    }

                    // 统计新开发高端会员数
                    LocalDateTime halfYearAgoDate = LocalDateTime.now().plusMonths(6);
                    Integer xkf = this.countXKF(firstDay,firstDay,LocalDateTime.now(),halfYearAgoDate,employee.getSellerId(),code);

                    SellDetailsSingleDO xkfDO = new SellDetailsSingleDO();

                    xkfDO.setCreateTime(now);
                    xkfDO.setUpdateTime(now);
                    xkfDO.setYear(now.getYear());
                    xkfDO.setMonth(now.getMonthValue());
                    xkfDO.setFinishQty(xkf);
                    xkfDO.setStructureCode(code);
                    xkfDO.setFlag("XKF");
                    xkfDO.setSellerId(empId);
                    xkfDO.setSellerName(empName);

                    Long id3 = sellDetailsDAO.isExitSellDetailsSingle(now.getYear(),now.getMonthValue(),empId,"XKF");
                    if (id3 != null){
                        xkfDO.setId(id3);
                        sellDetailsDAO.updateSellDetailsSingle(xkfDO);
                    }else {
                        sellDetailsDAO.addSellDetailsSingle(xkfDO);
                    }

                    // 统计销量
                    Double sales = this.countXL(empId);

                    SellDetailsSingleDO xlDO = new SellDetailsSingleDO();

                    xlDO.setCreateTime(now);
                    xlDO.setUpdateTime(now);
                    xlDO.setYear(now.getYear());
                    xlDO.setMonth(now.getMonthValue());
                    xlDO.setFinishSales(sales);
                    xlDO.setStructureCode(code);
                    xlDO.setFlag("XL");
                    xlDO.setSellerId(empId);
                    xlDO.setSellerName(empName);

                    Long id4 = sellDetailsDAO.isExitSellDetailsSingle(now.getYear(),now.getMonthValue(),empId,"XL");
                    if (id4 != null){
                        xlDO.setId(id3);
                        sellDetailsDAO.updateSellDetailsSingle(xlDO);
                    }else {
                        sellDetailsDAO.addSellDetailsSingle(xlDO);
                    }

                    // 统计周完成情况
                    this.countWeekXKF(firstDay,halfYearAgoDate,empId,code,xkfDO.getId());

                }catch (Exception e){
                    logger.info(e.getLocalizedMessage());
                    // 记录失败
                    logger.info(now+ " 导购：" + employee.getSellerName() + "业绩统计失败！");
                    e.printStackTrace();
                    SellDetailsStatisticErrorLog log = new SellDetailsStatisticErrorLog();

                    log.setCreateTime(new Date());
                    log.setErrorMsg(e.getMessage());
                    log.setSellerId(employee.getSellerId());

                    sellDetailsDAO.addEmpPerformanceStatisticErrorLog(log);
                }
            }
        }
    }

    public Integer countGDTS(LocalDateTime startTime,LocalDateTime endTime , Long sellerId , String structureCode){
        Integer orderQty = sellDetailsDAO.countGDTS(startTime,endTime,sellerId,structureCode,"0");
        Integer returnQty = sellDetailsDAO.countGDTS(startTime,endTime,sellerId,structureCode,"1");

        if (orderQty == null){
            orderQty = 0;
        }

        if (returnQty == null){
            returnQty = 0;
        }

        Integer gdQty = (orderQty - returnQty) < 0 ? 0 : (orderQty - returnQty);
        return  gdQty;
    }

    public Integer countHYS(LocalDateTime startTime,LocalDateTime endTime , Long sellerId,String structureCode){
        if (sellerId.equals(488L)){
            System.out.println("123");
        }

        Integer qty = 0;
        List<Long> list = sellDetailsDAO.countHYS(startTime,endTime,sellerId,structureCode);
        if (list != null){
            qty = list.size();
        }
        return qty;
    }

    public Integer countXKF(LocalDateTime firstDay,LocalDateTime startTime,LocalDateTime endTime,LocalDateTime halfYearAgoDate,Long sellerId,String  structureCode){
        Integer qty = 0;
        List<Long> list = sellDetailsDAO.countXKF(firstDay,startTime,endTime,halfYearAgoDate,sellerId,structureCode);

        if(sellerId.equals(562L)){
            System.out.println("123");
        }

        if (list != null && list.size() > 0){
            /**  过滤2018年4月1号前 已经是高端的活跃会员 **/
            /** 2018年10月1号之前都需要执行 **/

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime date = LocalDateTime.of(2018,10,1,0,0,1);

            if (now.isBefore(date)){
                list = sellDetailsDAO.cusIdFiltration(list,sellerId);
            }

            if (list != null || list.size() > 0) {
                qty = list.size();
            }
        }

        return qty;
    }

    public Double countXL(Long sellerId){
        // 计算截至头一天的销量
        LocalDateTime time = LocalDateTime.now().minusDays(1);

        LocalDateTime startTime = LocalDateTime.of(time.getYear(),time.getMonth(),1,0,0,0);
        LocalDateTime endTime = LocalDateTime.of(time.getYear(),time.getMonth(),time.getDayOfMonth(),23,59,59);

        if (sellerId == null || sellerId < 0)
        {
            return 0.00;
        }
        Double sales = 0D;
        Double result = sellDetailsDAO.countXL(startTime,endTime,sellerId);
        sales = result == null ? 0D:result;

        return  sales;
    }

    public void countWeekXKF(LocalDateTime fristDay,LocalDateTime halfYearAgoDate,Long sellerId,String  structureCode,Long headId){
        List<Map<String, LocalDateTime>> weekMapList =  WeekDateUtil.getWeekDate();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        Integer xkfQty = 0;

        for (int i = 0 ; i < weekMapList.size() ; i++){
            Map<String , LocalDateTime> weekMap = weekMapList.get(i);

            SellDetailsWeekFinishResponse response = new SellDetailsWeekFinishResponse();

            response.setHeadId(headId);
            response.setWeek(weekMapList.size() - i);
            response.setSellerId(sellerId);

            if (headId == 10843){
                System.out.println("123");
            }
            if (i == weekMapList.size()-1){
                // 第一周
                startTime = fristDay;
                endTime = weekMap.get("Sun");
                endTime = LocalDateTime.of(endTime.getYear(),endTime.getMonthValue(),endTime.getDayOfMonth(),23,59,59);
                xkfQty = this.countXKF(fristDay,startTime,endTime,halfYearAgoDate,sellerId,structureCode);
            }else if (i == 0 && weekMapList.size() > 1){
                // 当前周
                startTime = weekMap.get("Mon");
                endTime = now;
                xkfQty = this.countXKF(fristDay,startTime,endTime,halfYearAgoDate,sellerId,structureCode);

            }else  {
                startTime = weekMap.get("Mon");
                endTime = weekMap.get("Sun");
                endTime = LocalDateTime.of(endTime.getYear(),endTime.getMonthValue(),endTime.getDayOfMonth(),23,59,59);
                xkfQty = this.countXKF(fristDay,startTime,endTime,halfYearAgoDate,sellerId,structureCode);
            }

            if (xkfQty == null){
                xkfQty = 0;
            }

            response.setStartDate(startTime);
            response.setEndDate(endTime);
            response.setFinishQty(xkfQty);

            Long id = sellDetailsDAO.isExitSellDetailsWeek(headId,response.getWeek());

            if (id == null){
                sellDetailsDAO.addSellDetailsWeekFinish(response);
            }else {
                response.setId(id);
                sellDetailsDAO.updateSellDetailsWeekFinish(response);
            }

        }

    }

    public void statisticOneSeller(Long empId){
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 当月1号 0 点 0 分 0 秒
        LocalDateTime firstDay = LocalDateTime.of(now.getYear(),now.getMonth(),1,0,0,0);
        AppEmployee employee = appEmployeeService.findById(empId);

        if (employee != null){
            String structureCode = "";
            AppStore store = appStoreService.findById(employee.getStoreId());

            if(store != null){
                String[] codeArr = store.getStoreStructureCode().split("|");
                // 取第二个code
                structureCode = codeArr[1];

                String empName = employee.getName();

                // 统计高端桶数
                Integer gdts = this.countGDTS(firstDay,now,empId,structureCode);

                SellDetailsSingleDO gdtsDO = new SellDetailsSingleDO();

                gdtsDO.setCreateTime(now);
                gdtsDO.setUpdateTime(now);
                gdtsDO.setYear(now.getYear());
                gdtsDO.setMonth(now.getMonthValue());
                gdtsDO.setFinishQty(gdts);
                gdtsDO.setStructureCode(structureCode);
                gdtsDO.setFlag("TS");
                gdtsDO.setSellerId(empId);
                gdtsDO.setSellerName(empName);

                Long id1 = sellDetailsDAO.isExitSellDetailsSingle(now.getYear(),now.getMonthValue(),empId,"TS");
                if (id1 != null){
                    gdtsDO.setId(id1);
                    sellDetailsDAO.updateSellDetailsSingle(gdtsDO);
                }else {
                    sellDetailsDAO.addSellDetailsSingle(gdtsDO);
                }

                // 统计活跃会员数
                Integer hys = this.countHYS(firstDay,now,employee.getEmpId(),structureCode);
                SellDetailsSingleDO hysDO = new SellDetailsSingleDO();

                hysDO.setCreateTime(now);
                hysDO.setUpdateTime(now);
                hysDO.setYear(now.getYear());
                hysDO.setMonth(now.getMonthValue());
                hysDO.setFinishQty(hys);
                hysDO.setStructureCode(structureCode);
                hysDO.setFlag("HYS");
                hysDO.setSellerId(empId);
                hysDO.setSellerName(empName);

                Long id2 = sellDetailsDAO.isExitSellDetailsSingle(now.getYear(),now.getMonthValue(),empId,"HYS");
                if (id2 != null){
                    hysDO.setId(id2);
                    sellDetailsDAO.updateSellDetailsSingle(hysDO);
                }else {
                    sellDetailsDAO.addSellDetailsSingle(hysDO);
                }

                if (empId.equals(386L)){
                    System.out.println("123");
                }

                // 统计新开发高端会员数
                LocalDateTime halfYearAgoDate = LocalDateTime.now().plusMonths(6);
                Integer xkf = this.countXKF(firstDay,firstDay,LocalDateTime.now(),halfYearAgoDate,employee.getEmpId(),structureCode);

                SellDetailsSingleDO xkfDO = new SellDetailsSingleDO();

                xkfDO.setCreateTime(now);
                xkfDO.setUpdateTime(now);
                xkfDO.setYear(now.getYear());
                xkfDO.setMonth(now.getMonthValue());
                xkfDO.setFinishQty(xkf);
                xkfDO.setStructureCode(structureCode);
                xkfDO.setFlag("XKF");
                xkfDO.setSellerId(empId);
                xkfDO.setSellerName(empName);

                Long id3 = sellDetailsDAO.isExitSellDetailsSingle(now.getYear(),now.getMonthValue(),empId,"XKF");
                if (id3 != null){
                    xkfDO.setId(id3);
                    sellDetailsDAO.updateSellDetailsSingle(xkfDO);
                }else {
                    sellDetailsDAO.addSellDetailsSingle(xkfDO);
                }

                // 统计销量
                Double sales = this.countXL(empId);

                SellDetailsSingleDO xlDO = new SellDetailsSingleDO();

                xlDO.setCreateTime(now);
                xlDO.setUpdateTime(now);
                xlDO.setYear(now.getYear());
                xlDO.setMonth(now.getMonthValue());
                xlDO.setFinishSales(sales);
                xlDO.setStructureCode(structureCode);
                xlDO.setFlag("XL");
                xlDO.setSellerId(empId);
                xlDO.setSellerName(empName);

                Long id4 = sellDetailsDAO.isExitSellDetailsSingle(now.getYear(),now.getMonthValue(),empId,"XL");
                if (id4 != null){
                    xlDO.setId(id3);
                    sellDetailsDAO.updateSellDetailsSingle(xlDO);
                }else {
                    sellDetailsDAO.addSellDetailsSingle(xlDO);
                }

                // 统计周完成情况
                this.countWeekXKF(firstDay,halfYearAgoDate,empId,structureCode,xkfDO.getId());
            }else {
                logger.info("找不到门店");
            }

        }else {
            logger.info("找不到导购");
        }

    }

    public void addSellDetails(List<SellDetailsDO> sellDetailsDOS) {
        if (sellDetailsDOS != null) {
            for (SellDetailsDO detailsDO : sellDetailsDOS) {
                sellDetailsDAO.addOneDetail(detailsDO);
            }
        }
    }

    @Override
    @Transactional(rollbackFor={RuntimeException.class, Exception.class})
    public void addOrderSellDetails(String orderNumber) throws Exception {
        if (StringUtils.isNotBlank(orderNumber)) {
            OrderBaseInfo orderBaseInfo = orderService.getOrderByOrderNumber(orderNumber);


            List<OrderGoodsInfo> orderGoodsInfos = orderService.getOrderGoodsInfoByOrderNumber(orderNumber);
            /** 判断是否结清 结清才记录销量 **/
            OrderBillingDetails billingDetails = orderService.getOrderBillingDetail(orderNumber);

            if (orderBaseInfo != null && orderBaseInfo != null && orderGoodsInfos.size() > 0) {
                if (billingDetails.getIsPayUp()){
                    Date createDate = new Date();
//                AppDeliveryType deliveryType = orderBaseInfo.getDeliveryType();
////                if (deliveryType.equals(AppDeliveryType.HOUSE_DELIVERY) || deliveryType.equals(AppDeliveryType.HOUSE_DELIVERY.getValue())){
////                    // 配送单 取发货时间
////                    WtaShippingOrderHeader header = wmsToAppOrderService.getWtaShippingOrderHeader(orderNumber);
////                    if (header != null && header.getSendFlag().equals(true)){
////                        createDate = header.getEndDt();
////                    }
////                }

                    //date 转 localDateTime
                    Instant instant = createDate.toInstant();
                    ZoneId zoneId = ZoneId.systemDefault();
                    LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

                    Integer year = localDateTime.getYear();
                    Integer month = localDateTime.getMonthValue();

                    List<Long> goodsIds = new ArrayList<>();
                    for (OrderGoodsInfo goodsInfo : orderGoodsInfos) {
                        // 检查明细是否已经记录
                        Boolean isExit = sellDetailsDAO.isExitByNumberAndGoodsLineId(orderNumber, goodsInfo.getId());

                        if (!isExit) {
                            SellDetailsDO detailsDO = new SellDetailsDO();

                            detailsDO.setCompanyId(orderBaseInfo.getSobId());
                            detailsDO.setYear(year);
                            detailsDO.setMonth(month);
                            detailsDO.setCreateTime(createDate);
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
                            if (goodsInfo.getGoodsLineType().equals(AppGoodsLineType.PRODUCT_COUPON)){
                                Double amount = orderService.getOrderProductCouponPurchasePrice(orderNumber,goodsInfo.getSku());
                                detailsDO.setAmount(amount);
                            }else{
                                detailsDO.setAmount(goodsInfo.getReturnPrice());
                            }
                            detailsDO.setGoodsLineId(goodsInfo.getId());
                            detailsDO.setOrderSubjectType(orderBaseInfo.getOrderSubjectType());
                            detailsDO.setCreatorIdentityType(orderBaseInfo.getCreatorIdentityType());
                            detailsDO.setCreatorId(orderBaseInfo.getCreatorId());
                            detailsDO.setCreatorName(orderBaseInfo.getCreatorName());
                            detailsDO.setSellDetalsFlag(0);
                            detailsDO.setCompanyFlag(goodsInfo.getCompanyFlag());
                            detailsDO.setGoodsLineType(goodsInfo.getGoodsLineType());
//                            Integer valid = sellDetailsDAO.getGDTSByOrdernumber(orderNumber,goodsInfo.getSku());
//                            detailsDO.setValidCouponQty(valid);
                            sellDetailsDAO.addOneDetail(detailsDO);

                        }
                        goodsIds.add(goodsInfo.getGid());
                    }

                    /*** 记录专供销量 ****/
                    this.recordZgSales(orderBaseInfo, goodsIds, orderGoodsInfos);

                    /**打上标记 标识已经记录销量**/
                    orderBaseInfo.setIsRecordSales(true);
                    orderService.updateOrderBaseInfo(orderBaseInfo);
                }else {
                    logger.info(orderNumber+"未结清");
                }
            } else {
                logger.info("订单：" + orderNumber + "数据异常，生成销量明细失败！");
                //  记录错误日志
                this.recordeErrorLog(orderNumber,"找不到订单");
                throw new RuntimeException("找不到订单或者商品明细");
            }
        }
    }

    @Override
    @Transactional(rollbackFor={RuntimeException.class, Exception.class})
    public void addReturnOrderSellDetails(String returnOrderNumber) throws Exception {
        if (StringUtils.isNotBlank(returnOrderNumber)) {
            ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(returnOrderNumber);


            List<ReturnOrderGoodsInfo> returnOrderGoodsInfos = returnOrderService.findReturnOrderGoodsInfoByOrderNumber(returnOrderNumber);
            // 订单信息
            OrderBaseInfo orderBaseInfo = orderService.getOrderByOrderNumber(returnOrderBaseInfo.getOrderNo());

            if (orderBaseInfo != null && returnOrderBaseInfo != null && returnOrderGoodsInfos != null && returnOrderGoodsInfos.size() != 0) {

                Date returnTime = new Date();

                // TODO 如果是配送单 取wms反配上架时间

                //date 转 localDateTime
                Instant instant = returnTime.toInstant();
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

                Integer year = localDateTime.getYear();
                Integer month = localDateTime.getMonthValue();

                for (ReturnOrderGoodsInfo goodsInfo : returnOrderGoodsInfos) {
                    // 检查明细是否已经记录
                    Boolean isExit = sellDetailsDAO.isExitByNumberAndGoodsLineId(returnOrderNumber, goodsInfo.getId());

                    if (!isExit) {
                        SellDetailsDO detailsDO = new SellDetailsDO();

                        detailsDO.setCompanyId(orderBaseInfo.getSobId());
                        detailsDO.setYear(year);
                        detailsDO.setMonth(month);
                        detailsDO.setCreateTime(returnTime);
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
                        if (goodsInfo.getGoodsLineType().equals(AppGoodsLineType.PRODUCT_COUPON)){
                            Double amount = orderService.getOrderProductCouponPurchasePrice(orderBaseInfo.getOrderNumber(),goodsInfo.getSku());
                            detailsDO.setAmount(-amount);
                        }else{
                            detailsDO.setAmount(-goodsInfo.getReturnPrice());
                        }
                        detailsDO.setGoodsLineId(goodsInfo.getId());
                        detailsDO.setOrderSubjectType(orderBaseInfo.getOrderSubjectType());
                        detailsDO.setCreatorIdentityType(orderBaseInfo.getCreatorIdentityType());
                        detailsDO.setCreatorId(orderBaseInfo.getCreatorId());
                        detailsDO.setCreatorName(orderBaseInfo.getCreatorName());
                        detailsDO.setSellDetalsFlag(1);
                        detailsDO.setCompanyFlag(goodsInfo.getCompanyFlag());
                        detailsDO.setGoodsLineType(goodsInfo.getGoodsLineType());
//                        Integer valid = sellDetailsDAO.getGDTSByReturnNo(returnOrderNumber,goodsInfo.getSku());
//                        detailsDO.setValidCouponQty(-valid);
                        sellDetailsDAO.addOneDetail(detailsDO);
                    }
                }

                returnOrderBaseInfo.setIsRecordSales(true);
                returnOrderService.modifyReturnOrderBaseInfo(returnOrderBaseInfo);
            } else {
                logger.info("订单：" + returnOrderNumber + "数据异常，生成销量明细失败！");
                // 记录错误日志
                this.recordeErrorLog(returnOrderNumber,"找不到退单,退单明细，或者订单信息");
                throw  new RuntimeException("找不到退单");
            }
        }
    }

    /**
     * 根据销量时间倒叙取四条记录单号
     *
     * @return
     */
    @Override
    public List<String> getCustomerSellDetailsOrderByCreateTimeDescLimit4(Long cusId, LocalDateTime dateTime, Long sellerId) {
        if (cusId == null || sellerId == null || dateTime == null) {
            return null;
        }

        return sellDetailsDAO.getCustomerSellDetailsOrderByCreateTimeDescLimit4(cusId, dateTime, sellerId);
    }

    @Override
    public PageInfo<AppCustomer> getCustomerIsDefaultStoreAndNoSellDetailsOrder(Long sellerId, Integer page, Integer size) {
        if (sellerId != null) {
            PageHelper.startPage(page, size);
            List<AppCustomer> appCustomerList = sellDetailsDAO.getCustomerIsDefaultStoreAndNoSellDetailsOrder(sellerId);
            return new PageInfo<>(appCustomerList);
        }
        return null;
    }

    @Override
    public List<String> getSellDetailsFrequencyBycusIdAndSellerIdAndCreateTime(Long cusId, LocalDateTime dateTime, Long sellerId) {
        if (cusId == null || sellerId == null || dateTime == null) {
            return null;
        }

        return sellDetailsDAO.getSellDetailsFrequencyBycusIdAndSellerIdAndCreateTime(cusId, dateTime, sellerId);
    }

    @Override
    public void recordeErrorLog(String orderNo,String msg) {
        if (orderNo != null || !orderNo.equals("")) {
            SellDetailsErrorLogDO log = new SellDetailsErrorLogDO();
            log.setOrderNo(orderNo);
            log.setRecordTime(new Date());
            log.setStatus(false);
            log.setErrorMsg(msg);

            sellDetailsDAO.recordeErrorLog(log);
        }
    }

    @Override
    public void createAllOrderDetails() {
        // 取所有已出货 结清 没有记录销量的订单
        List<String> orderNos = orderService.getNotSellDetailsOrderNOs(false);
        LocalDateTime startTime = LocalDateTime.now();
        logger.info("***********生成订单销量，一共有："+orderNos.size()+"条*****************");
        for (String orderNo : orderNos){
            try {
                this.addOrderSellDetails(orderNo);
            }catch (Exception e){
                e.printStackTrace();
                logger.info(e.getMessage());
                // 记录错误日志
                this.recordeErrorLog(orderNo,e.getMessage());
            }
        }
        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime,endTime);
        logger.info("生成完毕，用时:"+duration.toMinutes() + "分钟");
    }

    @Override
    public void createAllreturnOrderDetails(){
        // 取出所有反配上架 没有记录退单销量的订单

        List<String> orderNos = returnOrderService.getNotReturnDetailsReturnNos(false);
        LocalDateTime startTime = LocalDateTime.now();
        logger.info("***********生成退单销量，一共有："+orderNos.size()+"条*****************");
        for (String orderNo : orderNos){
            try {
                this.addReturnOrderSellDetails(orderNo);
            } catch (Exception e) {
                e.printStackTrace();
                logger.info(e.getMessage());
                // 记录错误日志
                this.recordeErrorLog(orderNo,e.getMessage());
            }
        }
        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime,endTime);
        logger.info("生成完毕，用时:"+duration.toMinutes() + "分钟");
    }

    /**
     * 修复销量 未结清的 产品券退货价未0的
     */
    @Override
    public void repairAllOrderDetails() {
        // 先将未结清销量对应的订单标志设置为 false
        sellDetailsDAO.updateOrderRecordFlagFalse();

        // 找到未结清销量数据 并删除
        List<Long> ids = sellDetailsDAO.getNotPayUpSellDetails();
        logger.info("***********一共有："+ids.size()+"条未结清*****************");
        //将list按100长度拆分
        List<List<Long>> lists = ArrayListUtils.splitList(ids,100);

        for (List<Long> subIds : lists) {
            // 删除
            if (subIds.size() > 0){
                sellDetailsDAO.deleteSellDetailsByIdList(subIds);
            }
        }

        // 找到产品券 销量金额为0的数据
        List<SellDetailsDO> sellDetailsDOS = sellDetailsDAO.getProductSellDetails();
        logger.info("***********一共有："+sellDetailsDOS.size()+"条产品券价格为0*****************");
        for (SellDetailsDO sellDetailsDO : sellDetailsDOS){
            if (sellDetailsDO.getAmount().equals(0D)){
                String orderNo = "";
                if (sellDetailsDO.getSellDetalsFlag().equals(0)){
                    orderNo = sellDetailsDO.getNumber();
                    Double amount = orderService.getOrderProductCouponPurchasePrice(orderNo,sellDetailsDO.getSku());
                    sellDetailsDO.setAmount(amount);
                }else if (sellDetailsDO.getSellDetalsFlag().equals(1)){
                    ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(sellDetailsDO.getNumber());
                    orderNo = returnOrderBaseInfo.getOrderNo();
                    Double amount = orderService.getOrderProductCouponPurchasePrice(orderNo,sellDetailsDO.getSku());
                    sellDetailsDO.setAmount(-amount);
                }
                sellDetailsDAO.update(sellDetailsDO);
            }
        }
    }

    /******************************** 专供销量 *********************************************/

    /**
     * 新增
     *
     * @param detailsDOS
     */
    public void addZgDetailsList(List<SellZgDetailsDO> detailsDOS) {
        if (detailsDOS != null || detailsDOS.size() > 0) {
            for (SellZgDetailsDO details : detailsDOS) {
                sellZgDetailsDAO.addOneDetail(details);
            }
        }
    }

    private Boolean recordZgSales(OrderBaseInfo orderBaseInfo, List<Long> goodsIds, List<OrderGoodsInfo> orderGoodsInfoList) {
        Long cusId = orderBaseInfo.getCustomerId();
        //判断会员身份
        CustomerRankInfoResponse customerRankInfoResponse = appCustomerService.findCusRankinfoByCusId(cusId);
        AppCustomer appCustomer = null;
        try {
            appCustomer = appCustomerService.findById(cusId);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.info("计算专供促销，顾客id:" + cusId + "找不到顾客信息");
        }

        if (customerRankInfoResponse == null) {
            // 非专供会员 不能参与专供促销
            return false;
        }

        // 根据用户购买产品id 返回专供产品
        List<GiftListResponseGoods> goodsZGList = goodsPriceService.findGoodsPriceListByGoodsIdsAndUserId(goodsIds, cusId, AppIdentityType.CUSTOMER);

        if (goodsZGList == null || goodsZGList.size() == 0) {
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

        for (GiftListResponseGoods zgGoods : goodsZGList) {
            for (OrderGoodsInfo goodsInfo : orderGoodsInfoList) {
                if (zgGoods.getSku().equals(goodsInfo.getSku())) {

                    if (goodsInfo.getGoodsLineType().equals(AppGoodsLineType.PRESENT)) {
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
                    if (goodsInfo.getGoodsLineType().equals(AppGoodsLineType.PRODUCT_COUPON)){
                        Double amount = orderService.getOrderProductCouponPurchasePrice(orderBaseInfo.getOrderNumber(),goodsInfo.getSku());
                        detailsDO.setAmount(amount);
                    }else{
                        detailsDO.setAmount(goodsInfo.getReturnPrice());
                    }
                    detailsDO.setGoodsLineId(goodsInfo.getId());

                    // 检查是否已经记录

                    Boolean exitFlag = sellZgDetailsDAO.isExitByNumberAndGoodsLineId(orderBaseInfo.getOrderNumber(), goodsInfo.getId());
                    if (!exitFlag) {
                        sellZgDetailsDAO.addOneDetail(detailsDO);
                    }

                }
            }
        }

        return true;
    }

    /**
     * 返回会员专供产品累计桶数
     *
     * @return
     */
    public Integer getZgTsBycusIdAndsku(Long cusId, String sku) {
        Integer totalQty = 0;

        if (cusId == null || sku == null || sku.equals("")) {

        } else {
            totalQty = sellZgDetailsDAO.getQtyByCusIdAndSku(cusId, sku);

            if (totalQty == null) {
                totalQty = 0;
            }
        }
        return totalQty;
    }

    /**
     * 根据专供会员id和返回专供销量结果结果
     *
     * @param cusId
     * @param sku
     * @return
     */
    public List<SellZgDetailsDO> getZgDetailsByCusIdAndSku(Long cusId, String sku) {
        if (cusId == null || sku == null || sku.equals("")) {
            return null;
        }

        return sellZgDetailsDAO.getDetailsByCusIdAndSku(cusId, sku);
    }

    public List<SellZgDetailsDO> getZgDetailsByCusId(Long cusId) {
        if (cusId == null) {
            return null;
        }

        return sellZgDetailsDAO.getDetailsByCusId(cusId);
    }

    public SellZgCusTimes getTimesByCusIdAndSku(Long cusId, String sku, ActBaseType actBaseType) {
        if (cusId == null) {
            return null;
        }

        return sellZgDetailsDAO.getTimesByCusIdAndSku(cusId, sku, actBaseType);
    }

    public void updateSellZgCusTimes(SellZgCusTimes sellZgCusTimes) {
        if (sellZgCusTimes != null) {
            sellZgDetailsDAO.updateSellZgCusTimes(sellZgCusTimes);
        }
    }

    public void addSellZgCusTimes(SellZgCusTimes sellZgCusTimes) {
        if (sellZgCusTimes != null) {
            sellZgDetailsDAO.addSellZgCusTimes(sellZgCusTimes);
        }
    }

    public void addOrUpdateSellZgCusTimes(Long cusId, String sku, Integer times, Integer qty, ActBaseType type) {
        SellZgCusTimes sellZgCusTimes = this.sellZgDetailsDAO.getTimesByCusIdAndSku(cusId, sku, type);

        if (sellZgCusTimes != null) {
            Integer enjoiedTimes = sellZgCusTimes.getTimes();
            Integer enjoideQty = sellZgCusTimes.getQty();

            sellZgCusTimes.setTimes(enjoiedTimes + times);
            sellZgCusTimes.setQty(enjoideQty + qty);
            sellZgDetailsDAO.updateSellZgCusTimes(sellZgCusTimes);

        } else {
            //新增一条参与记录
            SellZgCusTimes sellZgCusTimes1 = new SellZgCusTimes();
            sellZgCusTimes1.setCusId(cusId);
            sellZgCusTimes1.setSku(sku);
            sellZgCusTimes1.setTimes(times);
            sellZgCusTimes1.setActBaseType(type);
            sellZgCusTimes1.setQty(qty);

            sellZgDetailsDAO.addSellZgCusTimes(sellZgCusTimes1);
        }
    }

    /**
     * 统计个人当月高端桶数销量
     */
    public SellDetailsResponse currentTsSellDetails(Long empId){
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 当月1号 0 点 0 分 0 秒
        LocalDateTime firstDay = LocalDateTime.of(now.getYear(),now.getMonth(),1,0,0,0);

        SellDetailsResponse sellDetailsResponse = sellDetailsDAO.statisticsSellDetailsSingle(now.getYear(),now.getMonthValue(),empId,"TS");

        Integer targetQty = sellDetailsResponse.getTargetQty() == null ? 0 : sellDetailsResponse.getTargetQty();
        Integer finishQty = sellDetailsResponse.getFinishQty() == null ? 0 : sellDetailsResponse.getFinishQty();

        if (targetQty.equals(0) || targetQty < 0){
            sellDetailsResponse.setFinishChance(0.00);
        }else{
            sellDetailsResponse.setFinishChance(CountUtil.div(finishQty,targetQty));
        }

        return sellDetailsResponse;
    }

    /**
     * 统计活跃会员数
     * @param empId
     * @return
     */
    public SellDetailsResponse currentHYS(Long empId){
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 当月1号 0 点 0 分 0 秒
        LocalDateTime firstDay = LocalDateTime.of(now.getYear(),now.getMonth(),1,0,0,0);

        SellDetailsResponse sellDetailsResponse = sellDetailsDAO.statisticsSellDetailsSingle(now.getYear(),now.getMonthValue(),empId,"HYS");

        Integer targetQty = sellDetailsResponse.getTargetQty() == null ? 0 : sellDetailsResponse.getTargetQty();
        Integer finishQty = sellDetailsResponse.getFinishQty() == null ? 0 : sellDetailsResponse.getFinishQty();

        if (targetQty.equals(0) || targetQty < 0){
            sellDetailsResponse.setFinishChance(0.00);
        }else{
            sellDetailsResponse.setFinishChance(CountUtil.div(finishQty,targetQty));
        }

        return sellDetailsResponse;
    }

    /**
     * 新开 高端会员数
     */
    public SellDetailsResponse currentXKF(Long empId){
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 当月1号 0 点 0 分 0 秒
        LocalDateTime firstDay = LocalDateTime.of(now.getYear(),now.getMonth(),1,0,0,0);

        SellDetailsResponse sellDetailsResponse = sellDetailsDAO.statisticsSellDetailsSingle(now.getYear(),now.getMonthValue(),empId,"XKF");

        Integer targetQty = sellDetailsResponse.getTargetQty() == null ? 0 : sellDetailsResponse.getTargetQty();
        Integer finishQty = sellDetailsResponse.getFinishQty() == null ? 0 : sellDetailsResponse.getFinishQty();

        if (targetQty.equals(0) || targetQty < 0){
            sellDetailsResponse.setFinishChance(0.00);
        }else{
            sellDetailsResponse.setFinishChance(CountUtil.div(finishQty,targetQty));
        }

        // 查询每周开发会员详情
        List<SellDetailsWeekFinishResponse> sellDetailsWeekFinishResponses = sellDetailsDAO.getWeekFinishDetails(sellDetailsResponse.getLineId());

        sellDetailsResponse.setWeekFinishDetails(sellDetailsWeekFinishResponses);
        return sellDetailsResponse;
    }

    /**
     * 销量
     */
    public SellDetailsResponse current(Long empId){
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 当月1号 0 点 0 分 0 秒
        LocalDateTime firstDay = LocalDateTime.of(now.getYear(),now.getMonth(),1,0,0,0);

        SellDetailsResponse sellDetailsResponse = sellDetailsDAO.statisticsSellDetailsSingle(now.getYear(),now.getMonthValue(),empId,"XL");

        Double target = sellDetailsResponse.getTargetSales() == null ? 0 : sellDetailsResponse.getTargetSales();
        Double finish = sellDetailsResponse.getFinishSales() == null ? 0 : sellDetailsResponse.getFinishSales();

        if (target.equals(0.00) || target < 0.00){
            sellDetailsResponse.setFinishChance(0.00);
        }else{
            sellDetailsResponse.setFinishChance(CountUtil.div(finish,target));
        }

        // 查询每周开发会员详情
        List<SellDetailsWeekFinishResponse> sellDetailsWeekFinishResponses = null;

        sellDetailsResponse.setWeekFinishDetails(sellDetailsWeekFinishResponses);
        return sellDetailsResponse;
    }

    /**
     * 分公司排名
     */
    @Override
    public List<SellDetailsResponse> getFgsRank(Long empId,String flag){
        String structureCode = "";
        LocalDateTime now = LocalDateTime.now();
        Integer year = now.getYear();
        Integer month = now.getMonthValue();

        AppEmployee employee = appEmployeeService.findById(empId);

        if (employee == null){
            return null;
        }

        AppStore store = appStoreService.findById(employee.getStoreId());
        String storeStructureCode = store.getStoreStructureCode();

        String[] structureArr = storeStructureCode.split("\\|");

        structureCode = structureArr[1];

        return sellDetailsDAO.getFgsRank(year,month,structureCode,flag);
    }

    /**
     * 集团排名
     */
    public List<SellDetailsResponse> getJtRank(String flag){
        LocalDateTime now = LocalDateTime.now();
        Integer year = now.getYear();
        Integer month = now.getMonthValue();

        return sellDetailsDAO.getJtRank(year,month,flag);
    }
}
