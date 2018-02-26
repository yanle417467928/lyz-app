package cn.com.leyizhuang.app.web.filter;

import cn.com.leyizhuang.app.core.utils.JwtUtils;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * token拦截器
 *
 * @author Richard
 * Created on 2017-09-19 17:07
 **/
public class JwtTokenFilter implements Filter {

    //需要排除的页面
    private String excludedPages;
    private String[] excludedPageArray;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());
        excludedPages = filterConfig.getInitParameter("excludedPages");
        if (StringUtils.isNotEmpty(excludedPages)) {
            excludedPageArray = excludedPages.split(",");
        }

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        boolean isExcludedPage = false;
        Pattern p = Pattern.compile("^(/app/resend)/*");

        //判断是否在过滤url之外
        for (String page : excludedPageArray) {
            if (((HttpServletRequest) request).getServletPath().equals(page) ||
                    p.matcher(((HttpServletRequest) request).getServletPath()).matches()) {
                isExcludedPage = true;
                break;
            }
        }
        if (isExcludedPage) {
            chain.doFilter(request, response);
        } else {
            ResultDTO<String> resultDTO;
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String auth = httpRequest.getHeader("Authorization");
            try {
                if (JwtUtils.parseJWT(auth) != null) {
                    chain.doFilter(request, response);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json; charset=utf-8");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ObjectMapper mapper = new ObjectMapper();
            resultDTO = new ResultDTO<>(1000, "TOKEN过期", null);
            httpResponse.getWriter().write(mapper.writeValueAsString(resultDTO));
            return;
        }

    }

    @Override
    public void destroy() {

    }
}
