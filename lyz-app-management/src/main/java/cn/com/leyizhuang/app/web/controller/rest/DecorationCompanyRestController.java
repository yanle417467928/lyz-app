package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.DecorationCompanyDO;
import cn.com.leyizhuang.app.foundation.pojo.dto.DecorationCompanyDTO;
import cn.com.leyizhuang.app.foundation.pojo.vo.DecorationCompanyVO;
import cn.com.leyizhuang.app.foundation.pojo.vo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.DecorationCompanyService;
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
@RequestMapping(value = DecorationCompanyRestController.PRE_URL,produces = "application/json;charset=utf-8")
public class DecorationCompanyRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/company";

    private final Logger logger = LoggerFactory.getLogger(DecorationCompanyRestController.class);

    @Autowired
    private DecorationCompanyService decorationCompanyServiceImpl;

    @GetMapping(value = "/page/grid")
    public GridDataVO<DecorationCompanyVO> restGoodsPageGird(Integer offset, Integer size, String keywords){
        size = getSize(size);
        Integer page = getPage(offset, size);

        PageInfo<DecorationCompanyDO> companyDOPage = this.decorationCompanyServiceImpl.queryPage(page,size);
        List<DecorationCompanyDO> companyDOList = companyDOPage.getList();
        List<DecorationCompanyVO> decorationCompanyVOList = DecorationCompanyVO.transform(companyDOList);
        return new GridDataVO<DecorationCompanyVO>().transform(decorationCompanyVOList,companyDOPage.getTotal());
    }

    @GetMapping(value = "/{id}")
    public ResultDTO<DecorationCompanyVO> restGoodsIdGet(@PathVariable(value = "id") Long id) {
        DecorationCompanyDO decorationCompanyDO = this.decorationCompanyServiceImpl.queryById(id);
        if (null == decorationCompanyDO) {
            logger.warn("查找角色失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            DecorationCompanyVO decorationCompanyVO = DecorationCompanyVO.transform(decorationCompanyDO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,null, decorationCompanyVO);
        }
    }

    @PutMapping(value = "/{id}")
    public ResultDTO<String> modifyCompanyPut(@Valid DecorationCompanyDTO decorationCompanyDTO, BindingResult result){
        if (!result.hasErrors()) {
            this.decorationCompanyServiceImpl.managerModifyCompany(decorationCompanyDTO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        }else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }

    @PostMapping
    public ResultDTO<String> restCompanyPost(@Valid DecorationCompanyDTO decorationCompanyDTO, BindingResult result) {
        if (!result.hasErrors()) {
            this.decorationCompanyServiceImpl.managerSaveCompany(decorationCompanyDTO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        }else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }

}
