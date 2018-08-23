package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.ProductCoupon;
import cn.com.leyizhuang.app.foundation.service.ProductCouponSendService;
import cn.com.leyizhuang.app.foundation.service.ProductCouponService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 产品券控制器
 * Created by panjie on 2018/1/10.
 */
@RestController
@RequestMapping(value = MaProductCouponRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaProductCouponRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/productCoupon";

    private final Logger logger = LoggerFactory.getLogger(MaProductCouponRestController.class);

    @Resource
    private ProductCouponService productCouponService;

    @Resource
    private ProductCouponSendService productCouponSendService;

    @GetMapping("/grid")
    public GridDataVO<ProductCoupon> gridData(Integer offset, Integer size, String keywords,String startTime,String endTime) {
        GridDataVO<ProductCoupon> gridDataVO = new GridDataVO<ProductCoupon>();
        Integer page = getPage(offset, size);
        PageInfo<ProductCoupon> pageInfo = productCouponService.queryPage(page, size, keywords,startTime,endTime);
        return gridDataVO.transform(pageInfo.getList(), pageInfo.getTotal());
    }

    @PostMapping("/save")
    public ResultDTO<?> save(@Valid ProductCoupon productCoupon, BindingResult result) throws IOException {
        if (!result.hasErrors()) {
            if (productCoupon == null) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "产品券数据不正确！", null);
            }
            Date effectiveStartTime = productCoupon.getEffectiveStartTime();
            Date effectiveEndTime = productCoupon.getEffectiveEndTime();
            int differDay = (int)((effectiveEndTime.getTime() - effectiveStartTime.getTime()) / (1000*3600*24));
            if (1 > differDay || differDay > 180) {
                return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE, "结束效日期应在开始日期后1天至6个月范围之内", null);
            }
            productCoupon.setCreateTime(new Date());
            productCoupon.setRemainingQuantity(productCoupon.getInitialQuantity());
            //新增的时候设置操作人是谁
            productCoupon.setOptUserid(this.getShiroUser().getId());
            productCouponService.addProductCoupon(productCoupon);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "新增产品券模版成功！", null);
        } else {
            return actFor400(result, "提交的数据有误");
        }
    }

    @PutMapping("/edit")
    public ResultDTO<?> edit(@Valid ProductCoupon productCoupon, BindingResult result) throws IOException {
        if (!result.hasErrors()) {
            if (productCoupon == null) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "产品券数据不正确！", null);
            }

            productCouponService.updateProductCoupon(productCoupon);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "编辑产品券模版成功！", null);
        } else {
            return actFor400(result, "提交的数据有误");
        }
    }

    @PostMapping(value = "/delete")
    public ResultDTO<?> deleteByIdList(String ids) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
        List<Long> idList = objectMapper.readValue(ids, javaType1);

        productCouponService.deletedProductCoupon(idList);

        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "删除成功", null);

    }

    @PostMapping(value = "/send")
    public ResultDTO<?> send(Long customerId, Long productCouponId, Long sellerId, Integer qty,Long optId) throws IOException {

        if (customerId == null || productCouponId == null || sellerId == null || qty == 0) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发送失败,参数有误", null);
        }

        optId=this.getShiroUser().getId();
        return productCouponSendService.send(customerId, productCouponId, sellerId, qty,optId);
    }

    @PostMapping(value = "/sendBatch")
    public ResultDTO<?> sendBatch(String customerIds, Long productCouponId, Long sellerId, Integer qty,Long optId) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
        List<Long> idList = objectMapper.readValue(customerIds, javaType1);

        if (idList == null || idList == null || sellerId == null || qty == 0) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发送失败", null);
        }

        return productCouponSendService.sendBatch(idList, productCouponId, sellerId, qty,optId);
    }
}
