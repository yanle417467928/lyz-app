package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.MemberAuth;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * MEMBER_ATUH表对应的Mapper
 *
 * @author Richard
 *         Created on 2017-05-23 17:30
 **/
@Repository
public interface AppAdminMemberAuthDAO extends BaseDAO<MemberAuth> {
    Boolean existsByMobile(String mobile);

    Boolean existsByMobileAndIdNot(@Param("mobile") String mobile,@Param("id") Long id);

    MemberAuth queryByMemberId(Long memberId);

    void modifyMemberPassword(@Param("id") Long id, @Param("password") String password);
}
