package cn.com.leyizhuang.app.web.controller;

import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 猫酷测试接口
 *
 * @author Richard
 * Created on 2017-09-14 13:12
 **/
@RestController
public class RestControllerDemo {

    @GetMapping(value = "/maoku/success")
    public ResultDTO<String> maokuInvokeMeSuccesss(){
        return new ResultDTO<String>(0,null,"猫酷，你好");
    }

    @GetMapping(value = "/maoku/failure")
    public ResultDTO<String> maokuInvokeMeFailure(){
        return new ResultDTO<String>(-1,"接口调用失败",null);
    }

    @PostMapping("/app/user")
    public ResultDTO<String> maokuTokenTest(String name){
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,"成功",name);
    }
}
