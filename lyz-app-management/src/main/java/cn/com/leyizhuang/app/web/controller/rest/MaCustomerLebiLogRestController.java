package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.AdminUserStoreService;
import cn.com.leyizhuang.app.foundation.service.MaCusLebiLogService;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CusLebiLogVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CusPreDepositLogVO;
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

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/13
 */
@RestController
@RequestMapping(value = MaCustomerLebiLogRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCustomerLebiLogRestController extends BaseRestController {

    protected static final String PRE_URL = "/rest/customer/lebi/log";

    private final Logger logger = LoggerFactory.getLogger(MaCustomerLebiLogRestController.class);

    @Autowired
    private MaCusLebiLogService maCusLebiLogService;

    @Autowired
    private AdminUserStoreService adminUserStoreService;

    /**
     * @title   顾客乐币变更明细列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/11
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<CusLebiLogVO> restCustomerLebiPageGird(Integer offset, Integer size, Long cusId, String keywords, Long cityId, Long storeId) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
        PageInfo<CusLebiLogVO> cusLebiLogVOPageInfo = this.maCusLebiLogService.findAllCusLebiLog(page, size, cusId, cityId, storeId, keywords, storeIds);
        return new GridDataVO<CusLebiLogVO>().transform(cusLebiLogVOPageInfo.getList(), cusLebiLogVOPageInfo.getTotal());
    }

    /**
     * @title   查询顾客乐币变更明细
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/15
     */
    @GetMapping(value = "/{id}")
    public ResultDTO<CusLebiLogVO> restCusPreDepositLogGet(@PathVariable(value = "id") Long id) {
        CusLebiLogVO cusLebiLogVO = this.maCusLebiLogService.findCusLebiLogById(id);
        if (null == cusLebiLogVO) {
            logger.warn("查找顾客乐币变更明细失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, cusLebiLogVO);
        }
    }
}
