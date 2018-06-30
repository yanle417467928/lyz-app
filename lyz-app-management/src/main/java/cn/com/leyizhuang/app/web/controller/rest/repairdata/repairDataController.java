package cn.com.leyizhuang.app.web.controller.rest.repairdata;
import cn.com.leyizhuang.app.foundation.service.RepairDataService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 修复数据
 * Created by 12421 on 2018/6/30.
 */
@RestController
@RequestMapping(value = repairDataController.PRE_URL, produces = "application/json;charset=utf8")
public class repairDataController {
    protected final static String PRE_URL = "/rest/repair/data";

    private final Logger logger = LoggerFactory.getLogger(repairDataController.class);

    @Resource
    private RepairDataService repairDataService;

    /**
     * 修复 加盟店 使用券 未返经销差价的订单
     * @param flag == go 持久化数据
     * @return
     */
    @GetMapping(value = "/jm/jxPrice")
    public ResultDTO repairJmJxPrice(String flag){

        repairDataService.repairProductReturnJxPrice(flag);

        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "修复完成", null);
    }

}
