package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.Member;
import cn.com.leyizhuang.app.foundation.pojo.MemberAuth;
import cn.com.leyizhuang.app.foundation.vo.AppAdminMemberVO;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;

import java.util.List;


/**
 * App后台管理会员服务Dao层
 *
 * @author Richard
 *         Created on 2017-05-09 10:11
 **/
public interface AppAdminMemberDAO  extends BaseDAO<Member>{
    Member modifyMember(Member memberDO);
    void updateUserAuth(MemberAuth memberAuth);
    MemberAuth queryAuthById(Long id);
    List<AppAdminMemberVO> queryMemberVOPage();

    AppAdminMemberVO queryMemberVOById(Long id);
}
