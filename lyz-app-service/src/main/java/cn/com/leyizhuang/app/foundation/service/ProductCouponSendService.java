package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;

import java.util.List;

/**
 * Created by panjie on 2018/1/11.
 */
public interface ProductCouponSendService {

    ResultDTO<String> send(Long customerId, Long productCouponId, Long sellerId, Integer qty,Long optId);

    ResultDTO<String> sendBatch(List<Long> customerIdList, Long productCouponId, Long sellerId, Integer qty,Long optId);

    void sendForPromotion(Long userId,Long gid,Integer qty,String ordNo,String sku);
}
