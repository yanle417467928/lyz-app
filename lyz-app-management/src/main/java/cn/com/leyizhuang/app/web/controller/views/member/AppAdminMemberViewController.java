package cn.com.leyizhuang.app.web.controller.views.member;

import cn.com.leyizhuang.app.core.constant.IdentityType;
import cn.com.leyizhuang.app.foundation.pojo.Manager;
import cn.com.leyizhuang.app.foundation.pojo.Store;
import cn.com.leyizhuang.app.foundation.service.AppAdminMemberService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import com.github.pagehelper.PageInfo;
import cn.com.leyizhuang.app.foundation.pojo.MemberDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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

    @Autowired
    private AppAdminMemberService memberService;


    @GetMapping(value = "/page")
    public String memberList(HttpServletRequest request, ModelMap map, Integer page, Integer size,Long menuId) {
        page = null == page ? CommonGlobal.PAGEABLE_DEFAULT_PAGE : page;
        size = null == size ? CommonGlobal.PAGEABLE_DEFAULT_SIZE : size;
        PageInfo<MemberDO> memberDOPage = memberService.queryPage(page, size);
        map.addAttribute("memberDOPage", memberDOPage);
        return "/views/user/user_page";
    }

    @GetMapping(value = "/add")
    public String memberAddPage() {
        return "/views/user/user_add";
    }

    @PostMapping(value = "/add")
    public String memberAdd(String city, String store, String seller, String identityType, String name, String birthday, String mobile, String email, String sex, Integer status){
        MemberDO memberDO = new MemberDO();
        memberDO.setCity(city);
        memberDO.setStore(new Store(0L,store,"daiding"));
        memberDO.setManager(new Manager(0L,seller,"110"));
        if (null != identityType) {
            if(identityType.equals(IdentityType.MEMBER)){
                memberDO.setIdentityType(IdentityType.MEMBER);
            }else if(identityType.equals((IdentityType.RETAIL))){
                memberDO.setIdentityType(IdentityType.RETAIL);
            }
        }
        memberService.save(memberDO);
        System.out.println(name);
        return "redirect:page";
    }




}
