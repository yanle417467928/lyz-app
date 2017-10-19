package cn.com.leyizhuang.app.web.controller.materialList;

import cn.com.leyizhuang.app.foundation.pojo.request.DeliveryAddressRequest;
import cn.com.leyizhuang.app.foundation.service.MaterialListService;
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
 * @date 2017/10/18
 */
@RestController
@RequestMapping("/app/materialList")
public class MaterialListController {
    private static final Logger logger = LoggerFactory.getLogger(MaterialListController.class);

    @Autowired
    private MaterialListService materialListServiceImpl;

    /**
     * @title   单或多个商品加入下料清单
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/10/18
     */
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> addMaterialList(Long userId, Integer identityType, String params) {
        logger.info("addMaterialList CALLED,单或多个商品加入下料清单，入参 userId:{} identityType:{} params:{}", userId, identityType, params);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("addMaterialList OUT,单或多个商品加入下料清单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                        null);
                logger.info("addMaterialList OUT,单或多个商品加入下料清单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == params) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品和数量信息不能为空！", null);
                logger.info("addMaterialList OUT,单或多个商品加入下料清单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            String[] param = params.split(",");

            this.materialListServiceImpl.batchSave(userId, identityType, param);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("addMaterialList OUT,单或多个商品加入下料清单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,单或多个商品加入下料清单失败!", null);
            logger.warn("addMaterialList EXCEPTION,单或多个商品加入下料清单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
      @title  编辑下料清单商品数量
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/10/18
     */
    @PostMapping(value = "/edit/number", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> editNumber(Long userId, Integer identityType, Long materialListId, Integer qty) {
        logger.info("editNumber CALLED,编辑下料清单商品数量，入参 userId:{}, identityType:{}, materialListId:{}, qty{}", userId, identityType, materialListId, qty);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("editNumber OUT,编辑下料清单商品数量失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                        null);
                logger.info("editNumber OUT,编辑下料清单商品数量失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == materialListId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品ID不能为空！", null);
                logger.info("editNumber OUT,编辑下料清单商品数量失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == qty) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品数量不能为空！", null);
                logger.info("editNumber OUT,编辑下料清单商品数量失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            this.materialListServiceImpl.modifyQty(materialListId, qty);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("editNumber OUT,编辑下料清单商品数量成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,编辑下料清单商品数量失败!", null);
            logger.warn("editNumber EXCEPTION,编辑下料清单商品数量失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**  
     * @title   删除下料清单商品
     * @descripe
     * @param
     * @return 
     * @throws 
     * @author GenerationRoad
     * @date 2017/10/18
     */
    @PostMapping(value = "/delete", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> deletematerialList(Long userId, Integer identityType, String materialListIds) {
        logger.info("deletematerialList CALLED,删除下料清单商品，入参 userId:{} identityType:{} materialListId:{}", userId, identityType, materialListIds);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("deletematerialList OUT,删除下料清单商品失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == materialListIds) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "下料清单商品ID不能为空！", null);
            logger.info("deletematerialList OUT,删除下料清单商品失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        String[] param = materialListIds.split(",");
        List<Long> list = new ArrayList<Long>();
        for (int i = 0; i < param.length; i++) {
            list.add(Long.parseLong(param[i]));
        }
        this.materialListServiceImpl.deleteMaterialList(list);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        logger.info("deletematerialList OUT,删除下料清单商品成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

}
