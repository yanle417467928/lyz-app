package cn.com.leyizhuang.app.foundation.service;


/**
 * 优惠券发放服务类
 * Created by panjie on 2017/12/27.
 */
public interface CashCouponSendService {

    void send(Long customerId,Long cashCouponId,Integer qty);


}
