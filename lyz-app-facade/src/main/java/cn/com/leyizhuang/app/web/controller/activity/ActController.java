package cn.com.leyizhuang.app.web.controller.activity;

import cn.com.leyizhuang.app.foundation.pojo.activity.ActBaseDO;
import cn.com.leyizhuang.app.foundation.service.AppActService;
import cn.com.leyizhuang.app.web.controller.order.OrderController;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 促销控制器
 * Created by panjie on 2017/12/1.
 */
@RestController
@RequestMapping(value = "/app/act")
public class ActController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private AppActService actService;

    @RequestMapping("/query/list")
    public Map<String,Object> queryActBaseDOList() {
        Map<String,Object> res = new HashMap<>();

        List<ActBaseDO> list = actService.queryList();

        return res;
    }
}
