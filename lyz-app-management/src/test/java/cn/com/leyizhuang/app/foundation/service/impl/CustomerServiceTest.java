package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCoupon;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.CommonService;
import junit.framework.AssertionFailedError;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @Description: 顾客service 测试
 * @Author Richard
 * @Date 2018/6/6 13:48
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerServiceTest {

    @Resource
    private AppCustomerService customerService;

    @Resource
    private CommonService commonService;

    @Test
    public void findOverdueCustomerProductCouponTest(){

       List<CustomerProductCoupon> customerProductCouponList =  customerService.findOverdueCustomerProductCoupon();
        Assert.assertNotNull(customerProductCouponList);
    }

    @Test
    public void cusProductCouponTransferPreDepositTest () throws UnsupportedEncodingException {
        commonService.cusProductCouponTransferPreDeposit();
    }

}
