package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.StoreCreditMoneyChangeType;
import cn.com.leyizhuang.app.core.utils.IpUtil;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.StoreCreditMoneyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyCredit;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanySubvention;
import cn.com.leyizhuang.app.foundation.service.MaDecorativeCompanyCreditService;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = MaDecorativeCompanyCreditRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaDecorativeCompanyCreditRestController extends BaseRestController {
    protected static final String PRE_URL = "/rest/decorativeCredit";

    private final Logger logger = LoggerFactory.getLogger(MaDecorativeCompanyCreditRestController.class);

    @Autowired
    private MaStoreService maStoreService;
    @Autowired
    private MaDecorativeCompanyCreditService maDecorativeCompanyCreditService;

    /**
     * 装饰公司信用金列表
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<DecorativeCompanyInfo> restDecorativeCompanyCreditPageGird(Integer offset, Integer size, String keywords) {
        logger.info("restDecorativeCompanyCreditPageGird 后台初始化装饰公司信用金列表 ,入参 offset:{}, size:{}, kewords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<DecorativeCompanyInfo> decorativeCompanyVOPage = this.maStoreService.queryDecorativeCreditPageVO(page, size);
            List<DecorativeCompanyInfo> decorativeCompanyVOList = decorativeCompanyVOPage.getList();
            logger.info("restDecorativeCompanyCreditPageGird ,后台初始化装饰公司信用金列表成功", decorativeCompanyVOList.size());
            return new GridDataVO<DecorativeCompanyInfo>().transform(decorativeCompanyVOList, decorativeCompanyVOPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restDecorativeCompanyCreditPageGird EXCEPTION,发生未知错误，后台初始化装饰公司信用金列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 查询装饰公司详细信息
     *
     * @param decorativeCompanyId
     * @return
     */
    @GetMapping(value = "/{decorativeId}")
    public ResultDTO<DecorativeCompanyInfo> restDecorativeCompanyCreditGet(@PathVariable(value = "decorativeId") Long decorativeCompanyId) {
        logger.info("restDecorativeCompanyCreditGet 查询装饰公司详细信息 ,入参 decorativeCompanyId:{}", decorativeCompanyId);
        try {
            DecorativeCompanyInfo decorativeCompanyVO = this.maStoreService.queryDecorativeCompanyCreditById(decorativeCompanyId);
            if (null == decorativeCompanyVO) {
                logger.warn("查询装饰公司详细信息失败：Role(id = {}) == null", decorativeCompanyId);
                return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                        "指定数据不存在，请联系管理员", null);
            } else {
                logger.info("restDecorativeCompanyCreditGet ,查询装饰公司详细信息成功", decorativeCompanyVO);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, decorativeCompanyVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restDecorativeCompanyCreditGet EXCEPTION,发生未知错误，查询装饰公司详细信息失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 根据名称 编码查询装饰公司
     *
     * @param queryDecorativeCreditInfo
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/infoGrid/{queryDecorativeCreditInfo}")
    public GridDataVO<DecorativeCompanyInfo> findDecorativeCompanyByNameOrCode(@PathVariable(value = "queryDecorativeCreditInfo") String queryDecorativeCreditInfo, Integer offset, Integer size, String keywords) {
        logger.info("findDecorativeCompanyByNameOrCode 根据名称,编码查询装饰公司 ,入参 offset:{}, size:{}, kewords:{},queryDecorativeCreditInfo:{}", offset, size, keywords, queryDecorativeCreditInfo);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<DecorativeCompanyInfo> decorativeCompanyVOPage = this.maStoreService.findDecorativeCreditByInfo(page, size, queryDecorativeCreditInfo);
            List<DecorativeCompanyInfo> decorativeCompanyVOList = decorativeCompanyVOPage.getList();
            logger.info("findDecorativeCompanyByNameOrCode ,根据名称,编码查询装饰公司成功", decorativeCompanyVOList.size());
            return new GridDataVO<DecorativeCompanyInfo>().transform(decorativeCompanyVOList, decorativeCompanyVOPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findDecorativeCompanyByNameOrCode EXCEPTION,发生未知错误，根据名称,编码查询装饰公司失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 根据下拉框筛选装饰公司
     *
     * @param enabled
     * @param cityId
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/findDecorativeCreditByCondition")
    public GridDataVO<DecorativeCompanyInfo> findDecorativeCompanyCreditByCondition(@RequestParam("enabled") String enabled, @RequestParam("cityId") Long cityId, Integer offset, Integer size, String keywords) {
        logger.info("findDecorativeCompanyCreditByCondition 根据下拉框筛选装饰公司 ,入参 offset:{}, size:{}, kewords:{},cityId:{},enabled{}", offset, size, keywords, cityId, enabled);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<DecorativeCompanyInfo> decorativeCompanyVOPage = this.maStoreService.findDecorativeCreditByCondition(page, size, enabled, cityId);
            List<DecorativeCompanyInfo> decorativeCompanyVOList = decorativeCompanyVOPage.getList();
            logger.info("findDecorativeCompanyCreditByCondition ,根据下拉框筛选装饰公司成功", decorativeCompanyVOList.size());
            return new GridDataVO<DecorativeCompanyInfo>().transform(decorativeCompanyVOList, decorativeCompanyVOPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findDecorativeCompanyCreditByCondition EXCEPTION,发生未知错误，根据下拉框筛选装饰公司失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 编辑装饰公司信用金
     *
     * @param decorativeCompanyCredit
     * @param decorativeCompanySubvention
     * @param result
     * @return
     */
    @PutMapping
    public ResultDTO<String> updateDecorativeCompanyCredit(@Valid DecorativeCompanyCredit decorativeCompanyCredit, @Valid DecorativeCompanySubvention decorativeCompanySubvention, BindingResult result, HttpServletRequest request) {
        logger.info("updateDecorativeCompanyCredit 编辑装饰公司信用金 ,入参 decorativeCompanyCredit:{}, decorativeCompanySubvention:{}", decorativeCompanyCredit, decorativeCompanySubvention);
        try {
            if (!result.hasErrors()) {
                ShiroUser shiroUser = this.getShiroUser();
                DecorativeCompanyCredit decorativeCompanyCreditBefore = maDecorativeCompanyCreditService.findDecorativeCompanyCreditByStoreId(decorativeCompanyCredit.getStoreId());
                StoreCreditMoneyChangeLog storeCreditMoneyChangeLog = new StoreCreditMoneyChangeLog();
                storeCreditMoneyChangeLog.setChangeAmount(decorativeCompanyCredit.getCredit().subtract(decorativeCompanyCreditBefore.getCredit()).doubleValue());
                storeCreditMoneyChangeLog.setChangeType(StoreCreditMoneyChangeType.ADMIN_RECHARGE);
                storeCreditMoneyChangeLog.setChangeTypeDesc(StoreCreditMoneyChangeType.ADMIN_RECHARGE.getDescription());
                storeCreditMoneyChangeLog.setCreateTime(new Date());
                storeCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(decorativeCompanyCredit.getCredit().doubleValue());
                storeCreditMoneyChangeLog.setStoreId(decorativeCompanyCredit.getStoreId());
                storeCreditMoneyChangeLog.setOperatorType(AppIdentityType.ADMINISTRATOR);
                storeCreditMoneyChangeLog.setOperatorId(shiroUser.getId());
                storeCreditMoneyChangeLog.setOperatorIp(IpUtil.getIpAddress(request));
                this.maDecorativeCompanyCreditService.updateDecorativeCompanyCredit(decorativeCompanyCredit,storeCreditMoneyChangeLog);
                this.maDecorativeCompanyCreditService.updateDecorativeCompanySubvention(decorativeCompanySubvention);
                logger.info("updateDecorativeCompanyCredit ,编辑装饰公司信用金成功");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } else {
                List<ObjectError> allErrors = result.getAllErrors();
                logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
                System.err.print(allErrors);
                return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                        errorMsgToHtml(allErrors), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("updateDecorativeCompanyCredit EXCEPTION,发生未知错误，编辑装饰公司信用金失败");
            logger.warn("{}", e);
            return null;
        }
    }


}


