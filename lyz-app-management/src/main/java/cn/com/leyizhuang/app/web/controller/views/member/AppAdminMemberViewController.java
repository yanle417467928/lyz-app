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

}
