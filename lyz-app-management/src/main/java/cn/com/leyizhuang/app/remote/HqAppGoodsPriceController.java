package cn.com.leyizhuang.app.remote;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.service.GoodsPriceService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.GoodsPriceDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    /**
     * @title   同步新增商品价目表行
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/10/26
     */
    @PostMapping(value = "save")
    public ResultDTO<Object> addGoodsPrice(@RequestBody GoodsPriceDTO goodsPriceDTO) {
        logger.info("addGoodsPrice CALLED,同步新增商品价目表行，入参 goodsPriceDTO:{}", goodsPriceDTO);
        ResultDTO<Object> resultDTO;
        if (null != goodsPriceDTO) {
            if (null == goodsPriceDTO.getGid()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品id不能为空！", null);
                logger.info("addGoodsPrice OUT,同步新增商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getStoreId()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店id不能为空！", null);
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
            try {
                GoodsPrice goodsPrice = this.GoodsPriceServiceImpl.findGoodsPrice(goodsPriceDTO.getPriceLineId());
                if (null == goodsPrice){
                    goodsPrice = new GoodsPrice();
                    goodsPrice.setGid(goodsPriceDTO.getGid());
                    goodsPrice.setStoreId(goodsPriceDTO.getStoreId());
                    goodsPrice.setSku(goodsPriceDTO.getSku());
                    goodsPrice.setPriceLineId(goodsPriceDTO.getPriceLineId());
                    goodsPrice.setRetailPrice(goodsPriceDTO.getRetailPrice());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    goodsPrice.setStartTime(LocalDateTime.parse(goodsPriceDTO.getStartTime(),df));
                    if (null != goodsPriceDTO.getWholesalePrice()){
                        goodsPrice.setWholesalePrice(goodsPriceDTO.getWholesalePrice());
                    }
                    goodsPrice.setVIPPrice(goodsPriceDTO.getVIPPrice());
                    if (null != goodsPriceDTO.getEndTime() && !"".equals(goodsPriceDTO.getEndTime())){
                        goodsPrice.setEndTime(LocalDateTime.parse(goodsPriceDTO.getEndTime(), df));
                    }

                    this.GoodsPriceServiceImpl.save(goodsPrice);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                    logger.info("addGoodsPrice OUT,同步新增商品价目表行成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }else{
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该商品价格已存在！", null);
                    logger.info("addGoodsPrice OUT,同步新增商品价目表行失败，出参 resultDTO:{}", resultDTO);
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
     * @title   同步修改商品价目表行
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/10/26
     */
    @PostMapping(value = "modify")
    public ResultDTO<Object> modifyGoodsPrice(@RequestBody GoodsPriceDTO goodsPriceDTO) {
        logger.info("modifyGoodsPrice CALLED,同步修改商品价目表行，入参 goodsPriceDTO:{}", goodsPriceDTO);
        ResultDTO<Object> resultDTO;
        if (null != goodsPriceDTO) {
            if (null == goodsPriceDTO.getGid()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品id不能为空！", null);
                logger.info("modifyGoodsPrice OUT,同步修改商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
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
            if (null == goodsPriceDTO.getRetailPrice()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "零售价不能为空！", null);
                logger.info("modifyGoodsPrice OUT,同步修改商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getVIPPrice()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "会员价不能为空！", null);
                logger.info("modifyGoodsPrice OUT,同步修改商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getStartTime()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品价格生效开始时间不能为空！", null);
                logger.info("modifyGoodsPrice OUT,同步修改商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            try {
                GoodsPrice goodsPrice = this.GoodsPriceServiceImpl.findGoodsPrice(goodsPriceDTO.getPriceLineId());
                if (null != goodsPrice){
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    goodsPrice.setRetailPrice(goodsPriceDTO.getRetailPrice());
                    goodsPrice.setStartTime(LocalDateTime.parse(goodsPriceDTO.getStartTime(), df));
                    if (null != goodsPriceDTO.getWholesalePrice()){
                        goodsPrice.setWholesalePrice(goodsPriceDTO.getWholesalePrice());
                    }
                    goodsPrice.setVIPPrice(goodsPriceDTO.getVIPPrice());
                    if (null != goodsPriceDTO.getEndTime() && !"".equals(goodsPriceDTO.getEndTime())){
                        goodsPrice.setEndTime(LocalDateTime.parse(goodsPriceDTO.getEndTime(), df));
                    }

                    this.GoodsPriceServiceImpl.modify(goodsPrice);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                    logger.info("modifyGoodsPrice OUT,同步修改商品价目表行成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }else{
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该商品价格不存在！", null);
                    logger.info("modifyGoodsPrice OUT,同步修改商品价目表行失败，出参 resultDTO:{}", resultDTO);
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
     * @title   同步删除商品价目表行
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/10/26
     */
    @PostMapping(value = "delete")
    public ResultDTO<Object> deleteGoodsPrice(@RequestBody GoodsPriceDTO goodsPriceDTO) {
        logger.info("deleteGoodsPrice CALLED,同步删除商品价目表行，入参 goodsPriceDTO:{}", goodsPriceDTO);
        ResultDTO<Object> resultDTO;
        if (null != goodsPriceDTO) {
            if (null == goodsPriceDTO.getGid()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品id不能为空！", null);
                logger.info("deleteGoodsPrice OUT,同步删除商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getStoreId()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店id不能为空！", null);
                logger.info("deleteGoodsPrice OUT,同步删除商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(goodsPriceDTO.getSku())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品编码不能为空！", null);
                logger.info("deleteGoodsPrice OUT,同步删除商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getPriceLineId()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "价目表行id不能为空！", null);
                logger.info("deleteGoodsPrice OUT,同步删除商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getRetailPrice()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "零售价不能为空！", null);
                logger.info("deleteGoodsPrice OUT,同步删除商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getVIPPrice()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "会员价不能为空！", null);
                logger.info("deleteGoodsPrice OUT,同步删除商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsPriceDTO.getStartTime()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品价格生效开始时间不能为空！", null);
                logger.info("deleteGoodsPrice OUT,同步删除商品价目表行失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            try {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                GoodsPrice goodsPrice = new GoodsPrice();
                goodsPrice.setGid(goodsPriceDTO.getGid());
                goodsPrice.setStoreId(goodsPriceDTO.getStoreId());
                goodsPrice.setSku(goodsPriceDTO.getSku());
                goodsPrice.setPriceLineId(goodsPriceDTO.getPriceLineId());
                goodsPrice.setRetailPrice(goodsPriceDTO.getRetailPrice());
                goodsPrice.setStartTime(LocalDateTime.parse(goodsPriceDTO.getStartTime(), df));
                if (null != goodsPriceDTO.getWholesalePrice()){
                    goodsPrice.setWholesalePrice(goodsPriceDTO.getWholesalePrice());
                }
                goodsPrice.setVIPPrice(goodsPriceDTO.getVIPPrice());
                if (null != goodsPriceDTO.getEndTime() && !"".equals(goodsPriceDTO.getEndTime())){
                    goodsPrice.setEndTime(LocalDateTime.parse(goodsPriceDTO.getEndTime(), df));
                }

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
     * @title   重新同步商品价目表行
     * @descripe
     * @param
     * @return
     * @throws
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
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                GoodsPrice goodsPrice = this.GoodsPriceServiceImpl.findGoodsPrice(goodsPriceDTO.getPriceLineId());
                if (null == goodsPrice){
                    goodsPrice = new GoodsPrice();
                    goodsPrice.setGid(goodsPriceDTO.getGid());
                    goodsPrice.setStoreId(goodsPriceDTO.getStoreId());
                    goodsPrice.setSku(goodsPriceDTO.getSku());
                    goodsPrice.setPriceLineId(goodsPriceDTO.getPriceLineId());
                    goodsPrice.setRetailPrice(goodsPriceDTO.getRetailPrice());
                    goodsPrice.setStartTime(LocalDateTime.parse(goodsPriceDTO.getStartTime(), df));
                    if (null != goodsPriceDTO.getWholesalePrice()){
                        goodsPrice.setWholesalePrice(goodsPriceDTO.getWholesalePrice());
                    }
                    goodsPrice.setVIPPrice(goodsPriceDTO.getVIPPrice());
                    if (null != goodsPriceDTO.getEndTime() && !"".equals(goodsPriceDTO.getEndTime())){
                        goodsPrice.setEndTime(LocalDateTime.parse(goodsPriceDTO.getEndTime(), df));
                    }

                    this.GoodsPriceServiceImpl.save(goodsPrice);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                    logger.info("retransmissionGoodsPrice OUT,重新同步新增商品价目表行成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }else{
                    goodsPrice.setRetailPrice(goodsPriceDTO.getRetailPrice());
                    goodsPrice.setStartTime(LocalDateTime.parse(goodsPriceDTO.getStartTime(), df));
                    if (null != goodsPriceDTO.getWholesalePrice()){
                        goodsPrice.setWholesalePrice(goodsPriceDTO.getWholesalePrice());
                    }
                    if (null != goodsPriceDTO.getVIPPrice()){
                        goodsPrice.setVIPPrice(goodsPriceDTO.getVIPPrice());
                    }
                    if (null != goodsPriceDTO.getEndTime() && !"".equals(goodsPriceDTO.getEndTime())){
                        goodsPrice.setEndTime(LocalDateTime.parse(goodsPriceDTO.getEndTime(), df));
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
