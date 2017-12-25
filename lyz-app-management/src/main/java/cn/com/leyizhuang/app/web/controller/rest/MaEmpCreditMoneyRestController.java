package cn.com.leyizhuang.app.web.controller.rest;

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
     * @param guideCreditMoneyVO
     * @param result
     * @return
     */
    @PutMapping(value = "/{id}")
    public ResultDTO<?> restGuideCreditMoneyVOPut(@Valid GuideCreditMoney guideCreditMoneyVO, BindingResult result) {
        if (!result.hasErrors()) {
             this.maEmpCreditMoneyService.update(guideCreditMoneyVO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            System.err.print(allErrors);
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }


    /**
     * 清零临时额度
     * @param id
     * @return
     */
    @PostMapping(value = "/clearTempCreditLimit/{id}")
    public ResultDTO<?> clearTempCreditLimit(@PathVariable(value = "id") Long id) {
        if(null !=id){
           this.maEmpCreditMoneyService.clearTempCreditLimit(id);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        }else {
            logger.warn("页面提交ID有误：errors = {}");
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,"id为空"
                    , null);
        }
    }


    @PostMapping(value = "/change")
    public ResultDTO<?> change(@RequestParam(value = "cronTime") String cronTime,@RequestParam(value = "jobName") String jobName) {
        if(null !=cronTime&&null !=jobName){
           Boolean enable  = maClearTempCreditService.update(cronTime,jobName);
           if(enable){
               QuartzManager.modifyJobTime("clearTempCredit","jobGroup","trigger","triggerGroup",cronTime);
               return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
           }else{
               logger.warn("更新临时额度时间失败");
               return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,"更新零食额度时间失败"
                       , null);
           }
        }else{
            logger.warn("页面提交ID有误：errors = {}");
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,"参数为空"
                    , null);
        }
    }


    @GetMapping(value = "/availableCreditChangePage/grid/{id}")
    public GridDataVO<GuideCreditChangeDetailVO> resAvailableCreditChangesPageGird(Integer offset, Integer size, String keywords, @PathVariable(value = "id")Long id) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GuideCreditChangeDetailDO> creditMoneyChangePage = this.maEmpCreditMoneyService.queryAvailableCreditMoneyChangePage(page, size,id);
        List<GuideCreditChangeDetailDO> creditMoneyChangePageList = creditMoneyChangePage.getList();
        List<GuideCreditChangeDetailVO> creditMoneyChangeVOPageList =GuideCreditChangeDetailVO.transform(creditMoneyChangePageList);
        return new GridDataVO<GuideCreditChangeDetailVO>().transform(creditMoneyChangeVOPageList, creditMoneyChangePage.getTotal());
    }

    @GetMapping(value = "/tempCreditChangePage/grid/{id}")
    public GridDataVO<GuideCreditChangeDetailVO> resTempCreditChangesPageGird(Integer offset, Integer size, String keywords, @PathVariable(value = "id")Long id) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GuideCreditChangeDetailDO> creditMoneyChangePage = this.maEmpCreditMoneyService.queryTempCreditMoneyChangePage(page, size,id);
        List<GuideCreditChangeDetailDO> creditMoneyChangePageList = creditMoneyChangePage.getList();
        List<GuideCreditChangeDetailVO> creditMoneyChangeVOPageList =GuideCreditChangeDetailVO.transform(creditMoneyChangePageList);
        return new GridDataVO<GuideCreditChangeDetailVO>().transform(creditMoneyChangeVOPageList, creditMoneyChangePage.getTotal());
    }


    @GetMapping(value = "/fixedCreditChangePage/grid/{id}")
    public GridDataVO<GuideCreditChangeDetailVO> resFixedCreditChangesPageGird(Integer offset, Integer size, String keywords, @PathVariable(value = "id")Long id) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GuideCreditChangeDetailDO> creditMoneyChangePage = this.maEmpCreditMoneyService.queryFixedCreditMoneyChangePage(page, size,id);
        List<GuideCreditChangeDetailDO> creditMoneyChangePageList = creditMoneyChangePage.getList();
        List<GuideCreditChangeDetailVO> creditMoneyChangeVOPageList =GuideCreditChangeDetailVO.transform(creditMoneyChangePageList);
        return new GridDataVO<GuideCreditChangeDetailVO>().transform(creditMoneyChangeVOPageList, creditMoneyChangePage.getTotal());
    }

    @PostMapping(value = "/saveCreditChange")
    public  ResultDTO<Object> saveCreditChange(@Valid GuideCreditMoneyDetail guideCreditMoneyDetail, BindingResult result, HttpServletRequest request) {
        if (!result.hasErrors()) {
            Map<String,Long> idList  =this.maEmpCreditMoneyService.saveCreditMoneyChange(guideCreditMoneyDetail);
            Long tempCreditChangeId = idList.get("tempCreditLimitId");
            Long fixedCreditChangeId = idList.get("fixedCreditLimitId");
            Long availableCreditChangId = idList.get("creditLimitAvailableId");
            ShiroUser shiroUser = this.getShiroUser();
            SimpleEmployeeParam employeeVO = new SimpleEmployeeParam();
            employeeVO.setId(shiroUser.getId());
            GuideCreditChangeDetailVO  guideCreditChangeDetailVO = new GuideCreditChangeDetailVO();
            guideCreditChangeDetailVO.setOperatorId(employeeVO);
            guideCreditChangeDetailVO.setEmpId(guideCreditMoneyDetail.getEmpId());
            guideCreditChangeDetailVO.setCreateTime(new Date());
            guideCreditChangeDetailVO.setChangeTypeDesc("后台修改");
            guideCreditChangeDetailVO.setOperatorIp(IpUtil.getIpAddress(request));
            guideCreditChangeDetailVO.setAvailableCreditChangId(new GuideAvailableCreditChange(availableCreditChangId,null,null));
            guideCreditChangeDetailVO.setFixedCreditChangeId(new GuideFixedCreditChange(fixedCreditChangeId,null,null));
            guideCreditChangeDetailVO.setTempCreditChangeId(new GuideTempCreditChange(tempCreditChangeId,null,null));
            this.maEmpCreditMoneyService.saveCreditChange(guideCreditChangeDetailVO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }
}
