package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.management.order.OrderFreightChange;
import cn.com.leyizhuang.app.foundation.pojo.management.order.SimpleOrderBillingDetails;
import cn.com.leyizhuang.app.foundation.service.MaOrderFreightService;
import cn.com.leyizhuang.app.foundation.vo.management.freight.OrderFreightChangeVO;
import cn.com.leyizhuang.app.foundation.vo.management.freight.OrderFreightDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.freight.OrderFreightVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = MaOrderFreightRestController.PRE_URL,produces = "application/json;charset=utf-8")
public class MaOrderFreightRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/orderFreight";

    private final Logger logger = LoggerFactory.getLogger(MaOrderFreightRestController.class);

    @Autowired
    private MaOrderFreightService maOrderFreightService;

    /**
     * 初始化订单运费列表
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<OrderFreightVO> getOrderFreightVOList(Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<OrderFreightVO> orderFreightPage = this.maOrderFreightService.queryPageVO(page, size);
        List<OrderFreightVO> orderFreightPageList = orderFreightPage.getList();
        return new GridDataVO<OrderFreightVO>().transform(orderFreightPageList, orderFreightPage.getTotal());
    }


    /**
     * 根据门店筛选order
     *
     * @param offset
     * @param size
     * @param keywords
     * @param storeId
     * @return
     */
    @GetMapping(value = "/page/storeGrid")
    public GridDataVO<OrderFreightVO> queryOrderFreightVOByStoreId(Integer offset, Integer size, String keywords, @RequestParam(value = "storeId") Long storeId) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<OrderFreightVO> orderFreightPage = this.maOrderFreightService.queryOrderFreightVOByStoreId(page, size,storeId);
        List<OrderFreightVO> orderFreightPageList = orderFreightPage.getList();
        return new GridDataVO<OrderFreightVO>().transform(orderFreightPageList, orderFreightPage.getTotal());
    }

    /**
     * 根据城市筛选order
     *
     * @param offset
     * @param size
     * @param keywords
     * @param cityId
     * @return
     */
    @GetMapping(value = "/page/cityGrid")
    public GridDataVO<OrderFreightVO> queryOrderFreightVOByCityId(Integer offset, Integer size, String keywords, @RequestParam(value = "cityId") Long cityId) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<OrderFreightVO> orderFreightPage = this.maOrderFreightService.queryOrderFreightVOByCityId(page, size,cityId);
        List<OrderFreightVO> orderFreightPageList = orderFreightPage.getList();
        return new GridDataVO<OrderFreightVO>().transform(orderFreightPageList, orderFreightPage.getTotal());
    }


    /**
     * 订单运费变更信息
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/freightChangeyGrid")
    public GridDataVO<OrderFreightChangeVO> queryOrderFreightChangeList(Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<OrderFreightChangeVO> orderFreightChangeVOPage = this.maOrderFreightService.queryOrderFreightChangeList(page, size);
        List<OrderFreightChangeVO> orderFreightChangePageList = orderFreightChangeVOPage.getList();
        return new GridDataVO<OrderFreightChangeVO>().transform(orderFreightChangePageList, orderFreightChangeVOPage.getTotal());
    }


    /**
     * 根据搜索信息查询order
     *
     * @param offset
     * @param size
     * @param keywords
     * @param queryOrderInfo
     * @return
     */
    @GetMapping(value = "/page/infoGrid/{queryOrderInfo}")
    public GridDataVO<OrderFreightVO> queryOrderFreightVOByInfo(Integer offset, Integer size, String keywords,@PathVariable(value = "queryOrderInfo") String queryOrderInfo) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<OrderFreightVO> orderFreightPage = this.maOrderFreightService.queryOrderFreightVOByInfo(page, size,queryOrderInfo);
        List<OrderFreightVO> orderFreightPageList = orderFreightPage.getList();
        return new GridDataVO<OrderFreightVO>().transform(orderFreightPageList, orderFreightPage.getTotal());
    }

    /**
     * 查询运费信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResultDTO<OrderFreightDetailVO> restDetailOrderFreight(@PathVariable(value = "id") Long id) {
        OrderFreightDetailVO orderFreightDetailVO = this.maOrderFreightService.queryOrderFreightDetailVOById(id);
        if (null == orderFreightDetailVO) {
            logger.warn("查找订单失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, orderFreightDetailVO);
        }
    }

    @PutMapping(value = "/update")
    public ResultDTO<?> updateOrderFreight(@Valid SimpleOrderBillingDetails simpleOrderBillingDetails, @Valid OrderFreightChange oderFreightChange, BindingResult result) {
        if (!result.hasErrors()) {
        this.maOrderFreightService.update(simpleOrderBillingDetails,oderFreightChange);
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            System.err.print(allErrors);
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }
}
