package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.WithdrawRefundInfo;

/**
 * 提现API
 *
 * @author Richard
 * Created on 2018-01-11 14:43
 **/
public interface WithdrawService {


    void saveWithdrawRefundInfo(WithdrawRefundInfo withdrawRefundInfo);

    WithdrawRefundInfo getWithdrawRefundInfoByRefundNo(String refundNo);
}
