package cn.com.leyizhuang.app.web.controller.qrcode;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.web.controller.BaseController;
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
public class QrcodeRegisterController extends BaseController {

    protected final static String PRE_URL = "/qrcode/register";

    private final Logger logger = LoggerFactory.getLogger(QrcodeRegisterController.class);

    @Autowired
    private AppEmployeeService employeeService;

    @Autowired
    private AppStoreService appStoreService;


    @RequestMapping(value = "/{empId}", method = RequestMethod.GET, produces = "text/html;charset=utf-8")
    public String qrcodeRegister(@PathVariable String empId, ModelMap map){
        logger.info("扫码注册,导购id："+empId);
        map.addAttribute("empId",empId);

        AppEmployee appEmployee = employeeService.findById(Long.valueOf(empId));

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

        // 排除分销仓库
        AppStore appStore = appStoreService.findById(appEmployee.getEmpId());

        if (appStore.getStoreType().equals(StoreType.FXCK)){
            map.addAttribute("message", "分销仓库员工下,不允许注册");
            return "/views/qrcode/failed";
        }

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

