package cn.com.leyizhuang.app.quartz;

import cn.com.leyizhuang.app.core.utils.ApplicationContextUtil;
import cn.com.leyizhuang.app.foundation.dao.MaOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.service.MaOrderService;
import cn.com.leyizhuang.app.foundation.service.ReturnOrderService;
import cn.com.leyizhuang.app.remote.queue.MaSinkSender;
import org.quartz.*;
import org.quartz.Calendar;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 扫描待付款订单是否过期
 * Created by caiyu on 2018/1/30.
 */
@Component
public class ScanningUnpaidOrderTask implements Job {

    @Resource
    private MaOrderDAO maOrderDAO;
    @Resource
    private MaSinkSender maSinkSender;
    @Resource
    private ReturnOrderService returnOrderService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(new Date() + "：开始扫描待付款订单");
        MaOrderService MaOrderService = (MaOrderService) ApplicationContextUtil.getBean("maOrderService");



        Date date = new Date();
        //获取当前时间5小时前时间
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.HOUR_OF_DAY, calendar.get(java.util.Calendar.HOUR_OF_DAY) - 5);
        String findDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

        //获取所有待付款订单
        List<OrderBaseInfo> orderBaseInfoList = maOrderDAO.scanningUnpaidOrder(findDate);
        if (null != orderBaseInfoList && orderBaseInfoList.size() > 0) {
            for (OrderBaseInfo orderBaseInfo : orderBaseInfoList) {
                if (date.after(orderBaseInfo.getEffectiveEndTime())) {
                    String returnNumber = MaOrderService.scanningUnpaidOrder(orderBaseInfo);

                    ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(returnNumber);
                    if (null != returnOrderBaseInfo){
                        //发送退单拆单消息到拆单消息队列
                        maSinkSender.sendReturnOrder(returnOrderBaseInfo.getReturnNo());
                    }
                } else {
                    System.out.println(new Date() + "：未查询到待付款超时订单，订单号：");
                }
            }
        }

    }
}
