package cn.com.leyizhuang.app.web.controller;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.MaterialListType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditSheet;
import cn.com.leyizhuang.app.foundation.pojo.request.MaterialAuditSheetRequest;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialAuditDetailsResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialAuditSheetResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 物料审核单
 * Created by caiyu on 2017/10/17.
 */
@RestController
@RequestMapping(value = "/app/material/audit")
public class MaterialAuditSheetController {

    private static final Logger logger = LoggerFactory.getLogger(MaterialAuditSheetController.class);
    @Resource
    private AppEmployeeService appEmployeeService;

    @Resource
    private MaterialAuditSheetService materialAuditSheetService;

    @Resource
    private MaterialAuditGoodsInfoService materialAuditGoodsInfoService;

    @Autowired
    private MaterialListService materialListService;

    @Autowired
    private CommonService commonService;

    /**
     * 新增物料审核单
     *
     * @param materialAuditSheetRequest 物料审核单相关信息
     * @return 返回物料审核单列表
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> addMaterialAuditSheet(MaterialAuditSheetRequest materialAuditSheetRequest) {
        ResultDTO<Object> resultDTO;
        logger.info("addMaterialAuditSheet CALLED,新增物料审核单，入参 materialAuditSheetRequest:{}", materialAuditSheetRequest);
        if (null == materialAuditSheetRequest.getUserID()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("addMaterialAuditSheet OUT,新增物料审核单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(materialAuditSheetRequest.getIdentityType())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("addMaterialAuditSheet OUT,新增物料审核单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == materialAuditSheetRequest.getGoodsList()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "购买商品不能为空", null);
            logger.info("addMaterialAuditSheet OUT,新增物料审核单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(materialAuditSheetRequest.getReceiver())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人姓名不能为空", null);
            logger.info("addMaterialAuditSheet OUT,新增物料审核单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(materialAuditSheetRequest.getReceiverPhone())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人电话号码不能为空", null);
            logger.info("addMaterialAuditSheet OUT,新增物料审核单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(materialAuditSheetRequest.getDeliveryCity())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货地址（市）不能为空", null);
            logger.info("addMaterialAuditSheet OUT,新增物料审核单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(materialAuditSheetRequest.getDeliveryCounty())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货地址（区）不能为空", null);
            logger.info("addMaterialAuditSheet OUT,新增物料审核单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(materialAuditSheetRequest.getDeliveryStreet())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货地址（街道）不能为空", null);
            logger.info("addMaterialAuditSheet OUT,新增物料审核单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(materialAuditSheetRequest.getResidenceName())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货地址（小区）不能为空", null);
            logger.info("addMaterialAuditSheet OUT,新增物料审核单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(materialAuditSheetRequest.getDetailedAddress())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货地址（详细地址）不能为空", null);
            logger.info("addMaterialAuditSheet OUT,新增物料审核单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == materialAuditSheetRequest.getReservationDeliveryTime()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "预约配送时间不能为空", null);
            logger.info("addMaterialAuditSheet OUT,新增物料审核单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //获取用户信息
            AppEmployee appEmployee = appEmployeeService.findById(materialAuditSheetRequest.getUserID());
            if (null == appEmployee) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查到下单工人", null);
                logger.info("addMaterialAuditSheet OUT,新增物料审核单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //进行物料审核单单头、商品明细的保存
            materialAuditSheetService.addMaterialAuditSheet(materialAuditSheetRequest, appEmployee);
            //创建一个存储返回参数对象的list
            List<MaterialAuditSheetResponse> materialAuditSheetResponsesList = new ArrayList<>();
            //获取工人所有的物料审核单列表
            List<MaterialAuditSheet> materialAuditSheetList = materialAuditSheetService.queryListByEmployeeID(materialAuditSheetRequest.getUserID());
            //遍历工人所有的物料审核单
            for (MaterialAuditSheet materialAuditSheet1 : materialAuditSheetList) {
                //创建一个返回参数对象
                MaterialAuditSheetResponse materialAuditSheetResponse = new MaterialAuditSheetResponse();
                //查询每单物料审核单所有商品信息
                List<MaterialAuditGoodsInfo> materialAuditGoodsInfoList = materialAuditGoodsInfoService.
                        queryListByAuditHeaderID(materialAuditSheet1.getAuditHeaderID());
                //创建一个图片list存储图片地址
                List<String> pictureList = new ArrayList<>();
                //商品总价格
                Double totalPrice = 0D;
                for (MaterialAuditGoodsInfo materialAuditGoodsInfo : materialAuditGoodsInfoList) {
                    pictureList.add(materialAuditGoodsInfo.getCoverImageUri());
                    totalPrice += (materialAuditGoodsInfo.getRetailPrice() * materialAuditGoodsInfo.getQty());
                }
                //向返回参数对象中设置
                materialAuditSheetResponse.setTotalPrice(totalPrice);
                materialAuditSheetResponse.setAuditNo(materialAuditSheet1.getAuditNo());
                materialAuditSheetResponse.setDeliveryCity(materialAuditSheet1.getDeliveryCity());
                materialAuditSheetResponse.setDeliveryCounty(materialAuditSheet1.getDeliveryCounty());
                materialAuditSheetResponse.setDeliveryStreet(materialAuditSheet1.getDeliveryStreet());
                materialAuditSheetResponse.setResidenceName(materialAuditSheet1.getResidenceName());
                materialAuditSheetResponse.setDetailedAddress(materialAuditSheet1.getDetailedAddress());
                materialAuditSheetResponse.setIsAudited(materialAuditSheet1.getIsAudited());
                materialAuditSheetResponse.setTotalQty(materialAuditGoodsInfoService.querySumQtyByAuditHeaderID(materialAuditSheet1.getAuditHeaderID()));
                materialAuditSheetResponse.setStatus(materialAuditSheet1.getStatus());
                materialAuditSheetResponse.setPictureList(pictureList);
                materialAuditSheetResponse.setWorker(appEmployee.getName());
                //把所有返回参数对象放入list
                materialAuditSheetResponsesList.add(materialAuditSheetResponse);
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, materialAuditSheetResponsesList);
            logger.info("addMaterialAuditSheet OUT,新增物料审核单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，新增物料审核单失败", null);
            logger.warn("addMaterialAuditSheet EXCEPTION,新增物料审核单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 取消物料审核单
     *
     * @param auditNo 物料审核单编号
     * @return 返回成功或失败
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public ResultDTO<Object> updateStatus(String auditNo) {
        ResultDTO<Object> resultDTO;
        logger.info("updateStatus CALLED,修改物料审核单状态，入参 suditNumber:{}", auditNo);
        if (StringUtils.isBlank(auditNo)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "物料审核单编号不能为空", null);
            logger.info("updateStatus OUT,修改物料审核单状态失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            MaterialAuditSheet materialAuditSheet = materialAuditSheetService.queryByAuditNo(auditNo);
            if (null == materialAuditSheet) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查到此物料审核单", null);
                logger.info("updateStatus OUT,修改物料审核单状态失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            materialAuditSheetService.modifyStatus(3, auditNo);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("addMaterialAuditSheet OUT,修改物料审核单状态成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，修改物料审核单状态失败", null);
            logger.warn("addMaterialAuditSheet EXCEPTION,修改物料审核单状态失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 根据物料审核单编号查询物料审核单详情
     *
     * @param auditNo 物料审核单编号
     * @return 返回物料审核单详情
     */
    @RequestMapping(value = "/details", method = RequestMethod.POST)
    public ResultDTO<Object> materialAuditGoodsDetails(String auditNo) {
        ResultDTO<Object> resultDTO;
        logger.info("materialAuditGoodsDetails CALLED,查看物料审核单详情，入参 suditNo:{}", auditNo);
        if (StringUtils.isBlank(auditNo)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "物料审核单编号不能为空", null);
            logger.info("materialAuditGoodsDetails OUT,查看物料审核单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //根据物料审核单编号查找物料审核单
            MaterialAuditSheet materialAuditSheet = materialAuditSheetService.queryByAuditNo(auditNo);
            //转换为返回值类
            MaterialAuditDetailsResponse materialAuditDetailsResponse = materialAuditSheet.getMaterialAuditDetailsResponse();
            if (null == materialAuditDetailsResponse) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此物料审核单", null);
                logger.info("materialAuditGoodsDetails OUT,查看物料审核单详情失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //定义时间格式
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //把LocalDateTime类型转换为String类型，并进行赋值
            materialAuditDetailsResponse.setCreateTime(df.format(materialAuditSheet.getCreateTime()));
            materialAuditDetailsResponse.setReservationDeliveryTime(materialAuditSheet.getReservationDeliveryTime());
            //商品总金额（零售）
            Double totalPrice = 0D;
            //查询物料审核单中对应的商品
            List<MaterialAuditGoodsInfo> materialAuditGoodsInfoList = materialAuditGoodsInfoService.queryListByAuditHeaderID(materialAuditSheet.getAuditHeaderID());
            if (null != materialAuditGoodsInfoList && materialAuditGoodsInfoList.size() > 0) {
                for (MaterialAuditGoodsInfo materialAuditGoodsInfo : materialAuditGoodsInfoList) {
                    totalPrice += (materialAuditGoodsInfo.getRetailPrice() * materialAuditGoodsInfo.getQty());
                }
                materialAuditDetailsResponse.setTotalPrice(totalPrice);
                //把物料审核单中所有的商品list放入返回值对象中
                materialAuditDetailsResponse.setGoodsList(materialAuditGoodsInfoList);

                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, materialAuditDetailsResponse);
                logger.info("materialAuditGoodsDetails OUT,查看物料审核单详情成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此物料审核单商品", null);
                logger.info("materialAuditGoodsDetails OUT,查看物料审核单详情失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，查看物料审核单详情失败", null);
            logger.warn("materialAuditGoodsDetails EXCEPTION,查看物料审核单详情失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 根据用户id与料单状态获取对应的物料审核单列表
     *
     * @param userID 用户id
     * @param status 料单状态
     * @return 返回物料审核单列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultDTO<Object> queryListByEmployeeIDAndStatus(Long userID, Integer status) {
        ResultDTO<Object> resultDTO;
        logger.info("queryListByEmployeeIDAndStatus CALLED,根据用户id与状态获取物料审核列表，入参 userID:{},status:{}", userID, status);
        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("queryListByEmployeeIDAndStatus OUT,获取物料审核单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //查询用户对应状态的所有物料审核单
            List<MaterialAuditSheetResponse> materialAuditSheetResponseList = materialAuditSheetService.queryListByEmployeeIDAndStatus(userID, status);
            for (MaterialAuditSheetResponse materialAuditSheetResponse : materialAuditSheetResponseList) {
                //计算每单物料审核单的商品总数
                int totalQty = materialAuditGoodsInfoService.querySumQtyByAuditHeaderID(materialAuditSheetResponse.getAuditHeaderID());
                //查询每单物料审核单所有商品信息
                List<MaterialAuditGoodsInfo> materialAuditGoodsInfoList = materialAuditGoodsInfoService.queryListByAuditHeaderID(materialAuditSheetResponse.getAuditHeaderID());
                //创建一个图片list存储图片地址
                List<String> pictureList = new ArrayList<>();
                //商品总金额
                Double totalPrice = 0d;
                for (MaterialAuditGoodsInfo materialAuditGoodsInfo : materialAuditGoodsInfoList) {
                    pictureList.add(materialAuditGoodsInfo.getCoverImageUri());
                    totalPrice += (materialAuditGoodsInfo.getRetailPrice() * materialAuditGoodsInfo.getQty());
                }
                //设值
                materialAuditSheetResponse.setTotalPrice(totalPrice);
                materialAuditSheetResponse.setTotalQty(totalQty);
                materialAuditSheetResponse.setPictureList(pictureList);
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, materialAuditSheetResponseList);
            logger.info("materialAuditGoodsDetails OUT,获取物料审核单列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取物料审核单列表失败", null);
            logger.warn("queryListByEmployeeIDAndStatus EXCEPTION,获取物料审核单列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 装饰公司项目经理查看物料审核单列表
     *
     * @param userID 用户id
     * @param status 订单状态
     * @return 返回物料审核单列表
     */
    @RequestMapping(value = "/manager/list", method = RequestMethod.POST)
    public ResultDTO<Object> managerGetMaterialAuditSheet(Long userID, Integer status) {
        ResultDTO<Object> resultDTO;
        logger.info("managerGetMaterialAuditSheet CALLED,根据用户id获取装饰公司审核列表，入参 userID:{},", userID);
        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("managerGetMaterialAuditSheet OUT,根据用户id获取装饰公司审核列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //获取用户信息
            AppEmployee appEmployee = appEmployeeService.findById(userID);
            //创建一个存储返回参数对象的list
            List<MaterialAuditSheetResponse> materialAuditSheetResponsesList = new ArrayList<>();
            //获取工人所有的物料审核单列表
            List<MaterialAuditSheet> materialAuditSheetList = materialAuditSheetService.queryListByStoreIDAndStatus(appEmployee.getStoreId(), status);
            //遍历工人所有的物料审核单
            for (MaterialAuditSheet materialAuditSheet1 : materialAuditSheetList) {
                //创建一个返回参数对象
                MaterialAuditSheetResponse materialAuditSheetResponse = new MaterialAuditSheetResponse();
                //查询每单物料审核单所有商品信息
                List<MaterialAuditGoodsInfo> materialAuditGoodsInfoList = materialAuditGoodsInfoService.queryListByAuditHeaderID(materialAuditSheet1.getAuditHeaderID());
                //创建一个图片list存储图片地址
                List<String> pictureList = new ArrayList<>();
                //商品总价格
                Double totalPrice = 0D;
                for (MaterialAuditGoodsInfo materialAuditGoodsInfo : materialAuditGoodsInfoList) {
                    pictureList.add(materialAuditGoodsInfo.getCoverImageUri());
                    totalPrice += (materialAuditGoodsInfo.getRetailPrice() * materialAuditGoodsInfo.getQty());
                }
                //向返回参数对象中设置
                materialAuditSheetResponse.setAuditNo(materialAuditSheet1.getAuditNo());
                materialAuditSheetResponse.setDeliveryCity(materialAuditSheet1.getDeliveryCity());
                materialAuditSheetResponse.setDeliveryCounty(materialAuditSheet1.getDeliveryCounty());
                materialAuditSheetResponse.setDeliveryStreet(materialAuditSheet1.getDeliveryStreet());
                materialAuditSheetResponse.setResidenceName(materialAuditSheet1.getResidenceName());
                materialAuditSheetResponse.setDetailedAddress(materialAuditSheet1.getDetailedAddress());
                materialAuditSheetResponse.setTotalQty(materialAuditGoodsInfoService.querySumQtyByAuditHeaderID(materialAuditSheet1.getAuditHeaderID()));
                materialAuditSheetResponse.setTotalPrice(totalPrice);
                materialAuditSheetResponse.setIsAudited(materialAuditSheet1.getIsAudited());
                materialAuditSheetResponse.setStatus(materialAuditSheet1.getStatus());
                materialAuditSheetResponse.setPictureList(pictureList);
                materialAuditSheetResponse.setWorker(materialAuditSheet1.getEmployeeName());
                //把所有返回参数对象放入list
                materialAuditSheetResponsesList.add(materialAuditSheetResponse);
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, materialAuditSheetResponsesList);
            logger.info("addMaterialAuditSheet OUT,新增物料审核单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取装饰公司审核列表失败", null);
            logger.warn("managerGetMaterialAuditSheet EXCEPTION,获取装饰公司审核列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 项目经理审核物料审核单
     *
     * @param auditNo   物料审核单编码
     * @param isAudited 是否通过审核
     * @param userID    用户id
     * @return 返回成功或失败
     */
    @RequestMapping(value = "/manager/check", method = RequestMethod.POST)
    public ResultDTO<Object> managerAudit(Long userID, String auditNo, Boolean isAudited) {
        ResultDTO<Object> resultDTO;
        logger.info("managerAudit CALLED,项目经理审核物料审核单，入参 userID:{}, auditNo:{}, isAudited:{}", userID, auditNo, isAudited);
        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("managerAudit OUT,项目经理审核物料审核单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(auditNo)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "物料审核单编号不能为空", null);
            logger.info("managerAudit OUT,项目经理审核物料审核单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == isAudited) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "是否审核通过不能为空", null);
            logger.info("managerAudit OUT,项目经理审核物料审核单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            Boolean isOther = materialListService.existOtherMaterialListByUserIdAndIdentityType(userID, 2);
            if (isOther) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "下料清单中已存在通过审核的料单", null);
                logger.info("managerAudit OUT,项目经理审核物料审核单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            MaterialAuditSheet materialAuditSheet = materialAuditSheetService.queryByAuditNo(auditNo);
            if (null == materialAuditSheet) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此物料审核单", null);
                logger.info("managerAudit OUT,项目经理审核物料审核单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (materialAuditSheet.getStatus() == 2) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此物料审核单已被审核", null);
                logger.info("managerAudit OUT,项目经理审核物料审核单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (isAudited) {
                materialAuditSheet.setIsAudited(true);
            } else {
                materialAuditSheet.setIsAudited(false);
            }
            materialAuditSheet.setStatus(2);
            materialAuditSheet.setAuditorID(userID);
            materialAuditSheetService.modifyMaterialAuditSheet(materialAuditSheet);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("managerAudit OUT,项目经理审核物料审核单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，项目经理审核物料审核单失败", null);
            logger.warn("managerAudit EXCEPTION,项目经理审核物料审核单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 装饰公司项目经理审核通过加入下料清单
     *
     * @param userID       用户ID
     * @param auditNo      物料审核单编号
     * @param identityType 用户类型
     * @return 返回审核料单商品信息
     * @anther Jerry.Ren
     */
    @RequestMapping(value = "/manager/transform/materialList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> transformMaterialList(Long userID, String auditNo, Integer identityType) {
        ResultDTO<Object> resultDTO;
        logger.info("transformMaterialList CALLED,装饰公司项目经理审核通过加入下料清单，入参 userID:{},auditNo:{},identityType:{}", userID, auditNo, identityType);
        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("transformMaterialList OUT,装饰公司项目经理审核通过加入下料清单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("transformMaterialList OUT,装饰公司项目经理审核通过加入下料清单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(auditNo)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "审核单编号不能为空", null);
            logger.info("transformMaterialList OUT,装饰公司项目经理审核通过加入下料清单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != 2) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该用户类型不能进行支付", null);
            logger.info("transformMaterialList OUT,装饰公司项目经理审核通过加入下料清单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            Boolean isOther = materialListService.existOtherMaterialListByUserIdAndIdentityType(userID, identityType);
            if (isOther) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "下料清单中已存在待支付的料单", null);
                logger.info("transformMaterialList OUT,装饰公司项目经理审核通过加入下料清单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //根据物料审核单编号查找物料审核单
            MaterialAuditSheet materialAuditSheet = materialAuditSheetService.queryByAuditNo(auditNo);
            if (null == materialAuditSheet) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此物料审核单", null);
                logger.info("transformMaterialList OUT,装饰公司项目经理审核通过加入下料清单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //查询物料审核单中对应的商品加入下料清单
            List<MaterialAuditGoodsInfo> materialAuditGoodsInfoList = materialAuditGoodsInfoService.queryListByAuditHeaderID(materialAuditSheet.getAuditHeaderID());
            if (null != materialAuditGoodsInfoList && materialAuditGoodsInfoList.size() > 0) {
                //创建一个持久化下料清单对象
                List<MaterialListDO> materialListSave = new ArrayList<>(materialAuditGoodsInfoList.size());
                List<MaterialListDO> materialListUpdate = new ArrayList<>(materialAuditGoodsInfoList.size());
                List<Long> deleteGoodsIds = new ArrayList<>();
                AppIdentityType appIdentityType = AppIdentityType.getAppIdentityTypeByValue(identityType);
                //遍历这个料单
                for (MaterialAuditGoodsInfo materialAuditGoodsInfo : materialAuditGoodsInfoList) {
                    //如果发现经理的下料清单中有相同的商品
                    MaterialListDO material = materialListService.findAuditListByUserIdAndIdentityTypeAndGoodsId(userID,
                            appIdentityType, materialAuditGoodsInfo.getGid());
                    //转化料单到经理的下料清单中
                    MaterialListDO materialListDO = transform(materialAuditGoodsInfo, userID, auditNo);
                    //有相同商品直接和料单的商品合并
                    if (material != null) {
                        materialListDO.setQty(materialListDO.getQty() + material.getQty());
                        deleteGoodsIds.add(material.getId());
                    }
                    materialListSave.add(materialListDO);
                }
                //删掉已经合并的商品
                materialListService.deleteMaterialListByUserIdAndIdentityTypeAndGoodsId(userID, appIdentityType, deleteGoodsIds);
                //新增转化的料单
                commonService.saveAndUpdateMaterialList(materialListSave, materialListUpdate);

                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                logger.info("transformMaterialList OUT,装饰公司项目经理审核通过加入下料清单成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此物料审核单商品", null);
                logger.info("transformMaterialList OUT,装饰公司项目经理审核通过加入下料清单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，装饰公司项目经理审核通过加入下料清单失败", null);
            logger.warn("transformMaterialList EXCEPTION,装饰公司项目经理审核通过加入下料清单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    private MaterialListDO transform(MaterialAuditGoodsInfo materialAuditGoodsInfo, Long userID, String auditNo) {

        MaterialListDO materialListDO = new MaterialListDO();

        materialListDO.setUserId(userID);
        materialListDO.setIdentityType(AppIdentityType.DECORATE_MANAGER);
        materialListDO.setQty(materialAuditGoodsInfo.getQty());
        materialListDO.setCoverImageUri(materialAuditGoodsInfo.getCoverImageUri());
        materialListDO.setGid(materialAuditGoodsInfo.getGid());
        materialListDO.setGoodsSpecification(materialAuditGoodsInfo.getGoodsSpecification());
        materialListDO.setGoodsUnit(materialAuditGoodsInfo.getGoodsUnit());
        materialListDO.setSku(materialAuditGoodsInfo.getSku());
        materialListDO.setSkuName(materialAuditGoodsInfo.getSkuName());
        materialListDO.setAuditNo(auditNo);
        materialListDO.setMaterialListType(MaterialListType.AUDIT_TRANSFORM);

        return materialListDO;

    }
}
