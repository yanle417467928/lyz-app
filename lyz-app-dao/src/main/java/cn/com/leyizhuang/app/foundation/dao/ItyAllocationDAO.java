package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AllocationTypeEnum;
import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
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
    List<AllocationVO> queryListVO(@Param("keywords") String keywords,@Param("storeId") Long storeId);

    /**
     * 查询调拨单
     *
     * @param id 调拨单id
     * @return
     */
    Allocation queryAllocationById(Long id);

    Allocation queryAllocationByNumber(@Param("number") String number);

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

    int updateAllocation(Allocation allocation);

    int insertAllocation(Allocation allocation);

    int insertAllocationDetails(AllocationDetail goodsDetails);

    int insertAllocationTrail(AllocationTrail allocationTrail);

    int insertAllocationInf(AllocationInf inf);

    int chagneAllocationStatus(@Param("id") Long id, @Param("status") AllocationTypeEnum status);

    int setDetailDRealQty(@Param("allcationId") Long allcationId, @Param("goodsId") Long goodsId,@Param("realQty") Integer realQty);

    /**
     * 接口回调
     * @param ids
     * @param msg
     * @param sendTime
     * @param flag
     */
    void updateSendFlagAndErrorMessage(@Param(value = "ids") List<Long> ids,
                                       @Param(value = "errorMsg") String msg,
                                       @Param(value = "sendTime") Date sendTime,
                                       @Param(value = "flag") AppWhetherFlag flag);

    List<AllocationInf> findAllocationInfByType(Integer type);

    void deleteAllocationInf(@Param("id") Long id);

    /**
     * 分页查询
     *
     * @param keywords
     * @return
     */
    List<AllocationVO> queryAllocationPage(@Param("keywords") String keywords,@Param("outCompany")  String outCompany,@Param("inCompany")  String inCompany,@Param("outStore")  Long outStore,@Param("inStore")  Long inStore,@Param("list") List<Long> storeIds);

}
