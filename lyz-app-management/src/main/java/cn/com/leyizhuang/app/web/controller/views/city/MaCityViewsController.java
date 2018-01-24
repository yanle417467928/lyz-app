package cn.com.leyizhuang.app.web.controller.views.city;


import cn.com.leyizhuang.app.foundation.service.CityService;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = MaCityViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCityViewsController extends BaseController {

    protected final static String PRE_URL = "/views/admin/citys";

    private final Logger logger = LoggerFactory.getLogger(MaCityViewsController.class);

    @Autowired
    private CityService cityService;

    /**
     * 城市列表页面
     * @return
     */
    @RequestMapping(value = "/list")
    public String citysListPage() {
        return "/views/city/city_page";
    }

    @RequestMapping(value = "/available/inventory")
    public String citysAvaliableItyPage() {
        return "/views/city/city_available_inventory";
    }
}

