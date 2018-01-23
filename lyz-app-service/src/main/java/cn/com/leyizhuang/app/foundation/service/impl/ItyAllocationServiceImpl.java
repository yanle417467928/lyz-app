package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AllocationTypeEnum;
import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.core.constant.StoreInventoryAvailableQtyChangeType;
import cn.com.leyizhuang.app.core.remote.ebs.EbsSenderService;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.core.utils.RandomUtil;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.ItyAllocationDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.*;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.ItyAllocationService;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreDetailVO;

import cn.com.leyizhuang.ebs.entity.dto.second.AllocationDetailSecond;
import cn.com.leyizhuang.ebs.entity.dto.second.AllocationHeaderSecond;
import cn.com.leyizhuang.ebs.entity.dto.second.AllocationReceiveSecond;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static cn.com.leyizhuang.app.core.utils.DateUtil.*;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2018/1/3.
 * Time: 13:57.
 */
@Service
public class ItyAllocationServiceImpl implements ItyAllocationService {

    private final Logger logger = LoggerFactory.getLogger(ItyAllocationServiceImpl.class);

    @Autowired
    private ItyAllocationDAO ityAllocationDAO;

    @Autowired
    private MaStoreService maStoreService;

    @Autowired
    private AppStoreService appStoreService;

    @Autowired
    private EbsSenderService ebsSenderService;

    @Override
    public PageInfo<AllocationVO> queryPage(Integer offset, Integer size, String keywords, AllocationQuery query) {
        PageHelper.startPage(offset, size);
        List<AllocationVO> allocationVOList;
        if (StringUtils.isNotBlank(keywords)) {
            allocationVOList = ityAllocationDAO.queryListVO(keywords);
            return new PageInfo<>(allocationVOList);
        }
        allocationVOList = ityAllocationDAO.queryByAllocationQuery(query);
        return new PageInfo<>(allocationVOList);
    }

    @Override
    public Allocation queryAllocationById(Long id) {
        if (id != null) {
            Allocation allocation = ityAllocationDAO.queryAllocationById(id);
            allocation.setDetails(ityAllocationDAO.queryDetailsByAllocationId(id));
            allocation.setTrails(ityAllocationDAO.queryTrailsByAllocationId(id));
            return allocation;
        }
        return null;
    }

    @Override
    public Allocation insert(Allocation allocation, String operaterdBy) {
        return null;
    }

    @Override
    public void update(Allocation allocation) {
        if (allocation != null) {
            ityAllocationDAO.updateAllocation(allocation);
        }
    }

    @Override
    public Allocation get(Long id) {
        return null;
    }

    @Override
    @Transactional
    public void cancel(Allocation allocation, String username) {

        // 调拨单状态设置为作废
        allocation.setStatus(AllocationTypeEnum.CANCELLED);
        ityAllocationDAO.updateAllocation(allocation);

        // 新建调拨轨迹
        this.createAllocationRecord(allocation.getId(), username, AllocationTypeEnum.CANCELLED);

    }

    @Override
    @Transactional
    public void sent(Allocation allocation, List<AllocationDetail> allocationDetailList, String username) {
        AppStore from = appStoreService.findById(allocation.getAllocationFrom());

        for (AllocationDetail goods : allocationDetailList) {
            try {
                // 扣除商品库存
                appStoreService.updateStoreInventoryByStoreCodeAndGoodsId(from.getStoreCode(), goods.getGoodsId(), -goods.getRealQty());
            } catch (RuntimeException e) {
                throw new RuntimeException("产品：" + goods.getSku() + " 库存不足，不允许调拨");
            }

            StoreInventory storeInventory = appStoreService.findStoreInventoryByStoreCodeAndGoodsId(from.getStoreCode(), goods.getGoodsId());
            if (storeInventory == null || storeInventory.getRealIty() <= 0) {
                throw new RuntimeException("产品：" + goods.getSku() + " 库存不足，不允许调拨");
            } else {

                // 创建库存变化日志
                StoreInventoryAvailableQtyChangeLog iLog = new StoreInventoryAvailableQtyChangeLog();
                iLog.setAfterChangeQty(storeInventory.getAvailableIty());
                iLog.setChangeQty(goods.getRealQty());
                iLog.setChangeTime(new Date());
                iLog.setChangeType(StoreInventoryAvailableQtyChangeType.STORE_ALLOCATE_OUTBOUND);
                iLog.setStoreId(from.getStoreId());
                iLog.setStoreCode(from.getStoreCode());
                iLog.setStoreName(from.getStoreName());
                iLog.setGid(goods.getGoodsId());
                iLog.setSku(goods.getSku());
                iLog.setSkuName(goods.getSkuName());
                iLog.setChangeTypeDesc("调拨出库");
                iLog.setReferenceNumber(allocation.getNumber());
                appStoreService.addStoreInventoryAvailableQtyChangeLog(iLog);
            }

            ityAllocationDAO.setDetailDRealQty(allocation.getId(), goods.getGoodsId(), goods.getRealQty());
        }

        // 调拨单状态设置为出库
        allocation.setModifier(username);
        allocation.setModifyTime(new Date());
        allocation.setStatus(AllocationTypeEnum.SENT);
        ityAllocationDAO.updateAllocation(allocation);

        // 新建调拨轨迹
        this.createAllocationRecord(allocation.getId(), username, AllocationTypeEnum.SENT);

    }

    @Override
    @Transactional
    public void receive(Allocation allocation, String username) {
        AppStore to = appStoreService.findById(allocation.getAllocationTo());

        List<AllocationDetail> allocationDetails = ityAllocationDAO.queryDetailsByAllocationId(allocation.getId());
        if (allocationDetails == null || allocationDetails.size() == 0){
            logger.info("调拨单商品详情不存在");
            throw new RuntimeException();
        }

        for (AllocationDetail detail : allocationDetails){

            StoreInventory storeInventory = appStoreService.findStoreInventoryByStoreCodeAndGoodsId(to.getStoreCode(), detail.getGoodsId());
            if (storeInventory == null){
                // 无此库存 TODO 新建门店库存

            }else{
                // 增加商品库存
                appStoreService.updateStoreInventoryByStoreCodeAndGoodsId(to.getStoreCode(), detail.getGoodsId(), detail.getRealQty());

                // 创建库存变化日志
                StoreInventoryAvailableQtyChangeLog iLog = new StoreInventoryAvailableQtyChangeLog();
                iLog.setAfterChangeQty(storeInventory.getAvailableIty()+detail.getRealQty());
                iLog.setChangeQty(detail.getRealQty());
                iLog.setChangeTime(new Date());
                iLog.setChangeType(StoreInventoryAvailableQtyChangeType.STORE_ALLOCATE_INBOUND);
                iLog.setStoreId(to.getStoreId());
                iLog.setStoreCode(to.getStoreCode());
                iLog.setStoreName(to.getStoreName());
                iLog.setGid(detail.getGoodsId());
                iLog.setSku(detail.getSku());
                iLog.setSkuName(detail.getSkuName());
                iLog.setChangeTypeDesc("调拨入库");
                iLog.setReferenceNumber(allocation.getNumber());
                appStoreService.addStoreInventoryAvailableQtyChangeLog(iLog);
            }

        }

        // 调拨单状态设置为入库
        allocation.setModifier(username);
        allocation.setModifyTime(new Date());
        allocation.setStatus(AllocationTypeEnum.ENTERED);
        ityAllocationDAO.updateAllocation(allocation);

        // 新建调拨轨迹
        this.createAllocationRecord(allocation.getId(), username, AllocationTypeEnum.ENTERED);

        // 调用ebs接口 将调拨出库消息加入队列
        //sinkSender.sendAllocationReceivedToEBSAndRecord(allocation.getNumber());
    }

    @Override
    public void resendAllAllocation() {

    }

    @Override
    @Transactional
    public void addAllocation(Allocation allocation, List<AllocationDetail> goodsDetails, ShiroUser shiroUser) {

        // 调出门店id
        Long storeId = allocation.getAllocationFrom();
        StoreDetailVO store = maStoreService.queryStoreVOById(storeId);

        allocation.setNumber(this.getAllocationNumber());
        allocation.setCreateTime(new Date());
        allocation.setCreator(shiroUser.getLoginName());
        allocation.setModifier(shiroUser.getLoginName());
        allocation.setModifyTime(new Date());
        allocation.setAllocationFromName(store.getStoreName());
        allocation.setStatus(AllocationTypeEnum.NEW);
        // TODO 城市 门店信息

        ityAllocationDAO.insertAllocation(allocation);
        for (AllocationDetail detail : goodsDetails) {
            detail.setAllocationId(allocation.getId());
            ityAllocationDAO.insertAllocationDetails(detail);
        }

        // 记录调拨轨迹
        AllocationTrail trail = new AllocationTrail();
        trail.setAllocationId(allocation.getId());
        trail.setOperator(shiroUser.getName());
        trail.setOperateTime(new Date());
        trail.setOperation(AllocationTypeEnum.NEW);
        ityAllocationDAO.insertAllocationTrail(trail);
    }

    @Override
    public Model queryAllocationDetail(Long id, Model model) {

        Allocation allocation = ityAllocationDAO.queryAllocationById(id);
        List<AllocationDetail> details = ityAllocationDAO.queryDetailsByAllocationId(id);
        List<AllocationTrail> trails = ityAllocationDAO.queryTrailsByAllocationId(id);


        model.addAttribute("allocation", allocation);
        model.addAttribute("details", details);
        model.addAttribute("trails", trails);

        return model;
    }

    public void chagneAllocationStatus(Long allocationId, AllocationTypeEnum status) {
        ityAllocationDAO.chagneAllocationStatus(allocationId, status);
    }

    public void setDetailDRealQty(Long allocationId, Long goodsId, Integer realQty) {
        ityAllocationDAO.setDetailDRealQty(allocationId, goodsId, realQty);
    }

    public void updateSendFlagAndErrorMessage(List<Long> ids, String msg, Date sendTime, AppWhetherFlag flag) {
        if (ids != null && ids.size() > 0) {
            ityAllocationDAO.updateSendFlagAndErrorMessage(ids, msg, sendTime, flag);
        }
    }

    @Override
    public String genHeaderJson(Allocation allocation) {
        AppStore to = appStoreService.findById(allocation.getAllocationTo());
        AppStore from = appStoreService.findById(allocation.getAllocationFrom());

        AllocationHeaderSecond header = new AllocationHeaderSecond();
        header.setSobId(toString(to.getSobId()));
        header.setHeaderId(toString(allocation.getId()));
        header.setOrderNumber(allocation.getNumber());
        header.setOrderDate(DateFormatUtils.format(allocation.getCreateTime(), DateUtil.FORMAT_DATETIME));
        header.setApproveDate(DateFormatUtils.format(allocation.getModifyTime(), DateUtil.FORMAT_DATETIME));
        header.setProductType("HR");
        header.setLyDiySiteCode(from.getStoreCode());
        header.setLyDiySiteName(from.getStoreName());
        header.setMdDiySiteCode(to.getStoreCode());
        header.setMdDiySiteName(to.getStoreCode());
        header.setComments(allocation.getComment());
        return JSON.toJSONString(header);
    }

    @Override
    public String genDetailJson(Allocation allocation) {
        List<AllocationDetailSecond> allocationDetails = Lists.newArrayList();
        for (AllocationDetail detail : allocation.getDetails()) {
            AllocationDetailSecond allocationDetail = new AllocationDetailSecond();
            allocationDetail.setHeaderId(toString(allocation.getId()));
            allocationDetail.setLineId(toString(detail.getId()));
            allocationDetail.setGoodsTitle(detail.getSkuName());
            allocationDetail.setSku(detail.getSku());
            allocationDetail.setQuantity(toString(detail.getRealQty()));
            allocationDetails.add(allocationDetail);
        }
        return JSON.toJSONString(allocationDetails);
    }

    @Override
    public String genReceiveJson(Allocation allocation) {
        AppStore to = appStoreService.findById(allocation.getAllocationTo());

        AllocationReceiveSecond receive = new AllocationReceiveSecond();
        receive.setSobId(toString(to.getSobId()));
        receive.setHeaderId(toString(allocation.getId()));
        receive.setOrderNumber(allocation.getNumber());
        receive.setReceiveDate(DateFormatUtils.format(allocation.getModifyTime(), DateUtil.FORMAT_DATETIME));
        return JSON.toJSONString(receive);
    }

    public void sendAllocationToEBSAndRecord(String number){
        Allocation allocation = ityAllocationDAO.queryAllocationByNumber(number);
        if (allocation == null){
            logger.info("单号："+number+" 调拨单不存在！");
        }else{
            List<AllocationDetail> allocationDetails = ityAllocationDAO.queryDetailsByAllocationId(allocation.getId());
            allocation.setDetails(allocationDetails);
            // 发送接口
            ebsSenderService.sendAllocationToEBSAndRecord(allocation);
        }
    }

    public void sendAllocationReceivedToEBSAndRecord(String number){
        Allocation allocation = ityAllocationDAO.queryAllocationByNumber(number);
        if (allocation == null){
            logger.info("单号："+number+" 调拨单不存在！");
        }else{
            // 发送接口
            ebsSenderService.sendAllocationReceivedToEBSAndRecord(allocation);
        }
    }

    private String getAllocationNumber() {
        StringBuilder number = new StringBuilder();
        number.append("DB_");
        number.append(getCurrentTimeStr("yyyyMMddHHmmssSSS"));
        number.append(RandomUtil.randomNumCode(3));
        return number.toString();
    }

    private void createAllocationRecord(Long allocationId, String username, AllocationTypeEnum status) {
        AllocationTrail trail = new AllocationTrail();
        trail.setAllocationId(allocationId);
        trail.setOperator(username);
        trail.setOperateTime(new Date());
        trail.setOperation(status);

        ityAllocationDAO.insertAllocationTrail(trail);
    }

    private String toString(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return String.valueOf(obj);
        }
    }
}
