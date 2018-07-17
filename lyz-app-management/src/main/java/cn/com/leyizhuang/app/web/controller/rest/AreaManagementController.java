package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.AreaManagementDO;
import cn.com.leyizhuang.app.foundation.service.AreaManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/7/14
 */
@RestController
@RequestMapping(value = AreaManagementController.PRE_URL, produces = "application/json;charset=utf-8")
public class AreaManagementController {

    protected final static String PRE_URL = "/rest/areaManagement";

    private final Logger logger = LoggerFactory.getLogger(AreaManagementController.class);

    @Autowired
    private AreaManagementService areaManagementService;

    /**
     * @title   根据城市ID获取配送地址区县列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/7/14
     */
    @GetMapping(value = "/findAreaListByCityId")
    public List<AreaManagementDO> findAreaListByCityId(Long cityId) {
        logger.info("findAreaListByCityId 根据城市ID获取配送地址区县列表 入参 cityId:{}", cityId);
        try {
            //查询登录用户门店权限的门店ID
            List<AreaManagementDO> areaList = this.areaManagementService.findAreaManagementByCityId(cityId);
            logger.info("findAreaListByCityId ,根据城市ID获取配送地址区县列表成功", areaList);
            return areaList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findAreaListByCityId EXCEPTION,发生未知错误，根据城市ID获取配送地址区县列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

}
