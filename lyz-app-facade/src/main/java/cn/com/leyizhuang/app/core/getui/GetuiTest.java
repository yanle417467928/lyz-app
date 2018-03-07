package cn.com.leyizhuang.app.core.getui;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Created on 2018-01-31 10:05
 **/
@RestController
public class GetuiTest {


    @RequestMapping(value = "/test/getui/logistic", method = RequestMethod.GET)
    public String testLogisticInfo() {
        NoticePushUtils.pushOrderLogisticInfo("CD_XN20180202163424919683");
        return "success";
    }

    @RequestMapping(value = "/test/getui/arrearage", method = RequestMethod.GET)
    public String testArrearageInfo() {
        NoticePushUtils.pushApplyArrearageInfo(1L);
        return "success";
    }
}
