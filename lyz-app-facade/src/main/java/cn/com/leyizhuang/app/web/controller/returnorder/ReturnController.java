package cn.com.leyizhuang.app.web.controller.returnorder;

import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Jerry.Ren
 * Notes: 退货单接口
 * Created with IntelliJ IDEA.
 * Date: 2017/12/4.
 * Time: 9:34.
 */

@RestController
@RequestMapping("/app/return")
public class ReturnController {

    private static final Logger logger = LoggerFactory.getLogger(ReturnController.class);

    @Resource
    private AppOrderService appOrderService;


}
