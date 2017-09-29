package cn.com.leyizhuang.app.web.controller.user;

import cn.com.leyizhuang.app.foundation.pojo.response.UserCollectGoodsResponse;
import cn.com.leyizhuang.app.foundation.service.IGoodsService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * 个人商品收藏接口
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/9/28.
 * Time: 13:40.
 */
@Controller
@RequestMapping(value = "/app/user/collect")
public class UserCollectController {

    private static final Logger logger = LoggerFactory.getLogger(UserCollectController.class);

    @Resource
    private IGoodsService goodsService;

    /**
     * 获取收藏商品列表
     * @param userId 用户ID
     * @param identityType 用户类型(0导购，1配送员，2经理，3工人，6顾客)
     * @return
     */
    @PostMapping(value = "/list",produces="application/json;charset=UTF-8")
    public ResultDTO<Object> getPersonalCollectGoodsList(Long userId, Integer identityType){

        logger.info("getPersonalCollectGoodsList CALLED,获取收藏商品列表，入参 userId {},type{}", userId, identityType);

        ResultDTO<Object> resultDTO;

        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getPersonalCollectGoodsList OUT,获取收藏商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        int [] types = {0,1,2,3,6};

        if (ArrayUtils.contains(types,identityType)){
            List<UserCollectGoodsResponse> collectGoodsResponseList = goodsService.findGoodsListByUserIdAndIdentityType(userId,identityType);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "获取收藏商品列表成功", collectGoodsResponseList);
            logger.info("getPersonalCollectGoodsList OUT,获取收藏商品列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                null);
        logger.info("getPersonalCollectGoodsList OUT,获取收藏商品列表失败，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }
}
