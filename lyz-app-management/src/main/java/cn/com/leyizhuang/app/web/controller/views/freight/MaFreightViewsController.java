package cn.com.leyizhuang.app.web.controller.views.freight;



import cn.com.leyizhuang.app.core.constant.FreightChangeType;
import cn.com.leyizhuang.app.foundation.service.MaOrderFreightService;
import cn.com.leyizhuang.app.foundation.vo.management.freight.OrderFreightDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.freight.OrderFreightVO;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = MaFreightViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaFreightViewsController extends BaseController {

    protected final static String PRE_URL = "/views/admin/freight";

    private final Logger logger = LoggerFactory.getLogger(MaFreightViewsController.class);

    @Autowired
    private MaOrderFreightService maOrderFreightService;

    /**
     * 跳转运费列表页面
     * @return
     */
    @RequestMapping(value = "/list")
    public String orderFreightListPage() {
        return "/views/freight/freight_page";
    }


    /**
     * 跳转运费变更详情页面
     * @return
     */
    @RequestMapping(value = "/orderFreightChange")
    public String orderFreightChangeListPage() {
        return "/views/freight/orderFreightChange_page";
    }

    /**
     *编辑订单运费页面
     * @param map
     * @param id
     * @return
     */
    @GetMapping(value = "/edit/{id}")
    public String freightEditPage(ModelMap map, @PathVariable(value = "id") Long id) {
        OrderFreightVO orderFreightVO = this.maOrderFreightService.queryOrderFreightVOById(id);
        if (id.equals(0L)) {
            logger.warn("跳转修改资源页面失败，Resource(id = {}) == null", id);
            error404();
            return "/error/404";
        } else {
            List<FreightChangeType> freightChangeTypes = FreightChangeType.getFreightChangeTypes();
            map.addAttribute("orderFreightVO",orderFreightVO);
            map.addAttribute("freightChangeTypes",freightChangeTypes);
            return "/views/freight/freight_edit";
        }
   }

    /**
     *编辑订单运费详情页面
     * @param
     * @param id
     * @return
     */
    @GetMapping(value = "/orderFreightDetail/{id}")
    public String orderFreightDetailPage(ModelMap map,@PathVariable(value = "id") Long id) {
        OrderFreightDetailVO orderFreightDetailVO = this.maOrderFreightService.queryOrderFreightDetailVOById(id);
        if (id.equals(0L)) {
                logger.warn("跳转修改资源页面失败，Resource(id = {}) == null", id);
                error404();
                return "/error/404";
            } else {
            map.addAttribute("orderFreightDetailVO",orderFreightDetailVO);
            return "/views/freight/orderDetail_page";
            }
        }

}

