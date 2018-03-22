package cn.com.leyizhuang.app.web.controller.views.employee;


import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditMoney;
import cn.com.leyizhuang.app.foundation.service.MaClearTempCreditService;
import cn.com.leyizhuang.app.foundation.service.MaEmployeeService;
import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideVO;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.Date;


@Controller
@RequestMapping(value = MaGuideViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaGuideViewsController extends BaseController {

    protected final static String PRE_URL = "/views/admin/guide";

    private final Logger logger = LoggerFactory.getLogger(MaGuideViewsController.class);

    @Autowired
    private MaEmployeeService maEmployeeService;

    @Autowired
    private MaClearTempCreditService maClearTempCreditService;

    @RequestMapping(value = "/list")
    /**
     * 跳转导购列表页面
     */
    public String guideLineListPage() {
        return "/views/employee/guide_page";
    }

    /**
     * 编辑员工额度页面
     *
     * @param map
     * @param id
     * @return
     */
    @GetMapping(value = "/edit/{id}")
    public String creditEditPage(ModelMap map, @PathVariable(value = "id") Long id) {
        if (!id.equals(0L)) {
            GuideVO guideVO = this.maEmployeeService.queryGuideVOById(id);
            if (null ==guideVO.getGuideCreditMoney()){
                GuideCreditMoney guideCreditMoney = new GuideCreditMoney();
                guideCreditMoney.setCreditLimitAvailable(BigDecimal.ZERO);
                guideCreditMoney.setTempCreditLimit(BigDecimal.ZERO);
                guideCreditMoney.setCreditLimit(BigDecimal.ZERO);
                guideCreditMoney.setLastUpdateTime(new Date());
                guideVO.setGuideCreditMoney(guideCreditMoney);
            }
            map.addAttribute("guideVO", guideVO);
        }
        return "/views/employee/guide_edit";
    }


    /**
     * 额度改变明细页面
     *
     * @param map
     * @param id
     * @return
     */
    @GetMapping(value = "/creditChangesList/{id}")
    public String empCreditMoneyChangesListPage(ModelMap map, @PathVariable(value = "id") Long id) {
        if (id.equals(0L)) {
            return "/error/404";
        } else {
            map.addAttribute("guideId", id);
        }
        return "/views/employee/guideCreditMoneyChanges_page";
    }

    /**
     * 跳转导购清零额度设置页面
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/clearTimeEdit")
    public String clearTimePage(ModelMap map) {
        String cron = maClearTempCreditService.getCron((long) 1);
        map.addAttribute("cron", cron);
        return "/views/employee/guideClearTime_edit";
    }


    /**
     * 跳转导购欠款审核页面
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/arrears/list")
    public String guideArrearsPage() {
        return "/views/employee/guideArrears_page";
    }

    /**
     * 跳转导购欠款还款页面
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/repayment/list")
    public String guideRepaymentPage() {
        return "/views/employee/guideRepayment_page";
    }
}

