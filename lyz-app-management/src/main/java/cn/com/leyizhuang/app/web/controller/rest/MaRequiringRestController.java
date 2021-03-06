package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.requiring.Requiring;
import cn.com.leyizhuang.app.foundation.pojo.inventory.requiring.RequiringVO;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrder;
import cn.com.leyizhuang.app.foundation.service.ItyRequiringService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jerry.Ren
 * Notes:门店库存要货控制器
 * Created with IntelliJ IDEA.
 * Date: 2018/1/16.
 * Time: 18:23.
 */

@RestController
@RequestMapping(value = MaRequiringRestController.PRE_URL, produces = "application/json;charset=utf8")
public class MaRequiringRestController {


    protected final static String PRE_URL = "/rest/store/requiring";

    private final Logger logger = LoggerFactory.getLogger(MaRequiringRestController.class);

    @Autowired
    private ItyRequiringService ityRequiringService;

    /**
     * 要货单列表分页
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<RequiringVO> dataRequiringVOPageGridGet(Integer offset, Integer size, String keywords,Long storeId,Long cityId) {
        logger.info("dataAllocationVOPageGridGet CREATE,门店库存要货管理分页查询, 入参 offset:{},size:{},keywords:{},query:{},storeId:{},cityId:{}", offset, size, keywords,storeId,cityId);
        PageInfo<AtwRequisitionOrder> requisitionOrderPageInfo = ityRequiringService.queryPage(offset, size, keywords,storeId,cityId);
        return new GridDataVO<RequiringVO>().transform(RequiringVO.transform(requisitionOrderPageInfo.getList()), requisitionOrderPageInfo.getTotal());
    }

    /**
     * 根据要货单ID 查要货详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResultDTO<Requiring> getRequiring(@PathVariable(value = "id") Long id) {

        Requiring requiring = this.ityRequiringService.queryRequiringById(id);
        if (null == requiring) {
            logger.warn("查找要货单详情失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, requiring);
        }
    }

    /**
     * 修改管理员备注信息
     *
     * @param atwRequisitionOrder
     * @return
     */
    @PostMapping(value = "/update")
    public ResultDTO<Object> updateInfo(AtwRequisitionOrder atwRequisitionOrder) {

        int count = this.ityRequiringService.updateManagerRemarkInfo(atwRequisitionOrder);

        if (count > 0) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            logger.warn("修改管理员备注信息失败：Role(managerRemarkInfo = {}) == null", atwRequisitionOrder);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "修改失败！请再次尝试", null);
        }
    }
}
