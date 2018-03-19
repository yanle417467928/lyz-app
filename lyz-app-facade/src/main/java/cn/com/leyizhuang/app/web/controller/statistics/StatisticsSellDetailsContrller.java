package cn.com.leyizhuang.app.web.controller.statistics;

import cn.com.leyizhuang.app.foundation.pojo.response.SellDetailsRankReponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellDetailsResponse;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 导购个人销量统计
     * flag : TS ：桶数；HYS：活跃数
     *
     * @return
     */
    @PostMapping(value = "/personal", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> statisticsPersonalSellDetais(Long sellerId, Long identityType, String flag) {
        logger.info("获取导购个人销量，入参 sellerId:{} identityType:{}", sellerId, identityType);

        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "无数据", "");

        if (identityType == 0) {
            SellDetailsResponse sellDetailsResponse = new SellDetailsResponse();
            sellDetailsResponse.setId(1L);
            sellDetailsResponse.setFinishQty(90);
            sellDetailsResponse.setTargetQty(100);
            sellDetailsResponse.setFinishChance(0.9);

            if (flag.equals("TS")) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "", sellDetailsResponse);
            } else if (flag.equals("HYS")) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "", sellDetailsResponse);
            } else {

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

        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "无数据", "");

        List<SellDetailsRankReponse> reponseList = new ArrayList<>();

        SellDetailsRankReponse rankReponse = new SellDetailsRankReponse();
        rankReponse.setId(1L);
        rankReponse.setName("张三");
        rankReponse.setFinishQty(3000);
        rankReponse.setRank(1);

        SellDetailsRankReponse rankReponse2 = new SellDetailsRankReponse();
        rankReponse2.setId(2L);
        rankReponse2.setName("张三2");
        rankReponse2.setFinishQty(2000);
        rankReponse2.setRank(2);

        SellDetailsRankReponse rankReponse3 = new SellDetailsRankReponse();
        rankReponse3.setId(3L);
        rankReponse3.setName("张三3");
        rankReponse3.setFinishQty(1000);
        rankReponse3.setRank(3);

        reponseList.add(rankReponse);
        reponseList.add(rankReponse2);
        reponseList.add(rankReponse3);

        if (flag.equals("FGS") && rankType.equals("TS")) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "", reponseList);
        } else if (flag.equals("FGS")  && rankType.equals("HYS")) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "", reponseList);
        }else if (flag.equals("JT")  && rankType.equals("TS")) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "", reponseList);
        }else if (flag.equals("JT") && rankType.equals("HYS")) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "", reponseList);
        } else {

        }


        return resultDTO;
    }
}
