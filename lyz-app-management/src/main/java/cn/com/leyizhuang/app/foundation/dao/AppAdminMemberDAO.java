package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.MemberAuthDO;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import cn.com.leyizhuang.app.foundation.pojo.MemberDO;

/**
 * App后台管理会员服务Dao层
 *
 * @author Richard
 *         Created on 2017-05-09 10:11
 **/
public interface AppAdminMemberDAO  extends BaseDAO<MemberDO>{
    void update(MemberDO memberDO);
    void updateUserAuth(MemberAuthDO memberAuthDO);
    MemberAuthDO queryAuthById(Long id);
}
