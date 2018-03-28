package cn.com.leyizhuang.app.remote;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.GoodsGrade;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.service.GoodsGradeService;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.HqAppGoodsDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.HqAppGoodsGradeDTO;
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
 * HQ-APP商品等级同步
 * Created by liuh on 2018/03/27.
 */
@RestController
@RequestMapping(value = "/remote/goodsGrade")
public class HqAppGoodsGradeController {
    private static final Logger logger = LoggerFactory.getLogger(HqAppGoodsGradeController.class);

    @Resource
    private GoodsGradeService goodsGradeService;


    /**
     * HQ-APP同步商品等级信息
     *
     * @param hqAppGoodsGradeDTO
     * @return 成功或失败
     */
    @PostMapping(value = "/sync")
    public ResultDTO<String> synchGoodsGrade(@RequestBody HqAppGoodsGradeDTO hqAppGoodsGradeDTO) {
        logger.warn("synchGoods CALLED,同步商品档次信息，入参 hqAppGoodsDTO:{}", hqAppGoodsGradeDTO);

        if (null == hqAppGoodsGradeDTO) {
            logger.warn("synchGoodsGrade OUT,同步商品档次信息失败，商品传输对象为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品传输对象为空！", null);
        }
        if (StringUtils.isBlank(hqAppGoodsGradeDTO.getSku())) {
            logger.warn("synchGoodsGrade OUT,同步存品档次信息失败，出参 resultDTO:{}", hqAppGoodsGradeDTO.getSku());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品编号不能为空！", null);
        }
        if (null == hqAppGoodsGradeDTO.getGrade()) {
            logger.warn("synchGoodsGrade OUT,同步商品档次信息失败，出参 resultDTO:{}", hqAppGoodsGradeDTO.getGrade());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品档次不能为空！", null);
        }
        if (null == hqAppGoodsGradeDTO.getSobId()) {
            logger.warn("synchGoodsGrade OUT,同步商品档次信息失败，出参 resultDTO:{}", hqAppGoodsGradeDTO.getSobId());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "分公司id不能为空！", null);
        }
        try {
            GoodsGrade goodsGrade = goodsGradeService.queryBySkuAndSobId(hqAppGoodsGradeDTO.getSku(), hqAppGoodsGradeDTO.getSobId());
            if (null == goodsGrade) {
                goodsGrade.setGrade(hqAppGoodsGradeDTO.getGrade());
                goodsGrade.setSku(hqAppGoodsGradeDTO.getSku());
                goodsGrade.setSkuName(hqAppGoodsGradeDTO.getSkuName());
                goodsGrade.setSobId(hqAppGoodsGradeDTO.getSobId());
                goodsGradeService.addGoodsGrade(goodsGrade);
            } else {
                goodsGrade.setGrade(hqAppGoodsGradeDTO.getGrade());
                goodsGrade.setSku(hqAppGoodsGradeDTO.getSku());
                goodsGrade.setSkuName(hqAppGoodsGradeDTO.getSkuName());
                goodsGrade.setSobId(hqAppGoodsGradeDTO.getSobId());
                goodsGradeService.updateGoodsGrade(goodsGrade);
            }
            logger.warn("synchGoodsGrade OUT,同步商品档次信息成功！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("synchGoodsGrade EXCEPTION,发生未知错误，同步商品档次信息失败");
            logger.warn("{}", e);
            return null;
        }
    }
}
