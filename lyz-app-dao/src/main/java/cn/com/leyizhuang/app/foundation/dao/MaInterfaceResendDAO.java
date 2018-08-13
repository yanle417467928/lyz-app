package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.interfaceResend.EbsAllocationResendInfo;
import cn.com.leyizhuang.app.foundation.pojo.interfaceResend.EbsOrderResendInfo;
import cn.com.leyizhuang.app.foundation.pojo.interfaceResend.EbsReturnOrderResendInfo;
import cn.com.leyizhuang.app.foundation.pojo.interfaceResend.InterfaceResendLog;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationInf;
import cn.com.leyizhuang.app.foundation.vo.OrderGoodsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author lh
 * @date 2017/12/16
 */
@Repository
public interface MaInterfaceResendDAO {

    List<EbsOrderResendInfo> queryNotSendEbsOrder(@Param(value = "isGenerate") Integer isGenerate,@Param(value = "keywords") String keywords);

    List<EbsReturnOrderResendInfo> queryNotSendEbsReturnOrder(@Param(value = "isGenerate") Integer isGenerate,@Param(value = "keywords") String keywords);

    List<EbsAllocationResendInfo> queryNotSendAllocation(@Param(value = "isGenerate") Integer isGenerate,@Param(value = "keywords") String keywords,@Param(value = "type") Integer type);

    List<EbsOrderResendInfo> queryNotSendSelfTakeOrder(@Param(value = "isGenerate") Integer isGenerate,@Param(value = "keywords") String keywords);

    List<EbsReturnOrderResendInfo> queryNotSendEbsReturnStoreOrder(@Param(value = "isGenerate") Integer isGenerate,@Param(value = "keywords") String keywords);

    void addResendEbsInterfaceLog(InterfaceResendLog interfaceResendLog);

    AllocationInf findAllocationInfByTypeAndNumber( @Param(value = "type") Integer type,@Param(value = "number") String number);

    List<AllocationInf> findAllocationInfAllByTypeAndNumber( @Param(value = "type") Integer type,@Param(value = "number") String number);

    void updateAllocationStatus( @Param(value = "type") Integer type,@Param(value = "number") String number,@Param(value = "status") Integer status,@Param(value = "msg") String msg);
}
