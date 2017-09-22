package cn.com.leyizhuang.app.web.controller;

import cn.com.leyizhuang.app.foundation.pojo.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.ProcessDO;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.app.foundation.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caiyu on 2017/9/20.
 */
@RestController
@RequestMapping(value = "/process")
public class ProcessController {
    @Autowired
    private ProcessService processService;
    @Autowired
    private GoodsService goodsServiceImpl;

    @RequestMapping(value = "/save")
    public String save() {

        return "添加工序包成功";
    }

    @RequestMapping(value = "/update")
    public String update() {
        ProcessDO processDO = processService.selectByProcessName("拆墙");
        if (processDO == null || "".equals(processDO)) {
            return "未查询到改工序包";
        }
        processDO.setProcessName("铲墙");
        processService.update(processDO);
        return "修改工序包信息成功";
    }

    @RequestMapping(value = "/delete")
    public String delete() {
        processService.delete(1L);
        return "删除工序包成功";
    }

    @RequestMapping(value = "/select")
    public String select() {

            return "根据ID查找成功";
        }

    @RequestMapping(value = "/selectList")
    public String selectList() {
        List<ProcessDO> processDOList = processService.queryList();
        for (ProcessDO processDO : processDOList) {
            System.out.println(processDO.getId());
        }
        return "查询所有工序包完成";
    }

    @RequestMapping(value = "/test")
    public String test(){
        List<ProcessDO> processDOList = processService.findAllProcessAndGoods();
        if (processDOList != null && processDOList.size()>0) {
            for (ProcessDO processDO : processDOList) {
                System.out.println(processDO);
            }
        }
        return "测试完成";
    }
}
