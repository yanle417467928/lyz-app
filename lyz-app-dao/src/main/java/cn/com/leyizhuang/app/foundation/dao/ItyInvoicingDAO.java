package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.inventory.invoicing.InvoicingQuery;
import cn.com.leyizhuang.app.foundation.pojo.inventory.invoicing.InvoicingVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2018/1/15.
 * Time: 13:20.
 */

@Repository
public interface ItyInvoicingDAO {

    /**
     * 获取城市库存变动日志
     *
     * @param keywords
     * @return
     */
    List<InvoicingVO> queryCityInventoryChangeLogList(@Param("keywords") String keywords);

    /**
     * 获取门店库存变动日志
     *
     * @param keywords
     * @return
     */
    List<InvoicingVO> queryStoreInventoryChangeLogList(@Param("keywords") String keywords);

    /**
     * 根据城市名称查询库存变动
     *
     * @param selectParam
     * @return
     */
    List<InvoicingVO> queryInvoicingCityBySelectParam(InvoicingQuery selectParam);

    /**
     * 根据门店名称查询库存变动
     *
     * @param selectParam
     * @return
     */
    List<InvoicingVO> queryInvoicingStoreBySelectParam(InvoicingQuery selectParam);
}
