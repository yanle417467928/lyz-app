package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.WareHouseDO;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.service.WareHouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/6/8
 */
@RestController
@RequestMapping(value = MaWareHouseRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaWareHouseRestController extends BaseRestController{
    protected static final String PRE_URL = "/rest/wareHouse";

    private final Logger logger = LoggerFactory.getLogger(MaWareHouseRestController.class);

    @Autowired
    private WareHouseService wareHouseService;

    /**
     * @title   根据城市查询仓库列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/6/8
     */
    @GetMapping(value = "/findWareHouseByCityId")
    public List<WareHouseDO> findWareHouseByCityId(Long cityId) {
        logger.info("findWareHouseByCityId 后台查询该城市下的仓库列表(下拉框) ,入参 storeId:{}", cityId);
        try {
            List<WareHouseDO> wareHouseDOList = this.wareHouseService.findWareHouseByCityId(cityId);
            logger.info("findWareHouseByCityId ,后台查询该城市下的仓库列表(下拉框)成功", wareHouseDOList.size());
            return wareHouseDOList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findWareHouseByCityId EXCEPTION,发生未知错误，后台查询该城市下的仓库列表(下拉框)失败");
            logger.warn("{}", e);
            return null;
        }
    }
}
