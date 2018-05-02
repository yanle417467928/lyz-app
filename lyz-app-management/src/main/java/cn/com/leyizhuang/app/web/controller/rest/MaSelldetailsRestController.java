package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.service.StatisticsSellDetailsService;
import cn.com.leyizhuang.app.foundation.service.impl.AppActDutchServiceImpl;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by panjie on 2018/4/11.
 */
@RestController
@RequestMapping(value = MaSelldetailsRestController.PRE_URL, produces = "application/json;charset=utf8")
public class MaSelldetailsRestController {

    protected final static String PRE_URL = "/rest/sell/details";

    private final Logger logger = LoggerFactory.getLogger(MaSelldetailsRestController.class);

    @Resource
    private StatisticsSellDetailsService statisticsSellDetailsService;

    @Resource
    private AppActDutchServiceImpl dutchService;

    /**
     * 计算所有导购业绩
     */
    @GetMapping(value = "/count")
    public void countTest(){
        List<String> list = new ArrayList<>();
        list.add("RCC001");
        list.add("PCC001");
        list.add("BYC001");
        list.add("RDC001");
        list.add("ZZC001");

        statisticsSellDetailsService.statisticsAllSellerSellDetails(list);
    }

    /**
     * 计算一个导购业绩
     * @param id
     * @return
     */
    @GetMapping(value = "count/one/{id}")
    public ResultDTO<Object> countOne(@PathVariable Long id){
            statisticsSellDetailsService.statisticOneSeller(id);
        return null;
    }

    @GetMapping("/reCreate/order/{orderNo}")
    public ResultDTO<Object> reCreateOrderSellDetails(@PathVariable(value = "orderNo") String orderNo){
        if (orderNo != null && orderNo != ""){
            try{
                statisticsSellDetailsService.addOrderSellDetails(orderNo);

            }catch (Exception e){
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "生成失败", null);
            }
        }
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重新生成订单销量数据", null);
    }

    @GetMapping("/reCreate/return/{returnNo}")
    public ResultDTO<Object> reCreateReturnSellDetails(@PathVariable(value = "returnNo") String returnNo){
        if (returnNo != null && returnNo != ""){
            try {
                statisticsSellDetailsService.addReturnOrderSellDetails(returnNo);
            }catch (Exception e){
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "生成失败", null);
            }
        }
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重新生成退单销量数据", null);
    }

    @GetMapping("/reCreate/order/ALL")
    public ResultDTO<Object> reCreateOrderSellDetails(){
         statisticsSellDetailsService.createAllOrderDetails();

        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重新生成订单销量数据", null);
    }

    @GetMapping("/reCreate/return/All")
    public ResultDTO<Object> reCreateReturnDetails(){
        statisticsSellDetailsService.createAllreturnOrderDetails();

        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "重新生成退单销量数据", null);
    }

    @GetMapping("/repair/all")
    public ResultDTO<Object> repairAllOrderDetails(){
        statisticsSellDetailsService.repairAllOrderDetails();
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "修复销量数据完成", null);
    }

    @GetMapping("/reCreate/errorLog")
    public String reCreateErrorLogDetail(){

        return null;
    }

    @GetMapping("/re/goodsLine/{flag}")
    public ResultDTO<Object> repairGoodsLine(@PathVariable("flag") String flag){
        dutchService.repairGoodsLine(flag);
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "修复商品数据完成", null);
    }

}
