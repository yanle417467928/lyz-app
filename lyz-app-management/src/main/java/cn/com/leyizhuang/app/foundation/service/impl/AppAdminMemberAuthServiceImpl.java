package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppAdminMemberAuthDAO;
import cn.com.leyizhuang.app.foundation.pojo.MemberAuthDO;
import cn.com.leyizhuang.app.foundation.service.AppAdminMemberAuthService;
import cn.com.leyizhuang.common.foundation.service.impl.BaseServiceImpl;
import cn.com.leyizhuang.common.util.CryptologyUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * App后台会员身份鉴权服务实现类
 *
 * @author Richard
 *         Created on 2017-05-23 10:04
 **/
@Service
@Transactional
public class AppAdminMemberAuthServiceImpl extends BaseServiceImpl<MemberAuthDO> implements AppAdminMemberAuthService {

    private AppAdminMemberAuthDAO memberAuthDAO;
    @Autowired
    public AppAdminMemberAuthServiceImpl(AppAdminMemberAuthDAO memberAuthDAO) {
        super(memberAuthDAO);
        this.memberAuthDAO = memberAuthDAO;
    }

    @Override
    public Boolean existsByMobile(String mobile) {
        if(null == mobile){
            return true;
        }else{
           return memberAuthDAO.existsByMobile(mobile);
        }
    }

    @Override
    public Boolean existsByMobileAndIdNot(String mobile, Long id) {
        if(null == mobile || null ==id){
            return true;
        }
        return memberAuthDAO.existsByMobileAndIdNot(mobile,id);
    }

    @Override
    public MemberAuthDO queryByMemberId(Long memberId) {
        if(null != memberId){
            return memberAuthDAO.queryByMemberId(memberId);
        }
        return null;
    }

    @Override
    public void modifyMemberPassword(Long id,String password) {
        if(null != id || null != password){
            memberAuthDAO.modifyMemberPassword(id, CryptologyUtils.MD5Encrypt(password));
        }
    }
}
