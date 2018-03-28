package cn.com.leyizhuang.app.foundation.service.datatransfer;

import cn.com.leyizhuang.app.foundation.pojo.datatransfer.*;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLogisticsInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;

import java.util.Date;
import java.util.List;
import java.util.Queue;

/**
 * @author Created on 2018-03-24 15:15
 **/
public interface DataTransferSupportService {


    void saveOrderRelevantInfo(OrderBaseInfo orderBaseInfo);

    void saveDataTransferErrorLog(Queue<DataTransferErrorLog> errorLogQueue);
}
