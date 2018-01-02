package cn.com.leyizhuang.app.web.controller.views.cashCoupon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 优惠券控制器
 * Created by panjie on 2018/1/2.
 */
@Controller
@RequestMapping(value = ManagementCashCouponViewController.PRE_URL, produces = "application/json;charset=utf-8")
public class ManagementCashCouponViewController {

    protected final static String PRE_URL = "/view/cashCoupon";
    private final Logger logger = LoggerFactory.getLogger(ManagementCashCouponViewController.class);

    @GetMapping("/page")
    public String page(HttpServletRequest request, ModelMap map){ return "/views/cashCoupon/cash_coupon_page";}


}
