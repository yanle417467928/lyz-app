package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillPaymentData;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentGoodsDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaActGoodsMapping;
import cn.com.leyizhuang.app.foundation.pojo.management.store.StoreDO;
import cn.com.leyizhuang.app.foundation.pojo.request.BillorderDetailsRequest;
import cn.com.leyizhuang.app.foundation.pojo.response.BillInfoResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.BillRepaymentGoodsInfoResponse;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.management.BillRuleLogVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.*;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后台装饰公司账单管理
 * Created by liuh on 2017/12/16.
 */
@RestController
@RequestMapping(value = MaFitBillRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaFitBillRestController extends BaseRestController {
    protected final static String PRE_URL = "/rest/fitBill";

    private final Logger logger = LoggerFactory.getLogger(MaFitBillRestController.class);

    @Resource
    private MaFitBillService maFitBillService;

    @Resource
    private AdminUserStoreService adminUserStoreService;

    @Resource
    private BillInfoService billInfoService;

    @Resource
    private MaStoreService maStoreService;

    @Resource
    private BillRuleService billRuleService;

    /**
     * 后台分页查询装饰公司未出账单
     *
     * @param offset   当前页
     * @param size     每页条数
     * @param keywords 不知
     * @return 订单列表
     */
    @GetMapping(value = "/notOut/page/grid")
    public GridDataVO<DecorativeCompanyDetailVO> restFitBillPageGird(Integer offset, Integer size, String keywords, Long cityId, String fitCompayType) {
        logger.warn("restFitBillPageGird 后台分页获取所有装饰公司未出账单 ,入参offsetL:{}, size:{}, kewords:{},fitCompayType:{}",
                offset, size, keywords,cityId, fitCompayType);
        try {
            //查询登录用户门店权限的门店ID
            List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
            size = getSize(size);
            Integer page = getPage(offset, size);
            keywords = keywords.trim();
            PageInfo<StoreDO> storePage = this.maStoreService.queryDecorativeCompanyList(page, size,storeIds,cityId,keywords,fitCompayType);
            List<StoreDO> storesList = storePage.getList();
            List<DecorativeCompanyDetailVO> pageAllDecorativeCompanyList = DecorativeCompanyDetailVO.transform(storesList);
            logger.info("restDecorativeCompanyPageGird ,后台初始化装饰公司列表成功", pageAllDecorativeCompanyList.size());
            return new GridDataVO<DecorativeCompanyDetailVO>().transform(pageAllDecorativeCompanyList, storePage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restFitBillPageGird EXCEPTION,发生未知错误，后台分页获取所有装饰公司未出账单失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 后台分页查询装饰公司历史账单
     *
     * @param offset   当前页
     * @param size     每页条数
     * @param keywords 不知
     * @return 订单列表
     */
    @GetMapping(value = "/history/page/grid")
    public GridDataVO<MaFitBillVO> restHistoryFitBillPageGird(Integer offset, Integer size, String keywords, Long storeId) {
        logger.warn("restFitBillPageGird 后台分页查询装饰公司历史账单 ,入参offsetL:{}, size:{}, kewords:{}", offset, size, keywords);
        try {
            //查询登录用户门店权限的门店ID
            List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
            size = getSize(size);
            Integer page = getPage(offset, size);
            if (null != storeId && -1L != storeId && storeIds.contains(storeId)) {
                storeIds.clear();
                storeIds.add(storeId);
            }
            keywords = keywords.trim();
            PageInfo<MaFitBillVO> maFitBillPageInfo = maFitBillService.getHistoryFitBill(page, size, storeIds, keywords);
            logger.warn("restFitBillPageGird ,后台分页查询装饰公司历史账单成功", maFitBillPageInfo.getList().size());
            return new GridDataVO<MaFitBillVO>().transform(maFitBillPageInfo.getList(), maFitBillPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restFitBillPageGird EXCEPTION,发生未知错误，后台分页查询装饰公司历史账单失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 后台分页查询装饰公司未付账单明细
     *
     * @param offset 当前页
     * @param size   每页条数
     * @param
     * @return 订单列表
     */
    @GetMapping(value = "/noPayOrderBill/page/{storeId}")
    public GridDataVO<BillRepaymentGoodsInfoResponse> restNoPayOrderBillPageGird(Integer offset, Integer size, @PathVariable Long storeId, String startTime, String endTime, ModelMap map) {
        logger.warn("restFitBillPageGird 后台分页查询装饰公司未付账单明细 ,入参offsetL:{}, size:{},storeId:{},startTime:{},endTime:{}", offset, size, storeId, startTime, endTime);
        try {
          /*  size = getSize(size);
            Integer page = getPage(offset, size);*/
            startTime = startTime.trim();
            endTime = endTime.trim();
            if (null != startTime && !"".equals(startTime)) {
                startTime += " 00:00:00";
            }
            if (null != endTime && !"".equals(endTime)) {
                endTime += " 23:59:59";
            }
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime endTimeL = null;
            if (null != endTime && !"".equals(endTime)) {
                endTimeL = LocalDateTime.parse(endTime, df);
            }
            LocalDateTime startTimeL = null;
            if (null != startTime && !"".equals(startTime)) {
                startTimeL = LocalDateTime.parse(startTime, df);
            }
            BillInfoResponse billInfoResponse = billInfoService.lookBill(startTimeL, endTimeL, storeId, null, null);
            //map.addAttribute("billInfoResponse", billInfoResponse);
            //PageInfo<BillRepaymentGoodsInfoResponse>  billRepaymentGoodsInfoResponseList = maFitBillService.getNoPayOrderBillByBillNo( page,size,maFitBillVO.getStoreId(), startTime, endTime, orderNo);
            return new GridDataVO<BillRepaymentGoodsInfoResponse>().transform(billInfoResponse.getNotPayOrderDetails(), (long)billInfoResponse.getNotPayOrderDetails().size());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restFitBillPageGird EXCEPTION,发生未知错误，后台分页查询装饰公司未付账单明细失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 后台分页查询装饰公司未付账单明细
     *
     * @param offset 当前页
     * @param size   每页条数
     * @param
     * @return 订单列表
     */
    @GetMapping(value = "/billInfo")
    public ResultDTO<Object> restNoPayOrderBillPageGird(Integer offset, Integer size,  Long storeId, String startTime, String endTime) {
        logger.warn("restFitBillPageGird 后台分页查询装饰公司未付账单明细 ,入参offsetL:{}, size:{},storeId:{},startTime:{},endTime:{},orderNo:{}", offset, size, storeId, startTime, endTime);
        ResultDTO<Object> resultDTO;
        try {

            startTime = startTime.trim();
            endTime = endTime.trim();
            if (null != startTime && !"".equals(startTime)) {
                startTime += " 00:00:00";
            }
            if (null != endTime && !"".equals(endTime)) {
                endTime += " 23:59:59";
            }
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime endTimeL = null;
            if (null != endTime && !"".equals(endTime)) {
                endTimeL = LocalDateTime.parse(endTime, df);
            }
            LocalDateTime startTimeL = null;
            if (null != startTime && !"".equals(startTime)) {
                startTimeL = LocalDateTime.parse(startTime, df);
            }
            BillInfoResponse billInfoResponse = billInfoService.lookBill(startTimeL, endTimeL, storeId, null, null);
            billInfoResponse.setNotPayOrderDetails(null);
            billInfoResponse.setPaidOrderDetails(null);
            if (billInfoResponse.getBillNo() == null){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "请联系管理员，创建账单规则！", null);
                logger.info("restFitBillPageGird OUT,后台分页查询装饰公司未付账单明细失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,billInfoResponse );
            //map.addAttribute("billInfoResponse", billInfoResponse);
            //PageInfo<BillRepaymentGoodsInfoResponse>  billRepaymentGoodsInfoResponseList = maFitBillService.getNoPayOrderBillByBillNo( page,size,maFitBillVO.getStoreId(), startTime, endTime, orderNo);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restFitBillPageGird EXCEPTION,发生未知错误，后台分页查询装饰公司未付账单明细失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 后台分页查询装饰公司已付账单明细
     *
     * @param offset 当前页
     * @param size   每页条数
     * @param
     * @return 订单列表
     */
    @GetMapping(value = "/payOrderBill/page/{billNo}")
    public GridDataVO<BillRepaymentInfoVO> restPayBillPaymentPageGird(Integer offset, Integer size, @PathVariable String billNo, String startTime, String endTime, String repaymentNo) {
        logger.warn("restFitBillPageGird 后台分页查询装饰公司已付账单明细 ,入参offsetL:{}, size:{}, kewords:{},billNo:{},startTime:{},endTime:{},repaymentNo:{}", offset, size, billNo, startTime, endTime, repaymentNo);
        if (null == billNo) {
            logger.warn("restFitBillPageGird EXCEPTION,发生未知错误，后台分页查询装饰公司已付账单明细失败");
            return null;
        }
        try {
            //查询登录用户门店权限的门店ID
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<BillRepaymentInfoVO> maBillRepaymentInfoPageInfo = maFitBillService.getbillRepaymentInfoByBillNo(page, size, billNo, startTime, endTime, repaymentNo);
            logger.warn("restFitBillPageGird ,后台分页查询装饰公司已付账单明细成功", maBillRepaymentInfoPageInfo.getList().size());
            return new GridDataVO<BillRepaymentInfoVO>().transform(maBillRepaymentInfoPageInfo.getList(), maBillRepaymentInfoPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restFitBillPageGird EXCEPTION,发生未知错误，后台分页查询装饰公司已付账单明细失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 后台分页查询装饰公司已付账单订单明细
     *
     * @param offset 当前页
     * @param size   每页条数
     * @param
     * @return 订单列表
     */
    @GetMapping(value = "/billOrderDetail/page/{repaymentNo}")
    public GridDataVO<BillRepaymentGoodsDetailsVO> restPayBillOrderPageGird(Integer offset, Integer size, @PathVariable String repaymentNo) {
        logger.warn("restFitBillPageGird 后台分页查询装饰公司已付账单订单明细 ,入参offsetL:{}, size:{}, kewords:{},repaymentNo:{}", offset, size, repaymentNo);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<BillRepaymentGoodsDetailsVO> maBillRepaymentOrderInfoPageInfo = maFitBillService.getbillRepaymentOrderInfoByBillNo(page, size, repaymentNo);
            logger.warn("restPayBillOrderPageGird ,后台分页查询装饰公司已付账单订单明细成功", maBillRepaymentOrderInfoPageInfo.getList().size());
            return new GridDataVO<BillRepaymentGoodsDetailsVO>().transform(maBillRepaymentOrderInfoPageInfo.getList(), maBillRepaymentOrderInfoPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restPayBillOrderPageGird EXCEPTION,发生未知错误，后台分页查询装饰公司已付账单订单明细失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 后台账单收款
     */
    @GetMapping(value = "/payBill")
    public ResultDTO<String> PayBill(BillPaymentData billPaymentData, String billorderDetailsRequest) {
        logger.warn("PayBill 后台账单收款 ,billRepaymentInfoDO:{}", billPaymentData);
        ResultDTO<String> resultDTO;
        if (null != billPaymentData) {
            if (null == billPaymentData.getBillNo()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "账单号不允许为空", null);
                logger.warn("PayBill OUT,后台账单收款失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, BillorderDetailsRequest.class);
                List<BillorderDetailsRequest> billorderDetailsRequestList = objectMapper.readValue(billorderDetailsRequest, javaType1);
                String billNo = billPaymentData.getBillNo();
                BillInfoDO maFitBillVO = maFitBillService.getFitBillByBillNo(billNo);
                List<Long> orderIds = new ArrayList<>();
                List<Long> returnIds = new ArrayList<>();
                for (BillorderDetailsRequest request : billorderDetailsRequestList){
                    if (request.getOrderType().equals("order")){
                        orderIds.add(request.getId());
                    }else if (request.getOrderType().equals("return")){
                        returnIds.add(request.getId());
                    }
                }
                List<BillRepaymentGoodsInfoResponse> paidOrderDetails = billInfoService.findPaidOrderDetailsByOids(orderIds,maFitBillVO.getStoreId());
                if (paidOrderDetails != null && paidOrderDetails.size() > 0) {
                    String msg = "存在已经还款订单";
                    for (int i = 0; i < paidOrderDetails.size(); i++) {
                        if (i < 3) {
                            msg += " " + paidOrderDetails.get(i).getOrderNo() + " ";
                        } else {
                            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, msg, null);
                            logger.info("toPayPage OUT,跳转账单支付页面，出参 resultDTO:{}", resultDTO);
                            return resultDTO;
                        }
                    }
                }
                if (null == billPaymentData.getCashMoney()) {
                    billPaymentData.setCashMoney(0d);
                }
                if (null == billPaymentData.getPosMoney()) {
                    billPaymentData.setPosMoney(0d);
                }
                if (null == billPaymentData.getOtherMoney()) {
                    billPaymentData.setOtherMoney(0d);
                }
                Double totalRepaymentAmount = billPaymentData.getCashMoney() + billPaymentData.getPosMoney() + billPaymentData.getOtherMoney();
                ShiroUser shiroUser = this.getShiroUser();
                List < BillorderDetailsRequest > billorderDetailsRequests = new ArrayList<>();

                billInfoService.createRepayMentInfo(maFitBillVO.getStoreId(), shiroUser.getId(), "MANAGE", billorderDetailsRequestList,0d, billPaymentData.getCashMoney(), billPaymentData.getPosMoney(), totalRepaymentAmount,
                        billPaymentData.getPosNumber(), billPaymentData.getOtherMoney(),
                        billNo);
                logger.warn("PayBill ,后台账单收款成功");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "后台账单收款成功！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("PayBill EXCEPTION,发生未知错误，后台账单收款失败");
                logger.warn("{}", e);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "后台账单收款失败！", null);
            }
        }
        logger.warn("账单收款信息为空！");
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "账单收款信息为空！", null);
    }


    /**
     * 下载账单
     */
    @GetMapping(value = "/downFitBill")
    public ResultDTO<String> downBill(String billNo) {
        logger.warn("downFitBill 后台账单下载 ,billNo:{}", billNo);
        ResultDTO<String> resultDTO;
        if (null != billNo) {
            try {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "后台账单下载成功！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("PayBill EXCEPTION,发生未知错误，后台账单收款失败");
                logger.warn("{}", e);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "后台账单下载失败！", null);
            }
        }
        logger.warn("账单收款信息为空！");
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "账单信息为空！", null);
    }

    /**
     * 新增账单规则
     **/
    @PostMapping(value = "/addRule")
    public ResultDTO<String> addBillRule(Long storeId,Integer billDate,Integer repaymentDeadlineDate,Double interestRate) {
        logger.warn("addBillRule 后台添加装饰公司账单规则 ,入参storeId:{}, billDate:{}, repaymentDeadlineDate:{},interestRate:{}", storeId, billDate, repaymentDeadlineDate,interestRate);
        try {
            ShiroUser shiroUser = this.getShiroUser();
             maFitBillService.addBillRule(storeId, billDate, repaymentDeadlineDate,interestRate,shiroUser);
            logger.warn("addBillRule ,后台添加装饰公司账单规则成功");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "添加装饰公司账单规则成功！", null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("addBillRule EXCEPTION,发生未知错误，后台添加装饰公司账单规则失败");
            logger.warn("{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "添加装饰公司账单规则失败！", null);
        }
    }

    /**
     * 编辑账单规则
     **/
    @PutMapping(value = "/editRule")
    public ResultDTO<String> editBillRule(Long storeId,Integer billDate,Integer repaymentDeadlineDate,Double interestRate,Long ruleId) {
        logger.warn("editBillRule 后台编辑装饰公司账单规则 ,入参storeId:{}, billDate:{}, repaymentDeadlineDate:{},interestRate:{}", storeId, billDate, repaymentDeadlineDate,interestRate,ruleId);
        try {
            ShiroUser shiroUser = this.getShiroUser();
            maFitBillService.editBillRule(storeId, billDate, repaymentDeadlineDate,interestRate,ruleId,shiroUser);
            logger.warn("editBillRule ,后台编辑装饰公司账单规则成功");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "编辑装饰公司账单规则成功！", null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("editBillRule EXCEPTION,发生未知错误，后台编辑装饰公司账单规则失败");
            logger.warn("{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "编辑装饰公司账单规则失败！", null);
        }
    }


    /**
     * 账单变更查询
     **/
    @GetMapping(value = "/billRuleChangelog")
    public GridDataVO<BillRuleLogVO> getBillRuleChanglogList(Long id,Integer offset,Integer size,String startTime,String endTime ,String changeUser) {
        logger.warn("editBillRule 后台查询账单变更日志 ,入参id:{}, startTime:{}, endTime:{},changeUser:{},page:{},size:{}", id, startTime,endTime,changeUser,offset, size);
        try {
            PageInfo<BillRuleLogVO> billRuleLogVOList =  billRuleService.findBillRuleLogVOById(offset,size,id,startTime,endTime,changeUser);
            return new GridDataVO<BillRuleLogVO>().transform(billRuleLogVOList.getList(), billRuleLogVOList.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("editBillRule EXCEPTION,发生未知错误，后台查询账单变更日志失败");
            logger.warn("{}", e);
            return null;
        }
    }

}
