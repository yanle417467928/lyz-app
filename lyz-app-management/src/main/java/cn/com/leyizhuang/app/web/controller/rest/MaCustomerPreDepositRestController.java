package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.foundation.dto.CusPreDepositDTO;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.AdminUserStoreService;
import cn.com.leyizhuang.app.foundation.service.MaCustomerService;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerPreDepositVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/8
 */
@RestController
@RequestMapping(value = MaCustomerPreDepositRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCustomerPreDepositRestController extends BaseRestController {

    protected static final String PRE_URL = "/rest/customer/preDeposit";

    private final Logger logger = LoggerFactory.getLogger(MaCustomerPreDepositRestController.class);

    @Autowired
    private MaCustomerService maCustomerService;

    @Autowired
    private AdminUserStoreService adminUserStoreService;


    /**
     * @title   获取顾客预存款列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/11
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<CustomerPreDepositVO> restCustomerPreDepositPageGird(Integer offset, Integer size, String keywords, Long cityId, Long storeId) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //获取登录用户ID
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        List<Long> storeIds = new ArrayList<>();
        if (null != shiroUser) {
            //查询登录用户门店权限的门店ID
            storeIds = this.adminUserStoreService.findStoreIdByUid(shiroUser.getId());
        }
        PageInfo<CustomerPreDepositVO> custmerPrePage = this.maCustomerService.findAllCusPredeposit(page, size, cityId, storeId, keywords, storeIds);
        return new GridDataVO<CustomerPreDepositVO>().transform(custmerPrePage.getList(), custmerPrePage.getTotal());
    }

    /**
     * @title   顾客预存款变更及日志保存
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/11
     */
    @PostMapping(value = "/edit")
    public ResultDTO<String> modifyPreDeposit(@Valid CusPreDepositDTO cusPreDepositDTO, BindingResult result) {
        if (!result.hasErrors()) {
            if (null != cusPreDepositDTO && null != cusPreDepositDTO.getCusId() && cusPreDepositDTO.getCusId() != 0){
                if (null != cusPreDepositDTO.getChangeMoney() && cusPreDepositDTO.getChangeMoney() != 0) {
                    try {
                        this.maCustomerService.changeCusPredepositByCusId(cusPreDepositDTO);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, e.getMessage(), null);
                    }
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                } else{
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "变更金额不能为零！", null);
                }
            } else {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "信息错误！", null);
            }

        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }



}
