package cn.com.leyizhuang.app.web.controller.views.member;

import cn.com.leyizhuang.app.core.constant.AccountStatus;
import cn.com.leyizhuang.app.core.constant.IdentityType;
import cn.com.leyizhuang.app.core.constant.SexType;
import cn.com.leyizhuang.app.foundation.pojo.SalesConsult;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.vo.AppAdminMemberVO;
import cn.com.leyizhuang.app.foundation.service.AppAdminMemberService;
import cn.com.leyizhuang.app.foundation.service.AppAdminSalesConsultService;
import cn.com.leyizhuang.app.foundation.service.IAppStoreService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * App后台管理会员控制器
 *
 * @author Richard
 *         Created on 2017-05-08 16:45
 **/
@Controller
@RequestMapping(value = AppAdminMemberViewController.PRE_URL,produces = "text/html;charset=utf-8")
public class AppAdminMemberViewController {
    protected final static String PRE_URL = "/views/admin/member";


    private AppAdminMemberService memberService;
    @Autowired
    public void setMemberService(AppAdminMemberService memberService) {
        this.memberService = memberService;
    }

    private IAppStoreService storeService;
    @Autowired
    public void setStoreService(IAppStoreService storeService) {
        this.storeService = storeService;
    }

    private AppAdminSalesConsultService salesConsultService;
    @Autowired
    public void setSalesConsultService(AppAdminSalesConsultService salesConsultService) {
        this.salesConsultService = salesConsultService;
    }

    /**
     * 后台会员列表展示
     * @param request
     * @param map
     * @param page
     * @param size
     * @param menuId
     * @return
     */
    @GetMapping(value = "/page")
    public String memberList(HttpServletRequest request, ModelMap map, Integer page, Integer size,Long menuId) {
        page = null == page ? CommonGlobal.PAGEABLE_DEFAULT_PAGE : page;
        size = null == size ? CommonGlobal.PAGEABLE_DEFAULT_SIZE : size;
        //PageInfo<Member> memberDOPage = memberService.queryPage(page, size);
        //map.addAttribute("memberDOPage", memberDOPage);
        return "/views/member/member_page";
    }

    @GetMapping(value = "/add")
    public String memberAddPage(Model model) {
        List<AppStore> appStoreList;
        appStoreList = storeService.findAll();
        List<SalesConsult> consultList;
        consultList = salesConsultService.findAll();
        model.addAttribute("store_list", appStoreList);
        model.addAttribute("sales_consult_list",consultList);
        model.addAttribute("identityType_list", IdentityType.values());
        model.addAttribute("sex_list", SexType.values());
        model.addAttribute("account_status_list", AccountStatus.values());
        return "/views/member/member_add";
    }

    /**
     * 根据ID查找会员信息
     * @param model
     * @param id 会员ID
     * @return 返回会员信息修改页面
     */
    @RequestMapping(value = "/edit/{id}")
    public String selectUser(Model model, @PathVariable Long id){
        AppAdminMemberVO memberVO= memberService.queryMemberVOById(id);
        List<AppStore> appStoreList = new ArrayList<>();
        appStoreList = storeService.findAll();
        List<SalesConsult> salesConsultList = new ArrayList<>();
        salesConsultList = salesConsultService.findAll();
        model.addAttribute("sales_consult_list",salesConsultList);
        model.addAttribute("store_list", appStoreList);
        model.addAttribute("identityType_list", IdentityType.values());
        model.addAttribute("sex_list", SexType.values());
        model.addAttribute("account_status_list", AccountStatus.values());
        model.addAttribute("member",memberVO);

        return "views/member/member_edit";
    }

}
