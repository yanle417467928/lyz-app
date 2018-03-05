package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.AppRechargeOrderStatus;
import cn.com.leyizhuang.app.core.constant.StorePreDepositChangeType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dto.StorePreDepositDTO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeOrder;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeReceiptInfo;
import cn.com.leyizhuang.app.foundation.service.AdminUserStoreService;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.service.RechargeService;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositVO;
import cn.com.leyizhuang.app.remote.queue.MaSinkSender;
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
import java.util.Date;
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

    @Autowired
    private AdminUserStoreService adminUserStoreService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private MaSinkSender sinkSender;

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
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
        PageInfo<StorePreDepositVO> storePredeposit = this.maStoreService.findAllStorePredeposit(page, size, cityId, keywords, storeType, storeIds);
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
        logger.info("门店预存款变更及日志保存 modifyPreDeposit 入参 storePreDepositDTO{}，result", storePreDepositDTO, result);
        if (!result.hasErrors()) {
            if (null != storePreDepositDTO && null != storePreDepositDTO.getStoreId() && storePreDepositDTO.getStoreId() != 0){
                if (null != storePreDepositDTO.getChangeMoney() && storePreDepositDTO.getChangeMoney() != 0) {
                    try {
                        AppStore store = this.maStoreService.findAppStoreByStoreId(storePreDepositDTO.getStoreId());
                        if (null == store){
                            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未找到该门店信息！", null);
                        }
                        //生成单号
                        String rechargeNo = OrderUtils.generateRechargeNumber(store.getCityId());

                        storePreDepositDTO.setChangeType(StorePreDepositChangeType.ADMIN_CHANGE);
                        this.maStoreService.changeStorePredepositByStoreId(storePreDepositDTO);
                        Integer identityType = AppIdentityType.SELLER.getValue();
                        if (StoreType.ZS.equals(store.getStoreType())){
                            identityType = AppIdentityType.DECORATE_MANAGER.getValue();
                        }
                        //生成充值单
                        RechargeOrder rechargeOrder = rechargeService.createStoreRechargeOrder(identityType, storePreDepositDTO.getStoreId(),
                                storePreDepositDTO.getChangeMoney(), rechargeNo, storePreDepositDTO.getPayType());

                        rechargeService.saveRechargeOrder(rechargeOrder);

                        //创建充值单收款
                        RechargeReceiptInfo receiptInfo = rechargeService.createStorePayRechargeReceiptInfo(AppIdentityType.CUSTOMER.getValue(), storePreDepositDTO, rechargeNo, store);
                        rechargeService.saveRechargeReceiptInfo(receiptInfo);

                        //将收款记录入拆单消息队列
                        sinkSender.sendRechargeReceipt(rechargeNo);
                    } catch (Exception e) {
                        List<ObjectError> allErrors = result.getAllErrors();
                        logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
                        return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, e.getMessage(), null);
                    }
                    logger.info("门店预存款变更及日志保存成功", null,null);
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                } else{
                    logger.info("门店预存款变更及日志保存失败", null,null);
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "变更金额不能为零！", null);
                }
            } else {
                logger.info("门店预存款变更及日志保存失败", null,null);
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
