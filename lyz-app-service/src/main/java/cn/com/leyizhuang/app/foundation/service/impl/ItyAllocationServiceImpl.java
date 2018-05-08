package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AllocationTypeEnum;
import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import cn.com.leyizhuang.app.core.constant.StoreInventoryAvailableQtyChangeType;
import cn.com.leyizhuang.app.core.constant.StoreInventoryRealQtyChangeType;
import cn.com.leyizhuang.app.core.remote.ebs.EbsSenderService;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.core.utils.RandomUtil;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.ItyAllocationDAO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.*;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreRealInventoryChange;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.ItyAllocationService;
import cn.com.leyizhuang.app.foundation.service.MaStoreInventoryService;
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

import java.util.Date;
import java.util.List;
import java.util.Map;

import static cn.com.leyizhuang.app.core.utils.DateUtil.getCurrentTimeStr;

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

    @Autowired
    private MaStoreInventoryService maStoreInventoryService;

    @Override
    public PageInfo<AllocationVO> queryPage(Integer offset, Integer size, String keywords, AllocationQuery query, Long storeId) {
        PageHelper.startPage(offset, size);

        List<AllocationVO> allocationVOList;
        if (StringUtils.isNotBlank(keywords)) {
            allocationVOList = ityAllocationDAO.queryListVO(keywords, storeId);
            return new PageInfo<>(allocationVOList);
        }
        query.setStoreId(storeId);
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

            StoreInventory storeInventory = appStoreService.findStoreInventoryByStoreCodeAndGoodsId(from.getStoreCode(), goods.getGoodsId());

            if (storeInventory == null || storeInventory.getRealIty() <= 0 || storeInventory.getAvailableIty() <= 0) {
                throw new RuntimeException("产品：" + goods.getSku() + " 库存不足，不允许调拨");
            } else {

                // 扣减后 库存 及 可用量
                Integer inventory = storeInventory.getRealIty() - goods.getRealQty();
                Integer availableIty = storeInventory.getAvailableIty() - goods.getRealQty();

                if (availableIty < 0 || inventory < 0) {
                    throw new RuntimeException("产品：" + goods.getSku() + " 库存不足，不允许调拨");
                }

                try {

                    // 扣减门店真实库存 和 可用量
                    maStoreInventoryService.updateStoreInventoryAndAvailableIty(from.getStoreId(), goods.getGoodsId(), inventory, availableIty, storeInventory.getLastUpdateTime());

                } catch (RuntimeException e) {
                    throw new RuntimeException("产品：" + goods.getSku() + " 库存不足，不允许调拨");
                }

                // 创建库存变化日志
                StoreInventoryAvailableQtyChangeLog iLog = new StoreInventoryAvailableQtyChangeLog();
                iLog.setCityId(from.getCityId());
                iLog.setCityName(from.getCity());
                iLog.setAfterChangeQty(availableIty);
                iLog.setChangeQty(-goods.getRealQty());
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


                //创建门店真实库存变化日志
                MaStoreRealInventoryChange maStoreInventoryChange = new MaStoreRealInventoryChange();
                maStoreInventoryChange.setCityId(from.getCityId());
                maStoreInventoryChange.setCityName(from.getCity());
                maStoreInventoryChange.setStoreId(from.getStoreId());
                maStoreInventoryChange.setStoreCode(from.getStoreCode());
                maStoreInventoryChange.setStoreCode(from.getStoreName());
                maStoreInventoryChange.setReferenceNumber(allocation.getNumber());
                maStoreInventoryChange.setGid(goods.getGoodsId());
                maStoreInventoryChange.setSku(goods.getSku());
                maStoreInventoryChange.setSkuName(goods.getSkuName());
                maStoreInventoryChange.setChangeTime(new Date());
                maStoreInventoryChange.setAfterChangeQty(storeInventory.getRealIty()+goods.getRealQty());
                maStoreInventoryChange.setChangeQty(goods.getRealQty());
                maStoreInventoryChange.setChangeType(StoreInventoryRealQtyChangeType.STORE_ALLOCATE_OUTBOUND);
                maStoreInventoryChange.setChangeTypeDesc(StoreInventoryRealQtyChangeType.STORE_ALLOCATE_OUTBOUND.getDescription());
                maStoreInventoryService.addRealInventoryChangeLog(maStoreInventoryChange);
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
        if (allocationDetails == null || allocationDetails.size() == 0) {
            logger.info("调拨单商品详情不存在");
            throw new RuntimeException();
        }

        for (AllocationDetail detail : allocationDetails) {

            StoreInventory storeInventory = appStoreService.findStoreInventoryByStoreCodeAndGoodsId(to.getStoreCode(), detail.getGoodsId());
            if (storeInventory == null) {
                // 无此库存 TODO 新建门店库存

            } else {

                // 求和后 库存 及 可用量
                Integer inventory = storeInventory.getRealIty() + detail.getRealQty();
                Integer availableIty = storeInventory.getAvailableIty() + detail.getRealQty();

                try {

                    // 更新门店真实库存 和 可用量
                    maStoreInventoryService.updateStoreInventoryAndAvailableIty(to.getStoreId(), detail.getGoodsId(), inventory, availableIty, storeInventory.getLastUpdateTime());

                } catch (RuntimeException e) {
                    throw new RuntimeException("入库失败");
                }

                // 创建库存变化日志
                StoreInventoryAvailableQtyChangeLog iLog = new StoreInventoryAvailableQtyChangeLog();
                iLog.setCityId(to.getCityId());
                iLog.setCityName(to.getCity());
                iLog.setAfterChangeQty(storeInventory.getAvailableIty() + detail.getRealQty());
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

                //创建门店真实库存变化日志
                MaStoreRealInventoryChange maStoreInventoryChange = new MaStoreRealInventoryChange();
                maStoreInventoryChange.setCityId(to.getCityId());
                maStoreInventoryChange.setCityName(to.getCity());
                maStoreInventoryChange.setStoreId(to.getStoreId());
                maStoreInventoryChange.setStoreCode(to.getStoreCode());
                maStoreInventoryChange.setStoreCode(to.getStoreName());
                maStoreInventoryChange.setReferenceNumber(allocation.getNumber());
                maStoreInventoryChange.setGid(detail.getGoodsId());
                maStoreInventoryChange.setSku(detail.getSku());
                maStoreInventoryChange.setSkuName(detail.getSkuName());
                maStoreInventoryChange.setChangeTime(new Date());
                maStoreInventoryChange.setAfterChangeQty(storeInventory.getRealIty()+detail.getRealQty());
                maStoreInventoryChange.setChangeQty(detail.getRealQty());
                maStoreInventoryChange.setChangeType(StoreInventoryRealQtyChangeType.STORE_ALLOCATE_INBOUND);
                maStoreInventoryChange.setChangeTypeDesc(StoreInventoryRealQtyChangeType.STORE_ALLOCATE_INBOUND.getDescription());
                maStoreInventoryService.addRealInventoryChangeLog(maStoreInventoryChange);
            }

        }

        // 调拨单状态设置为入库
        allocation.setModifier(username);
        allocation.setModifyTime(new Date());
        allocation.setStatus(AllocationTypeEnum.ENTERED);
        ityAllocationDAO.updateAllocation(allocation);

        // 新建调拨轨迹
        this.createAllocationRecord(allocation.getId(), username, AllocationTypeEnum.ENTERED);

    }

    @Override
    public void resendAllAllocation() {
        // 失败的调拨单ID集合
        List<Long> faildIds = Lists.newArrayList();

        // 查找所有失败的调拨单头记录和商品记录
        List<AllocationInf> headerRecords = ityAllocationDAO.findAllocationInfByType(1);
        logger.info("Resend allocaton header record, count=" + headerRecords.size());
        for (AllocationInf record : headerRecords) {
            Map<String, Object> result = ebsSenderService.sendFaildAllocationToEBS(record);
            //if ((Boolean) result.get("success") || String.valueOf(result.get("msg")).contains("ORA-00001")) {
            if ((Boolean) result.get("success")) {
                ityAllocationDAO.deleteAllocationInf(record.getId());
            } else {
                Date now = new Date();
                record.setUpdatedTime(now);
                record.setMsg((String) result.get("msg"));
                record.setTimes(record.getTimes() + 1);
                ityAllocationDAO.insertAllocationInf(record);
                faildIds.add(record.getAllocationId());
            }
        }

        // 查找所有失败的调拨入库记录
        List<AllocationInf> receiveRecords = ityAllocationDAO.findAllocationInfByType(3);
        logger.info("Resend allocaton receive record, count=" + receiveRecords.size());
        for (AllocationInf record : receiveRecords) {
            if (!faildIds.contains(record.getAllocationId())) {
                Map<String, Object> result = ebsSenderService.sendFaildAllocationToEBS(record);
                //if ((Boolean) result.get("success") || String.valueOf(result.get("msg")).contains("ORA-00001")) {
                if ((Boolean) result.get("success")) {
                    ityAllocationDAO.deleteAllocationInf(record.getId());
                } else {
                    Date now = new Date();
                    record.setUpdatedTime(now);
                    record.setMsg((String) result.get("msg"));
                    record.setTimes(record.getTimes() + 1);
                    ityAllocationDAO.insertAllocationInf(record);
                    faildIds.add(record.getAllocationId());
                }
            }
        }
        logger.info("Resend allocaton done, faild allocation ids=" + faildIds);
    }

    @Override
    @Transactional
    public void addAllocation(Allocation allocation, List<AllocationDetail> goodsDetails, ShiroUser shiroUser, Long toStoreId) {

        // 调出门店id
        Long storeId = allocation.getAllocationFrom();
        StoreDetailVO fromStore = maStoreService.queryStoreVOById(storeId);
        StoreDetailVO toStore = maStoreService.queryStoreVOById(toStoreId);

        allocation.setNumber(this.getAllocationNumber());
        allocation.setCreateTime(new Date());
        allocation.setCreator(shiroUser.getLoginName());
        allocation.setModifier(shiroUser.getLoginName());
        allocation.setModifyTime(new Date());
        allocation.setAllocationFromName(fromStore.getStoreName());
        allocation.setAllocationTo(toStore.getStoreId());
        allocation.setAllocationToName(toStore.getStoreName());
        allocation.setCityId(fromStore.getCityCode().getCityId());
        allocation.setCityName(fromStore.getCityCode().getName());
        allocation.setStatus(AllocationTypeEnum.NEW);


        ityAllocationDAO.insertAllocation(allocation);
        for (AllocationDetail detail : goodsDetails) {
            detail.setAllocationId(allocation.getId());
            ityAllocationDAO.insertAllocationDetails(detail);
        }

        // 记录调拨轨迹
        AllocationTrail trail = new AllocationTrail();
        trail.setAllocationId(allocation.getId());
        trail.setOperator(shiroUser.getLoginName());
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

    public void sendAllocationToEBSAndRecord(String number) {
        Allocation allocation = ityAllocationDAO.queryAllocationByNumber(number);
        if (allocation == null) {
            logger.info("单号：" + number + " 调拨单不存在！");
        } else {
            List<AllocationDetail> allocationDetails = ityAllocationDAO.queryDetailsByAllocationId(allocation.getId());
            allocation.setDetails(allocationDetails);
            // 发送接口
            ebsSenderService.sendAllocationToEBSAndRecord(allocation);
        }
    }

    public void sendAllocationReceivedToEBSAndRecord(String number) {
        Allocation allocation = ityAllocationDAO.queryAllocationByNumber(number);
        if (allocation == null) {
            logger.info("单号：" + number + " 调拨单不存在！");
        } else {
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
