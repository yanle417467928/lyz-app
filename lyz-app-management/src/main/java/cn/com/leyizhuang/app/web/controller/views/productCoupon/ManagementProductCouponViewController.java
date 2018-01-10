package cn.com.leyizhuang.app.web.controller.views.productCoupon;

import cn.com.leyizhuang.app.foundation.service.ProductCouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by panjie on 2018/1/10.
 */
@Controller
@RequestMapping(value = ManagementProductCouponViewController.PRE_URL, produces = "application/json;charset=utf-8")
public class ManagementProductCouponViewController {

    protected final static String PRE_URL = "/view/productCoupon";
    private final Logger logger = LoggerFactory.getLogger(ManagementProductCouponViewController.class);

    @Resource
    private ProductCouponService productCouponService;

    @GetMapping("/page")
    public String page(HttpServletRequest request, ModelMap map){ return "/views/productCoupon/productCoupon_page"; }

    @GetMapping("/add/{id}")
    public String add(@PathVariable Long id, HttpServletRequest request, ModelMap map){ return "/views/productCoupon/productCoupon_add"; }
}
