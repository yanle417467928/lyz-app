package cn.com.leyizhuang.app.web.controller.goods;

import cn.com.leyizhuang.app.foundation.pojo.response.ProcessResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.UserGoodsResponse;
import cn.com.leyizhuang.app.foundation.service.ProcessService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/29
 */
@RestController
@RequestMapping(value = "/app/process")
public class ProcessController {

    private static final Logger logger = LoggerFactory.getLogger(ProcessController.class);

    @Resource
    private ProcessService processServiceImpl;

    /**
     * @param
     * @return
     * @throws
     * @title 获取工序包列表
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/29
     */
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<List> getProcess(Long userId, Long cityId) {
        logger.info("getProcess CALLED,获取工序包列表，入参 null");

        ResultDTO<List> resultDTO;
        List<ProcessResponse> processResponseList = this.processServiceImpl.queryAllListByCityId(cityId);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, processResponseList);
        logger.info("getProcess OUT,获取工序包列表，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * @param userId
     * @param identityType
     * @param processId
     * @return
     * @throws
     * @title 获取工序包商品列表
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/29
     */
    @PostMapping(value = "/processGoods/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getProcessGoods(Long userId, Integer identityType, Long processId) {

        logger.info("getProcessGoods CALLED,获取工序包商品列表，入参 userId {},identityType{},processId{}", userId, identityType, processId);

        ResultDTO<Object> resultDTO;

        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getProcessGoods OUT,获取工序包商品列表，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getProcessGoods OUT,获取工序包商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<UserGoodsResponse> processGoodsResponseList = this.processServiceImpl.queryByProcessIdAndUserId(userId, processId, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, processGoodsResponseList);
            logger.info("getProcessGoods OUT,获取工序包商品列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取常购商品列表失败", null);
            logger.warn("getProcessGoods EXCEPTION,获取工序包商品列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

}
