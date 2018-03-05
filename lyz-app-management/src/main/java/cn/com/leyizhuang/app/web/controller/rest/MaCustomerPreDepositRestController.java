package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.AppRechargeOrderStatus;
import cn.com.leyizhuang.app.core.constant.CustomerPreDepositChangeType;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dto.CusPreDepositDTO;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeOrder;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeReceiptInfo;
import cn.com.leyizhuang.app.foundation.service.AdminUserStoreService;
import cn.com.leyizhuang.app.foundation.service.MaCustomerService;
import cn.com.leyizhuang.app.foundation.service.RechargeService;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerPreDepositVO;
import cn.com.leyizhuang.app.remote.queue.MaSinkSender;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/8
 */
@RestController
@RequestMapping(value = MaCustomerPreDepositRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCustomerPreDepositRestController extends BaseRestController {

    protected static final String PRE_URL = "/rest/customer/preDeposit";

    private final Logger logger = LoggerFactory.getLogger(MaCustomerPreDepositRestController.class);

    @Autowired
    private MaCustomerService maCustomerService;

    @Autowired
    private AdminUserStoreService adminUserStoreService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private MaSinkSender sinkSender;

    /**
     * @title   获取顾客预存款列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/11
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<CustomerPreDepositVO> restCustomerPreDepositPageGird(Integer offset, Integer size, String keywords, Long cityId, Long storeId) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
        PageInfo<CustomerPreDepositVO> custmerPrePage = this.maCustomerService.findAllCusPredeposit(page, size, cityId, storeId, keywords, storeIds);
        return new GridDataVO<CustomerPreDepositVO>().transform(custmerPrePage.getList(), custmerPrePage.getTotal());
    }

    /**
     * @title   顾客预存款变更及日志保存
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/11
     */
    @PostMapping(value = "/edit")
    public ResultDTO<String> modifyPreDeposit(@Valid CusPreDepositDTO cusPreDepositDTO, BindingResult result) {
        logger.info("顾客预存款变更及日志保存 modifyPreDeposit 入参 cusPreDepositDTO{}，result", cusPreDepositDTO, result);
        if (!result.hasErrors()) {
            if (null != cusPreDepositDTO && null != cusPreDepositDTO.getCusId() && cusPreDepositDTO.getCusId() != 0){
                if (null != cusPreDepositDTO.getChangeMoney() && cusPreDepositDTO.getChangeMoney() != 0) {
                    try {
                        Long cityId = this.maCustomerService.findCityIdByCusId(cusPreDepositDTO.getCusId());
                        //生成单号
                        String rechargeNo = OrderUtils.generateRechargeNumber(cityId);

                        cusPreDepositDTO.setChangeType(CustomerPreDepositChangeType.ADMIN_CHANGE);
                        this.maCustomerService.changeCusPredepositByCusId(cusPreDepositDTO);
                        //生成充值单
                        RechargeOrder rechargeOrder = rechargeService.createCusRechargeOrder(AppIdentityType.CUSTOMER.getValue(), cusPreDepositDTO.getCusId(),
                                cusPreDepositDTO.getChangeMoney(), rechargeNo, cusPreDepositDTO.getPayType());


                        rechargeService.saveRechargeOrder(rechargeOrder);

                        //创建充值单收款
                        RechargeReceiptInfo receiptInfo = rechargeService.createCusPayRechargeReceiptInfo(AppIdentityType.CUSTOMER.getValue(), cusPreDepositDTO, rechargeNo);
                        rechargeService.saveRechargeReceiptInfo(receiptInfo);

                        //将收款记录入拆单消息队列
                        sinkSender.sendRechargeReceipt(rechargeNo);
                    } catch (Exception e) {
                        e.printStackTrace();
                        List<ObjectError> allErrors = result.getAllErrors();
                        logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
                        return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, e.getMessage(), null);
                    }
                    logger.info("顾客预存款变更及日志保存成功", null,null);
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                } else{
                    logger.info("顾客预存款变更及日志保存失败", null,null);
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "变更金额不能为零！", null);
                }
            } else {
                logger.info("顾客预存款变更及日志保存失败", null,null);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "信息错误！", null);
            }

        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }



}
