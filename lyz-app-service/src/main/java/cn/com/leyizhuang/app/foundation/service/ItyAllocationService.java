package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.Allocation;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationQuery;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationVO;
import com.github.pagehelper.PageInfo;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2018/1/3.
 * Time: 13:57.
 */

public interface ItyAllocationService {

    Allocation insert(Allocation allocation, String operaterdBy);

    void update(Allocation allocation, String operaterdBy);

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

    void send(Allocation allocation, String realNums, String username);

    void receive(Allocation allocation, String username);

    void resendAllAllocation();


}
