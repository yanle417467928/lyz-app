package cn.com.leyizhuang.app.web.controller.views.qrcode;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 扫码注册 控制类
 * Created by panjie on 2018/2/22.
 */
@Controller
@RequestMapping(value = QrcodeRegisterController.PRE_URL)
public class QrcodeRegisterController {

    protected final static String PRE_URL = "/qrcode/register";

    private final Logger logger = LoggerFactory.getLogger(QrcodeRegisterController.class);

    @Autowired
    private AppEmployeeService employeeService;

    @Autowired
    private AppStoreService appStoreService;

    @RequestMapping(value = "/{phone}", method = RequestMethod.GET, produces = "text/html;charset=utf-8")
    public String qrcodeRegister(@PathVariable String phone, ModelMap map){
        logger.info("扫码注册,手机号："+phone);
        map.addAttribute("phone",phone);

        AppEmployee appEmployee = employeeService.findByMobile(phone);

        if (appEmployee == null){
            map.addAttribute("message", "读取推荐人信息失败");
            return "/views/qrcode/failed";
        }else{

            if (!appEmployee.getIdentityType().equals(AppIdentityType.SELLER)){
                map.addAttribute("message", "读取推荐人信息失败");
                return "/views/qrcode/failed";
            }

            AppStore appStore = appStoreService.findById(appEmployee.getStoreId());
            map.addAttribute("storeName",appStore.getStoreName());
            map.addAttribute("employee",appEmployee);

        }

        // TODO 排除分销门店


        return "/views/qrcode/register";
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET, produces = "text/html;charset=utf-8")
    public String success(){

        return "/views/qrcode/success";
    }

    @RequestMapping(value = "/failed", method = RequestMethod.GET, produces = "text/html;charset=utf-8")
    public String failed(){

        return "/views/qrcode/failed";
    }


}

