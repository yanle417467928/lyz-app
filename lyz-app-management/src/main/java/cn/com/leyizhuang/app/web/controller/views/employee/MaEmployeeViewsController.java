package cn.com.leyizhuang.app.web.controller.views.employee;


import cn.com.leyizhuang.app.foundation.service.CityService;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = MaEmployeeViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaEmployeeViewsController extends BaseController {

    protected final static String PRE_URL = "/views/admin/employees";

    private final Logger logger = LoggerFactory.getLogger(MaEmployeeViewsController.class);

    /**
     * 跳转员工列表
     * @return
     */
    @RequestMapping(value = "/list")
    public String employeesListPage() {
        return "/views/employee/employee_page";
    }

}

