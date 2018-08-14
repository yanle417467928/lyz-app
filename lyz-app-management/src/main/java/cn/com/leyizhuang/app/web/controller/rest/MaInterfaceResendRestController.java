package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.InterfaceResendChangeType;
import cn.com.leyizhuang.app.core.utils.IpUtil;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.interfaceResend.EbsAllocationResendInfo;
import cn.com.leyizhuang.app.foundation.pojo.interfaceResend.EbsOrderResendInfo;
import cn.com.leyizhuang.app.foundation.pojo.interfaceResend.EbsReturnOrderResendInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.webservice.ebs.MaOrderReceiveInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.ReturnOrderBaseInf;
import cn.com.leyizhuang.app.foundation.service.AppSeparateOrderService;
import cn.com.leyizhuang.app.foundation.service.MaInterfaceResendService;
import cn.com.leyizhuang.app.foundation.service.MaOrderService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping(value = MaInterfaceResendRestController.PRE_URL, produces = "application/json;charset=utf8")
public class MaInterfaceResendRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/interface";
    private final Logger logger = LoggerFactory.getLogger(MaInterfaceResendRestController.class);

    @Resource
    private MaInterfaceResendService maInterfaceResendService;
    @Resource
    private AppSeparateOrderService separateOrderService;
    @Resource
    private MaOrderService maOrderService;


    @GetMapping(value = "/ebs/order/page")
    public GridDataVO<EbsOrderResendInfo> ebsOrderPage(Integer offset, Integer size,Integer isGenerate, String keywords) {
        logger.info("ebsOrderPage ,查询发送失败订单, 入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<EbsOrderResendInfo> ebsOrderResendInfoPageInfo = maInterfaceResendService.queryNotSendEbsOrder(page, size, isGenerate, keywords);
            return new GridDataVO<EbsOrderResendInfo>().transform(ebsOrderResendInfoPageInfo.getList(), ebsOrderResendInfoPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("ebsPage EXCEPTION,发生未知错误，后台分页获取所有列表失败");
            logger.warn("{}", e);
            return null;
        }
    }


    @GetMapping(value = "/ebs/returnOrder/page")
    public GridDataVO<EbsReturnOrderResendInfo> ebsReturnOrederPage(Integer offset, Integer size,Integer isGenerate, String keywords) {
        logger.info("ebsReturnOrederPage ,查询EBS发送失败退单, 入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<EbsReturnOrderResendInfo> ebsOrderResendInfoPageInfo = maInterfaceResendService.queryNotSendEbsReturnOrder(page, size, isGenerate, keywords);
            return new GridDataVO<EbsReturnOrderResendInfo>().transform(ebsOrderResendInfoPageInfo.getList(), ebsOrderResendInfoPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("ebsPage EXCEPTION,发生未知错误，后台分页获取所有列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    @GetMapping(value = "/ebs/allocationInbound/page")
    public GridDataVO<EbsAllocationResendInfo> ebsAllocationInboundPage(Integer offset, Integer size,Integer isGenerate, String keywords) {
        logger.info("ebsAllocationInboundPage ,查询发送失败调拨单, 入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<EbsAllocationResendInfo> ebsAllocationInboundPageInfo = maInterfaceResendService.queryNotSendAllocation(page, size, isGenerate, keywords,3);
            return new GridDataVO<EbsAllocationResendInfo>().transform(ebsAllocationInboundPageInfo.getList(), ebsAllocationInboundPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("ebsAllocationInboundPage EXCEPTION,发生未知错误，后台分页获取所有列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    @GetMapping(value = "/ebs/allocationOutbound/page")
    public GridDataVO<EbsAllocationResendInfo> ebsAllocationOutboundPage(Integer offset, Integer size,Integer isGenerate, String keywords) {
        logger.info("ebsAllocationOutboundPage ,查询发送失败调拨单, 入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<EbsAllocationResendInfo> ebsAllocationOutboundPageInfo = maInterfaceResendService.queryNotSendAllocation(page, size, isGenerate, keywords,1);
            return new GridDataVO<EbsAllocationResendInfo>().transform(ebsAllocationOutboundPageInfo.getList(), ebsAllocationOutboundPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("ebsAllocationOutboundPage EXCEPTION,发生未知错误，后台分页获取所有列表失败");
            logger.warn("{}", e);
            return null;
        }
    }


    @GetMapping(value = "/ebs/selfTakeOrder/page")
    public GridDataVO<EbsOrderResendInfo> ebsSelfTakeOrderPage(Integer offset, Integer size,Integer isGenerate, String keywords) {
        logger.info("ebsSelfTakeOrderPage ,查询发送失败调拨单, 入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<EbsOrderResendInfo> ebsSelfTakeOrderPageInfo = maInterfaceResendService.queryNotSendSelfTakeOrder(page, size, isGenerate, keywords);
            return new GridDataVO<EbsOrderResendInfo>().transform(ebsSelfTakeOrderPageInfo.getList(),ebsSelfTakeOrderPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("ebsSelfTakeOrderPage EXCEPTION,发生未知错误，后台分页获取所有列表失败");
            logger.warn("{}", e);
            return null;
        }
    }


    @GetMapping(value = "/ebs/returnStoreOrder/page")
    public GridDataVO<EbsReturnOrderResendInfo> ebsReturnStoreOrderPage(Integer offset, Integer size,Integer isGenerate, String keywords) {
        logger.info("ebsReturnStoreOrderPage ,查询EBS发送失败退货到店单, 入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<EbsReturnOrderResendInfo> ebsReturnStoreOrderResendInfoPageInfo = maInterfaceResendService.queryNotSendEbsReturnStoreOrder(page, size, isGenerate, keywords);
            return new GridDataVO<EbsReturnOrderResendInfo>().transform(ebsReturnStoreOrderResendInfoPageInfo.getList(), ebsReturnStoreOrderResendInfoPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("ebsPage EXCEPTION,发生未知错误，后台分页获取所有列表失败");
            logger.warn("{}", e);
            return null;
        }
    }


    @RequestMapping(value = "/ebs/order/generate/{orderNumber}")
    public ResultDTO<Object> generateEbsOrderInfo(@PathVariable String orderNumber, HttpServletRequest request) {
        ResultDTO<Object> resultDTO;
        logger.info("generateEbsOrderInfo ,生成ebs接口订单, 入参 orderNumber:{}", orderNumber);
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("generateEbsOrderInfo OUT,EBS生成订单头和商品数据失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        ShiroUser user = this.getShiroUser();
        String ip = IpUtil.getIpAddress(request);
        try {
            Boolean isExist = separateOrderService.isOrderExist(orderNumber);
            if (isExist) {
                logger.info("该订单已拆单，不能重复拆单!");
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该订单已拆单，不能重复拆单!", null);
                return resultDTO;
            }
            maInterfaceResendService.generateEbsOrderInfo(orderNumber,user,ip);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "生成接口成功!", null);
            logger.info("generateEbsOrderInfo OUT,EBS生成订单头和商品数据成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "生成接口失败!发生未知异常!", null);
            maInterfaceResendService.addResendEbsInterfaceLog( orderNumber,user,InterfaceResendChangeType.EBS_ORER_GENERATE,Boolean.FALSE,e.getMessage(),ip,"EBS");
            logger.info("generateEbsOrderInfo OUT,EBS生成订单头和商品数据失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            e.printStackTrace();
            return resultDTO;
        }
    }


    @RequestMapping(value = "/ebs/order/resend/{orderNumber}")
    public ResultDTO<Object> resendEbsOrderInfo(@PathVariable String orderNumber,HttpServletRequest request) {
        ResultDTO<Object> resultDTO;
        logger.info("resendEbsOrderInfo ,发送ebs订单, 入参 orderNumber:{}", orderNumber);
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("resendEbsOrderInfo OUT,发送订单头和商品数据失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        ShiroUser user = this.getShiroUser();
        String ip = IpUtil.getIpAddress(request);
        try {
            maInterfaceResendService.resendEbsOrderInfo(orderNumber,user,ip);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "传输成功!", null);
            logger.info("resendEbsOrderInfo OUT,发送订单头和商品数据成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            maInterfaceResendService.addResendEbsInterfaceLog( orderNumber,user,InterfaceResendChangeType.EBS_ORER_RESEND,Boolean.FALSE,e.getMessage(),ip,"EBS");
            logger.info("resendEbsOrderInfo OUT,发送订单头和商品数据失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            e.printStackTrace();
            return resultDTO;
        }
    }



    @RequestMapping(value = "/ebs/returnOrder/generate/{returnNo}")
    public ResultDTO<Object> generateEbsReturnOrderInfo(@PathVariable String returnNo, HttpServletRequest request) {
        ResultDTO<Object> resultDTO;
        logger.info("generateEbsReturnOrderInfo ,生成ebs接口, 入参 returnNo:{}", returnNo);
        if (null == returnNo) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("generateEbsReturnOrderInfo OUT,EBS生成退单头和商品数据失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        ShiroUser user = this.getShiroUser();
        String ip = IpUtil.getIpAddress(request);
        try {
            Boolean isExist = separateOrderService.isReturnOrderExist(returnNo);
            if (isExist) {
                logger.info("该退单已拆单，不能重复拆单!");
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该退单已拆单，不能重复拆单!", null);
                return resultDTO;
            }
            maInterfaceResendService.generateEbsReturnOrderInfo(returnNo,user,ip);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "生成接口成功!", null);
            logger.info("generateEbsReturnOrderInfo OUT,EBS生成退单头和商品数据失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "生成接口失败!发生未知异常!", null);
            maInterfaceResendService.addResendEbsInterfaceLog( returnNo,user,InterfaceResendChangeType.EBS_RETURN_ORER_GENERATE,Boolean.FALSE,e.getMessage(),ip,"EBS");
            logger.info("generateEbsReturnOrderInfo OUT,EBS生成退单头和商品数据失败，出参 resultDTO:{}", resultDTO);
            e.printStackTrace();
            return resultDTO;
        }
    }


    @RequestMapping(value = "/ebs/returnOrder/resend/{returnNo}")
    public ResultDTO<Object> resendEbsReturnOrderInfo(@PathVariable String returnNo,HttpServletRequest request) {
        ResultDTO<Object> resultDTO;
        logger.info("resendEbsReturnOrderInfo ,发送ebs退单, 入参 returnNo:{}", returnNo);
        if (null == returnNo) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("resendEbsReturnOrderInfo OUT,发送退单头和商品数据失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        ShiroUser user = this.getShiroUser();
        String ip = IpUtil.getIpAddress(request);
        try {
            maInterfaceResendService.resendEbsReturnOrderInfo(returnNo,user,ip);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "传输成功!", null);
            logger.info("resendEbsReturnOrderInfo OUT,发送退单头和商品数据成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            maInterfaceResendService.addResendEbsInterfaceLog( returnNo,user,InterfaceResendChangeType.EBS_RETURN_ORER_RESEND,Boolean.FALSE,e.getMessage(),ip,"EBS");
            logger.info("resendEbsReturnOrderInfo OUT,发送退单头和商品数据失败，出参 resultDTO:{}", resultDTO);
            e.printStackTrace();
            return resultDTO;
        }
    }


    @RequestMapping(value = "/ebs/allocationInbound/resend/{number}")
    public ResultDTO<Object> resendEbsAllocationInboundInfo(@PathVariable String number,HttpServletRequest request) {
        ResultDTO<Object> resultDTO;
        logger.info("resendEbsAllocationInboundInfo ,发送ebs调拨单, 入参 number:{}", number);
        if (null == number) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("resendEbsAllocationInboundInfo OUT,发送调拨单失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        ShiroUser user = this.getShiroUser();
        String ip = IpUtil.getIpAddress(request);
        try {
            maInterfaceResendService.resendEbsAllocationInboundInfo(number,user,ip);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "传输成功!", null);
            logger.info("resendEbsReturnOrderInfo OUT,发送退单头和商品数据成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            maInterfaceResendService.addResendEbsInterfaceLog( number,user,InterfaceResendChangeType.EBS_ALLOCATIONINBOUND_RESEND,Boolean.FALSE,e.getMessage(),ip,"EBS");
            logger.info("resendEbsReturnOrderInfo OUT,发送退单头和商品数据失败，出参 resultDTO:{}", resultDTO);
            e.printStackTrace();
            return resultDTO;
        }
    }


    @RequestMapping(value = "/ebs/allocationOutbound/resend/{number}")
    public ResultDTO<Object> resendEbsAllocationOutboundInfo(@PathVariable String number,HttpServletRequest request) {
        ResultDTO<Object> resultDTO;
        logger.info("resendEbsAllocationOutboundInfo ,发送ebs调拨单, 入参 number:{}", number);
        if (null == number) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("resendEbsAllocationOutboundInfo OUT,发送调拨单失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        ShiroUser user = this.getShiroUser();
        String ip = IpUtil.getIpAddress(request);
        try {
            maInterfaceResendService.resendEbsAllocationOutboundInfo(number,user,ip);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "传输成功!", null);
            logger.info("resendEbsAllocationOutboundInfo OUT,发送退单头和商品数据成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            maInterfaceResendService.addResendEbsInterfaceLog( number,user,InterfaceResendChangeType.EBS_ALLOCATIONOUTBOUND_RESEND,Boolean.FALSE,e.getMessage(),ip,"EBS");
            logger.info("resendEbsAllocationOutboundInfo OUT,发送退单头和商品数据失败，出参 resultDTO:{}", resultDTO);
            e.printStackTrace();
            return resultDTO;
        }
    }


    @RequestMapping(value = "/ebs/selfTakeOrder/generate/{orderNumber}")
    public ResultDTO<Object> generateEbsSelfTakeOrderInfo(@PathVariable String orderNumber, HttpServletRequest request) {
        ResultDTO<Object> resultDTO;
        logger.info("generateEbsSelfTakeOrderInfo ,生成ebs自提单接口订单, 入参 orderNumber:{}", orderNumber);
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("generateEbsSelfTakeOrderInfo OUT,EBS生成自提单数据失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        ShiroUser user = this.getShiroUser();
        String ip = IpUtil.getIpAddress(request);
        try {
            maInterfaceResendService.generateEbsSelfTakeOrderInfo(orderNumber,user,ip);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "生成接口成功!", null);
            logger.info("generateEbsSelfTakeOrderInfo OUT,EBS生成自提单数据成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "生成接口失败!发生未知异常!", null);
            maInterfaceResendService.addResendEbsInterfaceLog( orderNumber,user,InterfaceResendChangeType.EBS_SELFTAKE_ORER_GENERATE,Boolean.FALSE,e.getMessage(),ip,"EBS");
            logger.info("generateEbsSelfTakeOrderInfo OUT,EBS生成自提单数据失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            e.printStackTrace();
            return resultDTO;
        }
    }


    @RequestMapping(value = "/ebs/selfTakeOrder/resend/{orderNumber}")
    public ResultDTO<Object> resendEbsSelfTakeOrderInfo(@PathVariable String orderNumber,HttpServletRequest request) {
        ResultDTO<Object> resultDTO;
        logger.info("resendEbsSelfTakeOrderInfo ,发送ebs自提订单, 入参 orderNumber:{}", orderNumber);
        if (null == orderNumber) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("resendEbsSelfTakeOrderInfo OUT,发送自提订单失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        ShiroUser user = this.getShiroUser();
        String ip = IpUtil.getIpAddress(request);
        try {
            maInterfaceResendService.resendEbsSelfTakeOrderInfo(orderNumber,user,ip);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "传输成功!", null);
            logger.info("resendEbsSelfTakeOrderInfo OUT,发送自提订单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            maInterfaceResendService.addResendEbsInterfaceLog( orderNumber,user,InterfaceResendChangeType.EBS_SELFTAKE_ORER_RESEND,Boolean.FALSE,e.getMessage(),ip,"EBS");
            logger.info("resendEbsSelfTakeOrderInfo OUT,发送自提订单失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            e.printStackTrace();
            return resultDTO;
        }
    }


    @RequestMapping(value = "/ebs/returnStoreOrder/generate/{returnNo}")
    public ResultDTO<Object> generateEbsReturnStoreOrderInfo(@PathVariable String returnNo, HttpServletRequest request) {
        ResultDTO<Object> resultDTO;
        logger.info("generateEbsReturnStoreOrderInfo ,生成ebs退货到店接口订单, 入参 returnNo:{}", returnNo);
        if (null == returnNo) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("generateEbsReturnStoreOrderInfo OUT,EBS生成退货到店数据失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        ShiroUser user = this.getShiroUser();
        String ip = IpUtil.getIpAddress(request);
        try {
            maInterfaceResendService.generateEbsReturnStoreOrderInfo(returnNo,user,ip);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "生成接口成功!", null);
            logger.info("generateEbsReturnStoreOrderInfo OUT,EBS生成退货到店数据成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "生成接口失败!发生未知异常!", null);
            maInterfaceResendService.addResendEbsInterfaceLog( returnNo,user,InterfaceResendChangeType.EBS_SELFTAKE_ORER_GENERATE,Boolean.FALSE,e.getMessage(),ip,"EBS");
            logger.info("generateEbsReturnStoreOrderInfo OUT,EBS生成退货到店数据失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            e.printStackTrace();
            return resultDTO;
        }
    }


    @RequestMapping(value = "/ebs/returnStoreOrder/resend/{returnNo}")
    public ResultDTO<Object> resendEbsReturnStoreOrderInfo(@PathVariable String returnNo,HttpServletRequest request) {
        ResultDTO<Object> resultDTO;
        logger.info("resendEbsReturnStoreOrderInfo ,发送ebs退货到店单, 入参 returnNo:{}", returnNo);
        if (null == returnNo) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单号不能为空!", null);
            logger.info("resendEbsReturnStoreOrderInfo OUT,发送退货到店单失败！出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        ShiroUser user = this.getShiroUser();
        String ip = IpUtil.getIpAddress(request);
        try {
            maInterfaceResendService.resendEbsReturnStoreOrderInfo(returnNo,user,ip);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "传输成功!", null);
            logger.info("resendEbsReturnStoreOrderInfo OUT,发送退货到店单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "传输失败!发生未知异常!", null);
            maInterfaceResendService.addResendEbsInterfaceLog( returnNo,user,InterfaceResendChangeType.EBS_SELFTAKE_ORER_RESEND,Boolean.FALSE,e.getMessage(),ip,"EBS");
            logger.info("resendEbsReturnStoreOrderInfo OUT,发送退货到店单失败，出参 resultDTO:{}", resultDTO);
            logger.debug("Exception:{}", e);
            e.printStackTrace();
            return resultDTO;
        }
    }



}
