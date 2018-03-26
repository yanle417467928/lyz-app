package cn.com.leyizhuang.app.foundation.service.datatransfer;

import cn.com.leyizhuang.app.foundation.pojo.datatransfer.TdOrder;

import java.util.Date;
import java.util.List;

/**
 * @author Created on 2018-03-24 15:15
 **/
public interface DataTransferService {

    List<String> getTransferStoreMainOrderNumber(Date startTime, Date endTime);

    TdOrder getMainOrderInfoByMainOrderNumber(String mainOrderNumber);
}
