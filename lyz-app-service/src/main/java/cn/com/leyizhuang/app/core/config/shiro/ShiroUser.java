package cn.com.leyizhuang.app.core.config.shiro;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

/**
 * shiro 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息
 *
 * @author Richard
 * Created on 2017-07-28 11:17
 **/
@Getter
@Setter
public class ShiroUser implements Serializable {
    private static final long serialVersionUID = -1373760761780840081L;
    private final String loginName;
    private Long id;
    private String name;
    private Set<String> urlSet;
    private Set<String> roles;

    public ShiroUser(String loginName) {
        this.loginName = loginName;
    }

    public ShiroUser(Long id, String loginName, String name, Set<String> urlSet) {
        this.id = id;
        this.loginName = loginName;
        this.name = name;
        this.urlSet = urlSet;
    }


    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
        return loginName;
    }
}