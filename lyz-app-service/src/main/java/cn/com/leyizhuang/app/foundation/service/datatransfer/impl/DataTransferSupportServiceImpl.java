package cn.com.leyizhuang.app.foundation.service.datatransfer.impl;

import cn.com.leyizhuang.app.foundation.dao.transferdao.TransferDAO;
import cn.com.leyizhuang.app.foundation.pojo.datatransfer.DataTransferErrorLog;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLogisticsInfo;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.datatransfer.DataTransferSupportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * 订单商品转换类
 *
 * @author Richard
 * @date 2018/3/24
 */
@Service
@Slf4j
public class DataTransferSupportServiceImpl implements DataTransferSupportService {

    @Resource
    private AppOrderService orderService;

    @Resource
    private TransferDAO transferDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderRelevantInfo(OrderBaseInfo orderBaseInfo, OrderBillingDetails orderBillingDetails,OrderLogisticsInfo orderLogisticsInfo) {
        orderService.saveOrderBaseInfo(orderBaseInfo);
        transferDAO.saveOrderBillingDetails(orderBillingDetails);
        orderLogisticsInfo.setOid(orderBaseInfo.getId());
        orderService.saveOrderLogisticsInfo(orderLogisticsInfo);
    }


    @Override
    public void saveDataTransferErrorLog(Queue<DataTransferErrorLog> errorLogQueue) {
        if (null != errorLogQueue && !errorLogQueue.isEmpty()){
            List<DataTransferErrorLog> errorLogList = new ArrayList<>(errorLogQueue);
            transferDAO.saveDataTransferErrorLogList(errorLogList);
        }


    }
}
