package cn.com.leyizhuang.app.core.aspect;

import com.gexin.fastjson.JSON;
import com.google.common.collect.Maps;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author Created on 2017-12-13 18:41
 **/
@Component
@Aspect
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(LogAspect.class);
    private String logStr = null;

    @Pointcut("execution(* cn.com.leyizhuang.app.web.controller.customer..*.*(..))")
    public void executeService() {

    }

    /**
     * 前置通知 切入方法调用前被调用
     *
     * @param joinPoint 连接点
     */
    @Before("executeService()")
    public void doBeforeAdvice(JoinPoint joinPoint) {
        //获取目标方法的参数信息
        Object[] args = joinPoint.getArgs();
        //AOP代理类的信息
        joinPoint.getThis();
        //代理的目标对象
        joinPoint.getTarget();
        //通知的签名
        Signature signature = joinPoint.getSignature();
        //代理的是哪一个方法
        System.out.println(signature.getName());
        //AOP代理类的类（class）信息
        signature.getDeclaringType();
        //获取请求参数
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //从获取到的RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        Enumeration<String> enumeration = request.getParameterNames();
        Map<String, String> parameterMap = Maps.newHashMap();
        while (enumeration.hasMoreElements()) {
            String parameter = enumeration.nextElement();
            parameterMap.put(parameter, request.getParameter(parameter));
        }
        String str = JSON.toJSONString(parameterMap);
        logStr = joinPoint.getTarget().getClass().getName() + "类的"
                + joinPoint.getSignature().getName() + "方法开始执行****** Start ******";
        logger.info(logStr);

        logger.info("方法参数: {}", str);
    }


    /**
     * 后置返回通知
     *
     * @param joinPoint 连接点
     * @param keys      返回值
     */
    @AfterReturning(value = "execution(* cn.com.leyizhuang.app.web.controller.customer..*.*(..))", returning = "keys")
    public void doAfterReturningAdvice1(JoinPoint joinPoint, Object keys) {
        logStr = joinPoint.getTarget().getClass().getName() + "类的"
                + joinPoint.getSignature().getName() + "方法返回值:" + keys;
        logger.info(logStr);
    }

    /**
     * 后置通知
     *
     * @param joinPoint 连接点
     */
    @After("executeService()")
    public void doAfter(JoinPoint joinPoint) {
        logStr = joinPoint.getTarget().getClass().getName() + "类的"
                + joinPoint.getSignature().getName() + "方法执行结束****** End ******";
        logger.info(logStr);
    }
}
