package cn.com.leyizhuang.app.web.controller.views.order;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.response.CustomerRankInfoResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.DeliveryAddressResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.MaUpdateMaterialResponse;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.MaMaterialListService;
import cn.com.leyizhuang.app.foundation.service.MaPhotoOrderService;
import cn.com.leyizhuang.app.foundation.vo.management.order.PhotoOrderVO;
import cn.com.leyizhuang.app.web.controller.BaseController;
import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/15
 */
@Controller
@RequestMapping(value = MaPhotoOrderViewsController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaPhotoOrderViewsController extends BaseController {
    protected final static String PRE_URL = "/views/admin/order/photo";

    private final Logger logger = LoggerFactory.getLogger(MaPhotoOrderViewsController.class);

    @Autowired
    private MaPhotoOrderService maPhotoOrderService;

    @Resource
    private MaMaterialListService maMaterialListService;

    @Autowired
    private AppCustomerService appCustomerService;
    /**
     * @title   跳转拍照下单列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/22
     */
    @RequestMapping(value = "/list")
    public String getPhotoOrderList(Model model) {
        logger.info("拍照下单列表");
        model.addAttribute("photoOrderStatus", PhotoOrderStatus.values());
        return "/views/order/photo_order_page";
    }


    /**
     * @title   跳转新增订单页面
     * @descripe
     * @param
     * @return
     * @throws
     */
    @RequestMapping(value = "add")
    public String addPhotoOrder() {
        return "/views/order/add_photo_order_edit";
    }

    @GetMapping(value = "/edit/{id}")
    public String resourceEdit(ModelMap map, @PathVariable(value = "id") Long id) {
        List<PhotoOrderStatus> status = new ArrayList<>();
        status.add(PhotoOrderStatus.PENDING);
        status.add(PhotoOrderStatus.PROCESSING);
        PhotoOrderVO photoOrderVO = this.maPhotoOrderService.findByIdAndStatus(id, status);

        if (null == photoOrderVO) {
            logger.warn("跳转拍照下单详情页面失败，photoOrderVO(id = {}) == null", id);
            error404();
            return "/error/404";
        } else {
            if("顾客".equals(photoOrderVO.getIdentityType().toString())){
                CustomerRankInfoResponse customerRankInfoResponse = appCustomerService.findCusRankinfoByCusId(photoOrderVO.getUserId());
                map.addAttribute("cusRank", customerRankInfoResponse);
            }
            this.maPhotoOrderService.updateStatus(id, PhotoOrderStatus.PROCESSING);
            map.addAttribute("photoOrderVO", photoOrderVO);
        }
        return "/views/order/photo_order_edit";
    }

    /**
     * 跳转到修改拍照下单页面
     * @param map
     * @return
     */
    @RequestMapping(value = "/update/{photoNo}")
    public String updatePhotoOrderGoods(ModelMap map, @PathVariable(value = "photoNo")String photoNo){
        logger.info("updatePhotoOrderGoods 入参 photoNo:{}",photoNo);

        PhotoOrderVO photoOrderVO = this.maPhotoOrderService.findByPhotoOrderNo(photoNo);
        if("顾客".equals(photoOrderVO.getIdentityType().toString())){
            CustomerRankInfoResponse customerRankInfoResponse = appCustomerService.findCusRankinfoByCusId(photoOrderVO.getUserId());
            map.addAttribute("cusRank", customerRankInfoResponse);
        }
        map.addAttribute("photoOrderVO",photoOrderVO);
        return "/views/order/photo_order_update";
    }

}
