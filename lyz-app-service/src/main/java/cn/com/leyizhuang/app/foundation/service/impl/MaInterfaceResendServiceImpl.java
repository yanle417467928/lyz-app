package cn.com.leyizhuang.app.foundation.service.impl;




import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.InterfaceResendChangeType;
import cn.com.leyizhuang.app.foundation.dao.MaInterfaceResendDAO;
import cn.com.leyizhuang.app.foundation.dao.MaReturnOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.interfaceResend.EbsAllocationResendInfo;
import cn.com.leyizhuang.app.foundation.pojo.interfaceResend.EbsOrderResendInfo;
import cn.com.leyizhuang.app.foundation.pojo.interfaceResend.EbsReturnOrderResendInfo;
import cn.com.leyizhuang.app.foundation.pojo.interfaceResend.InterfaceResendLog;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationInf;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderTempInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.returnOrder.MaReturnOrderDetailInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.returnOrder.MaStoreReturnOrderAppToEbsBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.webservice.ebs.MaOrderReceiveInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.ReturnOrderBaseInf;
import cn.com.leyizhuang.app.foundation.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MaInterfaceResendServiceImpl implements MaInterfaceResendService {

    @Autowired
    private MaInterfaceResendDAO maInterfaceResendDAO;

    @Resource
    private AppSeparateOrderService separateOrderService;

    @Resource
    private ItyAllocationService ityAllocationService;

    @Resource
    private MaOrderService maOrderService;

    @Resource
    private MaStoreService MaStoreService;

    @Resource
    private MaReturnOrderService maReturnOrderService;

    @Resource
    private MaReturnOrderDAO maReturnOrderDAO;


    @Override
    public PageInfo<EbsOrderResendInfo> queryNotSendEbsOrder(Integer page, Integer size,Integer isGenerate, String keywords) {
        PageHelper.startPage(page, size);
        List<EbsOrderResendInfo> ebsOrderResendInfoList =maInterfaceResendDAO.queryNotSendEbsOrder(isGenerate,keywords);
        return new PageInfo<>(ebsOrderResendInfoList) ;
    }

    @Override
    public PageInfo<EbsReturnOrderResendInfo> queryNotSendEbsReturnOrder(Integer page, Integer size,Integer isGenerate, String keywords) {
        PageHelper.startPage(page, size);
        List<EbsReturnOrderResendInfo> ebsOrderResendInfoList =maInterfaceResendDAO.queryNotSendEbsReturnOrder(isGenerate,keywords);
        return new PageInfo<>(ebsOrderResendInfoList) ;
    }

    @Override
    public PageInfo<EbsAllocationResendInfo> queryNotSendAllocation(Integer page, Integer size,Integer isGenerate, String keywords,Integer type) {
        PageHelper.startPage(page, size);
        List<EbsAllocationResendInfo> ebsAllocationResendInfoList =maInterfaceResendDAO.queryNotSendAllocation(isGenerate,keywords,type);
        return new PageInfo<>(ebsAllocationResendInfoList) ;
    }

    @Override
    public PageInfo<EbsOrderResendInfo> queryNotSendSelfTakeOrder(Integer page, Integer size,Integer isGenerate, String keywords) {
        PageHelper.startPage(page, size);
        List<EbsOrderResendInfo> ebsOrderResendInfoList =maInterfaceResendDAO.queryNotSendSelfTakeOrder(isGenerate,keywords);
        return new PageInfo<>(ebsOrderResendInfoList) ;
    }


    @Override
    public PageInfo<EbsReturnOrderResendInfo> queryNotSendEbsReturnStoreOrder(Integer page, Integer size,Integer isGenerate, String keywords) {
        PageHelper.startPage(page, size);
        List<EbsReturnOrderResendInfo> ebsOrderResendInfoList =maInterfaceResendDAO.queryNotSendEbsReturnStoreOrder(isGenerate,keywords);
        return new PageInfo<>(ebsOrderResendInfoList) ;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateEbsOrderInfo(String orderNumber, ShiroUser user,String ip){
        separateOrderService.separateOrderAndGoodsInf(orderNumber);
        this.addResendEbsInterfaceLog( orderNumber,user,InterfaceResendChangeType.EBS_ORER_GENERATE,Boolean.TRUE,"生成接口成功",ip,"EBS");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resendEbsOrderInfo(String orderNumber, ShiroUser user,String ip){
        separateOrderService.sendNotXQOrderBaseInfAndOrderGoodsInf(orderNumber);
        List<OrderBaseInf> orderBaseInfList = separateOrderService.getPendingSendOrderBaseInf(orderNumber);
        OrderBaseInf orderBaseInf = orderBaseInfList.get(0);
        if("N".equals(orderBaseInf.getSendFlag().getValue())){
            this.addResendEbsInterfaceLog( orderNumber,user,InterfaceResendChangeType.EBS_ORER_RESEND,Boolean.FALSE,orderBaseInf.getErrorMsg(),ip,"EBS");
        }else if("Y".equals(orderBaseInf.getSendFlag().getValue())){
            this.addResendEbsInterfaceLog( orderNumber,user,InterfaceResendChangeType.EBS_ORER_RESEND,Boolean.TRUE,"接口发送成功",ip,"EBS");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateEbsReturnOrderInfo(String returnNo, ShiroUser user,String ip){
        separateOrderService.separateReturnOrderAndGoodsInf(returnNo);
        this.addResendEbsInterfaceLog( returnNo,user,InterfaceResendChangeType.EBS_ORER_GENERATE,Boolean.TRUE,"生成接口成功",ip,"EBS");

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resendEbsReturnOrderInfo(String returnNo, ShiroUser user,String ip){
        separateOrderService.sendNotXQReturnOrderBaseInfAndReturnOrderGoodsInf(returnNo);
        List<ReturnOrderBaseInf> returnOrderBaseInfList = separateOrderService.getReturnOrderBaseInfByReturnNumber(returnNo);
        ReturnOrderBaseInf returnOrderBaseInf = returnOrderBaseInfList.get(0);
        if("N".equals(returnOrderBaseInf.getSendFlag().getValue())){
            this.addResendEbsInterfaceLog( returnNo,user,InterfaceResendChangeType.EBS_RETURN_ORER_RESEND,Boolean.FALSE,returnOrderBaseInf.getErrorMsg(),ip,"EBS");
        }else if("Y".equals(returnOrderBaseInf.getSendFlag().getValue())){
            this.addResendEbsInterfaceLog( returnNo,user,InterfaceResendChangeType.EBS_RETURN_ORER_RESEND,Boolean.TRUE,"接口发送成功",ip,"EBS");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resendEbsAllocationInboundInfo(String number, ShiroUser user,String ip){
        AllocationInf allocationInfBefore = this.findAllocationInfByTypeAndNumber(3,number);
        Date allocationInfDateBefore = allocationInfBefore.getCreatedTime();
        ityAllocationService.sendAllocationReceivedToEBSAndRecord(number);
        AllocationInf allocationInfAfter = this.findAllocationInfByTypeAndNumber(3,number);
        Date allocationInfDateAfter = allocationInfAfter.getCreatedTime();
        if(allocationInfDateAfter.after(allocationInfDateBefore)){
            this.addResendEbsInterfaceLog( number,user,InterfaceResendChangeType.EBS_ALLOCATIONINBOUND_RESEND,Boolean.FALSE,allocationInfAfter.getMsg(),ip,"EBS");
        }else if(allocationInfDateAfter.compareTo(allocationInfDateBefore)==0){
            this.addResendEbsInterfaceLog( number,user,InterfaceResendChangeType.EBS_ALLOCATIONINBOUND_RESEND,Boolean.TRUE,"接口发送成功",ip,"EBS");
            this.updateAllocationStatus(3,number,0,null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resendEbsAllocationOutboundInfo(String number, ShiroUser user,String ip){
        AllocationInf allocationInfBefore = this.findAllocationInfByTypeAndNumber(1,number);
        Date allocationInfDateBefore = allocationInfBefore.getCreatedTime();
        ityAllocationService.sendAllocationToEBSAndRecord(number);
        AllocationInf allocationInfAfter = this.findAllocationInfByTypeAndNumber(1,number);
        Date allocationInfDateAfter = allocationInfAfter.getCreatedTime();
        if(allocationInfDateAfter.after(allocationInfDateBefore)){
            this.addResendEbsInterfaceLog( number,user,InterfaceResendChangeType.EBS_ALLOCATIONOUTBOUND_RESEND,Boolean.FALSE,allocationInfAfter.getMsg(),ip,"EBS");
        }else if(allocationInfDateAfter.compareTo(allocationInfDateBefore)==0){
            this.addResendEbsInterfaceLog( number,user,InterfaceResendChangeType.EBS_ALLOCATIONOUTBOUND_RESEND,Boolean.TRUE,"接口发送成功",ip,"EBS");
            this.updateAllocationStatus(1,number,0,null);
        }

    }

    @Override
    public  void addResendEbsInterfaceLog(String referenceNumber, ShiroUser user, InterfaceResendChangeType interfaceResendChangeType,Boolean isSuccess,String msg,String ip,String systemName) {
        InterfaceResendLog interfaceResendLog = new InterfaceResendLog();
        interfaceResendLog.setChangeType(interfaceResendChangeType);
        interfaceResendLog.setChangeTypeDesc(interfaceResendChangeType.getDescription());
        interfaceResendLog.setCreateTime(new Date());
        interfaceResendLog.setMessage(msg);
        interfaceResendLog.setOperatorId(user.getId());
        interfaceResendLog.setOperatorIp(ip);
        interfaceResendLog.setOperator(user.getLoginName());
        interfaceResendLog.setReferenceNumber(referenceNumber);
        interfaceResendLog.setSendSystem(systemName);
        interfaceResendLog.setIsSuccess(isSuccess);
        maInterfaceResendDAO.addResendEbsInterfaceLog(interfaceResendLog);
    }

    @Override
    public AllocationInf findAllocationInfByTypeAndNumber(Integer type,String number){
        AllocationInf allocationInf =maInterfaceResendDAO.findAllocationInfByTypeAndNumber(type,number);
        return allocationInf;
    }

    @Override
    public List<AllocationInf> findAllocationInfAllByTypeAndNumber(Integer type,String number){
        List<AllocationInf> allocationInfList =maInterfaceResendDAO.findAllocationInfAllByTypeAndNumber(type,number);
        return allocationInfList;
    }

    @Override
    public void updateAllocationStatus(Integer type,String number,Integer status,String msg){
        maInterfaceResendDAO.updateAllocationStatus(type,number,status,msg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateEbsSelfTakeOrderInfo(String orderNumber, ShiroUser user,String ip) {
        MaOrderTempInfo maOrderTempInfo = maOrderService.getOrderInfoByOrderNo(orderNumber);
        MaOrderReceiveInf maOrderReceiveInfOld = maOrderService.queryOrderReceiveInf(orderNumber);
        if(null !=maOrderReceiveInfOld){
            throw new RuntimeException("该订单接口已经生成,不能重复生成接口信息");
        }
        if("FINISHED".equals(maOrderTempInfo.getStatus().getValue().toString())){
            String storeCode =  maOrderTempInfo.getStoreCode();
            MaStoreInfo store = MaStoreService.findStoreByStoreCode(storeCode);
            if("JM".equals(store.getStoreType().getValue().toString())){
                throw new RuntimeException("门店类型错误,不能生成接口信息");
            }
            MaOrderReceiveInf maOrderReceiveInf = new MaOrderReceiveInf();
            maOrderReceiveInf.setDeliverTypeTitle(AppDeliveryType.SELF_TAKE);
            maOrderReceiveInf.setOrderNumber(orderNumber);
            maOrderReceiveInf.setReceiveDate(new Date());
            maOrderReceiveInf.setSobId(maOrderTempInfo.getSobId());
            maOrderReceiveInf.setInitDate(maOrderTempInfo.getCreateTime());
            maOrderReceiveInf.setHeaderId(maOrderTempInfo.getId());
            maOrderService.saveAppToEbsOrderReceiveInf(maOrderReceiveInf);
            separateOrderService.separateOrderAndGoodsInf(orderNumber);
            this.addResendEbsInterfaceLog( orderNumber,user,InterfaceResendChangeType.EBS_SELFTAKE_ORER_GENERATE,Boolean.TRUE,"生成接口成功",ip,"EBS");
        }else {
            throw new RuntimeException("订单状态错误,不能生成接口信息");
        }
        //生成ebs接口表数据
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resendEbsSelfTakeOrderInfo(String orderNumber, ShiroUser user,String ip){
        maOrderService.sendOrderReceiveInfAndRecord(orderNumber);
        MaOrderReceiveInf maOrderReceiveInf = maOrderService.queryOrderReceiveInf(orderNumber);
        if("N".equals(maOrderReceiveInf.getSendFlag().getValue())){
            this.addResendEbsInterfaceLog( orderNumber,user,InterfaceResendChangeType.EBS_SELFTAKE_ORER_RESEND,Boolean.FALSE,maOrderReceiveInf.getErrorMsg(),ip,"EBS");
        }else if("Y".equals(maOrderReceiveInf.getSendFlag().getValue())){
            this.addResendEbsInterfaceLog( orderNumber,user,InterfaceResendChangeType.EBS_SELFTAKE_ORER_RESEND,Boolean.TRUE,"接口发送成功",ip,"EBS");
        }
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateEbsReturnStoreOrderInfo(String returnNo, ShiroUser user,String ip) {
        MaReturnOrderDetailInfo maReturnOrderDetailInfo = maReturnOrderService.queryMaReturnOrderByReturnNo(returnNo);
        MaOrderTempInfo maOrderTempInfo = maOrderService.getOrderInfoByOrderNo(maReturnOrderDetailInfo.getOrderNo());
        MaStoreReturnOrderAppToEbsBaseInfo maStoreReturnOrderAppToEbsBaseInfoOld = maReturnOrderDAO.findMaStoreReturnOrderAppToEbsInfoByReturnNumber(returnNo);
        if(null !=maStoreReturnOrderAppToEbsBaseInfoOld){
            throw new RuntimeException("该退单接口已经生成,不能重复生成接口信息");
        }
        if(5==(maReturnOrderDetailInfo.getReturnStatus().getValue())){
            String storeCode =  maReturnOrderDetailInfo.getStoreCode();
            MaStoreInfo store = MaStoreService.findStoreByStoreCode(storeCode);
            if("JM".equals(store.getStoreType().getValue().toString())){
                throw new RuntimeException("门店类型错误,不能生成接口信息");
            }
            separateOrderService.separateReturnOrder(returnNo);
            MaStoreReturnOrderAppToEbsBaseInfo maStoreReturnOrderAppToEbs = new MaStoreReturnOrderAppToEbsBaseInfo();
            maStoreReturnOrderAppToEbs.setSobId(maOrderTempInfo.getSobId());
            maStoreReturnOrderAppToEbs.setMainOrderNumber(maReturnOrderDetailInfo.getOrderNo());
            maStoreReturnOrderAppToEbs.setReturnDate(new Date());
            maStoreReturnOrderAppToEbs.setReturnNumber(maReturnOrderDetailInfo.getReturnNo());
            maReturnOrderDAO.saveAppToEbsReturnOrderInf(maStoreReturnOrderAppToEbs);
            this.addResendEbsInterfaceLog( returnNo,user,InterfaceResendChangeType.EBS_RETURNSTORE_ORER_GENERATE,Boolean.TRUE,"生成接口成功",ip,"EBS");
        }else {
            throw new RuntimeException("订单状态错误,不能生成接口信息");
        }
        //生成ebs接口表数据
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resendEbsReturnStoreOrderInfo(String returnNo, ShiroUser user,String ip){
        maReturnOrderService.sendReturnOrderReceiptInfAndRecord(returnNo);
        MaStoreReturnOrderAppToEbsBaseInfo maStoreReturnOrderAppToEbsBaseInfo = maReturnOrderDAO.findMaStoreReturnOrderAppToEbsInfoByReturnNumber(returnNo);
        if("N".equals(maStoreReturnOrderAppToEbsBaseInfo.getSendFlag().getValue())){
            this.addResendEbsInterfaceLog( returnNo,user,InterfaceResendChangeType.EBS_RETURNSTORE_ORER_RESEND,Boolean.FALSE,maStoreReturnOrderAppToEbsBaseInfo.getErrorMsg(),ip,"EBS");
        }else if("Y".equals(maStoreReturnOrderAppToEbsBaseInfo.getSendFlag().getValue())){
            this.addResendEbsInterfaceLog( returnNo,user,InterfaceResendChangeType.EBS_RETURNSTORE_ORER_RESEND,Boolean.TRUE,"接口发送成功",ip,"EBS");
        }
    }
}
