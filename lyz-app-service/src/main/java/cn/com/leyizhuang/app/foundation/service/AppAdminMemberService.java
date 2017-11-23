package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.Member;
import cn.com.leyizhuang.app.foundation.pojo.MemberAuth;
import cn.com.leyizhuang.app.foundation.vo.AppAdminMemberVO;
import cn.com.leyizhuang.common.foundation.service.BaseService;
import com.github.pagehelper.PageInfo;

/**
 * App后台管理会员服务
 *
 * @author Richard
 * Created on 2017-05-09 9:52
 **/
public interface AppAdminMemberService extends BaseService<Member> {

    PageInfo<AppAdminMemberVO> queryMemberVOPage(Integer page, Integer size);

    void modifyMemberInfo(AppAdminMemberVO memberVO);

    void updateUserAuth(MemberAuth memberAuth);

    MemberAuth queryAuthById(Long id);

    void saveMemberInfo(AppAdminMemberVO memberVO);

    AppAdminMemberVO queryMemberVOById(Long id);
}
