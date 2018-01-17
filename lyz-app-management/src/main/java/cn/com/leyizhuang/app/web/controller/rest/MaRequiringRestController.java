package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.requiring.Requiring;
import cn.com.leyizhuang.app.foundation.pojo.inventory.requiring.RequiringVO;
import cn.com.leyizhuang.app.foundation.service.ItyRequiringService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public GridDataVO<RequiringVO> dataRequiringVOPageGridGet(Integer offset, Integer size, String keywords) {

        logger.info("dataAllocationVOPageGridGet CREATE,门店库存调拨分页查询, 入参 offset:{},size:{},keywords:{},query:{}", offset, size, keywords);

        PageInfo<RequiringVO> allocationVOPageInfo = ityRequiringService.queryPage(offset, size, keywords);
        return new GridDataVO<RequiringVO>().transform(allocationVOPageInfo.getList(), allocationVOPageInfo.getTotal());
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
}
