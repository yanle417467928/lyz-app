package cn.com.leyizhuang.app.remote;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPhysicalClassify;
import cn.com.leyizhuang.app.foundation.service.GoodsPhysicalClassifyService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.HqAppGoodsPhysicalClassifyDTO;
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
@RequestMapping(value = "/remote/goods/physical/classify")
public class HqAppGoodsPhysicalClassifyController {
    private static final Logger logger = LoggerFactory.getLogger(HqAppGoodsPhysicalClassifyController.class);
    @Resource
    private GoodsPhysicalClassifyService goodsPhysicalClassifyService;

    /**
     * 同步添加商品物理分类信息
     * @param hqAppGoodsPhysicalClassifyDTO  同步传输类
     * @return  成功或失败
     */
    @PostMapping(value = "/save")
    public ResultDTO<String> addPhusicalClassify(HqAppGoodsPhysicalClassifyDTO hqAppGoodsPhysicalClassifyDTO){
        logger.warn("addPhusicalClassify CALLED,同步添加商品物理分类信息，入参 hqAppGoodsPhysicalClassifyDTO:{}", hqAppGoodsPhysicalClassifyDTO);

        if (null == hqAppGoodsPhysicalClassifyDTO){
            logger.warn("addPhusicalClassify OUT,同步添加商品物理分类信息失败，商品分类传输对象为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品分类传输对象为空！", null);
        }
        if (null == hqAppGoodsPhysicalClassifyDTO.getHqId()){
            logger.warn("addPhusicalClassify OUT,同步添加商品物理分类信息失败，hq分类id为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "hq分类id为空！", null);
        }
        if (StringUtils.isBlank(hqAppGoodsPhysicalClassifyDTO.getPhysicalClassifyName())){
            logger.warn("addPhusicalClassify OUT,同步添加商品物理分类信息失败，分类名称为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "分类名称为空！", null);
        }
        if (null == hqAppGoodsPhysicalClassifyDTO.getParentCategoryId()){
            logger.warn("addPhusicalClassify OUT,同步添加商品物理分类信息失败，父分类id为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "父分类id为空！", null);
        }

        try {
            GoodsPhysicalClassify physical = goodsPhysicalClassifyService.findByHqId(hqAppGoodsPhysicalClassifyDTO.getHqId());
            if (null != physical){
                logger.warn("addPhusicalClassify OUT,同步添加商品物理分类信息失败，该hq分类id已存在！");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该hq分类id已存在！", null);
            }
            GoodsPhysicalClassify goodsPhysicalClassify = new GoodsPhysicalClassify();
            goodsPhysicalClassify.setHqId(hqAppGoodsPhysicalClassifyDTO.getHqId());
            goodsPhysicalClassify.setParentCategoryId(hqAppGoodsPhysicalClassifyDTO.getParentCategoryId());
            goodsPhysicalClassify.setPhysicalClassifyName(hqAppGoodsPhysicalClassifyDTO.getPhysicalClassifyName());

            goodsPhysicalClassifyService.saveSynchronize(goodsPhysicalClassify);
            logger.warn("addPhusicalClassify OUT,同步添加商品物理分类信息成功！", goodsPhysicalClassify);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        }catch (Exception e){
            e.printStackTrace();
            logger.warn("addPhusicalClassify EXCEPTION,同步添加商品物理分类信息失败，出参 e:{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，同步添加商品物理分类信息失败", null);
        }
    }

    /**
     * 同步修改商品物理分类信息
     * @param hqAppGoodsPhysicalClassifyDTO  同步传输类
     * @return  成功或失败
     */
    @PostMapping(value = "/update")
    public ResultDTO<String> updatePhusicalClassify(HqAppGoodsPhysicalClassifyDTO hqAppGoodsPhysicalClassifyDTO){
        logger.warn("updatePhusicalClassify CALLED,同步修改商品物理分类信息，入参 hqAppGoodsPhysicalClassifyDTO:{}", hqAppGoodsPhysicalClassifyDTO);

        if (null == hqAppGoodsPhysicalClassifyDTO){
            logger.warn("updatePhusicalClassify OUT,同步修改商品物理分类信息失败，商品分类传输对象为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品分类传输对象为空！", null);
        }

        try {
            GoodsPhysicalClassify physical = goodsPhysicalClassifyService.findByHqId(hqAppGoodsPhysicalClassifyDTO.getHqId());
            if (null == physical){
                logger.warn("updatePhusicalClassify OUT,同步修改商品物理分类信息失败，该hq分类id不存在！");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该hq分类id不存在！", null);
            }else {
                physical.setPhysicalClassifyName(hqAppGoodsPhysicalClassifyDTO.getPhysicalClassifyName());
                physical.setParentCategoryId(hqAppGoodsPhysicalClassifyDTO.getParentCategoryId());

                goodsPhysicalClassifyService.modifySynchronize(physical);
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
