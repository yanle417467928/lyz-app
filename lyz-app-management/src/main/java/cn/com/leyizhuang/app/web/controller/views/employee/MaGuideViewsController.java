package cn.com.leyizhuang.app.web.controller.views.employee;


import cn.com.leyizhuang.app.foundation.service.MaClearTempCreditService;
import cn.com.leyizhuang.app.foundation.service.MaEmployeeService;
import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideDetailVO;
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
    public String getGuideLineList() {
        return "/views/employee/guide_page";
    }

    /**
     *编辑员工额度页面
     * @param map
     * @param id
     * @return
     */
    @GetMapping(value = "/edit/{id}")
    public String creditEdit(ModelMap map, @PathVariable(value = "id") Long id) {
        if (!id.equals(0L)) {
            GuideDetailVO guideVO = this.maEmployeeService.queryGuideVOById(id);
            if (null == guideVO) {
                logger.warn("跳转修改资源页面失败，Resource(id = {}) == null", id);
                error404();
                return "/error/404";
            } else {
                map.addAttribute("guideVO",guideVO);
            }
        }
        return "/views/employee/guide_edit";
    }


    /**
     *额度改变明细页面
     * @param map
     * @param id
     * @return
     */
    @GetMapping(value = "/creditChangesList/{id}")
    public String empCreditMoneyChangesList (ModelMap map, @PathVariable(value = "id") Long id) {
        if (id.equals(0L)) {
            return "/error/404";
        } else {
            map.addAttribute("id",id);
        }
        return "/views/employee/guideCreditMoneyChanges_page";
    }


     @RequestMapping(value = "/clearTimeEdit")
     public String clearTime(ModelMap map) {
        String cron = maClearTempCreditService.getCron();
        map.addAttribute("cron",cron);
        return "/views/employee/guide_clearTimeEdit";
    }
}
