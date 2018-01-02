package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.CashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.CashCouponService;
import cn.com.leyizhuang.app.foundation.vo.ActBaseVO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 现金券控制器
 * Created by panjie on 2018/1/2.
 */
@RestController
@RequestMapping(value = ManagementActivityRestController.PRE_URL,produces = "application/json;charset=utf-8")
public class ManagementCashCouponRestController extends  BaseRestController{

    protected final static String PRE_URL = "/rest/cashCoupon";

    private final Logger logger = LoggerFactory.getLogger(ManagementCashCouponRestController.class);

    @Resource
    private CashCouponService cashCouponService;

    @GetMapping("/grid")
    public GridDataVO<CashCoupon> gridData(Integer offset, Integer size, String keywords){
        GridDataVO<CashCoupon> gridDataVO = new GridDataVO<>();
        Integer page = getPage(offset, size);

        PageInfo<CashCoupon> pageInfo = cashCouponService.queryPage(page,size,keywords);

        return gridDataVO.transform(pageInfo.getList(),pageInfo.getTotal());
    }
}
