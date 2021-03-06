package cn.com.leyizhuang.app.web.controller.goods;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.oss.FileUploadOSSUtils;
import cn.com.leyizhuang.app.foundation.pojo.GoodsEvaluation;
import cn.com.leyizhuang.app.foundation.pojo.OrderEvaluation;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderEvaluationRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderGoodsEvaluationRequest;
import cn.com.leyizhuang.app.foundation.pojo.response.GoodsEvaluationListResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.GoodsEvaluationService;
import cn.com.leyizhuang.app.foundation.service.OrderEvaluationService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * 商品评价接口
 *
 * @author caiyu
 * @date 2017/11/16
 */
@RestController
@RequestMapping(value = "/app/order/evaluation")
public class EvaluationController {

    private static final Logger logger = LoggerFactory.getLogger(EvaluationController.class);

    @Resource
    private OrderEvaluationService orderEvaluationService;

    @Resource
    private AppCustomerService customerService;

    @Resource
    private AppEmployeeService employeeService;

    @Resource
    private GoodsEvaluationService goodsEvaluationService;

    /**
     * 保存订单评价
     *
     * @param orderEvaluationRequest 订单评价参数
     * @return 保存结果 ,成功 or 失败
     */
    @PostMapping(value = "/submit", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> orderEvaluationSubmit(OrderEvaluationRequest orderEvaluationRequest) {
        ResultDTO<Object> resultDTO;
        logger.info("orderEvaluationSubmit CALLED,订单评价提交，入参 orderEvaluationRequest:{}", orderEvaluationRequest);

        if (null == orderEvaluationRequest.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("orderEvaluationSubmit OUT,订单评价提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderEvaluationRequest.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("orderEvaluationSubmit OUT,订单评价提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderEvaluationRequest.getOrderNumber())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单编号不能为空", null);
            logger.info("orderEvaluationSubmit OUT,订单评价提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderEvaluationRequest.getProductStar()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "产品星级不能为空", null);
            logger.info("orderEvaluationSubmit OUT,订单评价提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderEvaluationRequest.getLogisticsStar()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "物流星级不能为空", null);
            logger.info("orderEvaluationSubmit OUT,订单评价提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderEvaluationRequest.getServiceStars()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "服务星级不能为空", null);
            logger.info("orderEvaluationSubmit OUT,订单评价提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            OrderEvaluation orderEvaluation = orderEvaluationService.queryOrderEvaluationListByOrderNumber(orderEvaluationRequest.getOrderNumber());
            if (orderEvaluation == null){
                //保存订单评价
                orderEvaluationService.addOrderEvaluation(orderEvaluationRequest);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "", "");
                return resultDTO;
            }else{
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此订单已评价不能重复评价！", null);
                logger.info("orderEvaluationSubmit OUT,此订单已评价，订单评价提交失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，订单评价提交失败", null);
            logger.warn("orderEvaluationSubmit EXCEPTION,订单评价提交失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 订单商品评价
     *
     * @param orderGoodsEvaluationRequest 订单商品评价参数
     * @param request                    评价图片文件
     * @return 图片地址
     */
    @PostMapping(value = "/goods/submit", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> orderEvaluationGoodsSubmit(OrderGoodsEvaluationRequest orderGoodsEvaluationRequest, HttpServletRequest request) {
        logger.info("orderEvaluationGoodsSubmit CALLED,订单商品评价提交,入参 orderGoodsEvaluationRequest:{}, " +
                "request:{}", orderGoodsEvaluationRequest, request);

        ResultDTO<Object> resultDTO;

        if (null == orderGoodsEvaluationRequest.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("orderEvaluationGoodsSubmit OUT,订单商品评价提交失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderGoodsEvaluationRequest.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                    null);
            logger.info("orderEvaluationGoodsSubmit OUT,订单商品评价提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderGoodsEvaluationRequest.getOrderNumber()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空！",
                    null);
            logger.info("orderEvaluationGoodsSubmit OUT,订单商品评价提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderGoodsEvaluationRequest.getGoodsId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品id不能为空！",
                    null);
            logger.info("orderEvaluationGoodsSubmit OUT,订单商品评价提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderGoodsEvaluationRequest.getEvaluationContent())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "评价内容不能为空！",
                    null);
            logger.info("orderEvaluationGoodsSubmit OUT,订单商品评价提交失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
//            List<String> pictureUrls = new ArrayList<>();
//            for (MultipartFile picture : pictures) {
//                String url = FileUploadOSSUtils.uploadProfilePhoto(picture, "order/evaluation/");
//                pictureUrls.add(url);
//            }

            /*
             * 因为图片只能上传一张，修改传入参数为request
             * GenerationRoad
             */
            StringBuilder pictureUrls = new StringBuilder();
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                    request.getSession().getServletContext());
            if (multipartResolver.isMultipart(request)) {
                // 转换成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                // 取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                int i = 0;
                while (iter.hasNext()) {
                    // 取得上传文件
                    MultipartFile f = multiRequest.getFile(iter.next());
                    if (f != null) {
                        // 取得当前上传文件的文件名称
                        String myFileName = f.getOriginalFilename();
                        // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                        if (!"".equals(myFileName.trim())) {
                            // 定义上传路径
                            if(!iter.hasNext()){
                                pictureUrls.append(FileUploadOSSUtils.uploadProfilePhoto(f, "order/evaluation/"));
                            }else{
                                pictureUrls.append(FileUploadOSSUtils.uploadProfilePhoto(f, "order/evaluation/")).append(",");
                            }
                            i += 1;
                            if (i > 2 ) {
                                break;
                            }
                        }
                    }
                }
            }

            GoodsEvaluation goodsEvaluation = new GoodsEvaluation();
            goodsEvaluation.setEvaluationTime(Calendar.getInstance().getTime());
            goodsEvaluation.setOrderNumber(orderGoodsEvaluationRequest.getOrderNumber());
            goodsEvaluation.setCommentContent(orderGoodsEvaluationRequest.getEvaluationContent());
            goodsEvaluation.setGid(orderGoodsEvaluationRequest.getGoodsId());
            if (AppIdentityType.getAppIdentityTypeByValue(orderGoodsEvaluationRequest.getIdentityType()).equals(AppIdentityType.CUSTOMER)) {
                AppCustomer customer = customerService.findById(orderGoodsEvaluationRequest.getUserId());
                goodsEvaluation.setEvaluationName(customer.getName());
                goodsEvaluation.setPicUrl(customer.getPicUrl());
            } else {
                AppEmployee employee = employeeService.findById(orderGoodsEvaluationRequest.getUserId());
                goodsEvaluation.setEvaluationName(employee.getName());
                goodsEvaluation.setPicUrl(employee.getPicUrl());
            }
            goodsEvaluation.setEvaluationPictures(pictureUrls.toString());
            goodsEvaluation.setIsShow(Boolean.TRUE);
            orderEvaluationService.addOrderGoodsEvaluation(goodsEvaluation);
            //修改商品为已评价
            orderEvaluationService.updeteGoodsEvaluationStatus(orderGoodsEvaluationRequest.getOrderNumber(),orderGoodsEvaluationRequest.getGoodsId());
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("orderEvaluationPictureSubmit OUT,订单评价图片上传成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,订单评价图片上传失败!", null);
            logger.warn("orderEvaluationPictureSubmit EXCEPTION,订单评价图片上传失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取商品评价
     *
     * @param gid 商品id
     * @return 商品评价List
     */
    @PostMapping(value = "/goods/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getGoodsEvaluationList(Long gid,Integer page, Integer size) {
        logger.info("getGoodsEvaluationList CALLED,获取商品评价,入参 gid:{}, ", gid);
        ResultDTO<Object> resultDTO;

        if (null == gid) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品id不能为空！", null);
            logger.info("getGoodsEvaluationList OUT,获取商品评价失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getGoodsEvaluationList OUT,获取商品评价失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getGoodsEvaluationList OUT,获取商品评价失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            //查询所有评价
            PageInfo<GoodsEvaluation> goodsEvaluationList = goodsEvaluationService.queryEvaluationListByGid(gid, page, size);
            //创建商品评价返回list
            List<GoodsEvaluationListResponse> goodsEvaluationListResponses = new ArrayList<>();
            for (GoodsEvaluation goodsEvaluation : goodsEvaluationList.getList()) {
                GoodsEvaluationListResponse goodsEvaluationListResponse = new GoodsEvaluationListResponse();
                goodsEvaluationListResponse.setCommentContent(goodsEvaluation.getCommentContent());
                goodsEvaluationListResponse.setEvaluationName(goodsEvaluation.getEvaluationName());
                List<String> pictureList = new ArrayList<>();
                String[] pictures = goodsEvaluation.getEvaluationPictures().split(",");
                for (int i = 0; i < pictures.length; i++) {
                    pictureList.add(pictures[i]);
                }
                goodsEvaluationListResponse.setPicUrl(goodsEvaluation.getPicUrl());
                goodsEvaluationListResponse.setEvaluationPictures(pictureList);
                goodsEvaluationListResponse.setIsShow(goodsEvaluation.getIsShow());
                goodsEvaluationListResponse.setEvaluationQuantity(goodsEvaluationService.getEvaluationQuantityByGid(gid));
                goodsEvaluationListResponse.setEvaluationTime(DateUtil.getDateTimeStr(goodsEvaluation.getEvaluationTime()));
                goodsEvaluationListResponses.add(goodsEvaluationListResponse);
            }
            PageInfo<GoodsEvaluationListResponse> pageInfo = new PageInfo<>(goodsEvaluationListResponses);
            pageInfo.setEndRow(goodsEvaluationList.getEndRow());
            pageInfo.setNextPage(goodsEvaluationList.getNextPage());
            pageInfo.setPageNum(goodsEvaluationList.getPageNum());
            pageInfo.setPages(goodsEvaluationList.getPages());
            pageInfo.setPageSize(goodsEvaluationList.getPageSize());
            pageInfo.setPrePage(goodsEvaluationList.getPrePage());
            pageInfo.setSize(goodsEvaluationList.getSize());
            pageInfo.setStartRow(goodsEvaluationList.getStartRow());
            pageInfo.setTotal(goodsEvaluationList.getTotal());
            pageInfo.setNavigateFirstPage(goodsEvaluationList.getNavigateFirstPage());
            pageInfo.setNavigatepageNums(goodsEvaluationList.getNavigatepageNums());
            pageInfo.setNavigateLastPage(goodsEvaluationList.getNavigateLastPage());
            pageInfo.setNavigatePages(goodsEvaluationList.getNavigatePages());
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,  new GridDataVO<GoodsEvaluationListResponse>().transform(pageInfo));
            logger.info("getGoodsEvaluationList OUT,获取商品评价成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,获取商品评价失败!", null);
            logger.warn("getGoodsEvaluationList EXCEPTION,获取商品评价失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}
