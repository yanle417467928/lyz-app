package cn.com.leyizhuang.app.remote.queue;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Created on 2017-12-26 11:27
 **/
@RestController
@Slf4j
public class MqMessageTest {

    @Autowired
    private SinkSender sinkSender;

    @RequestMapping(value = "/send/message/order")
    public void testSendOrder(String mainOrderNumber) throws Exception {
        sinkSender.sendOrder(mainOrderNumber);
    }
}
