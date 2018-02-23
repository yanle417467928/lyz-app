package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.constant.EmpCreditMoneyChangeType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.SimpleEmployeeParam;
import cn.com.leyizhuang.app.quartz.QuartzManager;
import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.utils.IpUtil;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.*;
import cn.com.leyizhuang.app.foundation.service.MaClearTempCreditService;
import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideCreditChangeDetailVO;
import cn.com.leyizhuang.app.foundation.service.MaEmpCreditMoneyService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = MaEmpCreditMoneyRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaEmpCreditMoneyRestController extends BaseRestController {
    protected static final String PRE_URL = "/rest/guideLine";

    private final Logger logger = LoggerFactory.getLogger(MaEmpCreditMoneyRestController.class);

    @Autowired
    private MaEmpCreditMoneyService maEmpCreditMoneyService;
    @Autowired
    private MaClearTempCreditService maClearTempCreditService;

    /**
     * 修改员工额度
     *
     * @param guideCreditMoneyDetail
     * @param result
     * @return
     */
    @PutMapping
    public ResultDTO<?> restGuideCreditMoneyVOPut(@Valid GuideCreditMoneyDetail guideCreditMoneyDetail, @RequestParam(value = "modifyReason") String modifyReason, BindingResult result, HttpServletRequest request,@RequestParam(value = "lastUpdateTime") String lastUpdateTime) {
        logger.info("restGuideCreditMoneyVOPut 后台修改员工额度 ,入参 guideCreditMoneyDetail:{}, modifyReason:{},", guideCreditMoneyDetail, modifyReason);
        try {
            if (!result.hasErrors()) {
                DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date lastUpdateTimeFormat =dateFormat.parse(lastUpdateTime);
                ShiroUser shiroUser = this.getShiroUser();
                GuideCreditChangeDetailVO guideCreditChangeDetailVO = new GuideCreditChangeDetailVO();
                guideCreditChangeDetailVO.setOperatorId(shiroUser.getId());
                guideCreditChangeDetailVO.setOperatorName(shiroUser.getName());
                guideCreditChangeDetailVO.setEmpId(guideCreditMoneyDetail.getEmpId());
                //TODO
                //没有修改原因字段
                guideCreditChangeDetailVO.setChangeTypeDesc(modifyReason);
                guideCreditChangeDetailVO.setChangeType(EmpCreditMoneyChangeType.ADMIN_RECHARGE);
                //guideCreditChangeDetailVO.setChangeTypeDesc(EmpCreditMoneyChangeType.ADMIN_RECHARGE.getDescription());
                guideCreditChangeDetailVO.setOperatorIp(IpUtil.getIpAddress(request));
                this.maEmpCreditMoneyService.update(guideCreditMoneyDetail, guideCreditChangeDetailVO,lastUpdateTimeFormat);
                logger.info("restGuideCreditMoneyVOPut ,后台修改员工额度成功");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } else {
                List<ObjectError> allErrors = result.getAllErrors();
                logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
                System.err.print(allErrors);
                logger.warn("restGuideCreditMoneyVOPut ,后台修改员工额度失败,参数有误");
                return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                        errorMsgToHtml(allErrors), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restGuideCreditMoneyVOPut EXCEPTION,发生未知错误，后台修改员工额度失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 手动清零临时额度
     *
     * @param
     * @return
     */
    @PostMapping(value = "/clearTempCreditLimit")
    public ResultDTO<?> clearTempCreditLimit(@Valid GuideCreditMoneyDetail guideCreditMoneyDetail, BindingResult result, HttpServletRequest request) {
        logger.info("clearTempCreditLimit 后台手动清零临时额度 ,入参 guideCreditMoneyDetail:{},", guideCreditMoneyDetail);
        try {
            if (!result.hasErrors()) {
                //获取当前操作人,并设置额度变更明细VO
                ShiroUser shiroUser = this.getShiroUser();
                GuideCreditChangeDetailVO guideCreditChangeDetailVO = new GuideCreditChangeDetailVO();
                guideCreditChangeDetailVO.setOperatorId(shiroUser.getId());
                guideCreditChangeDetailVO.setOperatorName(shiroUser.getName());
                guideCreditChangeDetailVO.setEmpId(guideCreditMoneyDetail.getEmpId());
                guideCreditChangeDetailVO.setChangeType(EmpCreditMoneyChangeType.TEMPORARY_CLEAR);
                guideCreditChangeDetailVO.setChangeTypeDesc(EmpCreditMoneyChangeType.TEMPORARY_CLEAR.getDescription());
                guideCreditChangeDetailVO.setOperatorIp(IpUtil.getIpAddress(request));
                this.maEmpCreditMoneyService.clearTempCreditLimit(guideCreditMoneyDetail, guideCreditChangeDetailVO);
                logger.info("clearTempCreditLimit ,后台手动清零临时额度成功");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } else {
                List<ObjectError> allErrors = result.getAllErrors();
                logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
                System.err.print(allErrors);
                logger.warn("clearTempCreditLimit ,后台手动清零临时额度,参数有误");
                return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE, errorMsgToHtml(allErrors), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("clearTempCreditLimit EXCEPTION,发生未知错误，后台手动清零临时额度失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 更新自动清空临时额度时间
     *
     * @param cronTime,jobName
     * @return
     */
    @PutMapping(value = "/change")
    public ResultDTO<?> changeClearTempCreditLimitTime(@RequestParam(value = "cronTime") String cronTime, @RequestParam(value = "jobName") String jobName) {
        logger.info("changeClearTempCreditLimitTime 后台更新自动清空临时额度时间 ,入参 cronTime:{},jobName:{},", cronTime, jobName);
        try {
            if (StringUtils.isNotBlank(cronTime) && StringUtils.isNotBlank(jobName)) {
                Boolean enable = maClearTempCreditService.update(cronTime, jobName);
                if (enable) {
                    QuartzManager.modifyJobTime("clearTempCredit", "jobGroup", "trigger", "triggerGroup", cronTime);
                    logger.info("changeClearTempCreditLimitTime ,后台更新自动清空临时额度时间成功");
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                } else {
                    logger.warn("后台更新自动清空临时额度时间失败");
                    return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE, "后台更新自动清空临时额度时间失败"
                            , null);
                }
            } else {
                logger.warn("页面提交ID有误：errors = {}");
                return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE, "参数为空"
                        , null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("changeClearTempCreditLimitTime EXCEPTION,发生未知错误，后台更新自动清空临时额度时间失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 可用额度改变详情列表
     *
     * @param offset
     * @param size
     * @param keywords
     * @param id
     * @return
     */
    @GetMapping(value = "/availableCreditChangePage/grid/{id}")
    public GridDataVO<GuideCreditChangeDetailVO> restAvailableCreditChangesPageGird(Integer offset, Integer size, String keywords, @PathVariable(value = "id") Long id) {
        logger.info("restAvailableCreditChangesPageGird 后台获取可用额度改变详情列表 ,入参 offset:{},size:{},keywords:{},id:{}", offset, size, keywords, id);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<GuideCreditChangeDetailDO> creditMoneyChangePage = this.maEmpCreditMoneyService.queryAvailableCreditMoneyChangePage(page, size, id);
            List<GuideCreditChangeDetailDO> creditMoneyChangePageList = creditMoneyChangePage.getList();
            List<GuideCreditChangeDetailVO> creditMoneyChangeVOPageList = GuideCreditChangeDetailVO.transform(creditMoneyChangePageList);
            logger.info("restAvailableCreditChangesPageGird ,后台获取可用额度改变详情列表成功", creditMoneyChangeVOPageList.size());
            return new GridDataVO<GuideCreditChangeDetailVO>().transform(creditMoneyChangeVOPageList, creditMoneyChangePage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restAvailableCreditChangesPageGird EXCEPTION,发生未知错误，后台获取可用额度改变详情列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 临时额度改变详情页
     *
     * @param offset
     * @param size
     * @param keywords
     * @param id
     * @return
     */
    @GetMapping(value = "/tempCreditChangePage/grid/{id}")
    public GridDataVO<GuideCreditChangeDetailVO> restTempCreditChangesPageGird(Integer offset, Integer size, String keywords, @PathVariable(value = "id") Long id) {
        logger.info("restTempCreditChangesPageGird 后台获取临时额度改变详列表 ,入参 offset:{},size:{},keywords:{},id:{}", offset, size, keywords, id);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<GuideCreditChangeDetailDO> creditMoneyChangePage = this.maEmpCreditMoneyService.queryTempCreditMoneyChangePage(page, size, id);
            List<GuideCreditChangeDetailDO> creditMoneyChangePageList = creditMoneyChangePage.getList();
            List<GuideCreditChangeDetailVO> creditMoneyChangeVOPageList = GuideCreditChangeDetailVO.transform(creditMoneyChangePageList);
            logger.info("restTempCreditChangesPageGird ,后台获取临时额度改变详列表成功", creditMoneyChangeVOPageList.size());
            return new GridDataVO<GuideCreditChangeDetailVO>().transform(creditMoneyChangeVOPageList, creditMoneyChangePage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restTempCreditChangesPageGird EXCEPTION,发生未知错误，后台获取临时额度改变详列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 固定额度改变详情页
     *
     * @param offset
     * @param size
     * @param keywords
     * @param id
     * @return
     */
    @GetMapping(value = "/fixedCreditChangePage/grid/{id}")
    public GridDataVO<GuideCreditChangeDetailVO> restFixedCreditChangesPageGird(Integer offset, Integer size, String keywords, @PathVariable(value = "id") Long id) {
        logger.info("restFixedCreditChangesPageGird 后台获取固定额度改变详情列表 ,入参 offset:{},size:{},keywords:{},id:{}", offset, size, keywords, id);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<GuideCreditChangeDetailDO> creditMoneyChangePage = this.maEmpCreditMoneyService.queryFixedCreditMoneyChangePage(page, size, id);
            List<GuideCreditChangeDetailDO> creditMoneyChangePageList = creditMoneyChangePage.getList();
            List<GuideCreditChangeDetailVO> creditMoneyChangeVOPageList = GuideCreditChangeDetailVO.transform(creditMoneyChangePageList);
            logger.info("restFixedCreditChangesPageGird ,后台获取固定额度改变详情列表成功", creditMoneyChangeVOPageList.size());
            return new GridDataVO<GuideCreditChangeDetailVO>().transform(creditMoneyChangeVOPageList, creditMoneyChangePage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restFixedCreditChangesPageGird EXCEPTION,发生未知错误，后台获取固定额度改变详情列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

}
