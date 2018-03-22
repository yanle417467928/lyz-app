package cn.com.leyizhuang.app.web.controller.activity;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActBaseDO;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.pojo.response.PromotionListViewResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppActService;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.web.controller.order.OrderController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

    @Autowired
    private AppCustomerService appCustomerService;

    @Autowired
    private AppEmployeeService appEmployeeService;

    @RequestMapping("/query/list")
    public Map<String,Object> queryActBaseDOList() {
        Map<String,Object> res = new HashMap<>();

        List<ActBaseDO> list = actService.queryList();
        res.put("actList",list);
        return res;
    }

    @RequestMapping("/insert")
    public void insertBatch(){
        actService.insertBatch();
    }

    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getValidActivityList(Long userId , Integer identityType){
        Long storeId = null;

        if (userId == null || identityType == null){
            new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "获取促销失败，数据有误！", "");
        }

        if (identityType.equals(0)){
            AppEmployee employ = appEmployeeService.findById(userId);

            storeId = employ == null ? null : employ.getStoreId();
        }else if(identityType.equals(6)){
            try {
                AppCustomer customer = appCustomerService.findById(userId);

                storeId = customer == null ? null : customer.getStoreId();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                logger.info(e.getMessage());
                logger.info("getValidActivityList 促销列表报错 导购信息错误");
            }

        }else {
            new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "获取促销失败，数据有误！", "");
        }

        if (storeId == null){
            new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "获取促销失败，数据有误！", "");
        }

        List<PromotionListViewResponse> promotionListViewResponseList = new ArrayList<>();
        try {
            // 查询出该门店下发布 并且未过期的促销
            promotionListViewResponseList = actService.queryValidRepListByStoreId(userId, AppIdentityType.getAppIdentityTypeByValue(identityType) ,storeId);

        }catch (Exception e){
            logger.info(e.getMessage());
            new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "获取促销失败，发生异常！", "");
        }

        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "", promotionListViewResponseList);
    }
}
