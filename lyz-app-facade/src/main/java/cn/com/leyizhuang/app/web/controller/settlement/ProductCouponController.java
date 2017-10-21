package cn.com.leyizhuang.app.web.controller.settlement;

import cn.com.leyizhuang.app.foundation.pojo.response.OrderUsableProductCouponResponse;
import cn.com.leyizhuang.app.foundation.service.ProductCouponService;
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
 * @date 2017/10/19
 */
@RestController
@RequestMapping("/app/productCoupon")
public class ProductCouponController {
    private static final Logger logger = LoggerFactory.getLogger(ProductCouponController.class);

    @Autowired
    private ProductCouponService productCouponServiceImpl;

    /**  
     * @title   获取订单可用产品券列表
     * @descripe
     * @param 
     * @return 
     * @throws 
     * @author GenerationRoad
     * @date 2017/10/19
     */
    @PostMapping(value = "/get/usable/product/coupon", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getUsableProductCoupon(Long userId, Integer identityType, String goodsParam, String giftParam) {
        logger.info("getUsableProductCoupon CALLED,获取订单可用产品券列表，入参 userId:{} identityType:{} goodsParam:{} giftParam:{}", userId, identityType, goodsParam, giftParam);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("getUsableProductCoupon OUT,获取订单可用产品券列表失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType || 6 != identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
                logger.info("getUsableProductCoupon OUT,获取订单可用产品券列表失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == goodsParam) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品和数量信息不能为空！", null);
                logger.info("getUsableProductCoupon OUT,获取订单可用产品券列表失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            List<Long> goodsIds = new ArrayList<Long>();
            String[] param = goodsParam.split(",");
            for (int i = 0; i < param.length; i++) {
                String[] goodsIdParam = param[i].split("-");
                if (goodsIdParam.length != 2) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品和数量信息不能为空！", null);
                    logger.info("getUsableProductCoupon OUT,获取订单可用产品券列表失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                goodsIds.add(Long.parseLong(goodsIdParam[0]));
            }
            List<OrderUsableProductCouponResponse> productCouponResponseList = this.productCouponServiceImpl.findProductCouponByCustomerIdAndGoodsId(userId, goodsIds);
            //计算订单可使用产品卷（先查可参加的活动，再减去赠品）


            for (int i = 0; i < productCouponResponseList.size(); i++) {
                if (null != productCouponResponseList.get(i).getCoverImageUri()) {
                    String[] url = productCouponResponseList.get(i).getCoverImageUri().split(",");
                    if (url.length > 0){
                        productCouponResponseList.get(i).setCoverImageUri(url[0]);
                    } else {
                        productCouponResponseList.get(i).setCoverImageUri("");
                    }
                }
                productCouponResponseList.get(i).setUsableNumber(1);
            }

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, productCouponResponseList);
            logger.info("getUsableProductCoupon OUT,获取订单可用产品券列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,获取订单可用产品券列表失败!", null);
            logger.warn("getUsableProductCoupon EXCEPTION,获取订单可用产品券列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

}
