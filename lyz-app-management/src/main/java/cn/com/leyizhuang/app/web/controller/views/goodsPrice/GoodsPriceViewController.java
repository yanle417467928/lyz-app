package cn.com.leyizhuang.app.web.controller.views.goodsPrice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author GenerationRoad
 * @date 2017/10/27
 */
@Controller
@RequestMapping(value = GoodsPriceViewController.PRE_URL, produces = "application/json;charset=utf-8")
public class GoodsPriceViewController {

    protected final static String PRE_URL = "/view/goodsPrice";
    private final Logger logger = LoggerFactory.getLogger(GoodsPriceViewController.class);


    @GetMapping(value = "/page")
    public String goodsPricePage(HttpServletRequest request, ModelMap map) {
        return "/views/goodsPrice/goodsPrice_page";
    }
}
