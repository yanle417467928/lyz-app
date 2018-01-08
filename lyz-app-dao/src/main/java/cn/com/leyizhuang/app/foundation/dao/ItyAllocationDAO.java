package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2018/1/3.
 * Time: 15:38.
 */
@Repository
public interface ItyAllocationDAO {

    /**
     * 分页查询
     *
     * @param keywords
     * @return
     */
    List<AllocationVO> queryListVO(@Param("keywords") String keywords);

    /**
     * 查询调拨单
     *
     * @param id 调拨单id
     * @return
     */
    Allocation queryAllocationById(Long id);

    /**
     * 查询一个调拨单的商品明细
     *
     * @param id
     * @return
     */
    List<AllocationDetail> queryDetailsByAllocationId(Long id);

    /**
     * 查询一个调拨单的操作轨迹
     *
     * @param id
     * @return
     */
    List<AllocationTrail> queryTrailsByAllocationId(Long id);

    /**
     * 根据参数对象查询
     *
     * @param query
     * @return
     */
    List<AllocationVO> queryByAllocationQuery(AllocationQuery query);
}
