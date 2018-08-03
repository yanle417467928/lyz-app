package cn.com.leyizhuang.app.web.controller.views.decorativeCompany;

import cn.com.leyizhuang.app.core.constant.FitCompayType;
import cn.com.leyizhuang.app.foundation.pojo.StorePreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.response.BillInfoResponse;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.MaFitBillVO;
import cn.com.leyizhuang.app.web.controller.rest.MaFitBillRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * @Description: 装饰公司账单
 * @Author Richard
 * @Date 2018/4/1314:24
 */
@Controller
@RequestMapping(value = "/views/admin/fit/bill")
public class MaDecoratorBillViewController {

    private final Logger logger = LoggerFactory.getLogger(MaDecoratorBillViewController.class);

    @Resource
    private MaFitBillService maFitBillService;

    @Resource
    private MaStoreService maStoreService;

    @Resource
    private BillInfoService billInfoService;

    @RequestMapping(method = RequestMethod.GET, value = "/notOutList")
    public String toNotOutBillPage(ModelMap map) {
        map.addAttribute("fitCompayTypes", FitCompayType.values());
        return "/views/decorativeCompany/fit_company_notOutBill_page";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/notOutBillDetail/{storeId}")
    public String notOutBillDetailPage(@PathVariable Long storeId, ModelMap map) {
        map.addAttribute("storeId",storeId);
        return "/views/decorativeCompany/fit_company_notOutBill_detail";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/historyList")
    public String toHistoryBillPage() {
        return "/views/decorativeCompany/fit_company_historyBill_page";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/historyDetail/{billNo}")
    public String HistoryDetailBill(@PathVariable String billNo, ModelMap map) {
        BillInfoDO maFitBillVO = maFitBillService.getFitBillByBillNo(billNo);
        BillInfoResponse billInfoResponse = BillInfoDO.transfer(maFitBillVO);
        map.addAttribute("maFitBillVO", billInfoResponse);
        return "/views/decorativeCompany/fit_company_historyDetailBill_detail";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/repayment")
    public String repaymentBill() {
        return "/views/decorativeCompany/fit_company_bill_repayment_page";
    }

}
