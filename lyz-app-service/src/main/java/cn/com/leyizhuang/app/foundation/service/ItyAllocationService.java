package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AllocationTypeEnum;
import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.Allocation;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationDetail;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationQuery;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationVO;
import com.github.pagehelper.PageInfo;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2018/1/3.
 * Time: 13:57.
 */

public interface ItyAllocationService {

    /**
     * 新增
     * @param allocation
     * @param operaterdBy
     * @return
     */
    Allocation insert(Allocation allocation, String operaterdBy);

    /**
     * 编辑
     * @param allocation
     */
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
    PageInfo<AllocationVO> queryPage(Integer offset, Integer size, String keywords, AllocationQuery query,Long storeId);

    /**
     * 作废
     * @param allocation
     * @param username
     */
    void cancel(Allocation allocation, String username);

    /**
     * 出库
     * @param allocation
     * @param details
     * @param username
     */
    void sent(Allocation allocation, List<AllocationDetail> details, String username);

    /**
     * 入库
     * @param allocation
     * @param username
     */
    void receive(Allocation allocation, String username);

    /**
     * 重传所有失败记录
     */
    void resendAllAllocation();

    /**
     * 新增调拨单
     * @param allocation
     * @param goodsDetails
     * @param toStoreId
     */
    void addAllocation(Allocation allocation, List<AllocationDetail> goodsDetails, ShiroUser shiroUser, Long toStoreId);

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

    /**
     * 接口回调
     * @param ids
     * @param msg
     * @param sendTime
     * @param flag
     */
    void updateSendFlagAndErrorMessage(List<Long> ids, String msg, Date sendTime, AppWhetherFlag flag);

    /**
     * 生成调拨单头Json
     * @param allocation
     * @return
     */
    String genHeaderJson(Allocation allocation);

    /**
     * 生成调拨单明细JSON
     * @param allocation
     * @return
     */
    String genDetailJson(Allocation allocation);

    /**
     * 生成调拨单头入库JSON
     * @param allocation
     * @return
     */
    String genReceiveJson(Allocation allocation);

    /**
     * 发送调拨出库接口
     * @param number
     */
    void sendAllocationToEBSAndRecord(String number);

    /**
     * 发送调拨入库接口
     * @param number
     */
    void sendAllocationReceivedToEBSAndRecord(String number);
}
