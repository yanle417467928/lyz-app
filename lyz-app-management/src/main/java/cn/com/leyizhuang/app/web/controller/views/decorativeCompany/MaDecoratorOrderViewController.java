package cn.com.leyizhuang.app.web.controller.views.decorativeCompany;

import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.GoodsPriceService;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.app.foundation.service.MaCityInventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * @Description: 装饰公司订单
 * @Author liuh
 * @Date 2018/5/13 14:24
 */
@Controller
@RequestMapping(value = "/views/admin/fit")
public class MaDecoratorOrderViewController {


    /**
     * 跳转装饰公司订单页面(导购支付)
     *
     * @return
     */
    @RequestMapping(value = "/fitOrder/list")
    public String fitOrderListPage() {
        return "/views/decorativeCompany/fitOrder_page";
    }

}
