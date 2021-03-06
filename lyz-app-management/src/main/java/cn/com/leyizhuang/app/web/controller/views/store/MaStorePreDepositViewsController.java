package cn.com.leyizhuang.app.web.controller.views.store;

import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.foundation.service.BankService;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.vo.management.BankVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.FitCreditMoneyChangeLogVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositLogVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositVO;
import cn.com.leyizhuang.app.web.controller.BaseController;
import cn.com.leyizhuang.app.web.controller.views.customer.MaCustomerPreDepositViewsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/12
 */
@Controller
@RequestMapping(value = MaStorePreDepositViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaStorePreDepositViewsController extends BaseController {
    protected final static String PRE_URL = "/views/admin/store/preDeposit";

    private final Logger logger = LoggerFactory.getLogger(MaStorePreDepositViewsController.class);

    @Autowired
    private MaStoreService maStoreService;

    @Autowired
    private BankService bankService;

    /**
     * @title 跳转门店预存款列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/15
     */
    @RequestMapping(value = "/list")
    public String getStoreList(ModelMap map) {
        map.addAttribute("storeTypes", StoreType.values());
        return "/views/store/store_pre_deposit_page";
    }

    /**
     * @title 跳转变更预存款页面
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/15
     */
    @GetMapping(value = "/edit/{storeId}")
    public String resourceEdit(ModelMap map, @PathVariable(value = "storeId") Long storeId) {
        StorePreDepositVO storePreDepositVO = this.maStoreService.queryStorePredepositByStoreId(storeId);
        StorePreDepositLogVO log = this.maStoreService.queryLastStoreChange(storeId);
        if (null == storePreDepositVO) {
            logger.warn("跳转变更预存款页面失败，storePreDepositVO(storeId = {}) == null", storeId);
            error404();
            return "/error/404";
        } else {
            List<OrderBillingPaymentType> paymentTypes = new ArrayList<>();
            paymentTypes.add(OrderBillingPaymentType.ALIPAY);
            paymentTypes.add(OrderBillingPaymentType.WE_CHAT);
            paymentTypes.add(OrderBillingPaymentType.CASH);
            paymentTypes.add(OrderBillingPaymentType.POS);
            paymentTypes.add(OrderBillingPaymentType.TRANSFER_ACCOUNTS);
            map.addAttribute("paymentTypes", paymentTypes);
            map.addAttribute("storePreDepositVO", storePreDepositVO);
            List<BankVO> bankVOS = this.bankService.findBankByIsEnable();
            map.addAttribute("bankVOS", bankVOS);
            map.addAttribute("changeLog",log);
        }
        return "/views/store/store_pre_deposit_edit";
    }


}
