package cn.com.leyizhuang.app.web.controller.order;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.core.utils.oss.FileUploadOSSUtils;
import cn.com.leyizhuang.app.foundation.pojo.PhotoOrderDO;
import cn.com.leyizhuang.app.foundation.pojo.response.PhotoOrderDetailsResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.PhotoOrderListResponse;
import cn.com.leyizhuang.app.foundation.service.PhotoOrderService;
import cn.com.leyizhuang.app.foundation.service.impl.BASE64DecodedMultipartFile;
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
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

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
     * @param userId       用户id
     * @param identityType 用户身份
     *                     //     * @param deliveryId       收货人地址
     *                     //     * @param isOwnerReceiving 是否主家收货
     * @param contactName  联系人姓名
     * @param contactPhone 联系人电话
     * @param remark       备注
     *                     //     * @param customerId       顾客id
     * @param request      http请求参数
     * @return 下单结果
     */
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> submitPhotoOrder(Long userId, Integer identityType, Long cityId, String contactName,/*@RequestParam(value = "myfiles", required = false) MultipartFile[] files,*/
                                              String contactPhone, String remark, HttpServletRequest request) {
        logger.info("submitPhotoOrder CALLED,拍照下单提交，入参 userId:{} identityType:{}  contactName:{} contactPhone:{} remark:{} cityId:{}", userId, identityType, contactName, contactPhone, remark, cityId);
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
            if (null == contactName) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "联系人姓名不能为空！",
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

            String orderNumber = OrderUtils.generatePhotoOrderNumber(cityId);
            String buffers = "订单号：" + orderNumber;
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
                Font font;
//                Color color = new Color(255, 255, 255, 128);
                int a = 1;
                while (iter.hasNext()) {
                    // 取得上传文件
                    MultipartFile f = multiRequest.getFile(iter.next());

                    if (f != null) {
                        //*******************************开始给图片增加单号********************************
//                        buffer.append("-"+i);
                        String buffer = buffers + "-" + a;
                        // 获取文件名
                        String fileName = f.getOriginalFilename();
                        // 获取文件后缀
                        String prefix = fileName.substring(fileName.lastIndexOf("."));
                        // 用uuid作为文件名，防止生成的临时文件重复
                        final File excelFile = File.createTempFile(orderNumber, prefix);
                        // MultipartFile to File
                        f.transferTo(excelFile);
                        Image file = ImageIO.read(excelFile);
                        int fileWidth = file.getWidth(null);
                        int fileHeight = file.getHeight(null);
                        int fontSize;
                        if (fileWidth >= 1000) {
                            font = new Font("微软雅黑", Font.PLAIN, 80);
                            fontSize = 80;
                        } else {
                            font = new Font("微软雅黑", Font.PLAIN, 50);
                            fontSize = 50;
                        }

                        BufferedImage bugImg = new BufferedImage(fileWidth, fileHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics2D g = bugImg.createGraphics();
                        g.setColor(Color.red);
                        g.setFont(font);
                        g.drawImage(file, 0, 0, fileWidth, fileHeight, null);


                        int fontlen = getWatermarkLength(buffer, g);
                        int line = fontlen / fileWidth;//文字长度相对于图片宽度应该有多少行
                        int y = fileHeight - (line + 1) * fontSize;
                        System.out.println("水印文字总长度:" + fontlen + ",图片宽度:" + fileWidth + ",字符个数:" + buffer.length());
                        //文字叠加,自动换行叠加
                        int tempX = 0;
                        int tempY = y;
                        int tempCharLen = 0;//单字符长度
                        int tempLineLen = 0;//单行字符总长度临时计算
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < buffer.length(); i++) {
                            char tempChar = buffer.charAt(i);
                            tempCharLen = getCharLen(tempChar, g);
                            tempLineLen += tempCharLen;
                            if (tempLineLen >= fileWidth) {
                                //长度已经满一行,进行文字叠加
                                g.drawString(sb.toString(), tempX, tempY);
                                sb.delete(0, sb.length());//清空内容,重新追加
                                tempY += fontSize;
                                tempLineLen = 0;
                            }
                            sb.append(tempChar);//追加字符
                        }
                        g.drawString(sb.toString(), tempX, tempY);//最后叠加余下的文字
                        g.dispose();


//                        int x = fileWidth - getWatermarkLength(buffer.toString(), g) - 3;
//                        int y = fileHeight - 3;
//                        g.drawString(buffer.toString(), x, y);
//                        g.dispose();

//                        // 输出图片
//                        FileOutputStream outImgStream = new FileOutputStream("E:/demo/aftertimg.jpg");
//                        ImageIO.write(bugImg, "jpg", outImgStream);
//                        outImgStream.flush();
//                        outImgStream.close();


                        String imageString = null;
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ImageIO.write(bugImg, "jpg", bos);
                        byte[] imageBytes = bos.toByteArray();
                        BASE64Encoder encoder = new BASE64Encoder();
                        imageString = encoder.encode(imageBytes);
                        bos.close();
                        MultipartFile multipartFile = base64ToMultipart(imageString);

                        deleteFile(excelFile);

                        //***********************************增加单号完成********************************************


                        // 取得当前上传文件的文件名称
                        String myFileName = f.getOriginalFilename();
                        // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                        if (!"".equals(myFileName.trim())) {
                            // 定义上传路径
                            if (!iter.hasNext()) {
                                photos.append(FileUploadOSSUtils.uploadProfilePhoto(multipartFile, "order/photo"));
                            } else {
                                photos.append(FileUploadOSSUtils.uploadProfilePhoto(multipartFile, "order/photo")).append(",");
                            }
                            a += 1;
                            if (a > 8) {
                                break;
                            }
                        }
                    }
                }
            }
            PhotoOrderDO photoOrderDO = new PhotoOrderDO();
            photoOrderDO.setCreateTime(LocalDateTime.now());
            photoOrderDO.setIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
            photoOrderDO.setContactPhone(contactPhone);
            photoOrderDO.setContactName(contactName);
            photoOrderDO.setPhotos(photos.toString());
            photoOrderDO.setRemark(remark);
            photoOrderDO.setStatus(PhotoOrderStatus.PENDING);
            photoOrderDO.setUserId(userId);
            photoOrderDO.setPhotoOrderNo(orderNumber);
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
    public ResultDTO<Object> getPhotoOrderOfPending(Long userId, Integer identityType, Integer page, Integer size) {
        logger.info("getPhotoOrderOfPending CALLED,获取未处理拍照下单列表，入参 userId:{} identityType:{},page:{},size:{}", userId, identityType, page, size);
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
        photoOrderStatuses.add(PhotoOrderStatus.PENDING);
        photoOrderStatuses.add(PhotoOrderStatus.PROCESSING);
        PageInfo<PhotoOrderListResponse> photoOrderListResponseList = this.photoOrderServiceImpl.findByUserIdAndIdentityTypeAndStatus(userId, AppIdentityType.getAppIdentityTypeByValue(identityType), photoOrderStatuses, page, size);
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
    public ResultDTO<Object> getPhotoOrderOfHandled(Long userId, Integer identityType, Integer page, Integer size) {
        logger.info("getPhotoOrderOfHandled CALLED,获取已处理拍照下单列表，入参 userId:{} identityType:{},page:{},size:{}", userId, identityType, page, size);
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
        PageInfo<PhotoOrderListResponse> photoOrderListResponseList = this.photoOrderServiceImpl.findByUserIdAndIdentityTypeAndStatus(userId, AppIdentityType.getAppIdentityTypeByValue(identityType), photoOrderStatuses, page, size);
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

    public int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }

    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public static MultipartFile base64ToMultipart(String base64) {
        try {
            String[] baseStrs = base64.split(",");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = new byte[0];
            b = decoder.decodeBuffer(baseStrs[0]);

            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return new BASE64DecodedMultipartFile(b, baseStrs[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getCharLen(char c, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charWidth(c);
    }

}
