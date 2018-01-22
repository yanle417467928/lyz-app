package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.dto.CusLebiDTO;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.MaCustomerService;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerLebiVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/13
 */
@RestController
@RequestMapping(value = MaCustomerLebiRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCustomerLebiRestController extends BaseRestController{

    protected static final String PRE_URL = "/rest/customer/lebi";

    private final Logger logger = LoggerFactory.getLogger(MaCustomerLebiRestController.class);

    @Autowired
    private MaCustomerService maCustomerService;

    /**
     * @title   获取顾客乐币列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/11
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<CustomerLebiVO> restCustomerPreDepositPageGird(Integer offset, Integer size, String keywords, Long cityId, Long storeId) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<CustomerLebiVO> customerLebiVOPageInfo = this.maCustomerService.findAllCusLebi(page, size, cityId, storeId, keywords);
        return new GridDataVO<CustomerLebiVO>().transform(customerLebiVOPageInfo.getList(), customerLebiVOPageInfo.getTotal());
    }

    /**
     * @title   顾客乐币变更及日志保存
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/15
     */
    @PostMapping(value = "/edit")
    public ResultDTO<String> modifyLebi(@Valid CusLebiDTO cusLebiDTO, BindingResult result) {
        if (!result.hasErrors()) {
            if (null != cusLebiDTO && null != cusLebiDTO.getCusId() && cusLebiDTO.getCusId() != 0){
                if (null != cusLebiDTO.getChangeNum() && cusLebiDTO.getChangeNum() != 0) {
                    try {
                        this.maCustomerService.changeCusLebiByCusId(cusLebiDTO);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, e.getMessage(), null);
                    }
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                } else{
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "变更数量不能为零！", null);
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
