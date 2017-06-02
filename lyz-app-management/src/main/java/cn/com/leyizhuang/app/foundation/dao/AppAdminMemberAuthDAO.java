package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.MemberAuthDO;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import org.apache.ibatis.annotations.Param;

/**
 * MEMBER_ATUH表对应的Mapper
 *
 * @author Richard
 *         Created on 2017-05-23 17:30
 **/
public interface AppAdminMemberAuthDAO extends BaseDAO<MemberAuthDO> {
    Boolean existsByMobile(String mobile);

    Boolean existsByMobileAndIdNot(@Param("mobile") String mobile,@Param("id") Long id);

    MemberAuthDO queryByMemberId(Long memberId);

    void modifyMemberPassword(@Param("id") Long id, @Param("password") String password);
}
