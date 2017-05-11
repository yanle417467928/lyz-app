package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.vo.TableDataVO;
import cn.com.leyizhuang.app.foundation.service.AppAdminMemberService;
import com.github.pagehelper.PageInfo;
import cn.com.leyizhuang.app.foundation.pojo.MemberDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * App后台管理会员列表数据控制器
 *
 * @author Richard
 *         Created on 2017-05-09 10:32
 **/
@RestController
@RequestMapping(value = AppAdminMemberDataController.PRE_URL, produces = "application/json;charset=utf8")
public class AppAdminMemberDataController {
    protected final static String PRE_URL = "/rest/member";

    private final Logger LOG = LoggerFactory.getLogger(AppAdminMemberDataController.class);

    @Autowired
    private AppAdminMemberService memberService;

    @GetMapping(value = "/page/grid")
    public TableDataVO<MemberDO> dataMenuPageGridGet(Integer offset, Integer size, String keywords) {

        // 根据偏移量计算当前页数
        Integer page = (offset / size) + 1;
        PageInfo<MemberDO> memberDOPage = memberService.queryPage(page, size);
        return new TableDataVO<MemberDO>().transform(memberDOPage);
    }

}
