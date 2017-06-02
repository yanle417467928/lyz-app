package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.RegistryType;
import cn.com.leyizhuang.app.foundation.dao.AppAdminMemberDAO;
import cn.com.leyizhuang.app.foundation.pojo.Manager;
import cn.com.leyizhuang.app.foundation.pojo.MemberAuthDO;
import cn.com.leyizhuang.app.foundation.pojo.Store;
import cn.com.leyizhuang.app.foundation.pojo.dto.AppAdminMemberDTO;
import cn.com.leyizhuang.app.foundation.service.AppAdminMemberAuthService;
import cn.com.leyizhuang.app.foundation.service.AppAdminMemberService;
import cn.com.leyizhuang.common.core.exception.data.InvalidDataException;
import cn.com.leyizhuang.common.foundation.service.impl.BaseServiceImpl;
import cn.com.leyizhuang.common.util.CryptologyUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.com.leyizhuang.app.foundation.pojo.MemberDO;
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
@Transactional
public class AppAdminMemberServiceImpl extends BaseServiceImpl<MemberDO> implements AppAdminMemberService {

    private AppAdminMemberDAO memberDAO;

    @Autowired
    private AppAdminMemberAuthService memberAuthService;
    @Autowired
    public AppAdminMemberServiceImpl(AppAdminMemberDAO memberDAO) {
        super(memberDAO);
        this.memberDAO = memberDAO;
    }

    @Override
    public PageInfo<MemberDO> queryPage(Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<MemberDO> memberDOList = memberDAO.queryList();
        return new PageInfo<>(memberDOList);
    }

    @Override
    public void saveMemberInfo(AppAdminMemberDTO memberDTO) {
        MemberDO memberDO = new MemberDO();
        try {
           memberDO=transform(memberDTO);
        } catch (ParseException e) {
            throw new InvalidDataException("日期转换异常");
        }
        memberDO.setCreatorInfoByManager(0L);
        memberDO.setEffectiveOrderCount(0);
        memberDO.setEffectiveConsumption(0L);
        memberDO.setLastLoginTime(new Date());
        memberDO.setRegistryTime(new Date());
        memberDO.setRegistryType(RegistryType.MANAGER);
        memberDO.setHeadImageUri("/images/user2-160x160.jpg");
        MemberAuthDO memberAuthDO = memberDO.getAuth();
        memberAuthDO.setCreatorInfoByManager(0L);
        memberAuthDO.setUsername("保密");
        memberAuthDO.setPassword(CryptologyUtils.MD5Encrypt("123456"));

        //存储member表信息
        MemberDO returnMemberDO =  this.save(memberDO);
        //将member表返回的主键id设置到member_auth表member_id字段中
        memberAuthDO.setMemberId(returnMemberDO.getId());
        //存储member_auth表信息
        MemberAuthDO returnAuthDO = memberAuthService.save(memberDO.getAuth());
    }

    @Override
    public void modifyMemberInfo(AppAdminMemberDTO memberDTO) {
        if (null != memberDTO){
            MemberDO memberDO = new MemberDO();
            try {
                memberDO  = transform(memberDTO);
            } catch (ParseException e) {
                throw new InvalidDataException("日期转换异常");
            }
            memberDO.setModifierInfoByManager(0L);
            //this.save(memberDO);
            MemberAuthDO memberAuthDO = memberDO.getAuth();
            memberAuthDO.setModifierInfoByManager(0L);
            //更新member表信息
            this.modify(memberDO);
            //更新member_auth表信息
            MemberAuthDO oldMemberAuthDO = memberAuthService.queryByMemberId(memberDO.getId());
            memberAuthDO.setId(oldMemberAuthDO.getId());
            memberAuthService.modify(memberAuthDO);
        }
    }

    @Override
    public void updateUserAuth(MemberAuthDO memberAuthDO) {
        if(null != memberAuthDO) {
            memberDAO.updateUserAuth(memberAuthDO);
        }
    }

    @Override
    public MemberAuthDO queryAuthById(Long id) {
        if (null != id) {
         return memberDAO.queryAuthById(id);
        }
        return null;
    }



    public static MemberDO transform(AppAdminMemberDTO memberDTO) throws ParseException {
        MemberDO memberDO = new MemberDO();
        if(null != memberDTO.getId()){
            memberDO.setId(memberDTO.getId());
        }
        if(null != memberDTO.getCity()){
            memberDO.setCity(memberDTO.getCity());
        }
        if (null != memberDTO.getStore()){
            memberDO.setStore(new Store(0L,memberDTO.getStore(),"MD"));
        }
        if(null != memberDTO.getSeller()){
            memberDO.setManager(new Manager(0L,memberDTO.getSeller(),"12345678910"));
        }
        if(null != memberDTO.getIdentityType()){
            memberDO.setIdentityType(memberDTO.getIdentityType());
        }
        if(null != memberDTO.getName()){
            memberDO.setName(memberDTO.getName());
        }
        if(null != memberDTO.getBirthday()){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            memberDO.setBirthday(sdf.parse(memberDTO.getBirthday()));
        }
        if(null != memberDTO.getSex()){
            memberDO.setSex(memberDTO.getSex());
        }
        MemberAuthDO memberAuthDO = new MemberAuthDO();
        if(null != memberDTO.getMobile()){
            memberAuthDO.setMobile(memberDTO.getMobile());
        }
        if(null != memberDTO.getStatus()){
            memberAuthDO.setStatus(memberDTO.getStatus());
        }
        memberDO.setAuth(memberAuthDO);
        return memberDO;
    }
}
