package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.WithdrawRefundInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 提现相关数据仓库
 *
 * @author Richard
 * Created on 2018-01-11 14:53
 **/
@Repository
public interface WithdrawDAO {


    void saveWithdrawRefundInfo(WithdrawRefundInfo withdrawRefundInfo);

    WithdrawRefundInfo getWithdrawRefundInfoByRefundNo(@Param(value = "refundNo") String refundNo);
}
