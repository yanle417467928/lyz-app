package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeCreditMoneyLogResponse;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes: 导购信用金变动记录服务
 * Created with IntelliJ IDEA.
 * Date: 2017/12/18.
 * Time: 17:49.
 */

public interface EmployeeCreditMoneyLogService {
    /**
     * 查询导购信用金变动记录
     *
     * @param userId
     * @return
     */
    List<EmployeeCreditMoneyLogResponse> findByUserId(Long userId);

}
