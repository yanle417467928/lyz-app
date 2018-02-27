package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.constant.CustomerPreDepositChangeType;
import cn.com.leyizhuang.app.core.constant.PreDepositWithdrawStatus;
import cn.com.leyizhuang.app.foundation.pojo.CashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.user.CusPreDepositWithdraw;
import cn.com.leyizhuang.app.foundation.service.AppPreDepositWithdrawService;
import cn.com.leyizhuang.app.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 后台 预存款提现控制器
 * Created by panjie on 2018/2/6.
 */
@RestController
@RequestMapping(value = MaPreDepositWithdrawRestController.PRE_URL,produces = "application/json;charset=utf-8")
public class MaPreDepositWithdrawRestController extends BaseRestController{

    protected final static String PRE_URL = "/rest/pre/deposit/withdraw";

    private final Logger logger = LoggerFactory.getLogger(MaPreDepositWithdrawRestController.class);

    @Resource
    private AppPreDepositWithdrawService appPreDepositWithdrawService;

    @GetMapping("/cus/grid")
    public GridDataVO<CusPreDepositWithdraw> CusGridData(Integer offset, Integer size, String keywords , String status){
        GridDataVO<CusPreDepositWithdraw> gridDataVO = new GridDataVO<>();
        Integer page = getPage(offset, size);

        PageInfo<CusPreDepositWithdraw> pageInfo = appPreDepositWithdrawService.getCusPageInfo(page,size,keywords, status);

        return gridDataVO.transform(pageInfo.getList(),pageInfo.getTotal());
    }


}
