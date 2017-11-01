package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.GoodsPriceService;
import cn.com.leyizhuang.app.foundation.vo.GoodsPriceVO;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/27
 */
@RestController
@RequestMapping(value = GoodsPriceRestController.PRE_URL,produces = "application/json;charset=utf-8")
public class GoodsPriceRestController extends BaseRestController {
    protected final static String PRE_URL = "/rest/goodsPrice";

    private final Logger logger = LoggerFactory.getLogger(GoodsPriceRestController.class);

    @Autowired
    private GoodsPriceService goodsPriceServiceImpl;

    @Autowired
    private AppEmployeeService appEmployeeServiceImpl;

    @GetMapping(value = "/page/grid")
    public GridDataVO<GoodsPriceVO> restGoodsPricePageGird(Integer offset, Integer size, String keywords){
        size = getSize(size);
        Integer page = getPage(offset, size);
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
//改
        AppEmployee appEmployee = this.appEmployeeServiceImpl.findByUserId(user.getId());

        PageInfo<GoodsPriceVO> goodsPricePageInfo = null;
        if (null != appEmployee && null != appEmployee.getStoreId()){
            goodsPricePageInfo = this.goodsPriceServiceImpl.queryPage(page, size, appEmployee.getStoreId(), keywords);
        }
        return new GridDataVO<GoodsPriceVO>().transform(goodsPricePageInfo.getList(),goodsPricePageInfo.getTotal());
    }

}
