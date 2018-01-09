package cn.com.leyizhuang.app.web.controller.views.city;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.service.MaCityDeliveryTimeService;
import cn.com.leyizhuang.app.foundation.vo.management.city.CityDeliveryTimeVO;
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

@Controller
@RequestMapping(value = MaCityDeliveryTimeViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCityDeliveryTimeViewsController extends BaseController {
    protected final static String PRE_URL = "/views/admin/citysDeliveryTimes";

    private final Logger logger = LoggerFactory.getLogger(MaCityViewsController.class);

    @Autowired
    private MaCityDeliveryTimeService maCityDeliveryTimeService;

    /**
     * 跳转城市列表
     * @return
     */
    @RequestMapping(value = "/list")
    public String getCitysDeliveryTime() {
        return "/views/city/cityDeliveryTime";
    }

    /**
     * 跳转该城市下的配送时间列表
     * @param cityId
     * @param cityName
     * @param model
     * @return
     */
   @RequestMapping(value = "/cityDeliveryTimeList/{cityId}")
    public String getCitysDeliveryTimeList(@PathVariable(value="cityId") Long cityId,@RequestParam(value="cityName") String cityName,Model model) {
       if (cityId.equals(0L)||StringUtils.isBlank(cityName)) {
           error404();
           return "/error/404";
       } else {
           model.addAttribute("cityId", cityId);
           model.addAttribute("cityName", cityName);
           return "/views/city/cityDeliveryTimeList";
       }
    }

    /**
     * 跳转新增配送时间列表
     * @param cityId
     * @param cityName
     * @param model
     * @return
     */
    @RequestMapping(value = "/add")
    public String addCitysDeliveryTime(@RequestParam(value="cityId") Long cityId,@RequestParam(value="cityName") String cityName,Model model) {
        if (cityId.equals(0L)|| StringUtils.isBlank(cityName)) {
                error404();
                return "/error/404";
            } else {
            model.addAttribute("cityId", cityId);
            model.addAttribute("cityName", cityName);
            return "/views/city/cityDeliveryTime_add";
        }
    }

    /**
     * 编辑城市配送时间列表
     * @param model
     * @param id
     * @return
     */
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
