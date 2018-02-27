package cn.com.leyizhuang.app.web.controller.rest;


import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.management.city.SimpleCityParam;
import cn.com.leyizhuang.app.foundation.service.MaCityService;
import cn.com.leyizhuang.app.foundation.vo.management.city.CityDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.city.CityVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = MaCityRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCityRestController extends BaseRestController {
    protected final static String PRE_URL = "/rest/citys";

    private final Logger logger = LoggerFactory.getLogger(MaCityRestController.class);

    @Autowired
    private MaCityService maCityService;

    /**
     * 城市信息分页查询
     *
     * @param
     * @return
     * @throws
     * @descripe
     * @author
     * @date 2017/11/3
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<CityVO> restCitysPageGird(Integer offset, Integer size, String keywords) {
        logger.info("restCitysPageGird,城市信息分页查询, 入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<CityVO> cityPage = this.maCityService.queryPageVO(page, size);
            List<CityVO> citysList = cityPage.getList();
            logger.info("restCitysPageGird ,城市信息分页查询成功", citysList.size());
            return new GridDataVO<CityVO>().transform(citysList, cityPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restCitysPageGird EXCEPTION,发生未知错误，城市信息分页查询失败");
            logger.warn("{}", e);
            return null;
        }

    }

    /**
     * @title   获取生效的城市列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/2/26
     */
    @GetMapping(value = "/list")
    public GridDataVO<CityVO> restCitysList(Integer offset, Integer size, String keywords) {
        logger.info("restCitysList 获取生效的城市列表 ,入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<CityVO> citys = this.maCityService.queryPageVOByEnableIsTrue(page, size, keywords);
            return new GridDataVO<CityVO>().transform(citys.getList(), citys.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restCitysList EXCEPTION,发生未知错误，获取生效的城市列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 开通配送服务城市分页信息
     *
     * @param
     * @return
     * @throws
     * @descripe
     * @author
     * @date 2017/11/3
     */
    @GetMapping(value = "/page/deliveryTimeGrid")
    public GridDataVO<CityVO> restCitysDeliveryTimePageGird(Integer offset, Integer size, String keywords) {
        logger.info("restCitysDeliveryTimePageGird, 开通配送服务城市分页查询, 入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<CityVO> cityPage = this.maCityService.queryDeliveryTimePageVO(page, size);
            List<CityVO> citysList = cityPage.getList();
            logger.info("restCitysDeliveryTimePageGird , 开通配送服务城市分页查询成功", citysList.size());
            return new GridDataVO<CityVO>().transform(citysList, cityPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restCitysDeliveryTimePageGird EXCEPTION,发生未知错误， 开通配送服务城市分页查询失败");
            logger.warn("{}", e);
            return null;
        }

    }
    /**
     * @param cityId
     * @return
     * @throws
     * @title 根据ID查询城市信息
     * @descripe
     * @author
     * @date 2017/11/3
     */
    @GetMapping(value = "/{cityId}")
    public ResultDTO<CityDetailVO> findityDetailVOById(@PathVariable(value = "cityId") Long cityId) {
        logger.info("findityDetailVOById,查询城市详细信息, 入参 cityId:{}", cityId);
        try {
            CityDetailVO cityVO = this.maCityService.queryCityVOById(cityId);
            if (null == cityVO) {
                logger.warn("查找城市失败：Role(id = {}) == null", cityId);
                return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                        "指定数据不存在，请联系管理员", null);
            } else {
                logger.info("findityDetailVOById ,查询城市详细信息成功", cityVO);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, cityVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findityDetailVOById EXCEPTION,发生未知错误，,查询城市详细信息失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * @param
     * @return
     * @throws
     * @title 查询城市列表(下拉框)
     * @descripe
     * @author
     * @date 2017/11/3
     */
    @GetMapping(value = "/findCitylist")

    public List<SimpleCityParam> findCitysList() {
        logger.info("findCitysList,查询城市列表(下拉框)");
        try {
            List<SimpleCityParam> citysList = this.maCityService.findCitysList();
            logger.info("findCitysList ,查询城市列表(下拉框)成功", citysList.size());
            return citysList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findCitysList EXCEPTION,发生未知错误，,查询城市列表(下拉框)失败");
            logger.warn("{}", e);
            return null;
        }
    }


}
