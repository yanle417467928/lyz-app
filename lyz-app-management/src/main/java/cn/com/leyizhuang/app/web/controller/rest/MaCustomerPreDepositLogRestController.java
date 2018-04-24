package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.AdminUserStoreService;
import cn.com.leyizhuang.app.foundation.service.MaCusPreDepositLogService;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CusPreDepositLogVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerPreDepositVO;
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
 * @date 2018/1/11
 */
@RestController
@RequestMapping(value = MaCustomerPreDepositLogRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCustomerPreDepositLogRestController extends BaseRestController{
    protected static final String PRE_URL = "/rest/customer/preDeposit/log";

    private final Logger logger = LoggerFactory.getLogger(MaCustomerPreDepositLogRestController.class);

    @Autowired
    private MaCusPreDepositLogService maCusPreDepositLogService;

    @Autowired
    private AdminUserStoreService adminUserStoreService;

    /**
     * @title   顾客预存款变更明细列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/11
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<CusPreDepositLogVO> restCustomerPreDepositPageGird(Integer offset, Integer size, Long cusId, String keywords, Long cityId, Long storeId, String changeType) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
        PageInfo<CusPreDepositLogVO> custmerPreLogPage = this.maCusPreDepositLogService.findAllCusPredepositLog(page, size, cusId, cityId, storeId, keywords, storeIds, changeType);
        return new GridDataVO<CusPreDepositLogVO>().transform(custmerPreLogPage.getList(), custmerPreLogPage.getTotal());
    }

    /**
     * @title   查询顾客预存款变更明细
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/11
     */
    @GetMapping(value = "/{id}")
    public ResultDTO<CusPreDepositLogVO> restCusPreDepositLogGet(@PathVariable(value = "id") Long id) {
        CusPreDepositLogVO cusPreDepositLogVO = this.maCusPreDepositLogService.findCusPredepositLogById(id);
        if (null == cusPreDepositLogVO) {
            logger.warn("查找顾客预存款变更明细失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, cusPreDepositLogVO);
        }
    }

}
