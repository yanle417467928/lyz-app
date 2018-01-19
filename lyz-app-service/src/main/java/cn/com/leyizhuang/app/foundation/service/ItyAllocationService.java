package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AllocationTypeEnum;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.Allocation;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationDetail;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationQuery;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationVO;
import com.github.pagehelper.PageInfo;
import org.springframework.ui.Model;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2018/1/3.
 * Time: 13:57.
 */

public interface ItyAllocationService {

    Allocation insert(Allocation allocation, String operaterdBy);

    void update(Allocation allocation);

    /**
     * 查询调拨单
     *
     * @param id 调拨单id
     * @return
     */
    Allocation queryAllocationById(Long id);

    Allocation get(Long id);

    /**
     * 分页查询
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    PageInfo<AllocationVO> queryPage(Integer offset, Integer size, String keywords, AllocationQuery query);

    void cancel(Allocation allocation, String username);

    void sent(Allocation allocation, List<AllocationDetail> details, String username);

    void receive(Allocation allocation, String username);

    void resendAllAllocation();

    /**
     * 新增调拨单
     * @param allocation
     * @param goodsDetails
     * @param shiroUser
     */
    void addAllocation(Allocation allocation, List<AllocationDetail> goodsDetails, ShiroUser shiroUser);

    Model queryAllocationDetail(Long id, Model model);

    /**
     * 调拨单状态
     * @param allocationId
     * @param status
     */
    void chagneAllocationStatus(Long allocationId , AllocationTypeEnum status);

    /**
     * 设置出库数量
     * @param allocationId
     * @param goodsId
     * @param realQty
     */
    void setDetailDRealQty(Long allocationId,Long goodsId,Integer realQty);

}
