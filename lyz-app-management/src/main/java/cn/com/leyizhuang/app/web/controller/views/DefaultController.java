package cn.com.leyizhuang.app.web.controller.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author CrazyApeDX
 * Created on 2017/5/6.
 */
@Controller
@RequestMapping(value = "/")
public class DefaultController {

    @RequestMapping
    public String defaultRedirect() {
        return "redirect:/views";
    }
}
