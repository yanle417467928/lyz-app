package cn.com.leyizhuang.app.core.getui;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Created on 2018-01-31 10:05
 **/
@RestController
public class GetuiTest {


    @RequestMapping(value = "/test/getui/logistic",method = RequestMethod.GET)
    public String testLogisticInfo() {
        NoticePushUtils.pushOrderLogisticInfo(1L, AppIdentityType.CUSTOMER, "CD_XN20180129145733063612");
        return "success";
    }

}
