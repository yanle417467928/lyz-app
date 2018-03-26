package cn.com.leyizhuang.app.web.controller.rest.datatransfer;

import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrder;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferService;
import cn.com.leyizhuang.app.foundation.service.datatransfer.OrderGoodsTransferService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by panjie on 2018/3/24.
 */
@RestController
public class DataTransferController {

    @Resource
    private DataTransferService dataTransferService;

    @Resource
    private OrderGoodsTransferService orderGoodsTransferService;

    @RequestMapping(value = "/data/transfer",method = RequestMethod.GET)
    public String dataTransfer(){
        // *********************** 订单迁移处理 ***************
        List<String> storeMainOrderNumberList;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017,10,01,0,0,0);
        Date startTime = calendar.getTime();
        Date endTime = new Date();
        //查询所有待处理的订单号
        storeMainOrderNumberList = dataTransferService.getTransferStoreMainOrderNumber(startTime,endTime);

        // *********************** 处理 订单基础表 ord_base_info *****************

        for(String mainOrderNumber:storeMainOrderNumberList){

            TdOrder tdOrder = dataTransferService.getMainOrderInfoByMainOrderNumber(mainOrderNumber);
        }



        return "success";
    }

    @RequestMapping(value = "/data/transfer/orderGoodsInfo",method = RequestMethod.GET)
    public void orderGoodsInfoTransfer(){

        orderGoodsTransferService.transferAll();
    }
}
