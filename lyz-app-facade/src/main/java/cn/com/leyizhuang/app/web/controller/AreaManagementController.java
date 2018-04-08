package cn.com.leyizhuang.app.web.controller;

import cn.com.leyizhuang.app.foundation.service.AreaManagementService;
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
 * @date 2018/4/8
 */
@RestController
@RequestMapping(value = "/app/areaManagement")
public class AreaManagementController {

    private static final Logger logger = LoggerFactory.getLogger(AreaManagementController.class);

    @Autowired
    private AreaManagementService areaManagementService;

    /**
     * @title   根据区的编码查询街道
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/4/8
     */
    @PostMapping(value = "/getStreet", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getStreet(String code) {
        logger.info("getStreet CALLED,根据区的编码查询街道，入参code:{}", code);
        ResultDTO<Object> resultDTO;
        if (null == code || "".equals(code)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "省-市-区不能为空！", null);
            logger.info("getStreet OUT,根据区的编码查询街道失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        List<String> streetList = this.areaManagementService.findAreaManagementByParentCodeAndLevelIsFive(code);
        if (null == streetList || streetList.size() == 0){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该区域还未开通配送服务！", null);
            logger.info("getStreet OUT,根据区的编码查询街道失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, streetList);
        logger.info("skipOrderArrive OUT,根据区的编码查询街道成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

}
