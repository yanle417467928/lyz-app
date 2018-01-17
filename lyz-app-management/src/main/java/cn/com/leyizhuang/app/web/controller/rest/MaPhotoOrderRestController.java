package cn.com.leyizhuang.app.web.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author GenerationRoad
 * @date 2018/1/15
 */
@Controller
@RequestMapping(value = MaPhotoOrderRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaPhotoOrderRestController extends BaseRestController {

    protected final static String PRE_URL = "/views/admin/order/photo";

    private final Logger logger = LoggerFactory.getLogger(MaPhotoOrderRestController.class);
}
