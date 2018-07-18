package cn.com.leyizhuang.app.web.controller.statistics;

import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.pojo.response.SellDetailsRankReponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellDetailsResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.StatisticsSellDetailsService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 统计销量控制器
 * Created by panjie on 2018/3/15.
 */
@RestController
@RequestMapping(value = "/app/statistics")
public class StatisticsSellDetailsContrller {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsSellDetailsContrller.class);

    @Autowired
    private AppEmployeeService appEmployeeService;

    @Autowired
    private StatisticsSellDetailsService statisticsSellDetailsService;

    @Autowired
    private AppStoreService appStoreService;

    /**
     * 导购当月 个人销量统计
     * flag : TS ：桶数；HYS：活跃数 ; XKF :新开发高端会员数 ; XL ：销量
     *
     * @return
     */
    @PostMapping(value = "/personal", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> statisticsPersonalSellDetais(Long sellerId, Long identityType, String flag) {
        /** 4月31号以前无销量 则提示功能暂未开放 **/
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = LocalDateTime.of(2018,5,30,23,59,59);

        if (now.isBefore(dateTime)){
            ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此功能暂未开放，敬请期待", "");
            return  resultDTO;
        }

        logger.info("获取导购个人销量，入参 sellerId:{} identityType:{}", sellerId, identityType);

        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "无数据", "");

        //检核导购信息
        AppEmployee employee = appEmployeeService.findById(sellerId);

        if (employee == null){
            return resultDTO;
        }

        if (identityType == 0) {
            SellDetailsResponse sellDetailsResponse = new SellDetailsResponse();
            sellDetailsResponse.setId(1L);
            sellDetailsResponse.setFinishQty(90);
            sellDetailsResponse.setTargetQty(100);
            sellDetailsResponse.setFinishChance(0.9);

            if (flag.equals("TS")) {
                sellDetailsResponse = statisticsSellDetailsService.currentTsSellDetails(sellerId);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "", sellDetailsResponse);
            } else if (flag.equals("HYS")) {
                sellDetailsResponse = statisticsSellDetailsService.currentHYS(sellerId);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "", sellDetailsResponse);
            } else if(flag.equals("XKF")) {
                sellDetailsResponse = statisticsSellDetailsService.currentXKF(sellerId);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "", sellDetailsResponse);
            }else if(flag.equals("XL")){
                sellDetailsResponse = statisticsSellDetailsService.currentXL(sellerId);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "", sellDetailsResponse);
            }

        }


        return resultDTO;
    }

    /**
     * 获取排名
     *
     * @param sellerId
     * @param flag     FGS:分公司 JT:集团
     * @return
     */
    @PostMapping(value = "/rank", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> statisticsSellDetaisRank(Long sellerId, String flag,String rankType) {
        logger.info("获取销量排名，入参 sellerId:{} flag:{}", sellerId, flag);

        /** 4月31号以前无销量 则提示功能暂未开放 **/
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = LocalDateTime.of(2019,5,30,23,59,59);

        if (now.isBefore(dateTime)){
            ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此功能暂未开放，敬请期待", "");
            return  resultDTO;
        }

        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "无数据", "");

        List<SellDetailsRankReponse> reponseList = new ArrayList<>();



        SellDetailsRankReponse rankReponse3 = new SellDetailsRankReponse();
        rankReponse3.setId(3L);
        rankReponse3.setName("张三3");
        rankReponse3.setFinishQty(1000);
        rankReponse3.setRank(3);
        rankReponse3.setOrganizationName("公司3");


        List<SellDetailsRankReponse> reponseList2 = new ArrayList<>();



        if (flag.equals("FGS") && rankType.equals("TS")) {
            List<SellDetailsResponse> reponses = statisticsSellDetailsService.getFgsRank(sellerId,"TS");
            for (int i=0 ;i<reponses.size();i++){
                SellDetailsResponse response = reponses.get(i);
                AppEmployee employee = appEmployeeService.findById(response.getId());
                AppStore store = appStoreService.findById(employee.getStoreId());
                SellDetailsRankReponse rankReponse = new SellDetailsRankReponse();
                rankReponse.setId(response.getId());
                rankReponse.setName(response.getName());
                rankReponse.setFinishQty(response.getFinishQty());
                rankReponse.setRank(i+1);
                rankReponse.setOrganizationName(store.getStoreName());
                reponseList.add(rankReponse);
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "", reponseList);
        } else if (flag.equals("FGS")  && rankType.equals("HYS")) {
            List<SellDetailsResponse> reponses = statisticsSellDetailsService.getFgsRank(sellerId,"HYS");
            for (int i=0 ;i<reponses.size();i++){
                SellDetailsResponse response = reponses.get(i);
                AppEmployee employee = appEmployeeService.findById(response.getId());
                AppStore store = appStoreService.findById(employee.getStoreId());
                SellDetailsRankReponse rankReponse = new SellDetailsRankReponse();
                rankReponse.setId(response.getId());
                rankReponse.setName(response.getName());
                rankReponse.setFinishQty(response.getFinishQty());
                rankReponse.setRank(i+1);
                rankReponse.setOrganizationName(store.getStoreName());
                reponseList.add(rankReponse);
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "", reponseList);
        }else if (flag.equals("JT")  && rankType.equals("TS")) {
            List<SellDetailsResponse> reponses = statisticsSellDetailsService.getJtRank("TS");
            for (int i=0 ;i<reponses.size();i++){
                SellDetailsResponse response = reponses.get(i);
                AppEmployee employee = appEmployeeService.findById(response.getId());
                AppStore store = appStoreService.findById(employee.getStoreId());
                SellDetailsRankReponse rankReponse = new SellDetailsRankReponse();
                rankReponse.setId(response.getId());
                rankReponse.setName(response.getName());
                rankReponse.setFinishQty(response.getFinishQty());
                rankReponse.setRank(i+1);
                rankReponse.setOrganizationName(store.getStoreName());
                reponseList.add(rankReponse);
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "", reponseList);
        }else if (flag.equals("JT") && rankType.equals("HYS")) {
            List<SellDetailsResponse> reponses = statisticsSellDetailsService.getJtRank("HYS");
            for (int i=0 ;i<reponses.size();i++){
                SellDetailsResponse response = reponses.get(i);
                AppEmployee employee = appEmployeeService.findById(response.getId());
                AppStore store = appStoreService.findById(employee.getStoreId());
                SellDetailsRankReponse rankReponse = new SellDetailsRankReponse();
                rankReponse.setId(response.getId());
                rankReponse.setName(response.getName());
                rankReponse.setFinishQty(response.getFinishQty());
                rankReponse.setRank(i+1);
                rankReponse.setOrganizationName(store.getStoreName());
                reponseList.add(rankReponse);
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "", reponseList);
        }

        return resultDTO;
    }

}
