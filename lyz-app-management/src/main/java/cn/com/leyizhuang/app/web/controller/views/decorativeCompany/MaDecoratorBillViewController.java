package cn.com.leyizhuang.app.web.controller.views.decorativeCompany;

import cn.com.leyizhuang.app.foundation.pojo.StorePreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillInfoDO;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.MaFitBillVO;
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

    @Resource
    private MaFitBillService maFitBillService;

    @Resource
    private MaStoreService maStoreService;


    @RequestMapping(method = RequestMethod.GET, value = "/notOutList")
    public String toNotOutBillPage() {
        return "/views/decorativeCompany/fit_company_notOutBill_page";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/notOutBillDetail/{billNo}")
    public String notOutBillDetailPage(@PathVariable String billNo, ModelMap map) {
        BillInfoDO maFitBillVO = maFitBillService.getFitBillByBillNo(billNo);
        StorePreDeposit storePreDeposit = maStoreService.findByStoreId(maFitBillVO.getStoreId());
        map.addAttribute("maFitBillVO", maFitBillVO);
        map.addAttribute("storePreDeposit", storePreDeposit.getBalance());
        return "/views/decorativeCompany/fit_company_notOutBill_detail";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/historyList")
    public String toHistoryBillPage() {
        return "/views/decorativeCompany/fit_company_historyBill_page";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/historyDetail/{billNo}")
    public String HistoryDetailBill(@PathVariable String billNo, ModelMap map) {
        BillInfoDO maFitBillVO = maFitBillService.getFitBillByBillNo(billNo);
        map.addAttribute("maFitBillVO", maFitBillVO);
        return "/views/decorativeCompany/fit_company_historyDetailBill_detail";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/repayment")
    public String repaymentBill() {
        return "/views/decorativeCompany/fit_company_bill_repayment_page";
    }

}
