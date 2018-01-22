package cn.com.leyizhuang.app.web.controller.views.order;

import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author GenerationRoad
 * @date 2018/1/15
 */
@Controller
@RequestMapping(value = MaPhotoOrderViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaPhotoOrderViewsController extends BaseController {
    protected final static String PRE_URL = "/views/admin/order/photo";

    private final Logger logger = LoggerFactory.getLogger(MaPhotoOrderViewsController.class);

    @RequestMapping(value = "/list")
    public String getPhotoOrderList() {
        return "/views/order/photo_order_page";
    }

}
