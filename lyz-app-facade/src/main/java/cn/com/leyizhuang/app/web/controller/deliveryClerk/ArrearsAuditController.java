package cn.com.leyizhuang.app.web.controller.deliveryClerk;

import cn.com.leyizhuang.app.foundation.pojo.response.ArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.service.ArrearsAuditService;
import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/16
 */
@RestController
@RequestMapping(value = "/app/arrearsAudit")
public class ArrearsAuditController {

    private static final Logger logger = LoggerFactory.getLogger(ArrearsAuditController.class);

    @Autowired
    private ArrearsAuditService arrearsAuditServiceImpl;

    /**
     * @param
     * @return
     * @throws
     * @title 获取待审核欠款申请列表
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/16
     */
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getArrearsAuditList(Long userId, Integer identityType) {
        logger.info("getArrearsAuditList CALLED,获取待审核欠款申请列表，入参 userId:{} identityType:{}", userId, identityType);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getArrearsAuditList OUT,获取待审核欠款申请列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType && identityType != 1) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("getArrearsAuditList OUT,获取待审核欠款申请列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        List<ArrearsAuditStatus> arrearsAuditStatusList = new ArrayList<ArrearsAuditStatus>();
        arrearsAuditStatusList.add(ArrearsAuditStatus.AUDITING);
        List<ArrearsAuditResponse> arrearsAuditResponseList = this.arrearsAuditServiceImpl.findByUserIdAndStatus(userId, arrearsAuditStatusList);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, arrearsAuditResponseList);
        logger.info("getArrearsAuditList OUT,获取待审核欠款申请列表成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

}
