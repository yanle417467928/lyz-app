package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.StoreCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.response.StoreCreditMoneyLogResponse;
import com.github.pagehelper.PageInfo;

/**
 * @author GenerationRoad
 * @date 2017/11/27
 */
public interface StoreCreditMoneyLogService {
    PageInfo<StoreCreditMoneyLogResponse> findByUserId(Long userId, Integer page, Integer size);

    /**
     * 根据用户id查询门店信用金
     * @param userId    用户id
     * @return  返回门店信用金类
     */
    StoreCreditMoney findStoreCreditMoneyByUserId(Long userId);
}
