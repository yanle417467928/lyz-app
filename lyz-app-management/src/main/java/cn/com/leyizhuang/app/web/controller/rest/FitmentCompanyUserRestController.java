package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.FitmentCompanyUserDO;
import cn.com.leyizhuang.app.foundation.pojo.dto.FitmentCompanyUserDTO;
import cn.com.leyizhuang.app.foundation.pojo.vo.FitmentCompanyUserVO;
import cn.com.leyizhuang.app.foundation.pojo.vo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.FitmentCompanyUserService;
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
 * @date 2017/9/20
 */
@RestController
@RequestMapping(value = FitmentCompanyUserRestController.PRE_URL,produces = "application/json;charset=utf-8")
public class FitmentCompanyUserRestController extends BaseRestController {
    protected final static String PRE_URL = "/rest/companyUser";

    private final Logger logger = LoggerFactory.getLogger(FitmentCompanyUserRestController.class);

    @Autowired
    private FitmentCompanyUserService fitmentCompanyUserServiceImpl;

    @GetMapping(value = "/page/grid")
    public GridDataVO<FitmentCompanyUserVO> restGoodsPageGird(Integer offset, Integer size, String keywords){
        size = getSize(size);
        Integer page = getPage(offset, size);

        PageInfo<FitmentCompanyUserDO> companyUserDOPage = this.fitmentCompanyUserServiceImpl.queryPage(page,size);
        List<FitmentCompanyUserDO> companyUserDOList = companyUserDOPage.getList();
        List<FitmentCompanyUserVO> companyUserVOList = FitmentCompanyUserVO.transform(companyUserDOList);
        return new GridDataVO<FitmentCompanyUserVO>().transform(companyUserVOList,companyUserDOPage.getTotal());
    }

    @GetMapping(value = "/{id}")
    public ResultDTO<FitmentCompanyUserVO> restGoodsIdGet(@PathVariable(value = "id") Long id) {
        FitmentCompanyUserDO fitmentCompanyUserDO = this.fitmentCompanyUserServiceImpl.queryById(id);
        if (null == fitmentCompanyUserDO) {
            logger.warn("查找角色失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            FitmentCompanyUserVO companyUserVO = FitmentCompanyUserVO.transform(fitmentCompanyUserDO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,null, companyUserVO);
        }
    }

    @PutMapping(value = "/{id}")
    public ResultDTO<String> modifyCompanyPut(@Valid FitmentCompanyUserDTO fitmentCompanyUserDTO, BindingResult result){
        if (!result.hasErrors()) {
            this.fitmentCompanyUserServiceImpl.managerModifyCompanyUser(fitmentCompanyUserDTO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        }else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }

    @PostMapping
    public ResultDTO<String> restCompanyPost(@Valid FitmentCompanyUserDTO fitmentCompanyUserDTO, BindingResult result) {
        if (!result.hasErrors()) {
            this.fitmentCompanyUserServiceImpl.managerSaveCompanyUser(fitmentCompanyUserDTO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        }else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }

}
