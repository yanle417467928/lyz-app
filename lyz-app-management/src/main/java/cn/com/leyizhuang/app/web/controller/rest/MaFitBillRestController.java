package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillPaymentData;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.MaFitBillVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    private MaFitBillService maFITBillService;

    @Resource
    private AdminUserStoreService adminUserStoreService;


    /**
     * 后台分页查询装饰公司未出账单
     *
     * @param offset   当前页
     * @param size     每页条数
     * @param keywords 不知
     * @return 订单列表
     */
    @GetMapping(value = "/notOut/page/grid")
    public GridDataVO<MaFitBillVO> restFitBillPageGird(Integer offset, Integer size, String keywords, Long storeId) {
        logger.warn("restFitBillPageGird 后台分页获取所有装饰公司未出账单 ,入参offsetL:{}, size:{}, kewords:{}", offset, size, keywords);
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
            PageInfo<MaFitBillVO> maFitBillPageInfo = maFITBillService.getFitNotOutBill(page, size, storeIds, keywords);
            logger.warn("restFitBillPageGird ,后台分页获取所有装饰公司未出账单成功", maFitBillPageInfo.getList().size());
            return new GridDataVO<MaFitBillVO>().transform(maFitBillPageInfo.getList(), maFitBillPageInfo.getTotal());
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
            PageInfo<MaFitBillVO> maFitBillPageInfo = maFITBillService.getHistoryFitBill(page, size, storeIds, keywords);
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
    @GetMapping(value = "/noPayOrderBill/page/{billNo}")
    public GridDataVO<MaFitBillVO> restNoPayOrderBillPageGird(Integer offset, Integer size, String billNo, String startTime, String endTime, String orderNo) {
        logger.warn("restFitBillPageGird 后台分页查询装饰公司未付账单明细 ,入参offsetL:{}, size:{}, kewords:{},billNo:{},startTime:{},endTime:{},orderNo:{}", offset, size, billNo, startTime, endTime, orderNo);
        try {
            //查询登录用户门店权限的门店ID
            List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<MaFitBillVO> maFitBillPageInfo = maFITBillService.getNoPayOrderBillByBillNo(page, size, storeIds, billNo, startTime, endTime, orderNo);
            logger.warn("restFitBillPageGird ,后台分页查询装饰公司未付账单明细成功", maFitBillPageInfo.getList().size());
            return new GridDataVO<MaFitBillVO>().transform(maFitBillPageInfo.getList(), maFitBillPageInfo.getTotal());
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
    public GridDataVO<MaFitBillVO> restPayOrderBillPageGird(Integer offset, Integer size, String billNo, String startTime, String endTime, String orderNo) {
        logger.warn("restFitBillPageGird 后台分页查询装饰公司已付账单明细 ,入参offsetL:{}, size:{}, kewords:{},billNo:{},startTime:{},endTime:{},orderNo:{}", offset, size, billNo, startTime, endTime, orderNo);
        try {
            //查询登录用户门店权限的门店ID
            List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<MaFitBillVO> maFitBillPageInfo = maFITBillService.getPayOrderBillByBillNo(page, size, storeIds, billNo, startTime, endTime, orderNo);
            logger.warn("restFitBillPageGird ,后台分页查询装饰公司已付账单明细成功", maFitBillPageInfo.getList().size());
            return new GridDataVO<MaFitBillVO>().transform(maFitBillPageInfo.getList(), maFitBillPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restFitBillPageGird EXCEPTION,发生未知错误，后台分页查询装饰公司已付账单明细失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 后台账单收款
     */
    @GetMapping(value = "/payBill")
    public ResultDTO<String> PayBill(BillPaymentData billPaymentData) {
        logger.warn("PayBill 后台账单收款 ,入参billPaymentData:{}", billPaymentData);
        ResultDTO<String> resultDTO;
        if (null != billPaymentData) {
            if (null == billPaymentData.getBillNo()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "账单号不允许为空", null);
                logger.warn("PayBill OUT,后台账单收款失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            try {
                //查询登录用户门店权限的门店ID
                //PageInfo<MaFitBillVO> maFitBillPageInfo = maFITBillService.getPayOrderBillByBillNo();
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
