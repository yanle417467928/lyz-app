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
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<StoreVO> storePage = this.maStoreService.queryPageVO(page, size);
        List<StoreVO> pageAllStoresList = storePage.getList();
        return new GridDataVO<StoreVO>().transform(pageAllStoresList, storePage.getTotal());
    }

    /**
     * 查询所有门店列表包括装饰公司(下拉框)
     *
     * @return
     */
    @GetMapping(value = "/findAllStorelist")
    public List<SimpleStoreParam> findAllStorelist() {
        List<SimpleStoreParam> allStoresList = this.maStoreService.findAllStorelist();
        return allStoresList;
    }

    /**
     * 查询门店列表(下拉框)
     *
     * @return
     */
    @GetMapping(value = "/findStorelist")
    public List<SimpleStoreParam> findStoresList() {
        List<SimpleStoreParam> allStoresList = this.maStoreService.findStoreList();
        return allStoresList;
    }

    /**
     * 查询该城市ID的门店列表包括装饰公司(下拉框)
     *
     * @param cityId
     * @return
     */
    @GetMapping(value = "/findAllStoresListByCityId/{cityId}")
    public List<SimpleStoreParam> findAllStoresListByCityId(@PathVariable(value = "cityId") Long cityId) {
        List<SimpleStoreParam> storesList = this.maStoreService.findAllStoresListByCityId(cityId);
        return storesList;
    }


    /**
     * 查询该城市ID的门店列表(下拉框)
     *
     * @param cityId
     * @return
     */
    @GetMapping(value = "/findStoresListByCityId/{cityId}")
    public List<SimpleStoreParam> findStoresListByCityId(@PathVariable(value = "cityId") Long cityId) {
        List<SimpleStoreParam> storesList = this.maStoreService.findStoresListByCityId(cityId);
        return storesList;
    }

    /**
     * 查询装饰公司门店列表(下拉框)
     *
     * @return  门店列表
     */
    @GetMapping(value = "/find/company/stores")
    public List<StoreVO> findCompanyStoresList() {
        List<StoreVO> allStoresList = this.maStoreService.findCompanyStoresList();
        return allStoresList;
    }

    /**
     * 根据城市id获取支持门店自提的门店列表（下拉框）
     * @param cityId    城市id
     * @return  门店列表
     */
    @GetMapping(value = "/find/city/selfDelivery/stores/{cityId}")
    public List<StoreVO> findSelfDeliveryStoresListByCityId(@PathVariable(value = "cityId") Long cityId){
        List<StoreVO> selfDeliveryStoreList = this.maStoreService.findSelfDeliveryStoresListByCityId(cityId);
        return selfDeliveryStoreList;
    }

    /**
     * 获取支持门店自提的门店列表（下拉框）
     * @return  门店列表
     */
    @GetMapping(value = "/find/selfDelivery/stores")
    public List<StoreVO> findSelfDeliveryStoresList(){
        List<StoreVO> selfDeliveryStores = this.maStoreService.findSelfDeliveryStoresList();
        return selfDeliveryStores;
    }

    /**
     * 查询该城市ID的装饰公司门店列表(下拉框)
     *
     * @param cityId    城市id
     * @return  门店列表
     */
    @GetMapping(value = "/find/company/StoresListByCityId/{cityId}")
    public List<StoreVO> findCompanyStoresListByCityId(@PathVariable(value = "cityId") Long cityId) {
        List<StoreVO> storesList = this.maStoreService.findCompanyStoresListByCityId(cityId);
        return storesList;
    }

    /**
     * 查询门店信息
     *
     * @param storeId
     * @return
     */
    @GetMapping(value = "/{storeId}")
    public ResultDTO<StoreDetailVO> restStoreIdGet(@PathVariable(value = "storeId") Long storeId) {
        StoreDetailVO storeVO = this.maStoreService.queryStoreVOById(storeId);
        if (null == storeVO) {
            logger.warn("查找门店失败：Role(id = {}) == null", storeId);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, storeVO);
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
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<StoreVO> storePage = this.maStoreService.queryStoreListByCityId(page, size, cityId);
        List<StoreVO> pageAllStoresList = storePage.getList();
        return new GridDataVO<StoreVO>().transform(pageAllStoresList, storePage.getTotal());
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
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<StoreVO> storePage = this.maStoreService.findStoresListByCondition(page, size, enabled, cityId);
        List<StoreVO> pageAllStoresList = storePage.getList();
        return new GridDataVO<StoreVO>().transform(pageAllStoresList, storePage.getTotal());
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
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<StoreVO> storePage = this.maStoreService.findStoresListByStoreInfo(page, size, queryStoreInfo);
        List<StoreVO> pageAllStoresList = storePage.getList();
        return new GridDataVO<StoreVO>().transform(pageAllStoresList, storePage.getTotal());
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
        if (null != storeId && null != isSelfDelivery) {
            maStoreService.update(storeId, isSelfDelivery);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            logger.warn("提交参数有误");
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "提交参数有误,请检查数据", null);
        }
    }
}
