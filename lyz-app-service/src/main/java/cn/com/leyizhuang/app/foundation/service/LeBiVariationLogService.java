package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.CustomerLeBiVariationLog;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by caiyu on 2017/11/8.
 */
public interface LeBiVariationLogService {
    //添加乐币变动日志
    void addCustomerLeBiVariationLog(CustomerLeBiVariationLog customerLeBiVariationLog);

    //查看顾客乐币所有变动明细
    List<CustomerLeBiVariationLog> queryListBycusID(Long cusId);

    //根据变动类型查看顾客乐币变动明细
    PageInfo<CustomerLeBiVariationLog> queryListBycusIDAndShowTypeType(Long cusId, Integer showType, Integer page, Integer size);

    /**
     * 取消订单返回乐币
     * @param quantity  返回后总数量
     * @param lastUpdateTime    修改时间
     * @param customerId    顾客id
     */
    Integer updateLeBiQtyByUserId(Integer quantity,Date lastUpdateTime,Long customerId);
}
