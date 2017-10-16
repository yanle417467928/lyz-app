package cn.com.leyizhuang.app.web.controller.store;

import cn.com.leyizhuang.app.foundation.service.IAppStoreService;
import cn.com.leyizhuang.app.web.controller.user.UserHomePageController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/10/13.
 * Time: 9:31.
 */
@RestController
@RequestMapping(value = "/app/store")
public class StoreController {

    private static final Logger logger = LoggerFactory.getLogger(UserHomePageController.class);

    @Resource
    private IAppStoreService appStoreService;

    /**
     * 获取门店赞助金余额
     * @param userId
     * @param identityType
     * @param storeId
     * @return
     */
    @PostMapping(value = "/subvention", produces = "application/json;charset=UTF-8")
    public ResultDTO getStoreSubventionBalance(Long userId,Integer identityType,Long storeId){

        logger.info("getStoreSubventionBalance CALLED,获取门店赞助金，入参 userId {},identityType{},storeId{}", userId, identityType,storeId);

        ResultDTO resultDTO;
        if (userId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("getStoreSubventionBalance OUT,获取门店赞助金失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("getStoreSubventionBalance OUT,获取门店赞助金失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (storeId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店id不能为空！", null);
            logger.info("getStoreSubventionBalance OUT,获取门店赞助金失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        return null;
    }

//    public ResultDTO getStore
}
