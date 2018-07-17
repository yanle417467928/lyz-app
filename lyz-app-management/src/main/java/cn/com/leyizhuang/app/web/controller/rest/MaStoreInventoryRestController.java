package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.dao.ItyAllocationDAO;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.*;
import cn.com.leyizhuang.app.foundation.pojo.management.store.StoreReturnAndRequireGoodsInf;
import cn.com.leyizhuang.app.foundation.service.AdminUserStoreService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.ItyAllocationService;
import cn.com.leyizhuang.app.foundation.service.MaStoreInventoryService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = MaStoreInventoryRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaStoreInventoryRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest";

    private final Logger logger = LoggerFactory.getLogger(MaStoreInventoryRestController.class);

    @Autowired
    private MaStoreInventoryService maStoreInventoryService;

    @Autowired
    private AdminUserStoreService adminUserStoreService;

    @Resource
    private AppStoreService storeService;

    @Resource
    private ItyAllocationService ityAllocationService;


    /**
     * 初始门店要货页面
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/storeInventory/goodRequire/page/grid")
    public GridDataVO<StoreReturnAndRequireGoodsInf> restStoresGoodRequirePageGird(Integer offset, Integer size, String keywords, String structureCode, Long storeId, String queryInfo) {
        logger.info("restStoresPageGird 后台初始门店要货列表 ,入参 offset:{},size:{},keywords:{},structureCode:{},storeId:{},queryInfo:{}", offset, size, keywords, structureCode, storeId, queryInfo);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
            PageInfo<StoreReturnAndRequireGoodsInf> storePage = this.maStoreInventoryService.queryStoresGoodRequirePageVO(page, size, structureCode, storeId, queryInfo, storeIds);
            List<StoreReturnAndRequireGoodsInf> pageAllStoresList = storePage.getList();
            logger.info("restStoresPageGird ,后台初始门店要货列表成功", pageAllStoresList.size());
            return new GridDataVO<StoreReturnAndRequireGoodsInf>().transform(pageAllStoresList, storePage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restStoresPageGird EXCEPTION,发生未知错误，后台初始门店要货列表失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 初始门店要货页面
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/storeInventory/goodReturn/page/grid")
    public GridDataVO<StoreReturnAndRequireGoodsInf> restStoresGoodReturnPageGird(Integer offset, Integer size, String keywords, String structureCode, Long storeId, String queryInfo) {
        logger.info("restStoresPageGird 后台初始门店退货列表 ,入参 offset:{},size:{},keywords:{},structureCode:{},storeCode:{},queryInfo:{}", offset, size, keywords, structureCode, storeId, queryInfo);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
            PageInfo<StoreReturnAndRequireGoodsInf> storePage = this.maStoreInventoryService.queryStoresGoodReturnPageVO(page, size, structureCode, storeId, queryInfo, storeIds);
            List<StoreReturnAndRequireGoodsInf> pageAllStoresList = storePage.getList();
            logger.info("restStoresPageGird ,后台初始门店退货列表成功", pageAllStoresList.size());
            return new GridDataVO<StoreReturnAndRequireGoodsInf>().transform(pageAllStoresList, storePage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restStoresPageGird EXCEPTION,发生未知错误，后台初始门店退货列表失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 门店调拨查询
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/allocation/queryPage/grid")
    public GridDataVO<AllocationVO> dataAllocationVOPageGridGet(Integer offset, Integer size, String keywords, String outCompany, String inCompany, Long outStore, Long inStore) {
        logger.info("dataAllocationVOPageGridGet CREATE,门店库存调拨分页查询, 入参 offset:{},size:{},keywords:{},outCompany:{},inCompany:{},outStore:{},inStore:{}", offset, size, keywords, outCompany, inCompany, outStore, inStore);
        List<Long> storeIds = adminUserStoreService.findStoreIdList();
        Integer page = getPage(offset, size);
        PageInfo<AllocationVO> allocationVOPageInfo = ityAllocationService.queryAllocationPage(page, size, keywords, outCompany, inCompany, outStore, inStore, storeIds);
        return new GridDataVO<AllocationVO>().transform(allocationVOPageInfo.getList(), allocationVOPageInfo.getTotal());
    }
}
