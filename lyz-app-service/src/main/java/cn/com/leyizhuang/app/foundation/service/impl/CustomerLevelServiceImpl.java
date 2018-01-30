package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppCustomerLightStatus;
import cn.com.leyizhuang.app.core.utils.ArrayListUtils;
import cn.com.leyizhuang.app.foundation.dao.MaCustomerDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.Customer;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.CustomerDO;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.service.CustomerLevelService;
import cn.com.leyizhuang.app.foundation.service.StatisticsSellDetailsService;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by panjie on 2018/1/26.
 */
@Service
public class CustomerLevelServiceImpl implements CustomerLevelService{

    private final Logger logger = LoggerFactory.getLogger(CustomerLevelServiceImpl.class);

    @Resource
    private MaCustomerDAO maCustomerDAO;

    @Resource
    private StatisticsSellDetailsService statisticsSellDetailsService;

//    public void refreshAllCustomerLevel(){
//
//    }
//
//    @Transactional
//    public void refreshCloseLight(){
//
//        // 获取90前的日期
//        LocalDateTime ninetyDaysAgoDate = LocalDateTime.now().minusDays(90);
//
//        //获取熄灯用户
//        List<CustomerDO> customerDOList = maCustomerDAO.findCustomerByLightAndStatusTrue(AppCustomerLightStatus.CLOSE.toString());
//
//        // 需要修改灯号的顾客id
//        List<Long> cusIds = new ArrayList<>();
//
//        for (CustomerDO customerDO : customerDOList){
//            // 根据用户信息查询销量
//            List<String> numberList = statisticsSellDetailsService.getCustomerSellDetailsOrderByCreateTimeDescLimit4(customerDO.getCusId(),ninetyDaysAgoDate);
//
//            if(numberList == null || numberList.size() == 0){
//                // 近期没有产生销量
//            }else{
//                // 记录id
//                cusIds.add(customerDO.getCusId());
//            }
//        }
//        // 更新灯号
//        this.updateLight(cusIds,AppCustomerLightStatus.CLOSE.toString());
//
//        LocalDateTime now = LocalDateTime.now();
//        logger.info("刷新灯号: "+now+", 共有"+cusIds.size()+"位熄灯客户变红灯！");
//    }
//
//    public void refreshRedLight(){
//        // 30天前日期
//        LocalDateTime thirtyDaysAgoDate = LocalDateTime.now().minusDays(30);
//        // 90天前日期
//        LocalDateTime ninetyDaysAgoDate = LocalDateTime.now().minusDays(90);
//        //获取红灯用户
//        List<CustomerDO> customerDOList = maCustomerDAO.findCustomerByLightAndStatusTrue(AppCustomerLightStatus.RED.toString());
//
//        // 需要熄灯的id
//        List<Long> closeIds = new ArrayList<>();
//        // 需要变黄灯的id
//        List<Long> yellowIds = new ArrayList<>();
//        // 需要变绿灯的用户
//        List<Long> greenIds = new ArrayList<>();
//
//        for (CustomerDO customerDO : customerDOList){
//            // 根据用户信息90天内销量
//            List<String> numberList1 = statisticsSellDetailsService.getCustomerSellDetailsOrderByCreateTimeDescLimit4(customerDO.getCusId(),ninetyDaysAgoDate);
//
//            if(numberList1 == null || numberList1.size() == 0){
//                // 90天内 没有产生销量 熄灯
//                closeIds.add(customerDO.getCusId());
//            }else{
//                // 90天内有产生销量
//
//                // 根据用户信息30天内销量
//                List<String> numberList2 = statisticsSellDetailsService.getCustomerSellDetailsOrderByCreateTimeDescLimit4(customerDO.getCusId(),thirtyDaysAgoDate);
//                if (numberList2 == null || numberList1.size() == 0){
//                    // 30天内吴销量 变黄灯
//                    yellowIds.add(customerDO.getCusId());
//                }else{
//                    // 90天内达成 4单销量 变绿灯
//                    if (numberList1.size() == 4){
//                        greenIds.add(customerDO.getCusId());
//                    }
//                }
//            }
//        }
//
//        // 更新灯号
//        this.updateLight(closeIds,AppCustomerLightStatus.CLOSE.toString());
//        this.updateLight(yellowIds,AppCustomerLightStatus.YELLOW.toString());
//        this.updateLight(greenIds,AppCustomerLightStatus.GREEN.toString());
//
//        LocalDateTime now = LocalDateTime.now();
//        logger.info("刷新灯号: "+now+", 共有"+closeIds.size()+"位客户被熄灯！");
//        logger.info("刷新灯号: "+now+", 共有"+yellowIds.size()+"位客户红灯变黄灯！");
//        logger.info("刷新灯号: "+now+", 共有"+greenIds.size()+"位客户红灯变绿灯！");
//    }
//
//    public void refreshYellowLight(){
//
//        // 90天前日期
//        LocalDateTime ninetyDaysAgoDate = LocalDateTime.now().minusDays(90);
//        //获取黄灯用户
//        List<CustomerDO> customerDOList = maCustomerDAO.findCustomerByLightAndStatusTrue(AppCustomerLightStatus.YELLOW.toString());
//
//        // 需要熄灯的id
//        List<Long> closeIds = new ArrayList<>();
//        // 需要变红灯的id
//        List<Long> redIds = new ArrayList<>();
//
//        for (CustomerDO customerDO : customerDOList){
//            // 根据用户信息90天内销量
//            List<String> numberList1 = statisticsSellDetailsService.getCustomerSellDetailsOrderByCreateTimeDescLimit4(customerDO.getCusId(),ninetyDaysAgoDate);
//
//            if(numberList1 == null || numberList1.size() == 0){
//                // 90天内 没有产生销量 熄灯
//                closeIds.add(customerDO.getCusId());
//            }else{
//                // 90天内有产生销量
//
//            }
//        }
//
//
//
//    }
//
//    public void refreshGreenLight(){
//
//    }
//
//    /**
//     * 更新灯号，避免list过长导致错误 将list拆分 500 一组
//     * @param ids 需要更新的id集合
//     * @param light 灯号
//     */
//    private void updateLight(List<Long> ids,String light){
//
//        if(ids.size() < 500){
//            maCustomerDAO.updateLight(ids,light);
//        }else{
//            // 拆分list
//
//           List<List<Long>> listList =  ArrayListUtils.splitList(ids,500);
//
//           for (List<Long> list : listList){
//               maCustomerDAO.updateLight(list,light);
//           }
//        }
//    }


    /********** 华丽的分割线 以上代码暂时不用*************/

    @Override
    public List<AppCustomer> countCustomerListLightlevel(List<AppCustomer> customers, Long sellerId){
        if (customers == null || customers.size() == 0){
            return  null;
        }

        for (AppCustomer customer : customers){
            customer = this.countCustomerLightLevel(customer,sellerId);
        }

        return customers;
    }

    @Override
    public AppCustomer countCustomerLightLevel(AppCustomer customer, Long sellerId){

        LocalDateTime now = LocalDateTime.now();
        // 30天前日期
        LocalDateTime thirtyDaysAgoDate = now.minusDays(30);
        // 90天前的日期
        LocalDateTime ninetyDaysAgoDate = now.minusDays(90);

        if (customer.getLastConsumptionTime() == null){
            // 判断是否为无灯

            customer.setLight(AppCustomerLightStatus.NOT);
            return  customer;
        }

        // 取90天内销量
        List<String> numberList1 = statisticsSellDetailsService.getCustomerSellDetailsOrderByCreateTimeDescLimit4(customer.getCusId(),ninetyDaysAgoDate,sellerId);
        // 30天内的销量
        List<String> numberList2 = statisticsSellDetailsService.getCustomerSellDetailsOrderByCreateTimeDescLimit4(customer.getCusId(),thirtyDaysAgoDate,sellerId);

        if (numberList1 == null || numberList1.size() == 0){
            // 90天内无销量 非新用户 熄灯

            customer.setLight(AppCustomerLightStatus.CLOSE);
            return customer;
        }else if (numberList1.size() <= 3){

            if (numberList2 == null || numberList2.size() == 0){
                // 30天内无销量 黄灯
                customer.setLight(AppCustomerLightStatus.YELLOW);
                return customer;
            }

            if (numberList2.size() > 0){
                // 30天内有销量 红灯
                customer.setLight(AppCustomerLightStatus.RED);
                return customer;
            }
        }else if (numberList1.size() == 4){
            // 90天内达到4单销量，再根据下单频率判断
            List<String> monthList = statisticsSellDetailsService.getSellDetailsFrequencyBycusIdAndSellerIdAndCreateTime(customer.getCusId(),ninetyDaysAgoDate,sellerId);

            if (numberList2.size() == 0){
                // 30天内 无销量 红灯
                customer.setLight(AppCustomerLightStatus.RED);
                return customer;
            }

            if (monthList.size() < 3){
                // 非连续3月 产生订单 红灯
                customer.setLight(AppCustomerLightStatus.RED);

            }else if (monthList.size() >= 3){

                // 绿灯
                customer.setLight(AppCustomerLightStatus.GREEN);

            }
            return customer;
        }


        return customer;
    }
}
