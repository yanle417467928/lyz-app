package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppRechargeOrderStatus;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeOrder;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeReceiptInfo;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.RechargeReceiptInf;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 充值相关数据仓库
 *
 * @author Richard
 * Created on 2018-01-11 14:53
 **/
@Repository
public interface RechargeDAO {

    public void saveRechargeOrder(RechargeOrder rechargeOrder);

    public void saveRechargeReceiptInfo(RechargeReceiptInfo receiptInfo);

    public void updateRechargeOrderStatusAndPayUpTime(@Param(value = "rechargeNo") String rechargeNo,
                                                      @Param(value = "payUpTime") Date payUpTime,
                                                      @Param(value = "status") AppRechargeOrderStatus status);

    List<RechargeReceiptInfo> findRechargeReceiptInfoByRechargeNo(String rechargeNo);

    List<RechargeOrder> findRechargeOrderByRechargeNo(String rechargeNo);

    List<RechargeReceiptInfo> findCreditRechargeReceiptInfoByRechargeNo(String receiptNumber);

    List<RechargeOrder> findRechargeOrderByWithdrawNo(String withdrawNo);

}
