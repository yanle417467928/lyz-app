package cn.com.leyizhuang.app.web.controller.views.customer;


import cn.com.leyizhuang.app.foundation.pojo.management.customer.CustomerDO;
import cn.com.leyizhuang.app.foundation.pojo.response.ManageUpdateCustomerTypeResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.RankClassification;
import cn.com.leyizhuang.app.foundation.service.MaCustomerService;
import cn.com.leyizhuang.app.web.controller.BaseController;
import cn.com.leyizhuang.app.web.controller.rest.MaCustomerRestController;
import org.apache.catalina.LifecycleState;
import org.bouncycastle.math.raw.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;


@Controller
@RequestMapping(value = MaCustomerViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCustomerViewsController extends BaseController {

    protected final static String PRE_URL = "/views/admin/customers";

    private final Logger logger = LoggerFactory.getLogger(MaCustomerViewsController.class);

    @Resource
    private MaCustomerService maCustomerService;

    /**
     * 跳转顾客列表
     * @return
     */
    @RequestMapping(value = "/list")
    public String customerListPage() {
        return "/views/customer/customer_page";
    }

    /**
     * 跳转顾客新增页面
     * @return
     */
    @RequestMapping(value = "/add")
    public String addPage() {
        logger.info("新增顾客");
        return "/views/customer/customer_add";
    }

    /**
     * 跳转顾客编辑页面
     * @return
     */
    @RequestMapping(value = "/edit/{id}")
    public String editPage(ModelMap map,@PathVariable(value = "id") Long id) {
        logger.info("编辑顾客");
        CustomerDO customerDO = maCustomerService.queryCustomerVOById(id);
        map.addAttribute("customer",customerDO);
        return "/views/customer/customer_edit";
    }

    /**
     * 跳转修改顾客类型页面
     * @return
     */
    @RequestMapping(value = "/update/{id}")
    public String updatePage(ModelMap map,@PathVariable(value = "id") Long id) {
        logger.info("修改顾客类型");
        ManageUpdateCustomerTypeResponse customerDO = maCustomerService.queryCustomerById(id);
        List<RankClassification> rankClassificationList = maCustomerService.findRankAll();
        map.addAttribute("customer",customerDO);
        map.addAttribute("rankClassificationList",rankClassificationList);
        return "/views/customer/customer_update_member_type";
    }

}
