package cn.com.leyizhuang.app.web.controller.login;

import cn.com.leyizhuang.app.core.config.shiro.ShiroDbRealm;
import cn.com.leyizhuang.app.core.utils.LoggerUtils;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.web.controller.BaseController;
import cn.com.leyizhuang.app.foundation.pojo.management.User;
import cn.com.leyizhuang.app.foundation.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;


@Controller
@Scope(value = "prototype")
@RequestMapping("/")
public class UserLoginController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(UserLoginController.class);

    protected Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

    @Autowired
    private UserService userService;

    @Autowired
    private ShiroDbRealm shiroDbRealm;

    /**
     * 登录跳转
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "/views/user/login";
    }

    /**
     * 注册跳转
     *
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public ModelAndView register() {

        return new ModelAndView("user/register");
    }
    /**
     * 注册 && 登录
     * @param vcode        验证码
     * @param entity    UUser实体
     * @return
     *//*
    @RequestMapping(value="subRegister",method= RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> subRegister(String vcode,UUser entity){
		resultMap.put("status", 400);
		if(!VerifyCodeUtils.verifyCode(vcode)){
			resultMap.put("message", "验证码不正确！");
			return resultMap;
		}
		String email =  entity.getEmail();
		
		UUser user = userService.findUserByEmail(email);
		if(null != user){
			resultMap.put("message", "帐号|Email已经存在！");
			return resultMap;
		}
		Date date = new Date();
		entity.setCreateTime(date);
		entity.setLastLoginTime(date);
		//把密码md5
		entity = UserManager.md5Pswd(entity);
		//设置有效
		entity.setStatus(UUser._1);
		
		entity = userService.insert(entity);
		LoggerUtils.fmtDebug(getClass(), "注册插入完毕！", JSONObject.fromObject(entity).toString());
		entity = TokenManager.login(entity, Boolean.TRUE);
		LoggerUtils.fmtDebug(getClass(), "注册后，登录完毕！", JSONObject.fromObject(entity).toString());
		resultMap.put("message", "注册成功！");
		resultMap.put("status", 200);
		return resultMap;
	}
	*/

    /**
     * 登录提交
     *
     * @param entity     登录的UUser
     * @param rememberMe 是否记住
     * @param request    request，用来取登录之前Url地址，用来登录后跳转到没有登录之前的页面。
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submitLogin(User entity, Boolean rememberMe, HttpServletRequest request) {
        logger.info("post请求登录,user:{}", entity.getLoginName());

        try {
            if (StringUtils.isBlank(entity.getLoginName())) {
                resultMap.put("status", 500);
                resultMap.put("message", "用户名不允许为空");
                return resultMap;
            }
            if (StringUtils.isBlank(entity.getPassword())) {
                resultMap.put("status", 500);
                resultMap.put("message", "密码不允许为空");
                return resultMap;
            }
            Subject user = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken();
            token.setUsername(entity.getLoginName());
            token.setPassword(entity.getPassword().toCharArray());
            // 设置记住密码
            token.setRememberMe(rememberMe);
            user.login(token);
            resultMap.put("status", 200);
            resultMap.put("message", "登录成功");

            //shiro 获取登录之前的地址
           /* SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            String url = null;
            if (null != savedRequest) {
                url = savedRequest.getRequestUrl();
            }*/
			/*
			  我们平常用的获取上一个请求的方式，在Session不一致的情况下是获取不到的
			  String url = (String) request.getAttribute(WebUtils.FORWARD_REQUEST_URI_ATTRIBUTE);
			 */
            String url = (String) request.getAttribute(WebUtils.FORWARD_REQUEST_URI_ATTRIBUTE);
            LoggerUtils.fmtDebug(getClass(), "获取登录之前的URL:[%s]", url);
            //如果登录之前没有地址，那么就跳转到首页。
            if (StringUtils.isBlank(url) || "/sitemash/template".equals(url)) {
                url = request.getContextPath() + "/index";
            }
            //跳转地址
            resultMap.put("back_url", url);
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            resultMap.put("status", 500);
            resultMap.put("message", "账号不存在");
        } catch (DisabledAccountException e) {
            e.printStackTrace();
            resultMap.put("status", 500);
            resultMap.put("message", "账号未启用");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            resultMap.put("status", 500);
            resultMap.put("message", "密码错误");
        }
        return resultMap;
    }

    /**
     * 退出
     * @return
     *//*
	@RequestMapping(value="logout",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> logout(){
		try {
			TokenManager.logout();
			resultMap.put("status", 200);
		} catch (Exception e) {
			resultMap.put("status", 500);
			logger.error("errorMessage:" + e.getMessage());
			LoggerUtils.fmtError(getClass(), e, "退出出现错误，%s。", e.getMessage());
		}
		return resultMap;
	}*/

    /**
     * 退出
     *
     * @return {Result}
     */
    @GetMapping("/logout")
    public String logout() {
        logger.info("登出系统,user:{}",getShiroUser().getLoginName());
        shiroDbRealm.removeUserCache(getShiroUser());
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "views/index";
    }
}
