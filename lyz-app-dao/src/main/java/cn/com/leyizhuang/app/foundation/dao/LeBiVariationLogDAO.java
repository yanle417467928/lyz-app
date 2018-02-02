package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.CustomerLeBiVariationLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by caiyu on 2017/11/8.
 */
@Repository
public interface LeBiVariationLogDAO {
    //添加乐币变动日志
    void addCustomerLeBiVariationLog(CustomerLeBiVariationLog customerLeBiVariationLog);

    //查看顾客乐币所有变动明细
    List<CustomerLeBiVariationLog> queryListBycusID(@Param("cusId") Long cusID);

    //根据变动类型查看顾客乐币变动明细
    List<CustomerLeBiVariationLog> queryListBycusIDAndShowTypeType(@Param("cusId") Long cusID, @Param("showType") Integer showType);

    /**
     * 取消订单返回乐币
     * @param quantity  返回后总数量
     * @param version    修改时间
     * @param customerId    顾客id
     */
    Integer updateLeBiQtyByUserId(@Param("quantity") Integer quantity, @Param("version")Date version, @Param("customerId") Long customerId);
}
