package cn.com.leyizhuang.app.web.controller.user;


import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.request.BrowseHistoryRequest;
import cn.com.leyizhuang.app.foundation.service.BrowseHistoryService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GenerationRoad
 * @date 2017/11/2
 */
@RestController
@RequestMapping(value = "/app/user/browseHistory")
public class UserBrowseHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(UserBrowseHistoryController.class);

    @Autowired
    private BrowseHistoryService browseHistoryServiceImpl;

    /**
     * @title   添加商品浏览记录
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/11/2
     */
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ResultDTO addBrowseHistory(Long userId, Long goodsId, Integer identityType) {

        logger.info("addBrowseHistory CALLED,添加商品浏览记录，入参 userId {},goodsId {},identityType{}", userId, goodsId, identityType);

        ResultDTO resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("addBrowseHistory OUT,添加商品浏览记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == goodsId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品id不能为空", null);
            logger.info("addBrowseHistory OUT,添加商品浏览记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("addBrowseHistory OUT,添加商品浏览记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if(identityType == 1){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该用户类型没有此功能", null);
            logger.info("addBrowseHistory OUT,添加商品浏览记录失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            BrowseHistoryRequest browseHistory = new BrowseHistoryRequest();
            browseHistory = browseHistory.setBrowseHistoryRequest(userId, identityType, goodsId);
            if (this.browseHistoryServiceImpl.existBrowseHistory(browseHistory)) {
                this.browseHistoryServiceImpl.save(browseHistory);
                this.browseHistoryServiceImpl.delete(browseHistory.getUserId(), browseHistory.getIdentityType());
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("addBrowseHistory OUT,添加商品浏览记录成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，添加商品浏览记录失败", null);
            logger.warn("addBrowseHistory EXCEPTION,添加商品浏览记录失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

}
