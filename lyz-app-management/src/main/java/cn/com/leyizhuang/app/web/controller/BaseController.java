package cn.com.leyizhuang.app.web.controller;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author CrazyApeDX
 *         Created on 2017/5/18.
 */
public abstract class BaseController {

    protected HttpServletRequest getRequest() {
        return getServletRequestAttributes().getRequest();
    }

    protected HttpServletResponse getResponse() {
        return getServletRequestAttributes().getResponse();
    }

    private ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    protected void error400() {
        getResponse().setStatus(400);
    }

    protected void error404() {
        getResponse().setStatus(404);
    }

    protected void error500() {
        getResponse().setStatus(500);
    }

    /**
     * 获取当前登录用户对象
     * @return {ShiroUser}
     */
    public ShiroUser getShiroUser() {
        return (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    }
}
