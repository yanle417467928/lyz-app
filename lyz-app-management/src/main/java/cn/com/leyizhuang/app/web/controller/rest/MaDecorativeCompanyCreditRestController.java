package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.decorativeCompany.DecorativeCompanyCredit;
import cn.com.leyizhuang.app.foundation.pojo.decorativeCompany.DecorativeCompanyInfo;
import cn.com.leyizhuang.app.foundation.pojo.decorativeCompany.DecorativeCompanySubvention;
import cn.com.leyizhuang.app.foundation.service.MaDecorativeCompanyCreditService;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = MaDecorativeCompanyCreditRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaDecorativeCompanyCreditRestController  extends BaseRestController {
    protected static final String PRE_URL = "/rest/decorativeCredit";

    private final Logger logger = LoggerFactory.getLogger(MaDecorativeCompanyCreditRestController.class);

    @Autowired
    private MaStoreService maStoreService;
    @Autowired
    private MaDecorativeCompanyCreditService maDecorativeCompanyCreditService;

    /**
     *装饰公司信用金列表
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<DecorativeCompanyInfo> restDecorativeCompanyCreditPageGird(Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<DecorativeCompanyInfo> decorativeCompanyVOPage = this.maStoreService.queryDecorativeCreditPageVO(page, size);
        List<DecorativeCompanyInfo> decorativeCompanyVOList = decorativeCompanyVOPage.getList();
        return new GridDataVO<DecorativeCompanyInfo>().transform(decorativeCompanyVOList, decorativeCompanyVOPage.getTotal());
    }

    /**
     * 查询装饰公司详细信息
     * @param decorativeCompanyId
     * @return
     */
    @GetMapping(value = "/{decorativeId}")
    public ResultDTO<DecorativeCompanyInfo> restDecorativeCompanyCreditGet(@PathVariable(value = "decorativeId") Long decorativeCompanyId) {
        DecorativeCompanyInfo decorativeCompanyVO = this.maStoreService.queryDecorativeCompanyCreditById(decorativeCompanyId);
        if (null == decorativeCompanyVO) {
            logger.warn("查找装饰公司信用失败：Role(id = {}) == null", decorativeCompanyId);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, decorativeCompanyVO);
        }
    }

    /**
     * 根据名称 编码查询装饰公司
     * @param queryDecorativeCreditInfo
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/infoGrid/{queryDecorativeCreditInfo}")
    public GridDataVO<DecorativeCompanyInfo> findDecorativeCompanyByNameOrCode(@PathVariable(value = "queryDecorativeCreditInfo") String queryDecorativeCreditInfo, Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<DecorativeCompanyInfo> decorativeCompanyVOPage = this.maStoreService.findDecorativeCreditByInfo(page, size,queryDecorativeCreditInfo);
        List<DecorativeCompanyInfo> decorativeCompanyVOList = decorativeCompanyVOPage.getList();
        return new GridDataVO<DecorativeCompanyInfo>().transform(decorativeCompanyVOList, decorativeCompanyVOPage.getTotal());
    }

    /**
     * 根据下拉框筛选装饰公司
     * @param enabled
     * @param cityId
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/findDecorativeCreditByCondition")
    public GridDataVO<DecorativeCompanyInfo> findDecorativeCompanyCreditByCondition(@RequestParam("enabled") String enabled, @RequestParam("cityId") Long cityId, Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<DecorativeCompanyInfo> decorativeCompanyVOPage = this.maStoreService.findDecorativeCreditByCondition(page, size, enabled, cityId);
        List<DecorativeCompanyInfo> decorativeCompanyVOList = decorativeCompanyVOPage.getList();
        return new GridDataVO<DecorativeCompanyInfo>().transform(decorativeCompanyVOList, decorativeCompanyVOPage.getTotal());
    }

    /**
     * 标记装饰公司信用金
     * @param decorativeCompanyCredit
     * @param decorativeCompanySubvention
     * @param result
     * @return
     */
    @PutMapping
    public ResultDTO<String> updateDecorativeCompanyCredit(@Valid DecorativeCompanyCredit decorativeCompanyCredit,  @Valid DecorativeCompanySubvention decorativeCompanySubvention, BindingResult result) {
        if (!result.hasErrors()) {
            this.maDecorativeCompanyCreditService.updateDecorativeCompanyCredit(decorativeCompanyCredit);
            this.maDecorativeCompanyCreditService.updateDecorativeCompanySubvention(decorativeCompanySubvention);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            System.err.print(allErrors);
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }



}


