package cn.com.leyizhuang.app.web.controller.views.decorativeCompany;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.utils.ExcelImportUtil;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.GoodsPriceService;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.app.foundation.service.MaCityInventoryService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.AssertUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 装饰公司后台下单
 * @Author Richard
 * @Date 2018/4/1314:24
 */
@Controller
@RequestMapping(value = "/views/admin/fit/place/order")
public class MaDecoratorPlaceOrderViewController {

    @Resource
    private AppStoreService storeService;

    @Resource
    private MaCityInventoryService cityInventoryService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private GoodsPriceService goodsPriceService;

    @RequestMapping(method = RequestMethod.GET, value = "/page")
    public String toOrderPage() {
        return "/views/company/fit_company_place_order";
    }


}
