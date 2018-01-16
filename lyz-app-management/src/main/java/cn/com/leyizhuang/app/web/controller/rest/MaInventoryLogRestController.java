package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.invoicing.InvoicingQuery;
import cn.com.leyizhuang.app.foundation.pojo.inventory.invoicing.InvoicingVO;
import cn.com.leyizhuang.app.foundation.service.ItyInvoicingService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jerry.Ren
 * Notes:进销存日志控制器
 * Created with IntelliJ IDEA.
 * Date: 2018/1/15.
 * Time: 11:39.
 */
@RestController
@RequestMapping(value = MaInventoryLogRestController.PRE_URL, produces = "application/json;charset=utf8")
public class MaInventoryLogRestController {

    protected final static String PRE_URL = "/rest/inventory/log";

    private final Logger logger = LoggerFactory.getLogger(MaInventoryLogRestController.class);

    @Autowired
    private ItyInvoicingService invoicingService;

    /**
     * 进销存页面分页
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<InvoicingVO> dataMenuPageGridGet(Integer offset, Integer size, String keywords, InvoicingQuery selectParam) {

        logger.info("dataMenuPageGridGet CREATE,进销存日志分页查询, 入参 offset:{},size:{},keywords:{},selectParam:{}", offset, size, keywords, selectParam);

        // 根据偏移量计算当前页数
        PageInfo<InvoicingVO> inventoryVOPageInfo = invoicingService.queryPage(offset, size, keywords, selectParam);
        return new GridDataVO<InvoicingVO>().transform(inventoryVOPageInfo.getList(), inventoryVOPageInfo.getTotal());
    }

}
