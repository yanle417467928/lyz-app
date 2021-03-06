package cn.com.leyizhuang.app.web.controller;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.CustomerLeBiVariationLog;
import cn.com.leyizhuang.app.foundation.pojo.response.LeBiVariationLogResPonse;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.LeBiVariationLogService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * 乐币变动明细
 * Created by caiyu on 2017/11/8.
 */
@RestController
@RequestMapping(value = "/app/lebi/variation")
public class LeBiVariationLogController {

    private static final Logger logger = LoggerFactory.getLogger(LeBiVariationLogController.class);

    @Autowired
    private LeBiVariationLogService leBiVariationLogService;

    @Autowired
    private AppCustomerService appCustomerService;

    /**
     * 根据乐币变动类型查看明细
     *
     * @param userID       用户id
     * @param identityType 用户类型
     * @param showType     查看类型
     * @return 乐币变动明细列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> queryListByCusIDAndLeBiVariationType(Long userID, Integer identityType, Integer showType,Integer page, Integer size) {
        ResultDTO<Object> resultDTO;
        logger.info("queryListByCusIDAndLeBiVariationType CALLED,查看乐币变动明细列表，入参 userID:{},identityType:{},showType:{},page:{},size:{}", userID, identityType, showType,page,size);
        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("queryListByCusIDAndLeBiVariationType OUT,查看乐币变动明细列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("queryListByCusIDAndLeBiVariationType OUT,查看乐币变动明细列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == showType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "变更类型不能为空", null);
            logger.info("queryListByCusIDAndLeBiVariationType OUT,查看乐币变动明细列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != 6) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "只有顾客可以查看乐币变动明细", null);
            logger.info("queryListByCusIDAndLeBiVariationType OUT,查看乐币变动明细列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("queryListByCusIDAndLeBiVariationType OUT,查看乐币变动明细列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("queryListByCusIDAndLeBiVariationType OUT,查看乐币变动明细列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //查询对应查看类型的所有变动记录
            PageInfo<CustomerLeBiVariationLog> customerLeBiVariationLogList = leBiVariationLogService.queryListBycusIDAndShowTypeType(userID, showType, page, size);
            //创建返回list
            List<LeBiVariationLogResPonse> leBiVariationLogResPonseList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (CustomerLeBiVariationLog customerLeBiVariationLog : customerLeBiVariationLogList.getList()) {
                //创建返回类
                LeBiVariationLogResPonse leBiVariationLogResponse = new LeBiVariationLogResPonse();
                //设值
                leBiVariationLogResponse.setVariationQuantity(customerLeBiVariationLog.getVariationQuantity());
                leBiVariationLogResponse.setVariationTime(sdf.format(customerLeBiVariationLog.getVariationTime()));
                switch (customerLeBiVariationLog.getLeBiVariationType()) {
                    case SIGN:
                        leBiVariationLogResponse.setLeBiVariationType("签到");
                        break;
                    case ADMINISTRATORS_UPDATE:
                        leBiVariationLogResponse.setLeBiVariationType("管理员修改");
                        break;
                    case CANCEL_ORDER:
                        leBiVariationLogResponse.setLeBiVariationType("取消订单返还");
                        break;
                    case RETURN_ORDER:
                        leBiVariationLogResponse.setLeBiVariationType("退货返还");
                        break;
                    case ORDER:
                        leBiVariationLogResponse.setLeBiVariationType("订单使用");
                        break;
                }
                if (showType == 2) {
                    leBiVariationLogResponse.setOrderNum(customerLeBiVariationLog.getOrderNum());
                }
                leBiVariationLogResponse.setTotalQuantity(appCustomerService.findLeBiQuantityByUserIdAndIdentityType(userID, identityType));
                leBiVariationLogResPonseList.add(leBiVariationLogResponse);
            }
            PageInfo<LeBiVariationLogResPonse> pageInfo = new PageInfo<>(leBiVariationLogResPonseList);
            pageInfo.setEndRow(customerLeBiVariationLogList.getEndRow());
            pageInfo.setNextPage(customerLeBiVariationLogList.getNextPage());
            pageInfo.setPageNum(customerLeBiVariationLogList.getPageNum());
            pageInfo.setPages(customerLeBiVariationLogList.getPages());
            pageInfo.setPageSize(customerLeBiVariationLogList.getPageSize());
            pageInfo.setPrePage(customerLeBiVariationLogList.getPrePage());
            pageInfo.setSize(customerLeBiVariationLogList.getSize());
            pageInfo.setStartRow(customerLeBiVariationLogList.getStartRow());
            pageInfo.setTotal(customerLeBiVariationLogList.getTotal());
            pageInfo.setNavigateFirstPage(customerLeBiVariationLogList.getNavigateFirstPage());
            pageInfo.setNavigatepageNums(customerLeBiVariationLogList.getNavigatepageNums());
            pageInfo.setNavigateLastPage(customerLeBiVariationLogList.getNavigateLastPage());
            pageInfo.setNavigatePages(customerLeBiVariationLogList.getNavigatePages());
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,  new GridDataVO<LeBiVariationLogResPonse>().transform(pageInfo));
            logger.info("queryListByCusIDAndLeBiVariationType OUT,查看乐币变动明细列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，查看乐币变动明细列表失败", null);
            logger.warn("queryListByCusIDAndLeBiVariationType EXCEPTION,查看乐币变动明细列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}
