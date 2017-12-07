package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.StoreCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.response.StoreCreditMoneyLogResponse;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/27
 */
public interface StoreCreditMoneyLogService {
    List<StoreCreditMoneyLogResponse> findByUserId(Long userId);

    /**
     * 根据用户id查询门店信用金
     * @param userId    用户id
     * @return  返回门店信用金类
     */
    StoreCreditMoney findStoreCreditMoneyByUserId(Long userId);
}
