package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.management.order.OrderFreightChange;
import cn.com.leyizhuang.app.foundation.service.AdminUserStoreService;
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
@RequestMapping(value = MaOrderFreightRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaOrderFreightRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/orderFreight";

    private final Logger logger = LoggerFactory.getLogger(MaOrderFreightRestController.class);

    @Autowired
    private MaOrderFreightService maOrderFreightService;
    @Autowired
    private AdminUserStoreService adminUserStoreService;

    /**
     * 初始化订单运费列表
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<OrderFreightVO> getOrderFreightVOList(Integer offset, Integer size, String keywords) {
        logger.info("getOrderFreightVOList 后台初始化订单运费列表 ,入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            //查询登录用户门店权限的门店ID
            List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
            PageInfo<OrderFreightVO> orderFreightPage = this.maOrderFreightService.queryPageVO(page, size, storeIds);
            List<OrderFreightVO> orderFreightPageList = orderFreightPage.getList();
            logger.info("getOrderFreightVOList ,后台初始化订单运费列表成功", orderFreightPageList.size());
            return new GridDataVO<OrderFreightVO>().transform(orderFreightPageList, orderFreightPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("getOrderFreightVOList EXCEPTION,发生未知错误，后台初始化订单运费列表失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 根据门店筛选订单
     *
     * @param offset
     * @param size
     * @param keywords
     * @param storeId
     * @return
     */
    @GetMapping(value = "/page/storeGrid")
    public GridDataVO<OrderFreightVO> queryOrderFreightVOByStoreId(Integer offset, Integer size, String keywords, @RequestParam(value = "storeId") Long storeId) {
        logger.info("queryOrderFreightVOByStoreId 后台根据门店筛选订单 ,入参 offset:{},size:{},keywords:{},storeId:{}", offset, size, keywords, storeId);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<OrderFreightVO> orderFreightPage = this.maOrderFreightService.queryOrderFreightVOByStoreId(page, size, storeId);
            List<OrderFreightVO> orderFreightPageList = orderFreightPage.getList();
            logger.info("queryOrderFreightVOByStoreId ,后台根据门店筛选订单成功", orderFreightPageList.size());
            return new GridDataVO<OrderFreightVO>().transform(orderFreightPageList, orderFreightPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("queryOrderFreightVOByStoreId EXCEPTION,发生未知错误，后台根据门店筛选订单失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 根据城市筛选订单
     *
     * @param offset
     * @param size
     * @param keywords
     * @param cityId
     * @return
     */
    @GetMapping(value = "/page/cityGrid")
    public GridDataVO<OrderFreightVO> queryOrderFreightVOByCityId(Integer offset, Integer size, String keywords, @RequestParam(value = "cityId") Long cityId) {
        logger.info("queryOrderFreightVOByCityId 后台根据城市筛选订单 ,入参 offset:{},size:{},keywords:{},cityId:{}", offset, size, keywords, cityId);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<OrderFreightVO> orderFreightPage = this.maOrderFreightService.queryOrderFreightVOByCityId(page, size, cityId);
            List<OrderFreightVO> orderFreightPageList = orderFreightPage.getList();
            logger.info("queryOrderFreightVOByCityId ,后台根据城市筛选订单成功", orderFreightPageList.size());
            return new GridDataVO<OrderFreightVO>().transform(orderFreightPageList, orderFreightPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("queryOrderFreightVOByCityId EXCEPTION,发生未知错误，后台根据城市筛选订单失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 订单运费变更信息
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/freightChangeyGrid")
    public GridDataVO<OrderFreightChangeVO> queryOrderFreightChangeList(Integer offset, Integer size, String keywords) {
        logger.info("queryOrderFreightChangeList 后台查询订单运费变更信息 ,入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<OrderFreightChangeVO> orderFreightChangeVOPage = this.maOrderFreightService.queryOrderFreightChangeList(page, size, keywords);
            List<OrderFreightChangeVO> orderFreightChangePageList = orderFreightChangeVOPage.getList();
            logger.info("queryOrderFreightChangeList ,后台查询订单运费变更信息成功", orderFreightChangePageList.size());
            return new GridDataVO<OrderFreightChangeVO>().transform(orderFreightChangePageList, orderFreightChangeVOPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("queryOrderFreightChangeList EXCEPTION,发生未知错误，后台查询订单运费变更信息失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 根据搜索信息查询订单
     *
     * @param offset
     * @param size
     * @param keywords
     * @param queryOrderInfo
     * @return
     */
    @GetMapping(value = "/page/infoGrid/{queryOrderInfo}")
    public GridDataVO<OrderFreightVO> queryOrderFreightVOByInfo(Integer offset, Integer size, String keywords, @PathVariable(value = "queryOrderInfo") String queryOrderInfo) {
        logger.info("queryOrderFreightVOByInfo 后台根据搜索信息查询订单列表 ,入参 offset:{},size:{},keywords:{},queryOrderInfo:{}", offset, size, keywords, queryOrderInfo);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<OrderFreightVO> orderFreightPage = this.maOrderFreightService.queryOrderFreightVOByInfo(page, size, queryOrderInfo);
            List<OrderFreightVO> orderFreightPageList = orderFreightPage.getList();
            logger.info("queryOrderFreightVOByInfo ,后台根据搜索信息查询订单列表成功", orderFreightPageList.size());
            return new GridDataVO<OrderFreightVO>().transform(orderFreightPageList, orderFreightPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("queryOrderFreightVOByInfo EXCEPTION,发生未知错误，后台根据搜索信息查询订单列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 查询运费信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResultDTO<OrderFreightDetailVO> restDetailOrderFreight(@PathVariable(value = "id") Long id) {
        logger.info("restDetailOrderFreight 后台查询运费详细信息 ,入参 id:{}", id);
        try {
            OrderFreightDetailVO orderFreightDetailVO = this.maOrderFreightService.queryOrderFreightDetailVOById(id);
            if (null == orderFreightDetailVO) {
                logger.warn("查找订单失败：Role(id = {}) == null", id);
                return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                        "指定数据不存在，请联系管理员", null);
            } else {
                logger.info("restDetailOrderFreight ,后台查询运费详细信息成功");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, orderFreightDetailVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restDetailOrderFreight EXCEPTION,发生未知错误，后台查询运费详细信息失败");
            logger.warn("{}", e);
            return null;
        }

    }

    /**
     * 更新运费信息
     *
     * @param
     * @param oderFreightChange
     * @return
     */
    @PutMapping(value = "/update")
    public ResultDTO<?> updateOrderFreight(@Valid OrderFreightChange oderFreightChange, BindingResult result) {
        logger.info("updateOrderFreight 后台更新运费信息 ,入参 oderFreightChange:{},", oderFreightChange);
        try {
            if (!result.hasErrors()) {
                logger.info("updateOrderFreight 后台更新运费信息失败 ,订单id为空");
                if (null == oderFreightChange.getOrderId()) {
                    return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                            "订单id为空", null);
                }
                this.maOrderFreightService.update(oderFreightChange);
                logger.info("updateOrderFreight ,后台更新运费信息成功");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } else {
                List<ObjectError> allErrors = result.getAllErrors();
                logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
                System.err.print(allErrors);
                return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                        errorMsgToHtml(allErrors), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("updateOrderFreight EXCEPTION,发生未知错误，后台更新运费信息失败");
            logger.warn("{}", e);
            return null;
        }
    }

}
