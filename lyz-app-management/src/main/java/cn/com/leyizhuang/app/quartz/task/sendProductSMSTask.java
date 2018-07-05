package cn.com.leyizhuang.app.quartz.task;


import cn.com.leyizhuang.app.core.utils.ApplicationContextUtil;
import cn.com.leyizhuang.app.foundation.service.MaEmpCreditMoneyService;
import cn.com.leyizhuang.app.foundation.service.ProductCouponService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class sendProductSMSTask implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(new Date() + ":执行发送产品劵即将过期短信功能");
        ProductCouponService productCouponService = (ProductCouponService) ApplicationContextUtil.getBean("productCouponService");
        productCouponService.sendMsgForExpiringSoonProductCoupon();
        System.out.println(new Date() + ": 执行发送产品劵即将过期短信功能成功");
    }
}
