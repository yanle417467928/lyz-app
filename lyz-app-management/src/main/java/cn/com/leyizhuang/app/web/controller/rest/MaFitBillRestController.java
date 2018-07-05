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

    /**
     * 后台分页查询装饰公司未出账单
     *
     * @param offset   当前页
     * @param size     每页条数
     * @param keywords 不知
     * @return 订单列表
     */
    @GetMapping(value = "/notOut/page/grid")
    public GridDataVO<DecorativeCompanyDetailVO> restFitBillPageGird(Integer offset, Integer size, String keywords, Long cityId) {
        logger.warn("restFitBillPageGird 后台分页获取所有装饰公司未出账单 ,入参offsetL:{}, size:{}, kewords:{}", offset, size, keywords,cityId);
        try {
            //查询登录用户门店权限的门店ID
            List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
            size = getSize(size);
            Integer page = getPage(offset, size);
            keywords = keywords.trim();
            PageInfo<StoreDO> storePage = this.maStoreService.queryDecorativeCompanyList(page, size,storeIds,cityId,keywords);
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

}
