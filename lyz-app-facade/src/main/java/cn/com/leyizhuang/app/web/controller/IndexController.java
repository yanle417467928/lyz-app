package cn.com.leyizhuang.app.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ricahrd
 * Created on 2018-03-16 16:46
 **/
@RestController
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String helloApp() {
        return "hello App";
    }
}
