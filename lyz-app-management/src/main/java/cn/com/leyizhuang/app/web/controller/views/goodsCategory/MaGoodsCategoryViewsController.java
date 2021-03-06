package cn.com.leyizhuang.app.web.controller.views.goodsCategory;

import cn.com.leyizhuang.app.foundation.service.MaGoodsCategoryService;
import cn.com.leyizhuang.app.foundation.vo.management.goodscategory.GoodsCategoryVO;
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
@RequestMapping(value = MaGoodsCategoryViewsController.PRE_URL,produces = "application/json;charset=utf-8")
public class MaGoodsCategoryViewsController extends BaseController {

    protected final static String PRE_URL = "/views/admin/goodsCategorys";

    private static final Logger logger = LoggerFactory.getLogger(MaGoodsCategoryViewsController.class);

    @Autowired
    private MaGoodsCategoryService maGoodsCategoryService;

    /**
     * 跳转商品分类列表
     * @return
     */
    @GetMapping(value = "/list")
    public String goodsCategoryListPage() {
        return "/views/goodsCategory/goodsCategory_page";
    }

    /**
     * 跳转商品分类增加页面
     * @return
     */
    @GetMapping(value = "/add")
    public String addGoodsCategoryPage() {
        return "/views/goodsCategory/goodsCategory_add";
    }

    /**
     * 跳转商品编辑页面
     * @param model
     * @param goodsCategoryId
     * @return
     */
    @GetMapping(value = "/edit/{id}")
    public String goodsCategoryEditPage(Model model, @PathVariable(value = "id") Long goodsCategoryId) {
        if (!goodsCategoryId.equals(0L)) {
            GoodsCategoryVO goodsCategoryVO = maGoodsCategoryService.queryGoodsCategoryVOById(goodsCategoryId);
            if (null == goodsCategoryVO) {
                logger.warn("跳转修改资源页面失败，Resource(id = {}) == null", goodsCategoryId);
                error404();
                return "/error/404";
            } else {
                model.addAttribute("goodsCategoryVO",goodsCategoryVO);
            }
        }
        return "/views/goodsCategory/goodsCategory_edit";
    }

}
