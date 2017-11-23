package cn.com.leyizhuang.app.web.controller.views.companyUser;

import cn.com.leyizhuang.app.foundation.pojo.FitmentCompanyDO;
import cn.com.leyizhuang.app.foundation.pojo.FitmentCompanyUserDO;
import cn.com.leyizhuang.app.foundation.service.FitmentCompanyService;
import cn.com.leyizhuang.app.foundation.service.FitmentCompanyUserService;
import cn.com.leyizhuang.app.foundation.vo.FitmentCompanyUserVO;
import cn.com.leyizhuang.app.foundation.vo.FitmentCompanyVO;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/20
 */
@Controller
@RequestMapping(value = FitmentCompanyUserViewController.PRE_URL, produces = "application/json;charset=utf-8")
public class FitmentCompanyUserViewController extends BaseController {

    protected final static String PRE_URL = "/view/companyUser";
    private final Logger logger = LoggerFactory.getLogger(FitmentCompanyUserViewController.class);

    @Autowired
    private FitmentCompanyUserService fitmentCompanyUserServiceImpl;

    @Autowired
    private FitmentCompanyService fitmentCompanyServiceImpl;

    @GetMapping(value = "/page")
    public String companyUserPage(HttpServletRequest request, ModelMap map) {
        return "/views/companyUser/companyUser_page";
    }

    @GetMapping(value = "/edit/{id}")
    public String companyUserEdit(ModelMap map, @PathVariable(value = "id") Long id) {
        if (!id.equals(0L)) {
            FitmentCompanyUserDO fitmentCompanyUserDO = this.fitmentCompanyUserServiceImpl.queryById(id);
            if (null == fitmentCompanyUserDO) {
                logger.warn("跳转修改装饰员工页面失败，FitmentCompanyUserDO(id = {}) == null", id);
                error404();
                return "/error/404";
            } else {
                FitmentCompanyUserVO fitCompanyUserVO = FitmentCompanyUserVO.transform(fitmentCompanyUserDO);
                map.addAttribute("fitCompanyUserVO", fitCompanyUserVO);
            }
        }
        List<FitmentCompanyDO> fitmentCompanyDOList = this.fitmentCompanyServiceImpl.queryListByFrozen(false);
        List<FitmentCompanyVO> fitmentCompanyVOList = FitmentCompanyVO.transform(fitmentCompanyDOList);
        map.addAttribute("fitmentCompanyVOList", fitmentCompanyVOList);
        return "/views/companyUser/companyUser_edit";
    }
}
