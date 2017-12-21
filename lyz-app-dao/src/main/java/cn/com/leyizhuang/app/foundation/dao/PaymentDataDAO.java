package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.PaymentDataStatus;
import cn.com.leyizhuang.app.foundation.pojo.PaymentDataDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/20
 */
@Repository
public interface PaymentDataDAO {

    void save(PaymentDataDO paymentDataDO);

    List<PaymentDataDO> findByOutTradeNoAndTradeStatus(@Param("outTradeNo") String outTradeNo, @Param("tradeStatus") PaymentDataStatus tradeStatus);

    void updateByTradeStatusIsWaitPay(PaymentDataDO paymentDataDO);

    /**
     * 查询还款记录详情
     * @param outTradeNo    还款号
     * @return  还款记录详情
     */
    PaymentDataDO findPaymentDataDOByOutTradeNo(@Param("outTradeNo") String outTradeNo);

}
