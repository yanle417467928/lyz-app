package cn.com.leyizhuang.app.web.controller.views.message;

import cn.com.leyizhuang.app.foundation.dao.MessageBaseDao;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActStoreDO;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import cn.com.leyizhuang.app.foundation.pojo.message.MessageListDO;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.RankClassification;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 王浩 on 2018/7/20.
 * 消息列表管视图控制器
 */
@Controller
@RequestMapping(value = MessageViewController.PRE_URL, produces = "application/json;charset=utf-8")
public class MessageViewController extends BaseController {
    protected final static String PRE_URL = "/view/message";
    private final Logger logger = LoggerFactory.getLogger(MessageViewController.class);

    @Resource
    private MessageBaseService messageBaseService;
    @Resource
    private CityService cityService;

    @Autowired
    private MaStoreService maStoreService;

    @Autowired
    private AdminUserStoreService adminUserStoreService;

    @Autowired
    private MaCustomerService maCustomerService;


    @GetMapping(value = "/list")
    public String messagePage() {
        return "/views/message/message_page";
    }

    @GetMapping("/add/{id}")
    public String messageAdd(@PathVariable Long id, ModelMap map) {
            return "/views/message/message_add";
    }


    @GetMapping("/edit/{id}")
    public String messageEdit(@PathVariable Long id, ModelMap map) {

        if (id == null){
            return "/error/404.ftl";
        }

        messageBaseService.getModelMapByActBaseId(map,id);
        List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
        // 城市下门店
        MessageListDO messageListDO = (MessageListDO) map.get("messageListDO");
        Long cityId = messageListDO.getCityId();
        List<SimpleStoreParam> stores = messageBaseService.findStore(id);
        List<AppCustomer> customerList = messageBaseService.findCustomer(id);


        map.addAttribute("stores",stores);
        map.addAttribute("customerList",customerList);
        // 获取专供类型
        List<RankClassification> rankClassificationList = maCustomerService.findRankAll();
        map.addAttribute("rankScopeList",rankClassificationList);
        return "/views/message/message_edit";
    }
}
