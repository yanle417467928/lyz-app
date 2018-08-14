package cn.com.leyizhuang.app.web.controller.views.Interface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(value = MaInterfaceResendViewController.PRE_URL, produces = "text/html;charset=utf-8")
public class MaInterfaceResendViewController {

    protected final static String PRE_URL = "/views/interface";
    private final Logger logger = LoggerFactory.getLogger(MaInterfaceResendViewController.class);

    @GetMapping(value = "/ebs/page")
    public String ebsPage(HttpServletRequest request, ModelMap map) {
        return "/views/interfaceResend/ebs_resend_page";
    }

    @GetMapping(value = "/wms/page")
    public String wmsPage(HttpServletRequest request, ModelMap map) {
        return "/views/interfaceResend/wms_resend_page";
    }
}
