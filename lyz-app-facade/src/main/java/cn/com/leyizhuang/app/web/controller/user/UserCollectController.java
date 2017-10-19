package cn.com.leyizhuang.app.web.controller.user;

import cn.com.leyizhuang.app.foundation.pojo.response.UserGoodsResponse;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
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
 * 个人商品收藏接口
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/9/28.
 * Time: 13:40.
 */
@RestController
@RequestMapping(value = "/app/user/collect")
public class UserCollectController {

    private static final Logger logger = LoggerFactory.getLogger(UserCollectController.class);

    @Resource
    private GoodsService goodsService;

    /**
     * 获取收藏商品列表
     *
     * @param userId       用户ID
     * @param identityType 用户类型
     * @return
     */
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getPersonalCollectGoodsList(Long userId, Integer identityType) {

        logger.info("getPersonalCollectGoodsList CALLED,获取收藏商品列表，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<Object> resultDTO;

        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getPersonalCollectGoodsList OUT,获取收藏商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("getPersonalCollectGoodsList OUT,获取收藏商品列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<UserGoodsResponse> collectGoodsResponseList = goodsService.findGoodsCollectListByUserIdAndIdentityType(userId, identityType);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, collectGoodsResponseList);
            logger.info("getPersonalCollectGoodsList OUT,获取收藏商品列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取收藏商品列表失败", null);
            logger.warn("getPersonalCollectGoodsList EXCEPTION,获取收藏商品列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 添加商品到我的收藏
     *
     * @param userId
     * @param goodsId
     * @param identityType
     * @return
     */
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ResultDTO addCollectGoods(Long userId, Long goodsId, Integer identityType) {

        logger.info("addCollectGoods CALLED,添加商品到我的收藏，入参 userId {},goodsId {},identityType{}", userId, goodsId, identityType);

        ResultDTO resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("addCollectGoods OUT,添加商品到我的收藏失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == goodsId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品id不能为空", null);
            logger.info("addCollectGoods OUT,添加商品到我的收藏失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("addCollectGoods OUT,添加商品到我的收藏失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if(identityType == 1){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该用户类型没有此功能", null);
            logger.info("addCollectGoods OUT,添加商品到我的收藏失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            goodsService.addCollectGoodsByUserIdAndGoodsIdAndIdentityType(userId, goodsId, identityType);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("addCollectGoods OUT,添加商品到我的收藏成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，添加商品到我的收藏失败", null);
            logger.warn("addCollectGoods EXCEPTION,添加商品到我的收藏失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 移除我的收藏商品
     *
     * @param userId
     * @param goodsId
     * @param identityType
     * @return
     */
    @PostMapping(value = "/remove", produces = "application/json;charset=UTF-8")
    public ResultDTO removeCollectGoods(Long userId, Long goodsId, Integer identityType) {

        logger.info("removeCollectGoods CALLED,移除我的收藏商品，入参 userId {},goodsId {},identityType{}", userId, goodsId, identityType);

        ResultDTO resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("removeCollectGoods OUT,移除我的收藏商品失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == goodsId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品id不能为空", null);
            logger.info("removeCollectGoods OUT,移除我的收藏商品失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("removeCollectGoods OUT,移除我的收藏商品失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if(identityType ==1){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该用户类型没有此功能", null);
            logger.info("removeCollectGoods OUT,移除我的收藏商品失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            goodsService.removeCollectGoodsByUserIdAndGoodsIdAndIdentityType(userId, goodsId, identityType);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("removeCollectGoods OUT,移除我的收藏商品成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，移除我的收藏商品失败", null);
            logger.warn("removeCollectGoods EXCEPTION,移除我的收藏商品失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}
