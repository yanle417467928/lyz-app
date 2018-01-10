package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.dto.CusPreDepositLogDTO;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.MaCusPreDepositLogService;
import cn.com.leyizhuang.app.foundation.service.MaCustomerService;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerPreDepositVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
    private MaCusPreDepositLogService maCusPreDepositLogService;

    @GetMapping(value = "/page/grid")
    public GridDataVO<CustomerPreDepositVO> restCustomerPreDepositPageGird(Integer offset, Integer size, String keywords, Long cityId, Long storeId) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<CustomerPreDepositVO> custmerPrePage = this.maCustomerService.findAllCusPredeposit(page, size, cityId, storeId, keywords);
        return new GridDataVO<CustomerPreDepositVO>().transform(custmerPrePage.getList(), custmerPrePage.getTotal());
    }

    @PutMapping(value = "/edit")
    public ResultDTO<String> modifyEmployeeIdPut(@Valid CusPreDepositLogDTO cusPreDepositLogDTO, BindingResult result) {
        if (!result.hasErrors()) {
            if (null != cusPreDepositLogDTO && null != cusPreDepositLogDTO.getCusId() && cusPreDepositLogDTO.getCusId() != 0){
                if (null != cusPreDepositLogDTO.getChangeMoney() && cusPreDepositLogDTO.getChangeMoney() != 0) {
                    this.maCustomerService.changeCusPredepositByCusId(cusPreDepositLogDTO);
                    this.maCusPreDepositLogService.save(cusPreDepositLogDTO);
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
