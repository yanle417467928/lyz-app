package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.ItyAllocationDAO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.Allocation;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationQuery;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationVO;
import cn.com.leyizhuang.app.foundation.service.ItyAllocationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void update(Allocation allocation, String operaterdBy) {

    }

    @Override
    public Allocation get(Long id) {
        return null;
    }

    @Override
    public void cancel(Allocation allocation, String username) {

    }

    @Override
    public void send(Allocation allocation, String realNums, String username) {

    }

    @Override
    public void receive(Allocation allocation, String username) {

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
     * @param from
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
}
