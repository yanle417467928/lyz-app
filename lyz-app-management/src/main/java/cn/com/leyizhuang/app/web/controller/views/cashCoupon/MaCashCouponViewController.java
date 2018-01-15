package cn.com.leyizhuang.app.web.controller.views.cashCoupon;

import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import cn.com.leyizhuang.app.foundation.service.CashCouponService;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 优惠券控制器
 * Created by panjie on 2018/1/2.
 */
@Controller
@RequestMapping(value = MaCashCouponViewController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCashCouponViewController {

    protected final static String PRE_URL = "/view/cashCoupon";
    private final Logger logger = LoggerFactory.getLogger(MaCashCouponViewController.class);

    @Resource
    private CashCouponService cashCouponService;

    @Autowired
    private MaStoreService maStoreService;

    @GetMapping("/page")
    public String page(HttpServletRequest request, ModelMap map){ return "/views/cashCoupon/cashCoupon_page"; }

    @GetMapping("/add/{id}")
    public String add(@PathVariable Long id,HttpServletRequest request, ModelMap map){ return "/views/cashCoupon/cashCoupon_add"; }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,HttpServletRequest request, ModelMap map){

        CashCoupon cashCoupon = cashCouponService.queryById(id);
        Long ccid = cashCoupon.getId();
        List<CashCouponStore> cashCouponStoreList = cashCouponService.queryStoreByCcid(ccid);
        List<CashCouponCompany> cashCouponCompanyList = cashCouponService.queryCompanyByCcid(ccid);
        List<CashCouponBrand> cashCouponBrandList = cashCouponService.queryBrandByCcid(ccid);
        List<CashCouponGoods> cashCouponGoodsList = cashCouponService.queryGoodsByCcid(ccid);

        // 城市下门店
        Long cityId = cashCoupon.getCityId();
        List<SimpleStoreParam> stores = maStoreService.findStoresListByCityId(cityId);

        for (SimpleStoreParam VO : stores) {
            if(cashCouponStoreList == null || cashCouponStoreList.size() == 0){
                VO.setIsSelected(false);
            }else{
                for ( CashCouponStore DO : cashCouponStoreList) {
                    if(VO.getStoreId() == DO.getStoreId()){
                        VO.setIsSelected(true);
                        break;
                    }else{
                        VO.setIsSelected(false);
                    }
                }
            }
        }

        map.addAttribute("cashCoupon",cashCoupon);
        map.addAttribute("stores",stores);
        map.addAttribute("companyList",cashCouponCompanyList);
        map.addAttribute("brandList",cashCouponBrandList);
        map.addAttribute("goodsList",cashCouponGoodsList);

        return "/views/cashCoupon/cashCoupon_edit";
    }

    /**
     * 发券
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/send/{id}")
    public String send(@PathVariable Long id, ModelMap map){
        CashCoupon cashCoupon = cashCouponService.queryById(id);

        map.addAttribute("cashCoupon",cashCoupon);
        return "/views/cashCoupon/cashCoupon_send";
    }

}
