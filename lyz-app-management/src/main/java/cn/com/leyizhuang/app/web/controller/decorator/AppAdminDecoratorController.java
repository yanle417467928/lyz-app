package cn.com.leyizhuang.app.web.controller.decorator;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author CrazyApeDX
 * Created on 2017/5/6.
 */
@Controller
@RequestMapping(value = AppAdminDecoratorController.PRE_URL, produces = "test/html;charset=utf-8")
public class AppAdminDecoratorController {

    protected final static String PRE_URL = "/sitemash";

    @GetMapping(value = "/template")
    public String decoratorTemplate() {
        return "/sitemash/template";
    }
}
