package cn.com.leyizhuang.app.web.controller.views.productCoupon;

import cn.com.leyizhuang.app.foundation.pojo.ProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
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
 * 产品券前端控制器
 * Created by panjie on 2018/1/10.
 */
@Controller
@RequestMapping(value = MaProductCouponViewController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaProductCouponViewController {

    protected final static String PRE_URL = "/view/productCoupon";
    private final Logger logger = LoggerFactory.getLogger(MaProductCouponViewController.class);

    @Resource
    private ProductCouponService productCouponService;

    @Resource
    private GoodsService goodsService;

    @GetMapping("/page")
    public String page(HttpServletRequest request, ModelMap map){ return "/views/productCoupon/productCoupon_page"; }

    @GetMapping("/add/{id}")
    public String add(@PathVariable Long id, HttpServletRequest request, ModelMap map){ return "/views/productCoupon/productCoupon_add"; }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, HttpServletRequest request, ModelMap map){
        ProductCoupon productCoupon = productCouponService.queryProductCouponById(id);
        GoodsDO goodsDO = goodsService.findGoodsById(productCoupon.getGid());
        map.addAttribute("productCoupon",productCoupon);
        map.addAttribute("goodsInfo",goodsDO);
        return "/views/productCoupon/productCoupon_edit";
    }

    @GetMapping("/send/{id}")
    public String send(@PathVariable Long id,HttpServletRequest request,ModelMap map){
        ProductCoupon productCoupon = productCouponService.queryProductCouponById(id);
        GoodsDO goodsDO = goodsService.findGoodsById(productCoupon.getGid());
        map.addAttribute("productCoupon",productCoupon);

        return "/views/productCoupon/productCoupon_send";
    }
}
