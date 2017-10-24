package cn.com.leyizhuang.app.web.controller.order;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.PhotoOrderStatus;
import cn.com.leyizhuang.app.core.utils.oss.FileUploadOSSUtils;
import cn.com.leyizhuang.app.foundation.pojo.PhotoOrderDO;
import cn.com.leyizhuang.app.foundation.service.PhotoOrderService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

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
     * @title   拍照下单
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/10/24
     */
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> photoOrder(Long userId, Integer identityType, @RequestParam(value = "myfiles",required = false) MultipartFile[] files,
                                             Long deliveryId, Boolean isOwnerReceiving, String remark, Long customerId) {
        logger.info("photoOrder CALLED,拍照下单提交，入参 userId:{} identityType:{} files:{} deliveryId:{} isOwnerReceiving:{} remark:{} customerId:{}", userId, identityType, files, deliveryId, isOwnerReceiving, remark, customerId);
        ResultDTO<Object> resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("photoOrder OUT,拍照下单提交失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！",
                        null);
                logger.info("photoOrder OUT,拍照下单提交失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == files || files.length == 0){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "图片不能为空！", null);
                logger.info("photoOrder OUT,拍照下单提交失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == deliveryId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货地址不能为空！",
                        null);
                logger.info("photoOrder OUT,拍照下单提交失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == isOwnerReceiving) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "是否主家收货不能为空！",
                        null);
                logger.info("photoOrder OUT,拍照下单提交失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (0 == identityType && null == customerId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "客户信息不能为空！",
                        null);
                logger.info("photoOrder OUT,拍照下单提交失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            String photos = "";
            for (int i = 0; i < files.length; i++) {
                photos += FileUploadOSSUtils.uploadProfilePhoto(files[i], "order/photo");
            }
            PhotoOrderDO photoOrderDO = new PhotoOrderDO();
            photoOrderDO.setCreateTime(LocalDateTime.now());
            photoOrderDO.setCustomerId(customerId);
            photoOrderDO.setDeliveryId(deliveryId);
            photoOrderDO.setIdentityType(AppIdentityType.getAppUserTypeByValue(identityType));
            photoOrderDO.setIsOwnerReceiving(isOwnerReceiving);
            photoOrderDO.setPhotos(photos);
            photoOrderDO.setRemark(remark);
            photoOrderDO.setStatus(PhotoOrderStatus.PENDING);
            photoOrderDO.setUserId(userId);

            this.photoOrderServiceImpl.save(photoOrderDO);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("photoOrder OUT,拍照下单提交成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,拍照下单提交失败!", null);
            logger.warn("photoOrder EXCEPTION,拍照下单提交失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

}
