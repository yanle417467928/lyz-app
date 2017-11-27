package cn.com.leyizhuang.app.web.controller.seller;

import cn.com.leyizhuang.app.foundation.pojo.response.ArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerArrearsAuditResponse;
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
 * @date 2017/11/24
 */
@RestController
@RequestMapping(value = "/app/seller/arrearsAudit")
public class SellerArrearsAuditController {

    private static final Logger logger = LoggerFactory.getLogger(cn.com.leyizhuang.app.web.controller.deliveryClerk.ArrearsAuditController.class);

    @Autowired
    private ArrearsAuditService arrearsAuditServiceImpl;


    /**
     * @title   导购获取待审核欠款申请列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/11/24
     */
    @PostMapping(value = "/auditing/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getArrearsAuditListBySeller(Long userId, Integer identityType) {
        logger.info("getArrearsAuditListBySeller CALLED,导购获取待审核欠款申请列表，入参 userId:{} identityType:{}", userId, identityType);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getArrearsAuditListBySeller OUT,导购获取待审核欠款申请列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType && identityType != 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("getArrearsAuditListBySeller OUT,导购获取待审核欠款申请列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        List<ArrearsAuditStatus> arrearsAuditStatusList = new ArrayList<ArrearsAuditStatus>();
        arrearsAuditStatusList.add(ArrearsAuditStatus.AUDITING);
        List<SellerArrearsAuditResponse> arrearsAuditResponseList = this.arrearsAuditServiceImpl.findBySellerIdAndStatus(userId, arrearsAuditStatusList);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, arrearsAuditResponseList);
        logger.info("getArrearsAuditListBySeller OUT,导购获取待审核欠款申请列表成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * @title   导购获取已审核欠款申请列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/11/24
     */
    @PostMapping(value = "/audited/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> findArrearsAuditedListBySeller(Long userId, Integer identityType) {
        logger.info("findArrearsAuditedListBySeller CALLED,导购获取已审核欠款申请列表，入参 userId:{} identityType:{}", userId, identityType);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("findArrearsAuditedListBySeller OUT,导购获取已审核欠款申请列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType && identityType != 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("findArrearsAuditedListBySeller OUT,导购获取已审核欠款申请列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        List<ArrearsAuditStatus> arrearsAuditStatusList = new ArrayList<ArrearsAuditStatus>();
        arrearsAuditStatusList.add(ArrearsAuditStatus.AUDIT_NO);
        arrearsAuditStatusList.add(ArrearsAuditStatus.AUDIT_PASSED);
        List<SellerArrearsAuditResponse> arrearsAuditResponseList = this.arrearsAuditServiceImpl.findBySellerIdAndStatus(userId, arrearsAuditStatusList);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, arrearsAuditResponseList);
        logger.info("findArrearsAuditedListBySeller OUT,导购获取已审核欠款申请列表成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }
}
