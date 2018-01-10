package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;

import java.util.List;

/**
 * 优惠券发放服务类
 * Created by panjie on 2017/12/27.
 */
public interface CashCouponSendService {

    ResultDTO<String> send(Long customerId, Long cashCouponId, Integer qty);

    ResultDTO<String> sendBatch(List<Long> customerIdList, Long cashCouponId, Integer qty);
}
