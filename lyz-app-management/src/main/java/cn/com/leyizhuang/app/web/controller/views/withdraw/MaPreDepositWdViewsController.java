package cn.com.leyizhuang.app.web.controller.views.withdraw;

import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 顾客预存款提现申请前端控制器
 * Created by panjie on 2018/2/27.
 */
@Controller
@RequestMapping(value = MaPreDepositWdViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaPreDepositWdViewsController extends BaseController {
    protected final static String PRE_URL = "/views/preDeposit/withdraw";

    private final Logger logger = LoggerFactory.getLogger(MaPreDepositWdViewsController.class);

    @GetMapping("/cus/page")
    public String cusListPage(){
        return "/views/customer/cus_pre_deposit_withdraw_page";
    }


}
