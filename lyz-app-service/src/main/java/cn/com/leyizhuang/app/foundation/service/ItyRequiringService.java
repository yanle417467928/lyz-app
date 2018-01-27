package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.inventory.requiring.Requiring;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrder;
import com.github.pagehelper.PageInfo;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2018/1/16.
 * Time: 18:29.
 */

public interface ItyRequiringService {

    /**
     * 分页
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    PageInfo<AtwRequisitionOrder> queryPage(Integer offset, Integer size, String keywords);

    /**
     * 根据ID查看详情
     *
     * @param id
     * @return
     */
    Requiring queryRequiringById(Long id);

    /**
     * 修改管理员备注信息
     *
     * @param atwRequisitionOrder
     * @return
     */
    int updateManagerRemarkInfo(AtwRequisitionOrder atwRequisitionOrder);
}
