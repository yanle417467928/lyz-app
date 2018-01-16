package cn.com.leyizhuang.app.web.controller.views.customer;

import cn.com.leyizhuang.app.foundation.service.MaCustomerService;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerLebiVO;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author GenerationRoad
 * @date 2018/1/13
 */
@Controller
@RequestMapping(value = MaCustomerLebiViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCustomerLebiViewsController extends BaseController {
    protected final static String PRE_URL = "/views/admin/customer/lebi";

    private final Logger logger = LoggerFactory.getLogger(MaCustomerLebiViewsController.class);

    @Autowired
    private MaCustomerService maCustomerService;

    /**
     * @title   跳转顾客乐币列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/15
     */
    @RequestMapping(value = "/list")
    public String getCustomerList() {
        return "/views/customer/customer_lebi_page";
    }

    /**
     * @title   跳转发放乐币页面
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/11
     */
    @GetMapping(value = "/edit/{cusId}")
    public String resourceEdit(ModelMap map, @PathVariable(value = "cusId") Long cusId) {
        CustomerLebiVO customerLebiVO = this.maCustomerService.queryCusLebiByCusId(cusId);
        if (null == customerLebiVO) {
            logger.warn("跳转发放乐币页面失败，customerLebiVO(cusId = {}) == null", cusId);
            error404();
            return "/error/404";
        } else {
            map.addAttribute("customerLebiVO", customerLebiVO);
        }
        return "/views/customer/customer_lebi_edit";
    }

}
