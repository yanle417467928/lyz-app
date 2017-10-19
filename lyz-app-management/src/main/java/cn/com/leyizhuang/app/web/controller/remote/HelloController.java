package cn.com.leyizhuang.app.web.controller.remote;

import cn.com.leyizhuang.app.foundation.pojo.management.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

/**
 * 服务提供方
 *
 * @author Richard
 * Created on 2017-09-06 17:26
 **/
@RestController
@RequestMapping(value = "/remote")
public class HelloController {

    private final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private DiscoveryClient client;

    @RequestMapping(value = "/hello",method= RequestMethod.GET)
    public String index(){
        ServiceInstance instance = client.getLocalServiceInstance();
        logger.info("/hello,host:"+instance.getHost()+",service_id:"+instance.getServiceId() );
        return "hello world";
    }

    @PostMapping(value = "/user/{name}")
    public User addUser(@PathVariable(value = "name") String name){
        User user = new User();
        user.setLoginName(name);
        user.setLoginName(name);
        user.setUid(1L);
        return user;
    }
}
