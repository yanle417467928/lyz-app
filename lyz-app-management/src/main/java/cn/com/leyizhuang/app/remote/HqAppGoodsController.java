package cn.com.leyizhuang.app.remote;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.HqAppGoodsDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

/**
 * HQ-APP商品同步
 * Created by caiyu on 2017/11/23.
 */
@RestController
@RequestMapping(value = "/remote/goods")
public class HqAppGoodsController {
    private static final Logger logger = LoggerFactory.getLogger(HqAppGoodsController.class);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Resource
    private GoodsService goodsService;

    /**
     * HQ-APP同步添加商品
     *
     * @param hqAppGoodsDTO 商品传输类
     * @return 成功或失败
     */
    @PostMapping(value = "/save")
    public ResultDTO<String> addGoods(@RequestBody HqAppGoodsDTO hqAppGoodsDTO) {
        logger.warn("addGoods CALLED,同步存储商品信息，入参 hqAppGoodsDTO:{}", hqAppGoodsDTO);

        if (null == hqAppGoodsDTO) {
            logger.warn("addGoods OUT,同步存储商品信息失败，商品传输对象为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品传输对象为空！", null);
        }
        if (StringUtils.isBlank(hqAppGoodsDTO.getSku())) {
            logger.warn("addGoods OUT,同步存储商品信息失败，出参 resultDTO:{}", hqAppGoodsDTO.getSku());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "物料编号不能为空！", null);
        }
        if (StringUtils.isBlank(hqAppGoodsDTO.getMaterialsName())) {
            logger.warn("addGoods OUT,同步存储商品信息失败，出参 resultDTO:{}", hqAppGoodsDTO.getMaterialsName());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "物料名称不能为空！", null);
        }
        if (StringUtils.isBlank(hqAppGoodsDTO.getMaterialsCode())) {
            logger.warn("addGoods OUT,同步存储商品信息失败，出参 resultDTO:{}", hqAppGoodsDTO.getMaterialsCode());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "物料条码不能为空！", null);
        }
        if (StringUtils.isBlank(hqAppGoodsDTO.getGoodsSpecification())) {
            logger.warn("addGoods OUT,同步存储商品信息失败，出参 resultDTO:{}", hqAppGoodsDTO.getGoodsSpecification());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "物料规格不能为空！", null);
        }
        if (StringUtils.isBlank(hqAppGoodsDTO.getGoodsUnit())) {
            logger.warn("addGoods OUT,同步存储商品信息失败，出参 resultDTO:{}", hqAppGoodsDTO.getGoodsUnit());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "物料单位不能为空！", null);
        }
        if (StringUtils.isBlank(hqAppGoodsDTO.getBrdName())) {
            logger.warn("addGoods OUT,同步存储商品信息失败，出参 resultDTO:{}", hqAppGoodsDTO.getBrdName());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品品牌不能为空！", null);
        }
        if (null == hqAppGoodsDTO.getPhysicalClassify()) {
            logger.warn("addGoods OUT,同步存储商品信息失败，出参 resultDTO:{}", hqAppGoodsDTO.getPhysicalClassify());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "物理分类不能为空！", null);
        }
        if (StringUtils.isBlank(hqAppGoodsDTO.getTypeName())) {
            logger.warn("addGoods OUT,同步存储商品信息失败，出参 resultDTO:{}", hqAppGoodsDTO.getTypeName());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "物料类型不能为空！", null);
        }
        if (null == hqAppGoodsDTO.getMaterialsEnable()) {
            logger.warn("addGoods OUT,同步存储商品信息失败，出参 resultDTO:{}", hqAppGoodsDTO.getMaterialsEnable());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "物料状态不能为空！", null);
        }
        if (null == hqAppGoodsDTO.getCompanyFlag()) {
            logger.warn("addGoods OUT,同步存储商品信息失败，出参 resultDTO:{}", hqAppGoodsDTO.getCompanyFlag());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "公司标识不能为空！", null);
        }
        if (null == hqAppGoodsDTO.getCreateTime()) {
            logger.warn("addGoods OUT,同步存储商品信息失败，出参 resultDTO:{}", hqAppGoodsDTO.getCreateTime());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "创建时间不能为空！", null);
        }

        try {
            GoodsDO goodsDO = goodsService.queryBySku(hqAppGoodsDTO.getSku());
            if (null == goodsDO) {
                GoodsDO goods = new GoodsDO();
                goods.setSkuName(hqAppGoodsDTO.getSkuName());
                goods.setSku(hqAppGoodsDTO.getSku());
                goods.setCoverImageUri(hqAppGoodsDTO.getCoverImageUri());
                goods.setRotationImageUri(hqAppGoodsDTO.getRotationImageUri());
                goods.setGoodsSpecification(hqAppGoodsDTO.getGoodsSpecification());
                goods.setGoodsUnit(hqAppGoodsDTO.getGoodsUnit());
                goods.setCid(hqAppGoodsDTO.getCId());
                goods.setCategoryName(hqAppGoodsDTO.getCategoryName());
                goods.setBrdId(hqAppGoodsDTO.getBrdId());
                goods.setBrandName(hqAppGoodsDTO.getBrdName());
                goods.setGtid(hqAppGoodsDTO.getGtid());
                goods.setTypeName(hqAppGoodsDTO.getTypeName());
                goods.setIsIndexRecommend(hqAppGoodsDTO.getIsIndexRecommend());
                goods.setIsHot(hqAppGoodsDTO.getIsHot());
                goods.setIsNew(hqAppGoodsDTO.getIsNew());
                goods.setSortId(hqAppGoodsDTO.getSortId());
                goods.setIsColorMixing(hqAppGoodsDTO.getIsColorMixing());
                goods.setCreateTime(sdf.parse(hqAppGoodsDTO.getCreateTime()));
                goods.setMaterialsCode(hqAppGoodsDTO.getMaterialsCode());
                goods.setPhysicalClassify(hqAppGoodsDTO.getPhysicalClassify());
                goods.setMaterialsEnable(hqAppGoodsDTO.getMaterialsEnable());
                goods.setCompanyFlag(hqAppGoodsDTO.getCompanyFlag());
                goods.setGoodsDetial(hqAppGoodsDTO.getGoodsDetial());
                goods.setSearchKeyword(hqAppGoodsDTO.getSearchKeyword());
                goods.setProductGrade(hqAppGoodsDTO.getProductGrade());
                goods.setMaterialsName(hqAppGoodsDTO.getMaterialsName());

                goodsService.saveSynchronize(goods);
                logger.warn("addGoods OUT,同步存储商品信息成功！",goods);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } else {
                logger.warn("addGoods OUT,同步存储商品信息失败，该SKU商品已存在！");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该SKU商品已存在！", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("addGoods EXCEPTION,同步存储商品信息失败，出参 e:{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，同步存储商品信息失败", null);
        }
    }

    /**
     * HQ-APP同步修改商品信息
     *
     * @param hqAppGoodsDTO 商品传输类
     * @return 成功或失败
     */
    @PostMapping(value = "/modify")
    public ResultDTO<String> updateGoods(HqAppGoodsDTO hqAppGoodsDTO) {
        logger.warn("updateGoods CALLED,同步修改商品信息，入参 hqAppGoodsDTO:{}", hqAppGoodsDTO);
        if (null == hqAppGoodsDTO) {
            logger.warn("updateGoods OUT,同步修改商品信息失败，商品传输对象为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品传输对象为空！", null);
        }
        try {
            GoodsDO goodsDO = goodsService.queryBySku(hqAppGoodsDTO.getSku());
            if (goodsDO != null) {
                if (!StringUtils.isBlank(hqAppGoodsDTO.getSkuName())) {
                    goodsDO.setSkuName(hqAppGoodsDTO.getSkuName());
                }
                if (!StringUtils.isBlank(hqAppGoodsDTO.getCoverImageUri())) {
                    goodsDO.setCoverImageUri(hqAppGoodsDTO.getCoverImageUri());
                }
                if (!StringUtils.isBlank(hqAppGoodsDTO.getRotationImageUri())) {
                    goodsDO.setRotationImageUri(hqAppGoodsDTO.getRotationImageUri());
                }
                if (!StringUtils.isBlank(hqAppGoodsDTO.getGoodsSpecification())) {
                    goodsDO.setGoodsSpecification(hqAppGoodsDTO.getGoodsSpecification());
                }
                if (!StringUtils.isBlank(hqAppGoodsDTO.getGoodsUnit())) {
                    goodsDO.setGoodsUnit(hqAppGoodsDTO.getGoodsUnit());
                }
                if (null != hqAppGoodsDTO.getCId()) {
                    goodsDO.setCid(hqAppGoodsDTO.getCId());
                }
                if (!StringUtils.isBlank(hqAppGoodsDTO.getCategoryName())) {
                    goodsDO.setCategoryName(hqAppGoodsDTO.getCategoryName());
                }
                if (null != hqAppGoodsDTO.getBrdId()) {
                    goodsDO.setBrdId(hqAppGoodsDTO.getBrdId());
                }
                if (!StringUtils.isBlank(hqAppGoodsDTO.getBrdName())) {
                    goodsDO.setBrandName(hqAppGoodsDTO.getBrdName());
                }
                if (null != hqAppGoodsDTO.getGtid()) {
                    goodsDO.setGtid(hqAppGoodsDTO.getGtid());
                }
                if (!StringUtils.isBlank(hqAppGoodsDTO.getTypeName())) {
                    goodsDO.setTypeName(hqAppGoodsDTO.getTypeName());
                }
                if (null != hqAppGoodsDTO.getIsIndexRecommend()) {
                    goodsDO.setIsIndexRecommend(hqAppGoodsDTO.getIsIndexRecommend());
                }
                if (null != hqAppGoodsDTO.getIsHot()){
                    goodsDO.setIsHot(hqAppGoodsDTO.getIsHot());
                }
                if (null != hqAppGoodsDTO.getIsNew()){
                    goodsDO.setIsNew(hqAppGoodsDTO.getIsNew());
                }
                if (null != hqAppGoodsDTO.getSortId()){
                    goodsDO.setSortId(hqAppGoodsDTO.getSortId());
                }
                if (null != hqAppGoodsDTO.getIsColorMixing()){
                    goodsDO.setIsColorMixing(hqAppGoodsDTO.getIsColorMixing());
                }
                if (null != hqAppGoodsDTO.getCreateTime()){
                    goodsDO.setCreateTime(sdf.parse(hqAppGoodsDTO.getCreateTime()));
                }
                if (!StringUtils.isBlank(hqAppGoodsDTO.getMaterialsCode())){
                    goodsDO.setMaterialsCode(hqAppGoodsDTO.getMaterialsCode());
                }
                if (null != hqAppGoodsDTO.getPhysicalClassify()){
                    goodsDO.setPhysicalClassify(hqAppGoodsDTO.getPhysicalClassify());
                }
                if (null != hqAppGoodsDTO.getMaterialsEnable()){
                    goodsDO.setMaterialsEnable(hqAppGoodsDTO.getMaterialsEnable());
                }
                if (!StringUtils.isBlank(hqAppGoodsDTO.getCompanyFlag())){
                    goodsDO.setCompanyFlag(hqAppGoodsDTO.getCompanyFlag());
                }
                if (!StringUtils.isBlank(hqAppGoodsDTO.getGoodsDetial())){
                    goodsDO.setGoodsDetial(hqAppGoodsDTO.getGoodsDetial());
                }
                if (!StringUtils.isBlank(hqAppGoodsDTO.getSearchKeyword())){
                    goodsDO.setSearchKeyword(hqAppGoodsDTO.getSearchKeyword());
                }
                if (!StringUtils.isBlank(hqAppGoodsDTO.getProductGrade())){
                    goodsDO.setProductGrade(hqAppGoodsDTO.getProductGrade());
                }
                if (!StringUtils.isBlank(hqAppGoodsDTO.getMaterialsName())){
                    goodsDO.setMaterialsName(hqAppGoodsDTO.getMaterialsName());
                }

                goodsService.modifySynchronize(goodsDO);
                logger.warn("updateGoods OUT,同步修改商品信息成功！",goodsDO);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } else {
                logger.warn("updateGoods OUT,同步修改商品信息失败，该SKU商品已存在！");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该SKU商品不存在！", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("updateGoods EXCEPTION,同步修改商品信息失败，出参 e:{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，同步修改商品信息失败", null);
        }
    }

    /**
     * HQ-APP同步删除商品信息
     * @param sku   物料编码
     * @return  成功或失败
     */
    @PostMapping(value = "/delete")
    public ResultDTO<String> deleteGoods(String sku){
        logger.warn("deleteGoods CALLED,同步删除商品信息，入参 sku:{}", sku);
        if (StringUtils.isBlank(sku)){
            logger.warn("deleteGoods OUT,同步删除商品信息失败，出参 sku:{}", sku);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "物料编号不能为空！", null);
        }
        try{
            goodsService.deleteSynchronize(sku);
            logger.warn("deleteGoods OUT,同步删除商品信息成功！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        }catch (Exception e){
            e.printStackTrace();
            logger.warn("deleteGoods EXCEPTION,同步删除商品信息失败，出参 e:{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，同步删除商品信息失败", null);
        }
    }
}
