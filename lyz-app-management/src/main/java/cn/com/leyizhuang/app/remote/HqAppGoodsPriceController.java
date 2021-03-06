package cn.com.leyizhuang.app.remote;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.GoodsPriceService;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.GoodsPriceDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author GenerationRoad
 * @date 2017/10/25
 */
@RestController
@RequestMapping(value = "/remote/goodsPrice")
public class HqAppGoodsPriceController {

    private static final Logger logger = LoggerFactory.getLogger(HqAppGoodsPriceController.class);

    @Autowired
    private GoodsPriceService GoodsPriceServiceImpl;

    @Resource
    private GoodsService goodsService;

    @Resource
    private AppStoreService storeService;
    /**
     * @param
     * @return
     * @throws
     * @title 同步新增商品价目表行
     * @descripe
     * @author GenerationRoad
     * @date 2017/10/26
     */
    @PostMapping(value = "save")
    public ResultDTO<Object> addGoodsPrice(@RequestBody GoodsPriceDTO goodsPriceDTO) {
        logger.info("addGoodsPrice CALLED,同步新增商品价目表行，入参 goodsPriceDTO:{}", goodsPriceDTO);
        ResultDTO<Object> resultDTO;
        if (null != goodsPriceDTO) {
//            if (null == goodsPriceDTO.getGid()) {
//                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品id不能为空！", null);
//                logger.info("addGoodsPrice OUT,同步新增商品价目表行失败，出参 resultDTO:{}", resultDTO);
//                return resultDTO;
//            }
            if (StringUtils.isBlank(goodsPriceDTO.getStoreCode())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店编码不能为空！", null);
                logger.info("addGoodsPrice OUT,同步新增商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(goodsPriceDTO.getSku())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品编码不能为空！", null);
                logger.info("addGoodsPrice OUT,同步新增商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getPriceLineId()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "价目表行id不能为空！", null);
                logger.info("addGoodsPrice OUT,同步新增商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getRetailPrice()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "零售价不能为空！", null);
                logger.info("addGoodsPrice OUT,同步新增商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getVIPPrice()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "会员价不能为空！", null);
                logger.info("addGoodsPrice OUT,同步修改商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getStartTime()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品价格生效开始时间不能为空！", null);
                logger.info("addGoodsPrice OUT,同步新增商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(goodsPriceDTO.getPriceType())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品价目类型不能为空！", null);
                logger.info("addGoodsPrice OUT,同步新增商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            try {
                GoodsPrice goodsPrice = this.GoodsPriceServiceImpl.findGoodsPriceByTypeAndStoreIDAndSku(goodsPriceDTO.getPriceType(),goodsPriceDTO.getStoreId(),goodsPriceDTO.getSku());
                GoodsDO goodsDO = goodsService.queryBySku(goodsPriceDTO.getSku());
                AppStore store = storeService.findByStoreCode(goodsPriceDTO.getStoreCode());
                if (null == store){
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未找到该门店信息！", null);
                    logger.info("addGoodsPrice OUT,同步新增商品价目表行失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                if (null == goodsPrice) {
                    goodsPrice = new GoodsPrice();
                    if (null != goodsDO){
                        goodsPrice.setGid(goodsDO.getGid());
                    }
                    goodsPrice.setStoreId(store.getStoreId());
                    goodsPrice.setSku(goodsPriceDTO.getSku());
                    goodsPrice.setPriceLineId(goodsPriceDTO.getPriceLineId());
                    goodsPrice.setRetailPrice(goodsPriceDTO.getRetailPrice());
                    goodsPrice.setStartTime(TimeTransformUtils.stringToLocalDateTime(goodsPriceDTO.getStartTime()));
                    if (null != goodsPriceDTO.getWholesalePrice()) {
                        goodsPrice.setWholesalePrice(goodsPriceDTO.getWholesalePrice());
                    }
                    goodsPrice.setVIPPrice(goodsPriceDTO.getVIPPrice());
                    goodsPrice.setPriceType(goodsPriceDTO.getPriceType());
                    if (null != goodsPriceDTO.getEndTime() && !"".equals(goodsPriceDTO.getEndTime())) {
                        goodsPrice.setEndTime(TimeTransformUtils.stringToLocalDateTime(goodsPriceDTO.getEndTime()));
                    }

                    this.GoodsPriceServiceImpl.save(goodsPrice);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                    logger.info("addGoodsPrice OUT,同步新增商品价目表行成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                } else {
                    if (null != goodsDO){
                        goodsPrice.setGid(goodsDO.getGid());
                    }
                    goodsPrice.setStoreId(store.getStoreId());
                    goodsPrice.setSku(goodsPriceDTO.getSku());
                    goodsPrice.setPriceLineId(goodsPriceDTO.getPriceLineId());
                    goodsPrice.setRetailPrice(goodsPriceDTO.getRetailPrice());
                    goodsPrice.setStartTime(TimeTransformUtils.stringToLocalDateTime(goodsPriceDTO.getStartTime()));
                    if (null != goodsPriceDTO.getWholesalePrice()) {
                        goodsPrice.setWholesalePrice(goodsPriceDTO.getWholesalePrice());
                    }
                    goodsPrice.setVIPPrice(goodsPriceDTO.getVIPPrice());
                    goodsPrice.setPriceType(goodsPriceDTO.getPriceType());
                    if (null != goodsPriceDTO.getEndTime() && !"".equals(goodsPriceDTO.getEndTime())) {
                        goodsPrice.setEndTime(TimeTransformUtils.stringToLocalDateTime(goodsPriceDTO.getEndTime()));
                    }
                    this.GoodsPriceServiceImpl.update(goodsPrice);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                    logger.info("addGoodsPrice OUT,同步新增商品价目表行成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，同步新增商品价目表行失败", null);
                logger.warn("addGoodsPrice EXCEPTION,同步新增商品价目表行失败，出参 resultDTO:{}", resultDTO);
                logger.warn("{}", e);
                return resultDTO;
            }
        }
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品价目表行为空！", null);
        logger.info("addGoodsPrice OUT,同步新增商品价目表行失败，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * @param
     * @return
     * @throws
     * @title 同步修改商品价目表行
     * @descripe
     * @author GenerationRoad
     * @date 2017/10/26
     */
    @PostMapping(value = "modify")
    public ResultDTO<Object> modifyGoodsPrice(@RequestBody GoodsPriceDTO goodsPriceDTO) {
        logger.info("modifyGoodsPrice CALLED,同步修改商品价目表行，入参 goodsPriceDTO:{}", goodsPriceDTO);
        ResultDTO<Object> resultDTO;
        if (null != goodsPriceDTO) {
//            if (null == goodsPriceDTO.getGid()) {
//                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品id不能为空！", null);
//                logger.info("modifyGoodsPrice OUT,同步修改商品价目表行失败，出参 resultDTO:{}", resultDTO);
//                return resultDTO;
//            }
            if (null == goodsPriceDTO.getStoreId()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店id不能为空！", null);
                logger.info("modifyGoodsPrice OUT,同步修改商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(goodsPriceDTO.getSku())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品编码不能为空！", null);
                logger.info("modifyGoodsPrice OUT,同步修改商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getPriceLineId()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "价目表行id不能为空！", null);
                logger.info("modifyGoodsPrice OUT,同步修改商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
//            if (null == goodsPriceDTO.getRetailPrice()) {
//                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "零售价不能为空！", null);
//                logger.info("modifyGoodsPrice OUT,同步修改商品价目表行失败，出参 resultDTO:{}", resultDTO);
//                return resultDTO;
//            }
//            if (null == goodsPriceDTO.getVIPPrice()) {
//                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "会员价不能为空！", null);
//                logger.info("modifyGoodsPrice OUT,同步修改商品价目表行失败，出参 resultDTO:{}", resultDTO);
//                return resultDTO;
//            }
//            if (null == goodsPriceDTO.getEndTime()) {
//                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品价格生效结束时间不能为空！", null);
//                logger.info("modifyGoodsPrice OUT,同步修改商品价目表行失败，出参 resultDTO:{}", resultDTO);
//                return resultDTO;
//            }
            try {
                GoodsPrice goodsPrice = this.GoodsPriceServiceImpl.findGoodsPriceByTypeAndStoreIDAndSku(goodsPriceDTO.getPriceType(),goodsPriceDTO.getStoreId(),goodsPriceDTO.getSku());
                if (null != goodsPrice) {
//                    goodsPrice.setRetailPrice(goodsPriceDTO.getRetailPrice());
//                    goodsPrice.setStartTime(TimeTransformUtils.stringToLocalDateTime(goodsPriceDTO.getStartTime()));
//                    if (null != goodsPriceDTO.getWholesalePrice()){
//                        goodsPrice.setWholesalePrice(goodsPriceDTO.getWholesalePrice());
//                    }
//                    goodsPrice.setVIPPrice(goodsPriceDTO.getVIPPrice());

                    goodsPrice.setVIPPrice(null == goodsPriceDTO.getVIPPrice()?0:goodsPriceDTO.getVIPPrice());
                    goodsPrice.setRetailPrice(null == goodsPriceDTO.getRetailPrice()?0:goodsPriceDTO.getRetailPrice());
                    goodsPrice.setWholesalePrice(null == goodsPriceDTO.getWholesalePrice()?0:goodsPriceDTO.getWholesalePrice());
                    if (null != goodsPriceDTO.getEndTime() && !"".equals(goodsPriceDTO.getEndTime())) {
                        goodsPrice.setEndTime(TimeTransformUtils.stringToLocalDateTime(goodsPriceDTO.getEndTime()));
                    }
                    this.GoodsPriceServiceImpl.modify(goodsPrice);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                    logger.info("modifyGoodsPrice OUT,同步修改商品价目表行成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                } else {
                    GoodsDO goodsDO = goodsService.queryBySku(goodsPriceDTO.getSku());
                    AppStore store = storeService.findByStoreCode(goodsPriceDTO.getStoreCode());
                    goodsPrice = new GoodsPrice();
                    if (null != goodsDO){
                        goodsPrice.setGid(goodsDO.getGid());
                    }
                    goodsPrice.setStoreId(store.getStoreId());
                    goodsPrice.setSku(goodsPriceDTO.getSku());
                    goodsPrice.setPriceLineId(goodsPriceDTO.getPriceLineId());
                    goodsPrice.setRetailPrice(goodsPriceDTO.getRetailPrice());
                    goodsPrice.setStartTime(TimeTransformUtils.stringToLocalDateTime(goodsPriceDTO.getStartTime()));
                    if (null != goodsPriceDTO.getWholesalePrice()) {
                        goodsPrice.setWholesalePrice(goodsPriceDTO.getWholesalePrice());
                    }
                    goodsPrice.setVIPPrice(goodsPriceDTO.getVIPPrice());
                    goodsPrice.setPriceType(goodsPriceDTO.getPriceType());
                    if (null != goodsPriceDTO.getEndTime() && !"".equals(goodsPriceDTO.getEndTime())) {
                        goodsPrice.setEndTime(TimeTransformUtils.stringToLocalDateTime(goodsPriceDTO.getEndTime()));
                    }
                    this.GoodsPriceServiceImpl.save(goodsPrice);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                    logger.info("modifyGoodsPrice OUT,同步修改商品价目表行成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，同步修改商品价目表行失败", null);
                logger.warn("modifyGoodsPrice EXCEPTION,同步修改商品价目表行失败，出参 resultDTO:{}", resultDTO);
                logger.warn("{}", e);
                return resultDTO;
            }
        }
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品价目表行为空！", null);
        logger.info("modifyGoodsPrice OUT,同步修改商品价目表行失败，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * @param
     * @return
     * @throws
     * @title 同步删除商品价目表行
     * @descripe
     * @author GenerationRoad
     * @date 2017/10/26
     */
    @PostMapping(value = "delete")
    public ResultDTO<Object> deleteGoodsPrice(@RequestBody GoodsPriceDTO goodsPriceDTO) {
        logger.info("deleteGoodsPrice CALLED,同步删除商品价目表行，入参 goodsPriceDTO:{}", goodsPriceDTO);
        ResultDTO<Object> resultDTO;
        if (null != goodsPriceDTO) {
//            if (null == goodsPriceDTO.getGid()) {
//                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品id不能为空！", null);
//                logger.info("deleteGoodsPrice OUT,同步删除商品价目表行失败，出参 resultDTO:{}", resultDTO);
//                return resultDTO;
//            }
//            if (null == goodsPriceDTO.getStoreId()) {
//                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店id不能为空！", null);
//                logger.info("deleteGoodsPrice OUT,同步删除商品价目表行失败，出参 resultDTO:{}", resultDTO);
//                return resultDTO;
//            }
//            if (StringUtils.isBlank(goodsPriceDTO.getSku())) {
//                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品编码不能为空！", null);
//                logger.info("deleteGoodsPrice OUT,同步删除商品价目表行失败，出参 resultDTO:{}", resultDTO);
//                return resultDTO;
//            }
            if (null == goodsPriceDTO.getPriceLineId()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "价目表行id不能为空！", null);
                logger.info("deleteGoodsPrice OUT,同步删除商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
//            if (null == goodsPriceDTO.getRetailPrice()) {
//                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "零售价不能为空！", null);
//                logger.info("deleteGoodsPrice OUT,同步删除商品价目表行失败，出参 resultDTO:{}", resultDTO);
//                return resultDTO;
//            }
//            if (null == goodsPriceDTO.getVIPPrice()) {
//                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "会员价不能为空！", null);
//                logger.info("deleteGoodsPrice OUT,同步删除商品价目表行失败，出参 resultDTO:{}", resultDTO);
//                return resultDTO;
//            }
//            if (null == goodsPriceDTO.getStartTime()) {
//                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品价格生效开始时间不能为空！", null);
//                logger.info("deleteGoodsPrice OUT,同步删除商品价目表行失败，出参 resultDTO:{}", resultDTO);
//                return resultDTO;
//            }
            try {
                GoodsPrice goodsPrice = new GoodsPrice();
//                goodsPrice.setGid(goodsPriceDTO.getGid());
//                goodsPrice.setStoreId(goodsPriceDTO.getStoreId());
//                goodsPrice.setSku(goodsPriceDTO.getSku());
                goodsPrice.setPriceLineId(goodsPriceDTO.getPriceLineId());
//                goodsPrice.setRetailPrice(goodsPriceDTO.getRetailPrice());
//                goodsPrice.setStartTime(TimeTransformUtils.stringToLocalDateTime(goodsPriceDTO.getStartTime()));
//                if (null != goodsPriceDTO.getWholesalePrice()){
//                    goodsPrice.setWholesalePrice(goodsPriceDTO.getWholesalePrice());
//                }
//                goodsPrice.setVIPPrice(goodsPriceDTO.getVIPPrice());
//                if (null != goodsPriceDTO.getEndTime() && !"".equals(goodsPriceDTO.getEndTime())){
//                    goodsPrice.setEndTime(TimeTransformUtils.stringToLocalDateTime(goodsPriceDTO.getEndTime()));
//                }

                this.GoodsPriceServiceImpl.delete(goodsPrice);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                logger.info("deleteGoodsPrice OUT,同步删除商品价目表行成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } catch (Exception e) {
                e.printStackTrace();
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，同步删除商品价目表行失败", null);
                logger.warn("deleteGoodsPrice EXCEPTION,同步删除商品价目表行失败，出参 resultDTO:{}", resultDTO);
                logger.warn("{}", e);
                return resultDTO;
            }
        }
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品价目表行为空！", null);
        logger.info("deleteGoodsPrice OUT,同步删除商品价目表行失败，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * @param
     * @return
     * @throws
     * @title 重新同步商品价目表行
     * @descripe
     * @author GenerationRoad
     * @date 2017/10/26
     */
    @PostMapping(value = "retransmission")
    public ResultDTO<Object> retransmissionGoodsPrice(@RequestBody GoodsPriceDTO goodsPriceDTO) {
        logger.info("retransmissionGoodsPrice CALLED,重新同步商品价目表行，入参 goodsPriceDTO:{}", goodsPriceDTO);
        ResultDTO<Object> resultDTO;
        if (null != goodsPriceDTO) {
            if (null == goodsPriceDTO.getGid()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品id不能为空！", null);
                logger.info("retransmissionGoodsPrice OUT,重新同步商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getStoreId()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店id不能为空！", null);
                logger.info("retransmissionGoodsPrice OUT,重新同步商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(goodsPriceDTO.getSku())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品编码不能为空！", null);
                logger.info("retransmissionGoodsPrice OUT,重新同步商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getPriceLineId()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "价目表行id不能为空！", null);
                logger.info("retransmissionGoodsPrice OUT,重新同步商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getRetailPrice()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "零售价不能为空！", null);
                logger.info("retransmissionGoodsPrice OUT,重新同步商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getVIPPrice()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "会员价不能为空！", null);
                logger.info("retransmissionGoodsPrice OUT,重新同步商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getStartTime()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品价格生效开始时间不能为空！", null);
                logger.info("retransmissionGoodsPrice OUT,重新同步商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            try {
                GoodsPrice goodsPrice = this.GoodsPriceServiceImpl.findGoodsPrice(goodsPriceDTO.getPriceLineId());
                if (null == goodsPrice) {
                    goodsPrice = new GoodsPrice();
                    goodsPrice.setGid(goodsPriceDTO.getGid());
                    goodsPrice.setStoreId(goodsPriceDTO.getStoreId());
                    goodsPrice.setSku(goodsPriceDTO.getSku());
                    goodsPrice.setPriceLineId(goodsPriceDTO.getPriceLineId());
                    goodsPrice.setRetailPrice(goodsPriceDTO.getRetailPrice());
                    goodsPrice.setStartTime(TimeTransformUtils.stringToLocalDateTime(goodsPriceDTO.getStartTime()));
                    if (null != goodsPriceDTO.getWholesalePrice()) {
                        goodsPrice.setWholesalePrice(goodsPriceDTO.getWholesalePrice());
                    }
                    goodsPrice.setVIPPrice(goodsPriceDTO.getVIPPrice());
                    if (null != goodsPriceDTO.getEndTime() && !"".equals(goodsPriceDTO.getEndTime())) {
                        goodsPrice.setEndTime(TimeTransformUtils.stringToLocalDateTime(goodsPriceDTO.getEndTime()));
                    }

                    this.GoodsPriceServiceImpl.save(goodsPrice);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                    logger.info("retransmissionGoodsPrice OUT,重新同步新增商品价目表行成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                } else {
//                    goodsPrice.setRetailPrice(goodsPriceDTO.getRetailPrice());
//                    goodsPrice.setStartTime(TimeTransformUtils.stringToLocalDateTime(goodsPriceDTO.getStartTime()));
//                    if (null != goodsPriceDTO.getWholesalePrice()){
//                        goodsPrice.setWholesalePrice(goodsPriceDTO.getWholesalePrice());
//                    }
//                    if (null != goodsPriceDTO.getVIPPrice()){
//                        goodsPrice.setVIPPrice(goodsPriceDTO.getVIPPrice());
//                    }
                    if (null != goodsPriceDTO.getEndTime() && !"".equals(goodsPriceDTO.getEndTime())) {
                        goodsPrice.setEndTime(TimeTransformUtils.stringToLocalDateTime(goodsPriceDTO.getEndTime()));
                    }

                    this.GoodsPriceServiceImpl.modify(goodsPrice);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                    logger.info("retransmissionGoodsPrice OUT,重新同步修改商品价目表行成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，重新同步商品价目表行失败", null);
                logger.warn("retransmissionGoodsPrice EXCEPTION,重新同步商品价目表行失败，出参 resultDTO:{}", resultDTO);
                logger.warn("{}", e);
                return resultDTO;
            }
        }
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品价目表行为空！", null);
        logger.info("retransmissionGoodsPrice OUT,重新同步商品价目表行失败，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }


}
