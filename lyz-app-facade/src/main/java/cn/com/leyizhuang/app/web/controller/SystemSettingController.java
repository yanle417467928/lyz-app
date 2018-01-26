package cn.com.leyizhuang.app.web.controller;

import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.foundation.service.SystemSettingService;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统设置相关控制器
 *
 * @author Richard
 * Created on 2018-01-26 17:52
 **/
@RestController
@RequestMapping(value = "/app/system/setting")
public class SystemSettingController {

    @Resource
    private SystemSettingService systemSettingService;

    @PostMapping(value = "/weChatLoginStatus")
    public ResultDTO<Object> isWeChatLoginAllowed() {
        String isWeChatLoginAllowedValue = systemSettingService.getSystemSettingValue("WE_CHAT_LOGIN");
        boolean isWeChatLoginAllowed;
        isWeChatLoginAllowed = null != isWeChatLoginAllowedValue && isWeChatLoginAllowedValue.equalsIgnoreCase(AppWhetherFlag.Y.toString());
        Map<String,Boolean> map = new HashMap<>();
        map.put("isWeChatLoginAllowed",isWeChatLoginAllowed);

        return new ResultDTO<>(0, null, map);
    }
}
