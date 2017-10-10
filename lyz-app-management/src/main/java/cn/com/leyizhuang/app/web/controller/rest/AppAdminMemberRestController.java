package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.SalesConsult;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.vo.AppAdminMemberVO;
import cn.com.leyizhuang.app.foundation.service.AppAdminMemberAuthService;
import cn.com.leyizhuang.app.foundation.service.AppAdminMemberService;
import cn.com.leyizhuang.app.foundation.service.AppAdminSalesConsultService;
import cn.com.leyizhuang.app.foundation.service.IAppStoreService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * App后台管理会员列表数据控制器
 *
 * @author Richard
 *         Created on 2017-05-09 10:32
 **/
@RestController
@RequestMapping(value = AppAdminMemberRestController.PRE_URL, produces = "application/json;charset=utf8")
public class AppAdminMemberRestController extends BaseRestController{
    public final static String PRE_URL = "/rest/member";

    private final Logger LOG = LoggerFactory.getLogger(AppAdminMemberRestController.class);


    private AppAdminMemberService memberService;
    @Autowired
    public void setMemberService(AppAdminMemberService memberService) {
        this.memberService = memberService;
    }

    private AppAdminMemberAuthService memberAuthService;
    @Autowired
    public void setMemberAuthService(AppAdminMemberAuthService memberAuthService) {
        this.memberAuthService = memberAuthService;
    }

    private AppAdminSalesConsultService salesConsultService;

    @Autowired
    public void setSalesConsultService(AppAdminSalesConsultService salesConsultService) {
        this.salesConsultService = salesConsultService;
    }

    private IAppStoreService storeService;

    @Autowired
    public void setStoreService(IAppStoreService storeService) {
        this.storeService = storeService;
    }

    /**
     * 会员列表
     * @param offset
     * @param size
     * @param keywords
     * @return 会员列表
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<AppAdminMemberVO> dataMenuPageGridGet(Integer offset, Integer size, String keywords) {
        // 根据偏移量计算当前页数
        Integer page = (offset / size) + 1;
        PageInfo<AppAdminMemberVO> memberPage = memberService.queryMemberVOPage(page, size);
        return new GridDataVO<AppAdminMemberVO>().transform(memberPage);
    }

    /**
     * 会员详情
     * @param id 会员id
     * @return MemberVO
     */
    @GetMapping(value = "/{id}")
    public ResultDTO<AppAdminMemberVO> restMemberIdGet(@PathVariable(value = "id")Long id) {
        AppAdminMemberVO memberVO = memberService.queryMemberVOById(id);
        if (null == memberVO) {
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, memberVO);
        }
    }
    /**
     * 手机号码验证
     * @param mobile
     * @param id
     * @return
     */
    @PostMapping(value = "/validator/mobile/{id}")
    public ValidatorResultDTO employeeValidatorMobileByIdPost(@RequestParam String mobile,@PathVariable Long id) {
        Boolean result;
        if (null != id){
             result = memberAuthService.existsByMobileAndIdNot(mobile,id);

        }else{
             result = memberAuthService.existsByMobile(mobile);
        }
        return new ValidatorResultDTO(!result);
    }

    /**
     * 手机号码验证
     * @param mobile
     * @return
     */
    @PostMapping(value = "/validator/mobile")
    public ValidatorResultDTO employeeValidatorMobilePost(@RequestParam String mobile) {
        Boolean result;
        result = memberAuthService.existsByMobile(mobile);
        return new ValidatorResultDTO(!result);
    }

    /**
     * 管理员新增会员
     * @param memberVO 会员信息
     * @param result 处理结果
     * @return 处理结果
     */
    @PostMapping
    public ResultDTO<String> restMemberIdPost(@Valid AppAdminMemberVO memberVO, BindingResult result) {
        if (!result.hasErrors()) {
            memberService.saveMemberInfo(memberVO);
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
     * @param memberVO
     * @param result
     * @return
     */
    @PutMapping(value = "/{id}")
    public ResultDTO<String> modifyMemberIdPut(@Valid AppAdminMemberVO memberVO, BindingResult result) {
        if (!result.hasErrors()) {
            memberService.modifyMemberInfo(memberVO);
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

    /**
     * 新增会员页面改变门店触发的事件
     * @param storeId 导购所属门店ID
     * @return 异步请求的数据
     */
    @PostMapping(value = "/change/store")
    public Map<String,Object> restMemberChangeStore(Long storeId) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<SalesConsult> salesConsultList = new ArrayList<>();
        if (null != storeId){
            salesConsultList = salesConsultService.findByStoreId(storeId);
        }
        map.put("code", 0);
        map.put("sales_consult_list", salesConsultList);
        return map;
    }

    /**
     * 新增会员改变导购触发的事件
     * @param consultId
     * @return
     */
    @PostMapping(value = "/change/consult")
    public Map<String,Object> restMemberChangeSalesConsult(Long consultId) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<AppStore> allAppStoreList = new ArrayList<>();
        if (null != consultId){
            allAppStoreList = storeService.findAll();
        }
        SalesConsult consult = salesConsultService.findByConsultId(consultId);
        Long storeId = consult.getAscriptionStoreId();
        map.put("code", 0);
        map.put("all_store_list", allAppStoreList);
        map.put("storeId",storeId);
        return map;
    }
}
