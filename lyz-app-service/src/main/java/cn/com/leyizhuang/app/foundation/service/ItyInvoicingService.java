package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.inventory.invoicing.InvoicingQuery;
import cn.com.leyizhuang.app.foundation.pojo.inventory.invoicing.InvoicingVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2018/1/15.
 * Time: 13:17.
 */

public interface ItyInvoicingService {

    /**
     * 进销存查询分页
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    PageInfo<InvoicingVO> queryPage(Integer offset, Integer size, String keywords, InvoicingQuery selectParam, List<Long> storeIds);
}
