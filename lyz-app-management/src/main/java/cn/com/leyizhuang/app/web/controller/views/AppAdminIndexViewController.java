package cn.com.leyizhuang.app.web.controller.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Richard
 *         Created on 2017/5/8.
 */
@Controller
@RequestMapping(value = AppAdminIndexViewController.PRE_URL, produces = "text/html;charset=utf-8")
public class AppAdminIndexViewController {

    protected final static String PRE_URL = "/views";

    @GetMapping
    public String index() {
        return "/views/index";
    }

}
