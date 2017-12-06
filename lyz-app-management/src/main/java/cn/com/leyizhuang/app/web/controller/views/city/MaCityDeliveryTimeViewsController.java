package cn.com.leyizhuang.app.web.controller.views.city;

import cn.com.leyizhuang.app.foundation.pojo.city.CityDeliveryTime;
import cn.com.leyizhuang.app.foundation.service.MaCityDeliveryTimeService;
import cn.com.leyizhuang.app.foundation.vo.CityDeliveryTimeVO;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = MaCityDeliveryTimeViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCityDeliveryTimeViewsController extends BaseController {
    protected final static String PRE_URL = "/views/admin/citysDeliveryTimes";

    private final Logger logger = LoggerFactory.getLogger(MaCityViewsController.class);

    @Autowired
    private MaCityDeliveryTimeService maCityDeliveryTimeService;

    @RequestMapping(value = "/list")
    public String getCitysDeliveryTime() {
        return "/views/city/cityDeliveryTime";
    }

   @RequestMapping(value = "/cityDeliveryTimeList/{cityId}")
    public String getCitysDeliveryTimeList(@PathVariable(value="cityId") Long cityId,Model model) {
        model.addAttribute("cityId",cityId);
        return "/views/city/cityDeliveryTimeList";
    }


    @RequestMapping(value = "/add")
    public String addCitysDeliveryTime(@RequestParam(value="cityId") Long cityId,Model model) {
        model.addAttribute("cityId",cityId);
        return "/views/city/cityDeliveryTime_add";
    }

    @GetMapping(value = "/edit/{id}")
    public String cityDeliveryTimeEdit(Model model, @PathVariable(value = "id") Long id) {
        if (!id.equals(0L)) {
            CityDeliveryTimeVO  cityDeliveryTimeVO = maCityDeliveryTimeService.queryById(id);
            if (null == cityDeliveryTimeVO) {
                logger.warn("跳转修改资源页面失败，Resource(id = {}) == null", id);
                error404();
                return "/error/404";
            } else {
                model.addAttribute("cityDeliveryTimeVO",cityDeliveryTimeVO);
            }
        }
        return "/views/city/cityDeliveryTime_edit";
    }
}
