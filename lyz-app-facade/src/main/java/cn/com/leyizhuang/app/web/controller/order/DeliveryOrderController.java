package cn.com.leyizhuang.app.web.controller.order;

import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * 配送单相关接口
 * @author Jerry.Ren
 * Date: 2017/11/8.
 * Time: 10:36.
 */
@RestController
@RequestMapping(value = "/app/deliveryOrder")
public class DeliveryOrderController {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryOrderController.class);


    @PostMapping(value = "/create", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> photoOrder(Long userId, Integer identityType){


        return null;
    }
}
