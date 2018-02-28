package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.constant.CustomerPreDepositChangeType;
import cn.com.leyizhuang.app.core.constant.PreDepositWithdrawStatus;
import cn.com.leyizhuang.app.foundation.pojo.CashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.user.CusPreDepositWithdraw;
import cn.com.leyizhuang.app.foundation.service.AppPreDepositWithdrawService;
import cn.com.leyizhuang.app.web.controller.BaseController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台 预存款提现控制器
 * Created by panjie on 2018/2/6.
 */
@RestController
@RequestMapping(value = MaPreDepositWithdrawRestController.PRE_URL,produces = "application/json;charset=utf-8")
public class MaPreDepositWithdrawRestController extends BaseRestController{

    protected final static String PRE_URL = "/rest/pre/deposit/withdraw";

    private final Logger logger = LoggerFactory.getLogger(MaPreDepositWithdrawRestController.class);

    @Resource
    private AppPreDepositWithdrawService appPreDepositWithdrawService;

    @GetMapping("/cus/grid")
    public GridDataVO<CusPreDepositWithdraw> CusGridData(Integer offset, Integer size, String keywords , String status){
        GridDataVO<CusPreDepositWithdraw> gridDataVO = new GridDataVO<>();
        Integer page = getPage(offset, size);

        PageInfo<CusPreDepositWithdraw> pageInfo = appPreDepositWithdrawService.getCusPageInfo(page,size,keywords, status);
        List<CusPreDepositWithdraw> cusPreDepositWithdraws = CusPreDepositWithdraw.transform(pageInfo.getList());
        return gridDataVO.transform(cusPreDepositWithdraws,pageInfo.getTotal());
    }

    /**
     * 顾客 -- 通过申请
     * @param applyId
     * @return
     */
    @PutMapping(value = "/cus/pass")
    public ResultDTO cusApplyPass(Long applyId){

        if (applyId == null){
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "数据有误，请联系管理员", null);
        }

        try {
            appPreDepositWithdrawService.cusApplyPass(applyId,super.getShiroUser());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "申请通过成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("预存款提现")){
                logger.info(e.getMessage());
            }

            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生异常，通过失败", null);
        }
    }

    /**
     * 顾客 -- 驳回申请
     * @param applyId
     * @return
     */
    @PutMapping(value = "/cus/reject")
    public ResultDTO cusApplyReject(Long applyId){

        if (applyId == null){
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "数据有误，请联系管理员", null);
        }

        try {
            appPreDepositWithdrawService.cusApplyreject(applyId,super.getShiroUser());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "申请驳回成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("预存款提现")){
                logger.info(e.getMessage());
            }
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生异常，驳回失败", null);
        }
    }

    /**
     * 顾客 -- 打款
     * @param applyId
     * @return
     */
    @PutMapping(value = "/cus/remit")
    public ResultDTO cusApplyRemit(Long applyId){

        if (applyId == null){
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "数据有误，请联系管理员", null);
        }

        try {
            appPreDepositWithdrawService.cusApplyRemit(applyId,super.getShiroUser());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "申请打款成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("预存款提现")){
                logger.info(e.getMessage());
            }
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生异常，打款失败", null);
        }
    }



}
