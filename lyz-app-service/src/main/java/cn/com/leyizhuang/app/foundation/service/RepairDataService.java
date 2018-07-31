package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;

/**
 * Created by 12421 on 2018/6/27.
 */
public interface RepairDataService {
    void repairProductReturnJxPrice(String flag);

    void repairAllRuleScope(String flag);

    ResultDTO repairEmpCredit(Long empId, String flag);

    void repairStCredit(String flag);
}
