package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.dto.StorePreDepositDTO;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositVO;
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
 * @date 2018/1/12
 */
@RestController
@RequestMapping(value = MaStorePreDepositRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaStorePreDepositRestController extends BaseRestController {
    protected static final String PRE_URL = "/rest/store/preDeposit";

    private final Logger logger = LoggerFactory.getLogger(MaStorePreDepositRestController.class);

    @Autowired
    private MaStoreService maStoreService;

    /**
     * @title   获取门店预存款列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/15
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<StorePreDepositVO> restStorePreDepositPageGird(Integer offset, Integer size, String keywords, Long cityId, String storeType) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<StorePreDepositVO> storePredeposit = this.maStoreService.findAllStorePredeposit(page, size, cityId, keywords, storeType);
        return new GridDataVO<StorePreDepositVO>().transform(storePredeposit.getList(), storePredeposit.getTotal());
    }

    /**
     * @title   门店预存款变更及日志保存
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/15
     */
    @PostMapping(value = "/edit")
    public ResultDTO<String> modifyPreDeposit(@Valid StorePreDepositDTO storePreDepositDTO, BindingResult result) {
        if (!result.hasErrors()) {
            if (null != storePreDepositDTO && null != storePreDepositDTO.getStoreId() && storePreDepositDTO.getStoreId() != 0){
                if (null != storePreDepositDTO.getChangeMoney() && storePreDepositDTO.getChangeMoney() != 0) {
                    try {
                        this.maStoreService.changeStorePredepositByStoreId(storePreDepositDTO);
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
