package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.BillRuleService;
import cn.com.leyizhuang.app.foundation.vo.management.BillRuleVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingDetailsVO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GenerationRoad
 * @date 2018/7/14
 */
@RestController
@RequestMapping(value = MaDecorativeCompanyBillRuleRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaDecorativeCompanyBillRuleRestController extends BaseRestController{

    protected static final String PRE_URL = "/rest/decorationCompany/billRule";

    private final Logger logger = LoggerFactory.getLogger(MaDecorativeCompanyBillRuleRestController.class);

    @Autowired
    private BillRuleService billRuleService;

    @GetMapping(value = "/page/grid")
    public GridDataVO<BillRuleVO> restCreditOrderPageGird(Integer offset, Integer size, Long storeId) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<BillRuleVO> pageInfo = this.billRuleService.findAllBillRuleVO(storeId, page, size);
        return new GridDataVO<BillRuleVO>().transform(pageInfo.getList(), pageInfo.getTotal());
    }


}
