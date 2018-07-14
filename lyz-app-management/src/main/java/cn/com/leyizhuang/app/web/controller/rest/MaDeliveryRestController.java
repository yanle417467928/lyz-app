package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.deliveryFeeRule.DeliveryFeeRule;
import cn.com.leyizhuang.app.foundation.pojo.deliveryFeeRule.DeliveryFeeRuleSpecailGoods;
import cn.com.leyizhuang.app.foundation.service.CityService;
import cn.com.leyizhuang.app.foundation.service.DeliveryFeeRuleService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 运费规则
 * Created by panjie on 2018/1/13.
 */
@RestController
@RequestMapping(value = MaDeliveryRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaDeliveryRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/deliveryFeeRule";

    private final Logger logger = LoggerFactory.getLogger(MaDeliveryRestController.class);

    @Resource
    private DeliveryFeeRuleService deliveryFeeRuleService;

    @Resource
    private CityService cityService;

    @GetMapping("/grid")
    public GridDataVO<DeliveryFeeRule> gridData(Integer offset, Integer size, String keywords) {
        GridDataVO<DeliveryFeeRule> gridDataVO = new GridDataVO<>();
        Integer page = getPage(offset, size);

        PageInfo<DeliveryFeeRule> pageInfo = deliveryFeeRuleService.queryPageDate(page, size);

        return gridDataVO.transform(pageInfo.getList(), pageInfo.getTotal());
    }

    @PostMapping("/save")
    public ResultDTO<?> save(@Valid DeliveryFeeRule rule, String goodsDetails, BindingResult result) throws IOException {
        if (!result.hasErrors()) {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, DeliveryFeeRuleSpecailGoods.class);
            List<DeliveryFeeRuleSpecailGoods> goodsList = objectMapper.readValue(goodsDetails, javaType1);

            // 先检查该城市下是否已经存在运费规则
            List<DeliveryFeeRule> deliveryFeeRuleList = deliveryFeeRuleService.findRuleByCityIdAndCountyName(rule.getCityId(), rule.getCountyName());

            if (deliveryFeeRuleList != null && deliveryFeeRuleList.size() > 0){
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该城市下已存在运费规则", null);
            }

            if (rule.getIncludeSpecialGoods() == true){
                if (goodsList == null || goodsList.size() == 0) {
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "特殊商品数据为空", null);
                }
            }

            List<City> cityList = cityService.findAll();
            for (City city : cityList){
                if (city.getCityId().equals(rule.getCityId())){
                    rule.setCityName(city.getName());
                    break;
                }
            }
            rule.setCreateDate(new Date());

            /* 持久化数据 */
            deliveryFeeRuleService.addRule(rule,goodsList);

            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "新增运费规则成功！", null);
        } else {
            return actFor400(result, "提交的数据有误");
        }
    }

    @PostMapping("/edit")
    public ResultDTO<?> edit(@Valid DeliveryFeeRule rule, String goodsDetails, BindingResult result) throws Exception{
        if (!result.hasErrors()) {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, DeliveryFeeRuleSpecailGoods.class);
            List<DeliveryFeeRuleSpecailGoods> goodsList = objectMapper.readValue(goodsDetails, javaType1);

            if (rule.getIncludeSpecialGoods() == true){
                if (goodsList == null || goodsList.size() == 0) {
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "特殊商品数据为空", null);
                }
            }

            /* 持久化数据 */
            deliveryFeeRuleService.updateRule(rule,goodsList);

            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "编辑运费规则成功！", null);
        } else {
            return actFor400(result, "提交的数据有误");
        }
    }

    @PostMapping("/delete")
    public ResultDTO<?> delete(String ids) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
        List<Long> idList = objectMapper.readValue(ids, javaType1);

        if (idList == null){
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "数据有误！", null);
        }

        deliveryFeeRuleService.deleteRule(idList);
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "删除运费规则成功！", null);
    }
}
