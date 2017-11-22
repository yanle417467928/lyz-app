package cn.com.leyizhuang.app.web.controller.views.goods;

import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.app.foundation.vo.GoodsVO;
import cn.com.leyizhuang.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author GenerationRoad
 * @date 2017/9/6
 */
@Controller
@RequestMapping(value = GoodsViewController.PRE_URL,produces = "application/json;charset=utf-8")
public class GoodsViewController extends BaseController{
    protected final static String PRE_URL = "/view/goods";
    private final Logger logger = LoggerFactory.getLogger(GoodsViewController.class);

    @Autowired
    private GoodsService goodsService;

    /**
     * @title 去商品信息列表页面
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/9/9
     */
    @GetMapping(value = "/page")
    public String storePage(HttpServletRequest request, ModelMap map) {
        return "/views/goods/goods_page";
    }

    /**
     * @title   去编辑商品信息页面
     * @descripe
     * @param id
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/9/9
     */
    @GetMapping(value = "/edit/{id}")
    public String goodsEdit(ModelMap map, @PathVariable(value = "id") Long id) {
        if (!id.equals(0L)) {
            GoodsDO goodsDO = this.goodsService.queryById(id);
            if (null == goodsDO) {
                logger.warn("跳转修改资源页面失败，Resource(id = {}) == null", id);
                error404();
                return "/error/404";
            } else {
                GoodsVO goodsVO = GoodsVO.transform(goodsDO);
                map.addAttribute("goodsVO",goodsVO);
            }
        }
        return "/views/goods/goods_edit";
    }
}
