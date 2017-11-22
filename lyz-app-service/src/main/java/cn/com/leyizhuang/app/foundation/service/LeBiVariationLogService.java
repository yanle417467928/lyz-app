package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.CustomerLeBiVariationLog;

import java.util.List;

/**
 * Created by caiyu on 2017/11/8.
 */
public interface LeBiVariationLogService {
    //添加乐币变动日志
    void addCustomerLeBiVariationLog(CustomerLeBiVariationLog customerLeBiVariationLog);

    //查看顾客乐币所有变动明细
    List<CustomerLeBiVariationLog> queryListBycusID(Long cusID);

    //根据变动类型查看顾客乐币变动明细
    List<CustomerLeBiVariationLog> queryListBycusIDAndShowTypeType(Long cusID, Integer showType);
}
