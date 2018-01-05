package cn.com.leyizhuang.app.web.controller.views.goodsBrand;

import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsBrand;
import cn.com.leyizhuang.app.foundation.service.MaGoodsBrandService;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = MaGoodsBrandViewsController.PRE_URL,produces = "application/json;charset=utf-8")
public class MaGoodsBrandViewsController extends BaseController {

    protected final static String PRE_URL = "/views/admin/goodsBrands";

    private static final Logger logger = LoggerFactory.getLogger(MaGoodsBrandViewsController.class);

    @Autowired
    private MaGoodsBrandService maGoodsBrandService;

    /**
     * 跳转商品品牌列表
     * @return
     */
    @GetMapping(value = "/list")
    public String getGoodBrandList() {
        return "/views/goodsBrand/goodsBrand_page";
    }

    /**
     * 跳转商品品牌增加页面
     * @return
     */
     @GetMapping(value = "/add")
     public String addGoodBrand() {
        return "/views/goodsBrand/goodsBrand_add";
    }

    /**
     * 跳转商品品牌编辑页面
     * @param model
     * @param goodsBrandId
     * @return
     */
    @GetMapping(value = "/edit/{id}")
    public String goodsBrandEdit(Model model, @PathVariable(value = "id") Long goodsBrandId) {
        if (!goodsBrandId.equals(0L)) {
            GoodsBrand goodsBrand = maGoodsBrandService.queryGoodsBrandVOById(goodsBrandId);
            if (null == goodsBrand) {
                logger.warn("跳转修改资源页面失败，Resource(id = {}) == null", goodsBrandId);
                error404();
                return "/error/404";
            } else {
                model.addAttribute("goodsBrand",goodsBrand);
            }
        }
        return "/views/goodsBrand/goodsBrand_edit";
    }
}
