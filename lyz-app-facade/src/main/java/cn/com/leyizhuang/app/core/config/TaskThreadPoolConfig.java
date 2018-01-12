package cn.com.leyizhuang.app.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Jerry.Ren
 * Notes:配置异步任务的线程池
 * 注意：该线程池被所有的异步任务共享，而不属于某一个异步任务
 * Created with IntelliJ IDEA.
 * Date: 2018/1/11.
 * Time: 14:46.
 */
@Slf4j
@Configuration
@EnableAsync
public class TaskThreadPoolConfig implements AsyncConfigurer {

    /**
     * 线程名字前缀
     */
    @Value("${task.executor.id}")
    private String threadNamePrefix;
    /**
     * 线程池维护线程的最少数量
     */
    @Value("${task.executor.core-pool-size}")
    private int corePoolSize;
    /**
     * 线程池维护线程的最大数量
     */
    @Value("${task.executor.max-pool-size}")
    private int maxPoolSize;
    /**
     * 允许的空闲时间
     */
    @Value("${task.executor.keep-alive}")
    private int keepAliveSeconds;
    /**
     * 缓存队列
     */
    @Value("${task.executor.queue-capacity}")
    private int queueCapacity;

    @Override
    public Executor getAsyncExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(threadNamePrefix);

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                log.info("Exception message - " + ex.getMessage());
                log.info("Method name - " + method.getName());
                for (Object param : params) {
                    log.info("Parameter value - " + param);
                }
            }
        };
    }
}
