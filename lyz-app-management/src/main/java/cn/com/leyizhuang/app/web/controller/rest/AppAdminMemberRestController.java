package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.dto.AppAdminMemberDTO;
import cn.com.leyizhuang.app.foundation.pojo.vo.TableDataVO;
import cn.com.leyizhuang.app.foundation.service.AppAdminMemberAuthService;
import cn.com.leyizhuang.app.foundation.service.AppAdminMemberService;
import cn.com.leyizhuang.app.foundation.service.impl.AppAdminMemberServiceImpl;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.ValidatorResultDTO;
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
 * App后台管理会员列表数据控制器
 *
 * @author Richard
 *         Created on 2017-05-09 10:32
 **/
@RestController
@RequestMapping(value = AppAdminMemberRestController.PRE_URL, produces = "application/json;charset=utf8")
public class AppAdminMemberRestController extends BaseRestController{
    protected final static String PRE_URL = "/rest/member";

    private final Logger LOG = LoggerFactory.getLogger(AppAdminMemberRestController.class);

    @Autowired
    private AppAdminMemberService memberService;

    @Autowired
    private AppAdminMemberAuthService memberAuthService;

    @GetMapping(value = "/page/grid")
    public TableDataVO<MemberDO> dataMenuPageGridGet(Integer offset, Integer size, String keywords) {
        // 根据偏移量计算当前页数
        Integer page = (offset / size) + 1;
        PageInfo<MemberDO> memberDOPage = memberService.queryPage(page, size);
        return new TableDataVO<MemberDO>().transform(memberDOPage);
    }

    @GetMapping(value = "/{id}")
    public ResultDTO<AppAdminMemberDTO> restMemberIdGet(@PathVariable(value = "id")Long id) {
        MemberDO memberDO = memberService.queryById(id);
        if (null == memberDO) {
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            //EmployeePageVO employeeVO = EmployeePageVO.transform(employeeDO);
            AppAdminMemberDTO memberDTO = AppAdminMemberDTO.transform(memberDO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, memberDTO);
        }
    }


    @PostMapping(value = "/validator/mobile/{id}")
    public ValidatorResultDTO employeeValidatorMobileByIdPost(@RequestParam String mobile,@PathVariable Long id) {
        Boolean result = Boolean.TRUE;
        if (null != id){
             result = memberAuthService.existsByMobileAndIdNot(mobile,id);

        }else{
             result = memberAuthService.existsByMobile(mobile);
        }
        return new ValidatorResultDTO(!result);
    }

    @PostMapping(value = "/validator/mobile")
    public ValidatorResultDTO employeeValidatorMobilePost(@RequestParam String mobile) {
        Boolean result = Boolean.TRUE;
        result = memberAuthService.existsByMobile(mobile);
        return new ValidatorResultDTO(!result);
    }

    /**
     * 管理员新增会员
     * @param memberDTO
     * @param result
     * @return
     */
    @PostMapping
    public ResultDTO<String> restMemberIdPost(@Valid AppAdminMemberDTO memberDTO, BindingResult result) {
        if (!result.hasErrors()) {
            memberService.saveMemberInfo(memberDTO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        }else {
            List<ObjectError> allErrors = result.getAllErrors();
            LOG.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }

    /**
     * 管理员修改会员信息
     * @param memberDTO
     * @param result
     * @return
     */
    @PutMapping(value = "/{id}")
    public ResultDTO<String> modifyMemberIdPut(@Valid AppAdminMemberDTO memberDTO, BindingResult result) {
        if (!result.hasErrors()) {
            memberService.modifyMemberInfo(memberDTO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        }else {
            List<ObjectError> allErrors = result.getAllErrors();
            LOG.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }

    /**
     * 管理员修改会员密码
     * @param id
     * @param password
     * @return
     */
    @PostMapping(value = "/revise/password")
    public ResultDTO<String> restEmployeePasswordPost(@RequestParam Long id, @RequestParam String password) {
        memberAuthService.modifyMemberPassword(id,password);
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
    }

}
