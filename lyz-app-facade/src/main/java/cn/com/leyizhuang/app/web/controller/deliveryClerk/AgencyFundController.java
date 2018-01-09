package cn.com.leyizhuang.app.web.controller.deliveryClerk;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.response.DeliveryAgencyFundResponse;
import cn.com.leyizhuang.app.foundation.service.OrderAgencyFundService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 配送员代收款
 * @author GenerationRoad
 * @date 2017/11/27
 */
@RestController
@RequestMapping(value = "/app/delivery/agencyFund")
public class AgencyFundController {

    private static final Logger logger = LoggerFactory.getLogger(AgencyFundController.class);

    @Autowired
    private OrderAgencyFundService orderAgencyFundServiceImpl;

    /**  
     * @title   获取配送员代收款明细
     * @descripe
     * @param
     * @return 
     * @throws 
     * @author GenerationRoad
     * @date 2017/11/27
     */
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getAgencyFundList(Long userId, Integer identityType, String startDate, String endDate,Integer page, Integer size) {
        logger.info("getAgencyFundList CALLED,获取配送员代收款明细，入参 userId:{} identityType:{} startDate:{} endDate:{},page:{},size:{}", userId, identityType, startDate, endDate,page,size);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getAgencyFundList OUT,获取配送员代收款明细失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType && identityType != 1) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("getAgencyFundList OUT,获取配送员代收款明细失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null != endDate){
            if ( "".equals(endDate.trim())){
                endDate = null;
            } else {
                endDate += " 23:59:59";
            }
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getAgencyFundList OUT,获取配送员代收款明细失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getAgencyFundList OUT,获取配送员代收款明细失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        PageInfo<DeliveryAgencyFundResponse> deliveryAgencyFundResponseList = this.orderAgencyFundServiceImpl.findByUserIdAndCreateTime(userId, startDate, endDate, page, size);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new GridDataVO<DeliveryAgencyFundResponse>().transform(deliveryAgencyFundResponseList));
        logger.info("getAgencyFundList OUT,获取配送员代收款明细成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }


}
