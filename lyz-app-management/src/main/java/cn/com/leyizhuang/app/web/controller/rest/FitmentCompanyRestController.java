package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.dto.FitmentCompanyDTO;
import cn.com.leyizhuang.app.foundation.pojo.FitmentCompanyDO;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.FitmentCompanyService;
import cn.com.leyizhuang.app.foundation.vo.FitmentCompanyVO;
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

/**
 * @author GenerationRoad
 * @date 2017/9/19
 */
@RestController
@RequestMapping(value = FitmentCompanyRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class FitmentCompanyRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/company";

    private final Logger logger = LoggerFactory.getLogger(FitmentCompanyRestController.class);

    @Autowired
    private FitmentCompanyService fitmentCompanyServiceImpl;

    @GetMapping(value = "/page/grid")
    public GridDataVO<FitmentCompanyVO> restGoodsPageGird(Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);

        PageInfo<FitmentCompanyDO> companyDOPage = this.fitmentCompanyServiceImpl.queryPage(page, size);
        List<FitmentCompanyDO> companyDOList = companyDOPage.getList();
        List<FitmentCompanyVO> decorationCompanyVOList = FitmentCompanyVO.transform(companyDOList);
        return new GridDataVO<FitmentCompanyVO>().transform(decorationCompanyVOList, companyDOPage.getTotal());
    }

    @GetMapping(value = "/{id}")
    public ResultDTO<FitmentCompanyVO> restGoodsIdGet(@PathVariable(value = "id") Long id) {
        FitmentCompanyDO fitmentCompanyDO = this.fitmentCompanyServiceImpl.queryById(id);
        if (null == fitmentCompanyDO) {
            logger.warn("查找角色失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            FitmentCompanyVO decorationCompanyVO = FitmentCompanyVO.transform(fitmentCompanyDO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, decorationCompanyVO);
        }
    }

    @PutMapping(value = "/{id}")
    public ResultDTO<String> modifyCompanyPut(@Valid FitmentCompanyDTO fitmentCompanyDTO, BindingResult result) {
        if (!result.hasErrors()) {
            this.fitmentCompanyServiceImpl.managerModifyCompany(fitmentCompanyDTO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }

    @PostMapping
    public ResultDTO<String> restCompanyPost(@Valid FitmentCompanyDTO fitmentCompanyDTO, BindingResult result) {
        if (!result.hasErrors()) {
            this.fitmentCompanyServiceImpl.managerSaveCompany(fitmentCompanyDTO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }

}
