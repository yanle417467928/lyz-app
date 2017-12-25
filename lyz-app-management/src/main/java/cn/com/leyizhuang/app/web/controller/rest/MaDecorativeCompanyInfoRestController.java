package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.management.store.StoreDO;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorativeCompanyDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorativeCompanyVO;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.SimpleDecorativeCompany;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = MaDecorativeCompanyInfoRestController .PRE_URL, produces = "application/json;charset=utf-8")
public class MaDecorativeCompanyInfoRestController extends BaseRestController {

    protected static final String PRE_URL = "/rest/decorativeInfo";

    private final Logger logger = LoggerFactory.getLogger(MaDecorativeCompanyInfoRestController.class);

    @Autowired
    private MaStoreService maStoreService;

    /**
     * 装饰公司列表
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<DecorativeCompanyVO> restDecorativeCompanyPageGird(Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<StoreDO> storePage = this.maStoreService.queryDecorativeCompanyPageVO(page, size);
        List<StoreDO> storesList = storePage.getList();
        List<DecorativeCompanyVO> pageAllDecorativeCompanyList = DecorativeCompanyVO.transform(storesList);
        return new GridDataVO<DecorativeCompanyVO>().transform(pageAllDecorativeCompanyList, storePage.getTotal());
    }

    /**
     *根据下拉框筛选装饰公司
     * @param enabled
     * @param cityId
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/findDecorativeByCondition")
    public GridDataVO<DecorativeCompanyVO> findDecorativeCompanyByCondition(@RequestParam("enabled") String enabled, @RequestParam("cityId") Long cityId, Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<StoreDO> storePage = this.maStoreService.findDecorativeByCondition(page, size, enabled, cityId);
        List<StoreDO> storesList = storePage.getList();
        List<DecorativeCompanyVO> pageDecorativeCompanyList = DecorativeCompanyVO.transform(storesList);
        return new GridDataVO<DecorativeCompanyVO>().transform(pageDecorativeCompanyList, storePage.getTotal());
    }

    /**
     *根据名称编码查询装饰公司
     * @param queryDecorativeInfo
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/infoGrid/{queryDecorativeInfo}")
    public GridDataVO<DecorativeCompanyVO> findDecorativeCompanyByNameOrCode(@PathVariable(value = "queryDecorativeInfo") String queryDecorativeInfo, Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<StoreDO> storePage = this.maStoreService.findDecorativeByInfo(page, size,queryDecorativeInfo);
        List<StoreDO> storesList = storePage.getList();
        List<DecorativeCompanyVO> pageAllDecorativeCompanyList = DecorativeCompanyVO.transform(storesList);
        return new GridDataVO<DecorativeCompanyVO>().transform(pageAllDecorativeCompanyList, storePage.getTotal());
    }

    /**
     *查询装饰公司详情信息
     * @param decorativeCompanyId
     * @return
     */
    @GetMapping(value = "/{decorativeCompanyId}")
    public ResultDTO<DecorativeCompanyDetailVO> restDecorativeCompanyGet(@PathVariable(value = "decorativeCompanyId") Long decorativeCompanyId) {
        DecorativeCompanyDetailVO decorativeCompanyVO = this.maStoreService.queryDecorativeCompanyById(decorativeCompanyId);
        if (null == decorativeCompanyVO) {
            logger.warn("查找装饰公司失败：Role(id = {}) == null", decorativeCompanyId);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, decorativeCompanyVO);
        }
    }

    /**
     *查询装饰公司列表(下拉框)
     * @return
     */
    @GetMapping(value = "/findDecorativeCompany")
    public List<SimpleDecorativeCompany> findDecorativeCompanyListByCityId() {
        List<SimpleDecorativeCompany> decorativeCompanyVOList = this.maStoreService.findDecorativeCompanyVOList();
        return decorativeCompanyVOList;
    }
}
