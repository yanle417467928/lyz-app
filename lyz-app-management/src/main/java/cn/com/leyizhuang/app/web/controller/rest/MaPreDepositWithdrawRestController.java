package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.StPreDepositWithdraw;
import cn.com.leyizhuang.app.foundation.pojo.user.CusPreDepositWithdraw;
import cn.com.leyizhuang.app.foundation.service.AppPreDepositWithdrawService;
import cn.com.leyizhuang.app.remote.queue.MaSinkSender;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台 预存款提现控制器
 * Created by panjie on 2018/2/6.
 */
@RestController
@RequestMapping(value = MaPreDepositWithdrawRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaPreDepositWithdrawRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/pre/deposit/withdraw";

    private final Logger logger = LoggerFactory.getLogger(MaPreDepositWithdrawRestController.class);

    @Resource
    private AppPreDepositWithdrawService appPreDepositWithdrawService;

    @Resource
    private MaSinkSender sinkSender;

    @GetMapping("/cus/grid")
    public GridDataVO<CusPreDepositWithdraw> CusGridData(Integer offset, Integer size, String keywords, String status, String startDateTime, String endDateTime) {
        GridDataVO<CusPreDepositWithdraw> gridDataVO = new GridDataVO<>();
        Integer page = getPage(offset, size);

        PageInfo<CusPreDepositWithdraw> pageInfo = appPreDepositWithdrawService.getCusPageInfo(page, size, keywords, status, startDateTime, endDateTime);
        List<CusPreDepositWithdraw> cusPreDepositWithdraws = CusPreDepositWithdraw.transform(pageInfo.getList());

        return gridDataVO.transform(cusPreDepositWithdraws, pageInfo.getTotal());
    }

    /**
     * 顾客 -- 通过申请
     *
     * @param applyId
     * @return
     */
    @PutMapping(value = "/cus/pass")
    public ResultDTO cusApplyPass(Long applyId) {

        if (applyId == null) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "数据有误，请联系管理员", null);
        }

        try {
            appPreDepositWithdrawService.cusApplyPass(applyId, super.getShiroUser());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "申请通过成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("预存款提现")) {
                logger.info(e.getMessage());
            }

            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生异常，通过失败", null);
        }
    }

    /**
     * 顾客 -- 驳回申请
     *
     * @param applyId
     * @return
     */
    @PutMapping(value = "/cus/reject")
    public ResultDTO cusApplyReject(Long applyId) {

        if (applyId == null) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "数据有误，请联系管理员", null);
        }

        try {
            String receiptNumber = appPreDepositWithdrawService.cusApplyreject(applyId, super.getShiroUser());
            sinkSender.sendRechargeReceipt(receiptNumber);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "申请驳回成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("预存款提现")) {
                logger.info(e.getMessage());
            }
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生异常，驳回失败", null);
        }
    }

    /**
     * 顾客 -- 打款
     *
     * @param applyId
     * @return
     */
    @PutMapping(value = "/cus/remit")
    public ResultDTO cusApplyRemit(Long applyId) {

        if (applyId == null) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "数据有误，请联系管理员", null);
        }

        try {
            appPreDepositWithdrawService.cusApplyRemit(applyId, super.getShiroUser());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "申请打款成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("预存款提现")) {
                logger.info(e.getMessage());
            }
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生异常，打款失败", null);
        }
    }

    @GetMapping("/st/grid")
    public GridDataVO<StPreDepositWithdraw> stGridData(Integer offset, Integer size, String keywords, String status, String startDateTime, String endDateTime) {
        GridDataVO<StPreDepositWithdraw> gridDataVO = new GridDataVO<>();
        Integer page = getPage(offset, size);

        PageInfo<StPreDepositWithdraw> pageInfo = appPreDepositWithdrawService.getStPageInfo(page, size, keywords, status, startDateTime, endDateTime);
        List<StPreDepositWithdraw> stPreDepositWithdraws = StPreDepositWithdraw.transform(pageInfo.getList());
        return gridDataVO.transform(stPreDepositWithdraws, pageInfo.getTotal());
    }

    /**
     * 门店 -- 通过申请
     *
     * @param applyId
     * @return
     */
    @PutMapping(value = "/st/pass")
    public ResultDTO stApplyPass(Long applyId) {

        if (applyId == null) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "数据有误，请联系管理员", null);
        }

        try {
            appPreDepositWithdrawService.stApplyPass(applyId, super.getShiroUser());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "申请通过成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("预存款提现")) {
                logger.info(e.getMessage());
            }

            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生异常，通过失败", null);
        }
    }

    /**
     * 门店 -- 驳回申请
     *
     * @param applyId
     * @return
     */
    @PutMapping(value = "/st/reject")
    public ResultDTO stApplyReject(Long applyId) {

        if (applyId == null) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "数据有误，请联系管理员", null);
        }

        try {
            String receiptNumber = appPreDepositWithdrawService.stApplyreject(applyId, super.getShiroUser());
            sinkSender.sendRechargeReceipt(receiptNumber);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "申请驳回成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("预存款提现")) {
                logger.info(e.getMessage());
            }
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生异常，驳回失败", null);
        }
    }

    /**
     * 门店 -- 打款
     *
     * @param applyId
     * @return
     */
    @PutMapping(value = "/st/remit")
    public ResultDTO stApplyRemit(Long applyId) {

        if (applyId == null) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "数据有误，请联系管理员", null);
        }

        try {
            appPreDepositWithdrawService.stApplyRemit(applyId, super.getShiroUser());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "申请打款成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("预存款提现")) {
                logger.info(e.getMessage());
            }
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生异常，打款失败", null);
        }
    }

}
