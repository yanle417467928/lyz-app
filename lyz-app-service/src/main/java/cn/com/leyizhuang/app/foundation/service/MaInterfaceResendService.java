package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.InterfaceResendChangeType;
import cn.com.leyizhuang.app.foundation.pojo.interfaceResend.EbsAllocationResendInfo;
import cn.com.leyizhuang.app.foundation.pojo.interfaceResend.EbsOrderResendInfo;
import cn.com.leyizhuang.app.foundation.pojo.interfaceResend.EbsReturnOrderResendInfo;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationInf;
import com.github.pagehelper.PageInfo;


import java.util.List;

public interface MaInterfaceResendService {

    PageInfo<EbsOrderResendInfo> queryNotSendEbsOrder(Integer page, Integer size,Integer isGenerate, String keywords);

    PageInfo<EbsReturnOrderResendInfo> queryNotSendEbsReturnOrder(Integer page, Integer size,Integer isGenerate, String keywords);

    PageInfo<EbsAllocationResendInfo> queryNotSendAllocation(Integer page, Integer size,Integer isGenerate, String keywords,Integer type);

    PageInfo<EbsOrderResendInfo> queryNotSendSelfTakeOrder(Integer page, Integer size,Integer isGenerate, String keywords);

    PageInfo<EbsReturnOrderResendInfo> queryNotSendEbsReturnStoreOrder(Integer page, Integer size,Integer isGenerate, String keywords);

    void  generateEbsOrderInfo( String orderNumber, ShiroUser user,String ip);

    void  resendEbsOrderInfo( String orderNumber, ShiroUser user,String ip);

    void  generateEbsReturnOrderInfo( String returnNo, ShiroUser user,String ip);

    void  resendEbsReturnOrderInfo( String returnNo, ShiroUser user,String ip);

    void  resendEbsAllocationInboundInfo( String number, ShiroUser user,String ip);

    void  resendEbsAllocationOutboundInfo( String number, ShiroUser user,String ip);

    void addResendEbsInterfaceLog(String referenceNumber, ShiroUser user, InterfaceResendChangeType interfaceResendChangeType, Boolean isSuccess, String msg, String ip, String systemName);

    AllocationInf  findAllocationInfByTypeAndNumber(Integer type,String number);

    List<AllocationInf>  findAllocationInfAllByTypeAndNumber(Integer type,String number);

    void  updateAllocationStatus(Integer type,String number,Integer status,String msg);

    void  generateEbsSelfTakeOrderInfo( String number, ShiroUser user,String ip);

    void  resendEbsSelfTakeOrderInfo( String number, ShiroUser user,String ip);

    void  generateEbsReturnStoreOrderInfo( String number, ShiroUser user,String ip);

    void  resendEbsReturnStoreOrderInfo( String number, ShiroUser user,String ip);
}
