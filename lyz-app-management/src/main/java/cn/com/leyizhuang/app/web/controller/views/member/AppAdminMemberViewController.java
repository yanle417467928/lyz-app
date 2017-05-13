package cn.com.leyizhuang.app.web.controller.views.member;

import cn.com.leyizhuang.app.core.constant.IdentityType;
import cn.com.leyizhuang.app.core.constant.RegistryType;
import cn.com.leyizhuang.app.core.constant.Sex;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.service.AppAdminMemberService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        memberDO.setCreatorInfoByManager(0L);
        memberDO.setName(name);
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
        if (null != status){
            if (status == 1){
                memberDO.setAuth(new MemberAuthDO(null,null,null,mobile,email,Boolean.TRUE,null));
            }else{
                memberDO.setAuth(new MemberAuthDO(null,null,null,mobile,email,Boolean.FALSE,null));
            }
        }
        memberDO.setEffectiveOrderCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            memberDO.setBirthday(sdf.parse(birthday));
        } catch (ParseException e) {
            memberDO.setBirthday(new Date());
            e.printStackTrace();
        }
        memberDO.setEffectiveConsumption(0L);
        memberDO.setHeadImageUri("/images/user2-160x160.jpg");
        if(null != sex ){
            if ("男".equals(sex)){
                memberDO.setSex(Sex.MALE);
            }else if( "女".equals(sex)){
                memberDO.setSex(Sex.FEMALE);
            }else{
                memberDO.setSex(Sex.SECRET);
            }
        }
        memberDO.setLastLoginTime(new Date());
        memberDO.setRegistryTime(new Date());
        memberDO.setRegistryType(RegistryType.MANAGER);
        if (null != identityType){
            if ("会员".equals(identityType)){
                memberDO.setIdentityType(IdentityType.MEMBER);
            }else {
                memberDO.setIdentityType(IdentityType.RETAIL);
            }
        }
        MemberLevelDO levelDO = new MemberLevelDO();
        levelDO.setId(0L);

        MemberRoleDO roleDO = new MemberRoleDO();
        roleDO.setId(0L);
        memberDO.setRole(roleDO);
        memberDO.setLevel(levelDO);
        memberService.save(memberDO);
        System.out.println(name);
        return "redirect:page";
    }




}
