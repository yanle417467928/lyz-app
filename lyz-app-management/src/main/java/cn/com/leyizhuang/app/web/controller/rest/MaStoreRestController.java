package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = MaStoreRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaStoreRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/stores";

    private final Logger logger = LoggerFactory.getLogger(MaStoreRestController.class);

    @Autowired
    private MaStoreService maStoreService;

    /**
     * 初始门店页面
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<StoreVO> restStoresPageGird(Integer offset, Integer size, String keywords) {
        logger.info("restStoresPageGird 后台初始门店页面列表 ,入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<StoreVO> storePage = this.maStoreService.queryPageVO(page, size);
            List<StoreVO> pageAllStoresList = storePage.getList();
            logger.info("restStoresPageGird ,后台初始门店页面列表成功", pageAllStoresList.size());
            return new GridDataVO<StoreVO>().transform(pageAllStoresList, storePage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restStoresPageGird EXCEPTION,发生未知错误，后台初始门店页面列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 查询所有门店列表(下拉框)(包括装饰公司)
     *
     * @return
     */
    @GetMapping(value = "/findAllStorelist")
    public List<SimpleStoreParam> findAllStorelist() {
        logger.info("findAllStorelist 后台查询所有门店列表(下拉框)(包括装饰公司)");
        try {
            List<SimpleStoreParam> allStoresList = this.maStoreService.findAllStorelist();
            logger.info("findAllStorelist ,后台查询所有门店列表(下拉框)(包括装饰公司)成功", allStoresList.size());
            return allStoresList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findAllStorelist EXCEPTION,发生未知错误，后台查询所有门店列表(下拉框)(包括装饰公司)失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 查询门店列表(下拉框)
     *
     * @return
     */
    @GetMapping(value = "/findStorelist")
    public List<SimpleStoreParam> findStoresList() {
        logger.info("findStoresList 后台查询门店列表(下拉框)");
        try {
            List<SimpleStoreParam> storesList = this.maStoreService.findStoreList();
            logger.info("findStoresList ,后台查询门店列表(下拉框)成功", storesList.size());
            return storesList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findStoresList EXCEPTION,发生未知错误，后台查询门店列表(下拉框)失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 查询该城市ID的门店列表包括装饰公司(下拉框)
     *
     * @param cityId
     * @return
     */
    @GetMapping(value = "/findAllStoresListByCityId/{cityId}")
    public List<SimpleStoreParam> findAllStoresListByCityId(@PathVariable(value = "cityId") Long cityId) {
        logger.info("findAllStoresListByCityId 后台查询该城市ID的门店列表包括装饰公司(下拉框) 入参 cityId:{}", cityId);
        try {
            List<SimpleStoreParam> storesList = this.maStoreService.findAllStoresListByCityId(cityId);
            logger.info("findAllStoresListByCityId ,后台查询该城市ID的门店列表包括装饰公司(下拉框)成功", storesList.size());
            return storesList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findAllStoresListByCityId EXCEPTION,发生未知错误，后台查询该城市ID的门店列表包括装饰公司(下拉框)失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 查询该城市ID的门店列表(下拉框)
     *
     * @param cityId
     * @return
     */
    @GetMapping(value = "/findStoresListByCityId/{cityId}")
    public List<SimpleStoreParam> findStoresListByCityId(@PathVariable(value = "cityId") Long cityId) {
        logger.info("findStoresListByCityId 后台查询该城市ID的门店列表(下拉框) 入参 cityId:{}", cityId);
        try {
            List<SimpleStoreParam> storesList = this.maStoreService.findStoresListByCityId(cityId);
            logger.info("findStoresListByCityId ,后台查询该城市ID的门店列表(下拉框)成功", storesList.size());
            return storesList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findStoresListByCityId EXCEPTION,发生未知错误，后台查询该城市ID的门店列表(下拉框)失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 查询装饰公司门店列表(下拉框)
     *
     * @return 门店列表
     */
    @GetMapping(value = "/find/decorativeCompany")
    public List<StoreVO> findDecorativeCompanyList() {
        logger.info("findDecorativeCompanyList 后台查询装饰公司门店列表(下拉框)");
        try {
            List<StoreVO> allDecorativeCompanyList = this.maStoreService.findDecorativeCompanyList();
            logger.info("findDecorativeCompanyList ,后台查询装饰公司门店列表(下拉框)成功", allDecorativeCompanyList.size());
            return allDecorativeCompanyList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findDecorativeCompanyList EXCEPTION,发生未知错误，后台查询装饰公司门店列表(下拉框)失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 根据城市id获取支持门店自提的门店列表（下拉框）
     *
     * @param cityId 城市id
     * @return 门店列表
     */
    @GetMapping(value = "/find/city/selfDelivery/stores/{cityId}")
    public List<StoreVO> findSelfDeliveryStoresListByCityId(@PathVariable(value = "cityId") Long cityId) {
        logger.info("findSelfDeliveryStoresListByCityId 根据城市id获取支持门店自提的门店列表（下拉框） 入参 cityId:{}", cityId);
        try {
            List<StoreVO> selfDeliveryStoreList = this.maStoreService.findSelfDeliveryStoresListByCityId(cityId);
            logger.info("findSelfDeliveryStoresListByCityId ,根据城市id获取支持门店自提的门店列表（下拉框）成功", selfDeliveryStoreList.size());
            return selfDeliveryStoreList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findSelfDeliveryStoresListByCityId EXCEPTION,发生未知错误，根据城市id获取支持门店自提的门店列表（下拉框）失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 获取支持门店自提的门店列表（下拉框）
     *
     * @return 门店列表
     */
    @GetMapping(value = "/find/selfDelivery/stores")
    public List<StoreVO> findSelfDeliveryStoresList() {
        logger.info("findSelfDeliveryStoresList 获取支持门店自提的门店列表（下拉框）");
        try {
            List<StoreVO> selfDeliveryStores = this.maStoreService.findSelfDeliveryStoresList();
            logger.info("findSelfDeliveryStoresList ,获取支持门店自提的门店列表（下拉框）成功", selfDeliveryStores.size());
            return selfDeliveryStores;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findSelfDeliveryStoresList EXCEPTION,发生未知错误，获取支持门店自提的门店列表（下拉框）失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 查询该城市ID的装饰公司门店列表(下拉框)
     *
     * @param cityId 城市id
     * @return 门店列表
     */
    @GetMapping(value = "/find/company/StoresListByCityId/{cityId}")
    public List<StoreVO> findCompanyStoresListByCityId(@PathVariable(value = "cityId") Long cityId) {
        logger.info("findCompanyStoresListByCityId 查询该城市ID的装饰公司门店列表(下拉框) 入参 cityId:{}", cityId);
        try {
            List<StoreVO> storesList = this.maStoreService.findCompanyStoresListByCityId(cityId);
            logger.info("findCompanyStoresListByCityId , 查询该城市ID的装饰公司门店列表(下拉框)成功", storesList.size());
            return storesList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findCompanyStoresListByCityId EXCEPTION,发生未知错误，查询该城市ID的装饰公司门店列表(下拉框)失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 查询门店信息
     *
     * @param storeId
     * @return
     */
    @GetMapping(value = "/{storeId}")
    public ResultDTO<StoreDetailVO> restStoreIdGet(@PathVariable(value = "storeId") Long storeId) {
        logger.info("restStoreIdGet 后台查询门店信息 入参 storeId:{}", storeId);
        try {
            StoreDetailVO storeVO = this.maStoreService.queryStoreVOById(storeId);
            if (null == storeVO) {
                logger.warn("查找门店失败：Role(id = {}) == null", storeId);
                return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                        "指定数据不存在，请联系管理员", null);
            } else {
                logger.info("restStoreIdGet , 后台查询门店信息成功");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, storeVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restStoreIdGet EXCEPTION,发生未知错误，后台查询门店信息失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 查询该城市下的门店
     *
     * @param cityId
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/findStoresListByCity/{cityId}")
    public GridDataVO<StoreVO> findStoresListByCity(@PathVariable(value = "cityId") Long cityId, Integer offset, Integer size, String keywords) {
        logger.info("findStoresListByCity 后台查询该城市下的门店 ,入参 offset:{},size:{},keywords:{},cityId:{}", offset, size, keywords, cityId);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<StoreVO> storePage = this.maStoreService.queryStoreListByCityId(page, size, cityId);
            List<StoreVO> pageAllStoresList = storePage.getList();
            logger.info("findStoresListByCity , 后台查询该城市下的门店成功", pageAllStoresList.size());
            return new GridDataVO<StoreVO>().transform(pageAllStoresList, storePage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findStoresListByCity EXCEPTION,发生未知错误，后台查询该城市下的门店失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 查询可用或不可用的门店
     *
     * @param enabled
     * @param cityId
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/findStoresListByCondition")
    public GridDataVO<StoreVO> findStoresListByCondition(@RequestParam("enabled") String enabled, @RequestParam("cityId") Long cityId, Integer offset, Integer size, String keywords) {
        logger.info("findStoresListByCondition 后台查询可用或不可用的门店 ,入参 offset:{},size:{},keywords:{},cityId:{},enabled:{}", offset, size, keywords, cityId, enabled);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<StoreVO> storePage = this.maStoreService.findStoresListByCondition(page, size, enabled, cityId);
            List<StoreVO> pageAllStoresList = storePage.getList();
            logger.info("findStoresListByCondition , 后台查询可用或不可用的门店成功", pageAllStoresList.size());
            return new GridDataVO<StoreVO>().transform(pageAllStoresList, storePage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findStoresListByCondition EXCEPTION,发生未知错误，后台查询可用或不可用的门店失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 通过门店名称或者门店编码查询门店
     *
     * @param queryStoreInfo
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/storeGrid/{queryStoreInfo}")
    public GridDataVO<StoreVO> findStoresListByStoreInfo(@PathVariable(value = "queryStoreInfo") String queryStoreInfo, Integer offset, Integer size, String keywords) {
        logger.info("findStoresListByStoreInfo 后台通过门店名称或者门店编码查询门店 ,入参 offset:{},size:{},keywords:{},queryStoreInfo:{}", offset, size, keywords, queryStoreInfo);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<StoreVO> storePage = this.maStoreService.findStoresListByStoreInfo(page, size, queryStoreInfo);
            List<StoreVO> pageAllStoresList = storePage.getList();
            logger.info("findStoresListByStoreInfo , 后台通过门店名称或者门店编码查询门店成功", pageAllStoresList.size());
            return new GridDataVO<StoreVO>().transform(pageAllStoresList, storePage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findStoresListByStoreInfo EXCEPTION,发生未知错误，后台通过门店名称或者门店编码查询门店失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 更新门店信息
     *
     * @param storeId
     * @param isSelfDelivery
     * @param
     * @return
     */
    @PutMapping(value = "/{storeId}")
    public ResultDTO<?> updateStoreById(@PathVariable(value = "storeId") Long storeId, @RequestParam(value = "isSelfDelivery") Boolean isSelfDelivery) {
        logger.info("updateStoreById 后台更新门店信息 ,入参 storeId:{},isSelfDelivery:{}", storeId, isSelfDelivery);
        try {
            if (null != storeId && null != isSelfDelivery) {
                maStoreService.update(storeId, isSelfDelivery);
                logger.info("updateStoreById , 后台更新门店信息成功");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } else {
                logger.warn("提交参数有误");
                return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                        "提交参数有误,请检查数据", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("updateStoreById EXCEPTION,发生未知错误，后台更新门店信息失败");
            logger.warn("{}", e);
            return null;
        }
    }
}
