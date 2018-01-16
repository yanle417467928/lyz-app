package cn.com.leyizhuang.app.web.controller.order;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.core.utils.oss.FileUploadOSSUtils;
import cn.com.leyizhuang.app.foundation.pojo.PhotoOrderDO;
import cn.com.leyizhuang.app.foundation.pojo.response.PhotoOrderDetailsResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.PhotoOrderListResponse;
import cn.com.leyizhuang.app.foundation.service.PhotoOrderService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/24
 */
@RestController
@RequestMapping("/app/photoOrder")
public class PhotoOrderController {
    private static final Logger logger = LoggerFactory.getLogger(PhotoOrderController.class);

    @Autowired
    private PhotoOrderService photoOrderServiceImpl;


    /**
     * @param userId           用户id
     * @param identityType     用户身份
//     * @param deliveryId       收货人地址
//     * @param isOwnerReceiving 是否主家收货
     * @param contactPhone 联系人电话
     * @param remark           备注
     * @param customerId       顾客id
     * @param request          http请求参数
     * @return 下单结果
     */
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> submitPhotoOrder(Long userId, Integer identityType, Long cityId, /*@RequestParam(value = "myfiles", required = false) MultipartFile[] files,*/
                                              String contactPhone, String remark, Long customerId, HttpServletRequest request) {
        logger.info("submitPhotoOrder CALLED,拍照下单提交，入参 userId:{} identityType:{}  contactPhone:{} remark:{} customerId:{} cityId:{}", userId, identityType, contactPhone, remark, customerId, cityId);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("submitPhotoOrder OUT,拍照下单提交失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                        null);
                logger.info("submitPhotoOrder OUT,拍照下单提交失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == contactPhone) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "联系人电话不能为空！",
                        null);
                logger.info("submitPhotoOrder OUT,拍照下单提交失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            if (0 == identityType && null == customerId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "客户信息不能为空！",
                        null);
                logger.info("submitPhotoOrder OUT,拍照下单提交失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            StringBuilder photos = new StringBuilder();
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                    request.getSession().getServletContext());
            if (multipartResolver.isMultipart(request)) {
                // 转换成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                // 取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                if (!iter.hasNext()) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "图片不能为空！", null);
                    logger.info("submitPhotoOrder OUT,拍照下单提交失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
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
                                photos.append(FileUploadOSSUtils.uploadProfilePhoto(f, "order/photo"));
                            }else{
                                photos.append(FileUploadOSSUtils.uploadProfilePhoto(f, "order/photo")).append(",");
                            }
                        }
                    }
                }
            }
            PhotoOrderDO photoOrderDO = new PhotoOrderDO();
            photoOrderDO.setCreateTime(LocalDateTime.now());
            photoOrderDO.setIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
            photoOrderDO.setContactPhone(contactPhone);
            photoOrderDO.setPhotos(photos.toString());
            photoOrderDO.setRemark(remark);
            photoOrderDO.setStatus(PhotoOrderStatus.PENDING);
            photoOrderDO.setUserId(userId);
            photoOrderDO.setPhotoOrderNo(OrderUtils.generatePhotoOrderNumber(cityId));
            this.photoOrderServiceImpl.save(photoOrderDO);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("submitPhotoOrder OUT,拍照下单提交成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,拍照下单提交失败!", null);
            logger.warn("submitPhotoOrder EXCEPTION,拍照下单提交失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 获取未处理拍照下单列表
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/28
     */
    @PostMapping(value = "/pending/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getPhotoOrderOfPending(Long userId, Integer identityType) {
        logger.info("getPhotoOrderOfPending CALLED,获取未处理拍照下单列表，入参 userId:{} identityType:{}", userId, identityType);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getPhotoOrderOfPending OUT,获取未处理拍照下单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("getPhotoOrderOfPending OUT,获取未处理拍照下单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        List<PhotoOrderStatus> photoOrderStatuses = new ArrayList<PhotoOrderStatus>();
        photoOrderStatuses.add(PhotoOrderStatus.PENDING);
        photoOrderStatuses.add(PhotoOrderStatus.PROCESSING);
        PageInfo<PhotoOrderListResponse> photoOrderListResponseList = this.photoOrderServiceImpl.findByUserIdAndIdentityTypeAndStatus(userId, AppIdentityType.getAppIdentityTypeByValue(identityType), photoOrderStatuses,null,null);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, photoOrderListResponseList.getList());
        logger.info("getPhotoOrderOfPending OUT,获取未处理拍照下单列表成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * @param
     * @return
     * @throws
     * @title 获取已处理拍照下单列表
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/28
     */
    @PostMapping(value = "/handled/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getPhotoOrderOfHandled(Long userId, Integer identityType,Integer page, Integer size) {
        logger.info("getPhotoOrderOfHandled CALLED,获取已处理拍照下单列表，入参 userId:{} identityType:{},page:{},size:{}", userId, identityType,page,size);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getPhotoOrderOfHandled OUT,获取已处理拍照下单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("getPhotoOrderOfHandled OUT,获取已处理拍照下单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getPhotoOrderOfHandled OUT,获取已处理拍照下单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getPhotoOrderOfHandled OUT,获取已处理拍照下单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        List<PhotoOrderStatus> photoOrderStatuses = new ArrayList<PhotoOrderStatus>();
        photoOrderStatuses.add(PhotoOrderStatus.FINISH);
        PageInfo<PhotoOrderListResponse> photoOrderListResponseList = this.photoOrderServiceImpl.findByUserIdAndIdentityTypeAndStatus(userId, AppIdentityType.getAppIdentityTypeByValue(identityType), photoOrderStatuses, page,  size);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new GridDataVO<PhotoOrderListResponse>().transform(photoOrderListResponseList));
        logger.info("getPhotoOrderOfHandled OUT,获取已处理拍照下单列表成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * @param
     * @return
     * @throws
     * @title 获取拍照下单详情
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/28
     */
    @PostMapping(value = "/details", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getPhotoOrderDetails(Long userId, Integer identityType, Long id) {
        logger.info("getPhotoOrderDetails CALLED,获取拍照下单详情，入参 userId:{} identityType:{} id:{}", userId, identityType, id);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getPhotoOrderDetails OUT,获取拍照下单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("getPhotoOrderDetails OUT,获取拍照下单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == id) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "拍照下单详情ID不能为空！", null);
            logger.info("getPhotoOrderDetails OUT,获取拍照下单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        PhotoOrderDetailsResponse photoOrderDetailsResponse = this.photoOrderServiceImpl.findById(id);
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, photoOrderDetailsResponse);
        logger.info("getPhotoOrderDetails OUT,获取拍照下单详情成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }


    /**
     * @param
     * @return
     * @throws
     * @title 取消拍照下单
     * @descripe
     * @author GenerationRoad
     * @date 2017/11/28
     */
    @PostMapping(value = "/cancel", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> cancelPhotoOrder(Long userId, Integer identityType, Long id) {
        logger.info("cancelPhotoOrder CALLED,取消拍照下单，入参 userId:{} identityType:{} id:{} ", userId, identityType, id);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("cancelPhotoOrder OUT,取消拍照下单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                        null);
                logger.info("cancelPhotoOrder OUT,取消拍照下单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == id) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "拍照下单详情ID不能为空！", null);
                logger.info("cancelPhotoOrder OUT,取消拍照下单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            PhotoOrderDetailsResponse photoOrderDetailsResponse = this.photoOrderServiceImpl.findById(id);
            if (null != photoOrderDetailsResponse && photoOrderDetailsResponse.getStatus().equals(PhotoOrderStatus.PENDING.getValue())) {
                this.photoOrderServiceImpl.updateStatus(id, PhotoOrderStatus.CANCEL);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                logger.info("cancelPhotoOrder OUT,取消拍照下单成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else if (null != photoOrderDetailsResponse && photoOrderDetailsResponse.getStatus().equals(PhotoOrderStatus.CANCEL.getValue())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "已取消,请勿重复操作！", null);
                logger.info("cancelPhotoOrder OUT,取消拍照下单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "已下单，不能取消！", null);
                logger.info("cancelPhotoOrder OUT,取消拍照下单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,取消拍照下单失败!", null);
            logger.warn("cancelPhotoOrder EXCEPTION,取消拍照下单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}
