package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.foundation.pojo.CashCouponGoods;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.Allocation;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationDetail;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationQuery;
import cn.com.leyizhuang.app.foundation.pojo.inventory.allocation.AllocationVO;
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
}
