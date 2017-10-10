package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppAdminMemberDAO;
import cn.com.leyizhuang.app.core.constant.RegistrationType;
import cn.com.leyizhuang.app.foundation.pojo.Member;
import cn.com.leyizhuang.app.foundation.pojo.MemberAuth;
import cn.com.leyizhuang.app.foundation.vo.AppAdminMemberVO;
import cn.com.leyizhuang.app.foundation.service.AppAdminMemberAuthService;
import cn.com.leyizhuang.app.foundation.service.AppAdminMemberService;
import cn.com.leyizhuang.common.core.exception.data.InvalidDataException;
import cn.com.leyizhuang.common.foundation.service.impl.BaseServiceImpl;
import cn.com.leyizhuang.common.util.CryptologyUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * App后台管理会员服务实现类
 *
 * @author Richard
 *         Created on 2017-05-09 10:04
 **/
@Service
public class AppAdminMemberServiceImpl extends BaseServiceImpl<Member> implements AppAdminMemberService {

    @Autowired
    private AppAdminMemberDAO memberDAO;

    @Autowired
    private AppAdminMemberAuthService memberAuthService;

    public AppAdminMemberServiceImpl(AppAdminMemberDAO memberDAO) {
        super(memberDAO);
        this.memberDAO = memberDAO;
    }

    @Override
    public PageInfo<AppAdminMemberVO> queryMemberVOPage(Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<AppAdminMemberVO> memberList = memberDAO.queryMemberVOPage();
        return new PageInfo<>(memberList);
    }

    public void modifyMemberInfo(AppAdminMemberVO memberVO) {
        if (null != memberVO){
            Member member = new Member();
            try {
                member  = transformMemberVoToMember(memberVO);
            } catch (ParseException e) {
                throw new InvalidDataException("日期转换异常");
            }
            member.setModifierInfoByManager(0L);
            MemberAuth memberAuth = new MemberAuth();
            memberAuth.setModifierInfoByManager(0L);
            MemberAuth oldMemberAuth = memberAuthService.queryByMemberId(member.getId());
            memberAuth.setId(oldMemberAuth.getId());
            memberAuth.setStatus(memberVO.getStatus());
            memberAuth.setMobile(memberVO.getMobile());
            //更新member表信息
            this.modify(member);
            //更新member_auth表信息
            memberAuthService.modify(memberAuth);
        }
    }

    @Override
    public void updateUserAuth(MemberAuth memberAuth) {
        if(null != memberAuth) {
            memberDAO.updateUserAuth(memberAuth);
        }
    }

    @Override
    public MemberAuth queryAuthById(Long id) {
        if (null != id) {
         return memberDAO.queryAuthById(id);
        }
        return null;
    }

    @Transactional
    public void saveMemberInfo(AppAdminMemberVO memberVO) {
        Member member = new Member();
        try {
            member=transformMemberVoToMember(memberVO);
        } catch (ParseException e) {
            throw new InvalidDataException("日期转换异常");
        }
        member.setCreatorInfoByManager(0L);
        member.setEffectiveOrderCount(0);
        member.setEffectiveConsumption(0L);
        member.setLastLoginTime(new Date());
        member.setRegistrationTime(new Date());
        member.setRegistrationType(RegistrationType.MANAGER);
        member.setHeadImageUri("/images/user2-160x160.jpg");
        MemberAuth memberAuth = new MemberAuth();
        memberAuth.setCreatorInfoByManager(0L);
        memberAuth.setUsername("保密");
        memberAuth.setPassword(CryptologyUtils.MD5Encrypt("123456"));
        memberAuth.setMobile(memberVO.getMobile());
        memberAuth.setStatus(memberVO.getStatus());
        //存储member表信息
        Member returnMemberDO =  this.save(member);
        //将member表返回的主键id设置到member_auth表member_id字段中
        memberAuth.setMemberId(returnMemberDO.getId());
        //存储member_auth表信息
        MemberAuth returnAuth = memberAuthService.save(memberAuth);
    }

    @Override
    public AppAdminMemberVO queryMemberVOById(Long id) {
        if (null != id){
            return memberDAO.queryMemberVOById(id);
        }
        return null;
    }


   /* public static Member transform(AppAdminMemberVO memberVO) throws ParseException {
        Member member = new Member();
        if(null != memberVO.getId()){
            member.setId(memberVO.getId());
        }
       if (null != memberVO.getStore()){
           member.setStoreId(memberVO.getStore());
        }
        if(null != memberVO.getSalesConsult()){
            member.setConsultId(memberVO.getSalesConsult());
        }
        if(null != memberVO.getIdentityType()){
            member.setIdentityType(memberVO.getIdentityType());
        }
        if(null != memberVO.getName()){
            member.setMemberName(memberVO.getName());
        }
        if(null != memberVO.getBirthday()){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            member.setBirthday(sdf.parse(memberVO.getBirthday()));
        }
        if(null != memberVO.getSex()){
            member.setSex(memberDTO.getSex());
        }
        MemberAuth memberAuth = new MemberAuth();
        if(null != memberDTO.getMobile()){
            memberAuth.setMobile(memberDTO.getMobile());
        }
        if(null != memberDTO.getStatus()){
            memberAuth.setStatus(memberDTO.getStatus());
        }
        //memberDO.setAuth(memberAuthDO);
        return memberDO;
    }*/

    public static Member transformMemberVoToMember(AppAdminMemberVO memberVO) throws ParseException {
        Member member = new Member();
        if(null != memberVO.getId()){
            member.setId(memberVO.getId());
        }
        if (null != memberVO.getStore()){
            member.setStoreId(memberVO.getStore());
        }
        if(null != memberVO.getSalesConsult()){
            member.setConsultId(memberVO.getSalesConsult());
        }
        if(null != memberVO.getIdentityType()){
            member.setIdentityType(memberVO.getIdentityType());
        }
        if(null != memberVO.getName()){
            member.setMemberName(memberVO.getName());
        }
        if(null != memberVO.getBirthday()){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            member.setBirthday(sdf.parse(memberVO.getBirthday()));
        }
        if(null != memberVO.getSex()){
            member.setSex(memberVO.getSex());
        }
        MemberAuth memberAuth = new MemberAuth();
        if(null != memberVO.getMobile()){
            memberAuth.setMobile(memberVO.getMobile());
        }
        if(null != memberVO.getStatus()){
            memberAuth.setStatus(memberVO.getStatus());
        }
        return member;
    }
}
