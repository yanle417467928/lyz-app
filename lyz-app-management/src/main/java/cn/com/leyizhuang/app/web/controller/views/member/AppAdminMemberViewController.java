package cn.com.leyizhuang.app.web.controller.views.member;

import cn.com.leyizhuang.app.core.constant.AccountStatusEnum;
import cn.com.leyizhuang.app.core.constant.IdentityTypeEnum;
import cn.com.leyizhuang.app.core.constant.IdentityTypeEnum;
import cn.com.leyizhuang.app.core.constant.RegistryType;
import cn.com.leyizhuang.app.core.constant.SexEnum;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.service.AppAdminMemberService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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

    @Autowired
    private AppAdminMemberService memberService;

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
        PageInfo<MemberDO> memberDOPage = memberService.queryPage(page, size);
        map.addAttribute("memberDOPage", memberDOPage);
        return "/views/member/member_page";
    }

    @GetMapping(value = "/add")
    public String memberAddPage() {
        return "/views/member/member_add";
    }


    /**
     * 根据ID查看会员信息详情
      * @param map
     * @param id 会员ID
     * @return 返回会员详情显示页面
     */
    @RequestMapping(value = "/details/{id}")
    public String MemberDetails(ModelMap map,@PathVariable Long id){
        MemberDO member= memberService.queryById(id);
       /* String status = "";
        if(member.getAuth().getStatus()){
            status = "启用";
        }else {
            status = "停用";
        }
        String sex ="";*/
        /*if(member.getSex().equals(Sex.FEMALE)){
            sex = "女";
        }else if(member.getSex().equals(Sex.MALE)){
            sex = "男";
        }else {
            sex = "保密";
        }*/
       /* String identityType = "";
        if(member.getIdentityType().equals(IdentityTypeEnum.MEMBER)){
            identityType = "会员";
        }else{
            identityType = "零售";
        }*/
        /*String registryTime= new SimpleDateFormat("yyyy-MM-dd").format(member.getRegistryTime());
        String birthday = new SimpleDateFormat("yyyy-MM-dd").format(member.getBirthday());
        map.addAttribute("identityType",identityType);
        map.addAttribute("status",status);
        map.addAttribute("sex",sex);
        map.addAttribute("birthday",birthday);
        map.addAttribute("registryTime",registryTime);
        map.addAttribute("member", member);*/
        map.addAttribute("member",member);
        return "views/member/member_details";
    }

    /**
     * 根据ID查找会员信息
     * @param model
     * @param id 会员ID
     * @return 返回会员信息修改页面
     */
    @RequestMapping(value = "/select/{id}")
    public String selectUser(Model model, @PathVariable Long id){
        MemberDO member= memberService.queryById(id);


        String [] sellerArray = new String[]{"杨平","刘申芳","李秀琳","程静","刘洁"};
        String [] storeArray = new String []{"富之源","富之美","贝彩店","亿彩店","真彩店"};
        String [] cityArray = new String []{"郑州分公司","成都分公司","重庆分公司","贵州分公司","太原分公司"};
        List<String> sellerList = Arrays.asList(sellerArray);
        List<String> storeList = Arrays.asList(storeArray);
        List<String> cityList = Arrays.asList(cityArray);


        model.addAttribute("seller_list",sellerList);
        model.addAttribute("store_list",storeList);
        model.addAttribute("city_list",cityList);
        model.addAttribute("identityType_list", IdentityTypeEnum.values());
        model.addAttribute("sex_list", SexEnum.values());
        model.addAttribute("account_status_list", AccountStatusEnum.values());
        model.addAttribute("member",member);

        return "views/member/member_edit";
    }

    /**
     * 修改会员信息，并返回会员列表页面
     * @param id    会员ID
     * @param city  城市
     * @param store 归属门店
     * @param seller    专属导购
     * @param identityType  会员性质
     * @param name  会员姓名
     * @param birthday  会员生日
     * @param mobile    会员电话
     * @param email     会员邮箱
     * @param sex   会员性别
     * @param usable    账号状态
     * @return
     */
    //@PostMapping(value = "/update")
    /*public String updateUser(Long id,String city, String store, String seller, String identityType, String name, String birthday, String mobile, String email, String sex, Integer usable){
        MemberDO memberDO = memberService.queryById(id);
        memberDO.setCreatorInfoByManager(0L);
        memberDO.setName(name);
        memberDO.setCity(city);
        memberDO.setStore(new Store(0L,store,"daiding"));
        memberDO.setManager(new Manager(0L,seller,"110"));
        if (null != identityType) {
            if(identityType.equals(IdentityTypeEnum.MEMBER)){
                memberDO.setIdentityType(IdentityTypeEnum.MEMBER);
            }else if(identityType.equals((IdentityTypeEnum.RETAIL))){
                memberDO.setIdentityType(IdentityTypeEnum.RETAIL);
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
        *//*if(null != sex ){
            if ("男".equals(sex)){
                memberDO.setSex(Sex.MALE);
            }else if( "女".equals(sex)){
                memberDO.setSex(Sex.FEMALE);
            }else{
                memberDO.setSex(Sex.SECRET);
            }
        }*//*
        memberDO.setLastLoginTime(new Date());
        memberDO.setRegistryTime(new Date());
        memberDO.setRegistryType(RegistryType.MANAGER);
        if (null != identityType){
            if ("会员".equals(identityType)){
                memberDO.setIdentityType(IdentityTypeEnum.MEMBER);
            }else {
                memberDO.setIdentityType(IdentityTypeEnum.RETAIL);
            }
        }
        MemberLevelDO levelDO = new MemberLevelDO();
        levelDO.setId(0L);

        MemberRoleDO roleDO = new MemberRoleDO();
        roleDO.setId(0L);
        memberDO.setRole(roleDO);
        memberDO.setLevel(levelDO);

        MemberAuthDO memberAuthDO  = memberService.queryAuthById(id);
        if(null != email){
            memberAuthDO.setEmail(email);
        }
        if (null != mobile){
            memberAuthDO.setMobile(mobile);
        }
        if (null != usable){
            if (usable == 1){
                memberAuthDO.setStatus(true);
            }else{
                memberAuthDO.setStatus(false);
            }
        }
        memberService.updateUserAuth(memberAuthDO);
        memberService.update(memberDO);
        return "redirect:/views/admin/member/page";
    }
*/
}
