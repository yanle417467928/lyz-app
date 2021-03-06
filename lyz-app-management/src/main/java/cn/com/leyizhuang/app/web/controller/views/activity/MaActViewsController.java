package cn.com.leyizhuang.app.web.controller.views.activity;

import cn.com.leyizhuang.app.foundation.pojo.activity.ActBaseDO;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActStoreDO;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.RankClassification;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 促销页面控制器
 * Created by panjie on 2017/12/15.
 */
@Controller
@RequestMapping(value = MaActViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaActViewsController extends BaseController{
    protected final static String PRE_URL = "/view/activity";
    private final Logger logger = LoggerFactory.getLogger(MaActViewsController.class);

    @Resource
    private AppActService appActService;

    @Resource
    private CityService cityService;

    @Autowired
    private MaStoreService maStoreService;

    @Autowired
    private AdminUserStoreService adminUserStoreService;

    @Autowired
    private MaCustomerService maCustomerService;


    @GetMapping("/page")
    public String acticityPage(HttpServletRequest request, ModelMap map){ return "/views/activity/activity_page";}

    @GetMapping("/add/{id}")
    public String activityAdd(@PathVariable Long id, ModelMap map){
        if(id == 0){
            // 获取专供类型
            List<RankClassification> rankClassificationList = maCustomerService.findRankAll();
            map.addAttribute("rankScopeList",rankClassificationList);

            return "/views/activity/activity_add";
        }else{
            appActService.getModelMapByActBaseId(map,id);
            List<City> cityList = cityService.findAll();
            map.addAttribute("cityList",cityList);

            List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
            // 城市下门店
            ActBaseDO actBaseDO = (ActBaseDO) map.get("actBaseDO");
            Long cityId = actBaseDO.getCityId();
            List<SimpleStoreParam> stores = maStoreService.findStoresListByCityId(cityId,storeIds);
            List<ActStoreDO> actStoreDOList = (List<ActStoreDO>) map.get("actStoresDO");

            for (SimpleStoreParam VO : stores) {
                for ( ActStoreDO DO : actStoreDOList) {
                    if(VO.getStoreId() == DO.getStoreId()){
                        VO.setIsSelected(true);
                        break;
                    }else{
                        VO.setIsSelected(false);
                    }
                }

            }
            map.addAttribute("stores",stores);

            // 获取专供类型
            List<RankClassification> rankClassificationList = maCustomerService.findRankAll();
            map.addAttribute("rankScopeList",rankClassificationList);


            return "/views/activity/activity_copy";
        }
    }

    @GetMapping("/edit/{id}")
    public String activityEdit(@PathVariable Long id, ModelMap map){

        if (id == null){
            return "/error/404.ftl";
        }

        appActService.getModelMapByActBaseId(map,id);
        List<City> cityList = cityService.findAll();
        map.addAttribute("cityList",cityList);
        List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
        // 城市下门店
        ActBaseDO actBaseDO = (ActBaseDO) map.get("actBaseDO");
        Long cityId = actBaseDO.getCityId();
        List<SimpleStoreParam> stores = maStoreService.findStoresListByCityId(cityId,storeIds);
        List<ActStoreDO> actStoreDOList = (List<ActStoreDO>) map.get("actStoresDO");
        List<AppCustomer> customerList = appActService.findCustomer(id);

        for (SimpleStoreParam VO : stores) {
            for ( ActStoreDO DO : actStoreDOList) {
                if(VO.getStoreId().equals(DO.getStoreId())){
                    VO.setIsSelected(true);
                    break;
                }else{
                    VO.setIsSelected(false);
                }
            }

        }
        map.addAttribute("stores",stores);
        map.addAttribute("customerList",customerList);
        // 获取专供类型
        List<RankClassification> rankClassificationList = maCustomerService.findRankAll();
        map.addAttribute("rankScopeList",rankClassificationList);
        return "/views/activity/activity_edit";
    }

}
