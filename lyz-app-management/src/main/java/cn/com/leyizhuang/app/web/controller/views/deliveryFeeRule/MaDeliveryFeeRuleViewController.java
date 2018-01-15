package cn.com.leyizhuang.app.web.controller.views.deliveryFeeRule;

import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.deliveryFeeRule.DeliveryFeeRule;
import cn.com.leyizhuang.app.foundation.pojo.deliveryFeeRule.DeliveryFeeRuleSpecailGoods;
import cn.com.leyizhuang.app.foundation.service.CityService;
import cn.com.leyizhuang.app.foundation.service.DeliveryFeeRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 运费规则前端控制器
 * Created by panjie on 2018/1/13.
 */
@Controller
@RequestMapping(value = MaDeliveryFeeRuleViewController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaDeliveryFeeRuleViewController {

    protected final static String PRE_URL = "/view/deliveryFeeRule";
    private final Logger logger = LoggerFactory.getLogger(MaDeliveryFeeRuleViewController.class);

    @Resource
    private DeliveryFeeRuleService deliveryFeeRuleService;

    @Resource
    private CityService cityService;

    @GetMapping("/page")
    public String page(HttpServletRequest request, ModelMap map){ return "/views/deliveryFeeRule/deliveryFeeRule_page"; }

    @GetMapping("/add/{id}")
    public String add(@PathVariable Long id, HttpServletRequest request, ModelMap map){ return "/views/deliveryFeeRule/deliveryFeeRule_add"; }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,HttpServletRequest request, ModelMap map){
        DeliveryFeeRule  deliveryFeeRule = deliveryFeeRuleService.findRuleById(id);
        List<DeliveryFeeRuleSpecailGoods> specailGoodsList = deliveryFeeRuleService.findSpecailGoodsByRuleId(deliveryFeeRule.getId());
        List<City> cityList = cityService.findAll();
        map.addAttribute("cityList",cityList);
        map.addAttribute("rule",deliveryFeeRule);
        map.addAttribute("goods",specailGoodsList);
        return "/views/deliveryFeeRule/deliveryFeeRule_edit";
    }

}
