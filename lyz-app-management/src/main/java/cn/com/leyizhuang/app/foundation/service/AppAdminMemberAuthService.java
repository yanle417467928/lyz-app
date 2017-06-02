package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.MemberAuthDO;
import cn.com.leyizhuang.app.foundation.pojo.MemberDO;
import cn.com.leyizhuang.common.foundation.service.BaseService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

/**
 * App后台会员身份鉴权信息服务
 *
 * @author Richard
 *         Created on 2017-05-23 9:52
 **/
public interface AppAdminMemberAuthService extends BaseService<MemberAuthDO> {

    Boolean existsByMobile(String mobile);

    Boolean existsByMobileAndIdNot(String mobile, Long id);

    MemberAuthDO queryByMemberId(Long memberId);

    void modifyMemberPassword(Long id, String password);
}
