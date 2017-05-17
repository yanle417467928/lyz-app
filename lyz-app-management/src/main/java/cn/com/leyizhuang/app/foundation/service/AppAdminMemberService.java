package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.MemberAuthDO;
import cn.com.leyizhuang.common.foundation.service.BaseService;
import com.github.pagehelper.PageInfo;
import cn.com.leyizhuang.app.foundation.pojo.MemberDO;

/**
 * App后台管理会员服务
 *
 * @author Richard
 *         Created on 2017-05-09 9:52
 **/
public interface AppAdminMemberService extends BaseService<MemberDO> {

    PageInfo<MemberDO> queryPage(Integer page, Integer size);
    void update(MemberDO memberDO);
    void updateUserAuth(MemberAuthDO memberAuthDO);
    MemberAuthDO queryAuthById(Long id);
}
