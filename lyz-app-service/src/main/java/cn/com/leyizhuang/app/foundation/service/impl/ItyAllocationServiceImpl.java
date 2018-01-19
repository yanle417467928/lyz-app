package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AllocationTypeEnum;
import cn.com.leyizhuang.app.core.utils.RandomUtil;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.ItyAllocationDAO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.*;
import cn.com.leyizhuang.app.foundation.service.ItyAllocationService;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreDetailVO;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

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

    @Autowired
    private ItyAllocationDAO ityAllocationDAO;

    @Autowired
    private MaStoreService maStoreService;

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
        this.createAllocationRecord(allocation.getId(),username,AllocationTypeEnum.CANCELLED);

    }

    @Override
    @Transactional
    public void sent(Allocation allocation, List<AllocationDetail> allocationDetailList, String username) {
        for (AllocationDetail goods : allocationDetailList) {
            ityAllocationDAO.setDetailDRealQty(allocation.getId(), goods.getGoodsId(), goods.getRealQty());
        }

        // 调拨单状态设置为出库
        allocation.setStatus(AllocationTypeEnum.SENT);
        ityAllocationDAO.updateAllocation(allocation);

        // 新建调拨轨迹
        this.createAllocationRecord(allocation.getId(), username, AllocationTypeEnum.SENT);

        // TODO 调用ebs接口
    }

    @Override
    @Transactional
    public void receive(Allocation allocation, String username) {
        // 调拨单状态设置为入库
        allocation.setStatus(AllocationTypeEnum.ENTERED);
        ityAllocationDAO.updateAllocation(allocation);

        // 新建调拨轨迹
        this.createAllocationRecord(allocation.getId(), username, AllocationTypeEnum.ENTERED);

        // TODO 调用ebs接口
    }

    @Override
    public void resendAllAllocation() {

    }
    /**
     * 创建动态查询条件组合.
     */
//    private Specification<Allocation> buildSpecification(final AllocationQuery allocation) {
//        Specification<Allocation> spec = new Specification<Allocation>() {
//            public Predicate toPredicate(Root<Allocation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                Integer allocationType = allocation.getAllocationType();
//                List<Predicate> list = Lists.newArrayList();
//
//                if (null != allocationType) {
//                    if (1 == allocationType) {
//                        if (null == allocation.getDiySiteId()) {
//                            list.add(cb.and(root.get("allocationFrom").as(Long.class).in(allocation.getDiySiteIds())));
//                        } else {
//                            list.add(cb.and(cb.equal(root.get("allocationFrom").as(Long.class), allocation.getDiySiteId())));
//                        }
//                    } else {
//                        if (null == allocation.getDiySiteId()) {
//                            list.add(cb.and(root.get("allocationTo").as(Long.class).in(allocation.getDiySiteIds())));
//                        } else {
//                            list.add(cb.and(cb.equal(root.get("allocationTo").as(Long.class), allocation.getDiySiteId())));
//                        }
//                    }
//                } else {
//                    if (null == allocation.getDiySiteId()) {
//                        Predicate pre1 = cb.or(root.get("allocationFrom").as(Long.class).in(allocation.getDiySiteIds()));
//                        Predicate pre2 = cb.or(root.get("allocationTo").as(Long.class).in(allocation.getDiySiteIds()));
//                        list.add(cb.and(cb.or(pre1, pre2)));
//                    } else {
//                        Predicate pre1 = cb.equal(root.get("allocationFrom").as(Long.class), allocation.getDiySiteId());
//                        Predicate pre2 = cb.equal(root.get("allocationTo").as(Long.class), allocation.getDiySiteId());
//                        list.add(cb.and(cb.or(pre1, pre2)));
//                    }
//                }
//
//                if (null != allocation.getStatus()) {
//                    list.add(cb.and(cb.equal(root.get("status").as(Integer.class), allocation.getStatus())));
//                }
//
//                if (null != allocation.getStartTime()) {
//                    list.add(cb.and(cb.greaterThanOrEqualTo(root.get("updatedTime").as(Date.class), allocation.getStartTime())));
//                }
//
//                if (null != allocation.getEndTime()) {
//                    list.add(cb.and(cb.lessThanOrEqualTo(root.get("updatedTime").as(Date.class), allocation.getEndTime())));
//                }
//
//                if (StringUtils.isNotBlank(allocation.getNumber())) {
//                    list.add(cb.and(cb.like(root.get("number").as(String.class), "%" + allocation.getNumber() + "%")));
//                }
//
//                Predicate[] p = new Predicate[list.size()];
//                return cb.and(list.toArray(p));
//            }
//        };
//        return spec;
//    }

    /**
     * 从门店库存记录创建一条新的门店库存记录
     *
     * @param
     * @return
     */
//    private TdDiySiteInventory copyTdDiySiteInventoryFromAnother(TdDiySiteInventory from) {
//        TdDiySiteInventory tdDiySiteInventory = new TdDiySiteInventory();
//        tdDiySiteInventory.setCategoryId(from.getCategoryId());
//        tdDiySiteInventory.setCategoryIdTree(from.getCategoryIdTree());
//        tdDiySiteInventory.setCategoryTitle(from.getCategoryTitle());
//        tdDiySiteInventory.setGoodsCode(from.getGoodsCode());
//        tdDiySiteInventory.setGoodsId(from.getGoodsId());
//        tdDiySiteInventory.setGoodsTitle(from.getGoodsTitle());
//        tdDiySiteInventory.setRegionId(from.getRegionId());
//        tdDiySiteInventory.setRegionName(from.getRegionName());
//        return tdDiySiteInventory;
//    }
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
}
