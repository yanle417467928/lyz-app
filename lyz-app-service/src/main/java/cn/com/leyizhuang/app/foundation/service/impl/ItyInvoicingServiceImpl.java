package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.ItyInvoicingDAO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.invoicing.InvoicingQuery;
import cn.com.leyizhuang.app.foundation.pojo.inventory.invoicing.InvoicingVO;
import cn.com.leyizhuang.app.foundation.service.ItyInvoicingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2018/1/15.
 * Time: 13:19.
 */

@Service
public class ItyInvoicingServiceImpl implements ItyInvoicingService {

    @Resource
    private ItyInvoicingDAO invoicingDAO;

    @Override
    public PageInfo<InvoicingVO> queryPage(Integer offset, Integer size, String keywords, InvoicingQuery selectParam) {

        List<InvoicingVO> invoicingCityVOS;
        if (StringUtils.isNotBlank(keywords)) {
            PageHelper.startPage(offset, size);
            invoicingCityVOS = invoicingDAO.queryInventoryChangeLogList(keywords);
        } else {
            PageHelper.startPage(offset, size);
            invoicingCityVOS = invoicingDAO.queryInvoicingBySelectParam(selectParam);
        }
        invoicingCityVOS.sort((o1, o2) -> o2.getChangeDate().compareTo(o1.getChangeDate()));
        return new PageInfo<>(invoicingCityVOS);
    }
}
