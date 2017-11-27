package cn.com.leyizhuang.app.remote;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.PhysicalClassify;
import cn.com.leyizhuang.app.foundation.service.PhysicalClassifyService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.HqAppPhysicalClassifyDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * HQ-APP同步商品分类信息
 * Created by caiyu on 2017/11/27.
 */
@RestController
@RequestMapping(value = "/remote/physical/classify")
public class HqAppPhysicalClassifyController {
    private static final Logger logger = LoggerFactory.getLogger(HqAppPhysicalClassifyController.class);
    @Resource
    private PhysicalClassifyService physicalClassifyService;

    /**
     * 同步添加商品物理分类信息
     * @param hqAppPhysicalClassifyDTO  同步传输类
     * @return  成功或失败
     */
    @PostMapping(value = "/save")
    public ResultDTO<String> addPhusicalClassify(HqAppPhysicalClassifyDTO hqAppPhysicalClassifyDTO){
        logger.warn("addPhusicalClassify CALLED,同步添加商品物理分类信息，入参 hqAppPhysicalClassifyDTO:{}", hqAppPhysicalClassifyDTO);

        if (null == hqAppPhysicalClassifyDTO){
            logger.warn("addPhusicalClassify OUT,同步添加商品物理分类信息失败，商品分类传输对象为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品分类传输对象为空！", null);
        }
        if (null == hqAppPhysicalClassifyDTO.getHqId()){
            logger.warn("addPhusicalClassify OUT,同步添加商品物理分类信息失败，hq分类id为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "hq分类id为空！", null);
        }
        if (StringUtils.isBlank(hqAppPhysicalClassifyDTO.getPhysicalClassifyName())){
            logger.warn("addPhusicalClassify OUT,同步添加商品物理分类信息失败，分类名称为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "分类名称为空！", null);
        }
        if (null == hqAppPhysicalClassifyDTO.getParentCategoryId()){
            logger.warn("addPhusicalClassify OUT,同步添加商品物理分类信息失败，父分类id为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "父分类id为空！", null);
        }

        try {
            PhysicalClassify physical = physicalClassifyService.findByHqId(hqAppPhysicalClassifyDTO.getHqId());
            if (null != physical){
                logger.warn("addPhusicalClassify OUT,同步添加商品物理分类信息失败，该hq分类id已存在！");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该hq分类id已存在！", null);
            }
            PhysicalClassify physicalClassify = new PhysicalClassify();
            physicalClassify.setHqId(hqAppPhysicalClassifyDTO.getHqId());
            physicalClassify.setParentCategoryId(hqAppPhysicalClassifyDTO.getParentCategoryId());
            physicalClassify.setPhysicalClassifyName(hqAppPhysicalClassifyDTO.getPhysicalClassifyName());

            physicalClassifyService.saveSynchronize(physicalClassify);
            logger.warn("addPhusicalClassify OUT,同步添加商品物理分类信息成功！",physicalClassify);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        }catch (Exception e){
            e.printStackTrace();
            logger.warn("addPhusicalClassify EXCEPTION,同步添加商品物理分类信息失败，出参 e:{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，同步添加商品物理分类信息失败", null);
        }
    }

    /**
     * 同步修改商品物理分类信息
     * @param hqAppPhysicalClassifyDTO  同步传输类
     * @return  成功或失败
     */
    @PostMapping(value = "/update")
    public ResultDTO<String> updatePhusicalClassify(HqAppPhysicalClassifyDTO hqAppPhysicalClassifyDTO){
        logger.warn("updatePhusicalClassify CALLED,同步修改商品物理分类信息，入参 hqAppPhysicalClassifyDTO:{}", hqAppPhysicalClassifyDTO);

        if (null == hqAppPhysicalClassifyDTO){
            logger.warn("updatePhusicalClassify OUT,同步修改商品物理分类信息失败，商品分类传输对象为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品分类传输对象为空！", null);
        }

        try {
            PhysicalClassify physical = physicalClassifyService.findByHqId(hqAppPhysicalClassifyDTO.getHqId());
            if (null == physical){
                logger.warn("updatePhusicalClassify OUT,同步修改商品物理分类信息失败，该hq分类id不存在！");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该hq分类id不存在！", null);
            }else {
                physical.setPhysicalClassifyName(hqAppPhysicalClassifyDTO.getPhysicalClassifyName());
                physical.setParentCategoryId(hqAppPhysicalClassifyDTO.getParentCategoryId());

                physicalClassifyService.modifySynchronize(physical);
                logger.warn("updatePhusicalClassify OUT,同步修改商品物理分类信息成功！",physical);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.warn("updatePhusicalClassify EXCEPTION,同步修改商品物理分类信息失败，出参 e:{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，同步修改商品物理分类信息失败", null);
        }
    }
}
