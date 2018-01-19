package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AllocationTypeEnum;
import cn.com.leyizhuang.app.foundation.pojo.CashCouponGoods;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.Allocation;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationDetail;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationQuery;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationVO;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.ItyAllocationService;
import cn.com.leyizhuang.app.web.controller.BaseController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:门店库存调拨控制器
 * Created with IntelliJ IDEA.
 * Date: 2018/1/3.
 * Time: 14:13.
 */
@RestController
@RequestMapping(value = MaAllocationRestController.PRE_URL, produces = "application/json;charset=utf8")
public class MaAllocationRestController extends BaseRestController{

    protected final static String PRE_URL = "/rest/store/allocation";

    private final Logger logger = LoggerFactory.getLogger(MaAllocationRestController.class);

    @Autowired
    private ItyAllocationService ityAllocationService;

    @Autowired
    private AppOrderService orderService;

    @GetMapping(value = "/page/grid")
    public GridDataVO<AllocationVO> dataAllocationVOPageGridGet(Integer offset, Integer size, String keywords, AllocationQuery query) {

        logger.info("dataAllocationVOPageGridGet CREATE,门店库存调拨分页查询, 入参 offset:{},size:{},keywords:{},query:{}", offset, size, keywords, query);

        PageInfo<AllocationVO> allocationVOPageInfo = ityAllocationService.queryPage(offset, size, keywords, query);
        return new GridDataVO<AllocationVO>().transform(allocationVOPageInfo.getList(), allocationVOPageInfo.getTotal());
    }

    /**
     * 根据调拨单ID 查调拨详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResultDTO<Allocation> getAllocation(@PathVariable(value = "id") Long id) {

        Allocation allocation = this.ityAllocationService.queryAllocationById(id);
        if (null == allocation) {
            logger.warn("查找调拨详情失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, allocation);
        }
    }

    @PostMapping("/save")
    public ResultDTO<?> save(Allocation allocation,String goodsList) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, AllocationDetail.class);
        List<AllocationDetail> allocationDetailList = objectMapper.readValue(goodsList,javaType1);

        if(allocation == null){
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "调拨单数据有误，请联系管理员", null);
        }
        if (allocationDetailList == null || allocationDetailList.size() == 0){
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "调拨商品数据有误，请联系管理员", null);
        }

        // 当前登录帐号
        ShiroUser shiroUser = super.getShiroUser();

        ityAllocationService.addAllocation(allocation,allocationDetailList,shiroUser);
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "调拨单新增成功！", allocation);
    }

    /**
     * 出库
     * @param allocationId
     * @param details
     * @return
     */
    @PutMapping("/sent")
    public ResultDTO<?> sent(Long allocationId, String details) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, AllocationDetail.class);
        List<AllocationDetail> allocationDetailList = objectMapper.readValue(details,javaType1);

        // 当前登录帐号
        ShiroUser user = super.getShiroUser();
        Allocation allocation = ityAllocationService.queryAllocationById(allocationId);
        allocation.setModifier(user.getLoginName());
        allocation.setModifyTime(new Date());

        if (allocation == null){
            logger.info("ID:"+allocationId+",调拨单数据不存在");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "调拨单数据不存在，请联系关理员", null);
        }

        if (!allocation.getStatus().equals(AllocationTypeEnum.NEW)){
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "调拨单无法出库，请联系管理员", null);
        }

        for (AllocationDetail goods : allocationDetailList){
            // 检查门店库存
            Boolean inventoryFlag = orderService.existGoodsStoreInventory(allocation.getAllocationFrom(),goods.getGoodsId(),goods.getRealQty());

            if (!inventoryFlag){
                logger.info(goods.getSku()+" 库存不足");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                        "调拨产品："+goods.getSkuName() +" 库存不足", null);
            }
        }

        ityAllocationService.sent(allocation,allocationDetailList,user.getLoginName());
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,
                "出库成功", null);
    }

    /**
     * 入库
     * @param allocationId
     * @return
     */
    @PutMapping("/entered")
    public ResultDTO<?> entered(Long allocationId){
        // 当前登录帐号
        ShiroUser user = super.getShiroUser();

        Allocation allocation = ityAllocationService.queryAllocationById(allocationId);
        allocation.setModifier(user.getLoginName());
        allocation.setModifyTime(new Date());

        if (allocation == null){
            logger.info("ID:"+allocationId+",调拨单数据不存在");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "调拨单数据不存在，请联系关理员", null);
        }

        if (!allocation.getStatus().equals(AllocationTypeEnum.SENT)){
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "调拨单未出库，请联系管理员", null);
        }

        ityAllocationService.receive(allocation,user.getLoginName());

        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,
                "入库成功", null);
    }

    /**
     * 作废
     * @param allocationId
     * @return
     */
    @PutMapping("/cancelled")
    public ResultDTO<?> cancelled(Long allocationId){
        // 当前登录帐号
        ShiroUser user = super.getShiroUser();

        Allocation allocation = ityAllocationService.queryAllocationById(allocationId);
        allocation.setModifier(user.getLoginName());
        allocation.setModifyTime(new Date());

        if (allocation == null){
            logger.info("ID:"+allocationId+",调拨单数据不存在");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "调拨单数据不存在，请联系关理员", null);
        }

        if (allocation.getStatus().equals(AllocationTypeEnum.SENT)){
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "调拨单已出库", null);
        }

        ityAllocationService.cancel(allocation,user.getLoginName());
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,
                "作废成功", null);
    }
}
