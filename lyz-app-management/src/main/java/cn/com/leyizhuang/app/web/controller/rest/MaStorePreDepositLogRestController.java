package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.AdminUserStoreService;
import cn.com.leyizhuang.app.foundation.service.MaStorePreDepositLogService;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositLogVO;
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

/**
 * @author GenerationRoad
 * @date 2018/1/12
 */
@RestController
@RequestMapping(value = MaStorePreDepositLogRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaStorePreDepositLogRestController extends BaseRestController {

    protected static final String PRE_URL = "/rest/store/preDeposit/log";

    private final Logger logger = LoggerFactory.getLogger(MaStorePreDepositLogRestController.class);

    @Autowired
    private MaStorePreDepositLogService maStorePreDepositLogService;

    @Autowired
    private AdminUserStoreService adminUserStoreService;

    /**
     * @title   门店预存款变更明细列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/11
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<StorePreDepositLogVO> restStorePreDepositPageGird(Integer offset, Integer size, Long storeId, String keywords, Long cityId, String storeType) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
        PageInfo<StorePreDepositLogVO> storePreDepositLogVOPageInfo = this.maStorePreDepositLogService.findAllStorePredepositLog(page, size, storeId, cityId, storeType, keywords, storeIds);
        return new GridDataVO<StorePreDepositLogVO>().transform(storePreDepositLogVOPageInfo.getList(), storePreDepositLogVOPageInfo.getTotal());
    }

    /**
     * @title   查询门店预存款变更明细
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/11
     */
    @GetMapping(value = "/{id}")
    public ResultDTO<StorePreDepositLogVO> restStorePreDepositLogGet(@PathVariable(value = "id") Long id) {
        StorePreDepositLogVO storePreDepositLogVO = this.maStorePreDepositLogService.findStorePredepositLogById(id);
        if (null == storePreDepositLogVO) {
            logger.warn("查找门店预存款变更明细失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, storePreDepositLogVO);
        }
    }
}
