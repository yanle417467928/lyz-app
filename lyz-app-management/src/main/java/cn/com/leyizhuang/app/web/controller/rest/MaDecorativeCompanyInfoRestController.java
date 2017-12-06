package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.store.StoreDO;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.vo.DecorativeCompanyVO;
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

    @GetMapping(value = "/page/grid")
    public GridDataVO<DecorativeCompanyVO> restDecorativeCompanyPagPageGird(Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<StoreDO> storePage = this.maStoreService.queryDecorativeCompanyPageVO(page, size);
        List<StoreDO> storesList = storePage.getList();
        List<DecorativeCompanyVO> pageAllDecorativeCompanyList = DecorativeCompanyVO.transform(storesList);
        return new GridDataVO<DecorativeCompanyVO>().transform(pageAllDecorativeCompanyList, storePage.getTotal());
    }

    @GetMapping(value = "/findDecorativeByCondition")
    public GridDataVO<DecorativeCompanyVO> findDecorativeByCondition(@RequestParam("enabled") String enabled, @RequestParam("cityId") Long cityId, Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<StoreDO> storePage = this.maStoreService.findDecorativeByCondition(page, size, enabled, cityId);
        List<StoreDO> storesList = storePage.getList();
        List<DecorativeCompanyVO> pageDecorativeCompanyList = DecorativeCompanyVO.transform(storesList);
        return new GridDataVO<DecorativeCompanyVO>().transform(pageDecorativeCompanyList, storePage.getTotal());
    }

    @GetMapping(value = "/page/infoGrid/{queryDecorativeInfo}")
    public GridDataVO<DecorativeCompanyVO> findDecorativeByNameOrCode(@PathVariable(value = "queryDecorativeInfo") String queryDecorativeInfo, Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<StoreDO> storePage = this.maStoreService.findDecorativeByInfo(page, size,queryDecorativeInfo);
        List<StoreDO> storesList = storePage.getList();
        List<DecorativeCompanyVO> pageAllDecorativeCompanyList = DecorativeCompanyVO.transform(storesList);
        return new GridDataVO<DecorativeCompanyVO>().transform(pageAllDecorativeCompanyList, storePage.getTotal());
    }
}
