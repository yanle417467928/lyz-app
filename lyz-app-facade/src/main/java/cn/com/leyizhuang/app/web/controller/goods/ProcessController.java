package cn.com.leyizhuang.app.web.controller.goods;

import cn.com.leyizhuang.app.foundation.pojo.response.ProcessResponse;
import cn.com.leyizhuang.app.foundation.service.ProcessService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/29
 */
@RestController
@RequestMapping(value = "/app/process")
public class ProcessController {

    private static final Logger logger = LoggerFactory.getLogger(ProcessController.class);

    @Autowired
    private ProcessService processServiceImpl;

    /**
     * @title 获取工序包列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/9/29
     */
    @PostMapping(value = "/get/Process",produces="application/json;charset=UTF-8")
    public ResultDTO<List> getProcess(){
        logger.info("getProcess CALLED,获取工序包列表，入参 goodsCode {},type{}");

        ResultDTO<List> resultDTO;
        List<ProcessResponse> processResponseList = this.processServiceImpl.queryAllList();
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "获取收货地址！", processResponseList);
        logger.info("getProcess OUT,获取工序包列表，出参 resultDTO:{}",resultDTO);
        return resultDTO;
    }

}
