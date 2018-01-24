package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.MaPhotoOrderService;
import cn.com.leyizhuang.app.foundation.vo.management.order.PhotoOrderVO;
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
 * @author GenerationRoad
 * @date 2018/1/22
 */
@RestController
@RequestMapping(value = MaPhotoOrderRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaPhotoOrderRestController extends BaseRestController{

    protected static final String PRE_URL = "/rest/order/photo";

    private final Logger logger = LoggerFactory.getLogger(MaPhotoOrderRestController.class);

    @Autowired
    private MaPhotoOrderService maPhotoOrderService;

    /**
     * @title   获取拍照下单列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/11
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<PhotoOrderVO> restPhotoOrderPageGird(Integer offset, Integer size, String keywords, Long cityId, Long storeId) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<PhotoOrderVO> photoOrderVOPageInfo = this.maPhotoOrderService.findAll(page, size, cityId, storeId, keywords);
        return new GridDataVO<PhotoOrderVO>().transform(photoOrderVOPageInfo.getList(), photoOrderVOPageInfo.getTotal());
    }

    /**
     * @title   获取拍照下单明细
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/22
     */
    @GetMapping(value = "/{id}")
    public ResultDTO<PhotoOrderVO> restCusPreDepositLogGet(@PathVariable(value = "id") Long id) {
        PhotoOrderVO photoOrderVO = this.maPhotoOrderService.findById(id);
        if (null == photoOrderVO) {
            logger.warn("查找拍照下单明细失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, photoOrderVO);
        }
    }



}
