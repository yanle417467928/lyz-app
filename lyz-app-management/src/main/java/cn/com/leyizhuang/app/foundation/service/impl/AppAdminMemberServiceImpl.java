package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppAdminMemberDAO;
import cn.com.leyizhuang.app.foundation.pojo.MemberAuthDO;
import cn.com.leyizhuang.app.foundation.service.AppAdminMemberService;
import cn.com.leyizhuang.common.foundation.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.com.leyizhuang.app.foundation.pojo.MemberDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void update(MemberDO memberDO) {
        if (null != memberDO){
            memberDAO.update(memberDO);
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
}
