package cn.com.leyizhuang.app.core.config.shiro;

import cn.com.leyizhuang.app.foundation.pojo.management.User;
import cn.com.leyizhuang.app.foundation.service.RoleService;
import cn.com.leyizhuang.app.foundation.service.UserService;
import cn.com.leyizhuang.app.foundation.vo.UserVO;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * shiro 权限认证
 *
 * @author Richard
 * Created on 2017-07-28 11:17
 **/
public class ShiroDbRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    public ShiroDbRealm(CacheManager cacheManager, CredentialsMatcher matcher) {
        super(cacheManager, matcher);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        ShiroUser shiroUser = (ShiroUser) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(shiroUser.getRoles());
        info.addStringPermissions(shiroUser.getUrlSet());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("shiro 开始登录认证");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        UserVO userVO = new UserVO();
        userVO.setLoginName(token.getUsername());
        List<User> list = userService.selectByLoginName(userVO);
        //账号不存在
        if (list == null ||list.isEmpty()){
            return null;
        }
        User user = list.get(0);
        //账号未启用
        if (user.getStatus()==false){
            throw new DisabledAccountException("账号未启用");
        }
        //读取用户的角色和url
        Map<String,Set<String>> resourceMap = roleService.selectResourceMapByUserId(user.getUid());
        Set<String> urls = resourceMap.get("urls");
        Set<String> roles = resourceMap.get("roles");
        ShiroUser shiroUser = new ShiroUser(user.getUid(),user.getLoginName(),user.getName(),urls);
        shiroUser.setRoles(roles);
        return new SimpleAuthenticationInfo(shiroUser,user.getPassword(), ByteSource.Util.bytes(user.getCredentialsSalt()),getName());
    }


    /**
     * 清除用户缓存
     * @param shiroUser
     */
    public void removeUserCache(ShiroUser shiroUser){
        SimplePrincipalCollection principals = new SimplePrincipalCollection();
        principals.add(shiroUser, super.getName());
        super.clearCachedAuthorizationInfo(principals);
        super.clearCachedAuthenticationInfo(principals);
    }

    /**
     * 清除用户缓存
     * @param loginName
     */
    public void removeUserCache(String loginName){
        SimplePrincipalCollection principals = new SimplePrincipalCollection();
        principals.add(new ShiroUser(loginName), super.getName());
        super.clearCachedAuthorizationInfo(principals);
        super.clearCachedAuthenticationInfo(principals);
    }

}
