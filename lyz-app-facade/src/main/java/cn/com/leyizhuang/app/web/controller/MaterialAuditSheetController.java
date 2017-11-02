package cn.com.leyizhuang.app.web.controller;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.order.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditSheet;
import cn.com.leyizhuang.app.foundation.pojo.request.MaterialAuditSheetRequest;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialAuditDetailsResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialAuditGoPayResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialAuditSheetResponse;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.app.foundation.service.MaterialAuditGoodsInfoService;
import cn.com.leyizhuang.app.foundation.service.MaterialAuditSheetService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
    private GoodsService goodsService;

    @Resource
    private MaterialAuditGoodsInfoService materialAuditGoodsInfoService;


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
            //新增物料审核单头赋值
            MaterialAuditSheet materialAuditSheet = new MaterialAuditSheet();
            materialAuditSheet.setEmployeeID(materialAuditSheetRequest.getUserID());
            materialAuditSheet.setEmployeeName(appEmployee.getName());
            materialAuditSheet.setDeliveryType("送货上门");
            materialAuditSheet.setStoreID(appEmployee.getStoreId());
            materialAuditSheet.setReceiver(materialAuditSheetRequest.getReceiver());
            materialAuditSheet.setReceiverPhone(materialAuditSheetRequest.getReceiverPhone());
            materialAuditSheet.setDeliveryCity(materialAuditSheetRequest.getDeliveryCity());
            materialAuditSheet.setDeliveryCounty(materialAuditSheetRequest.getDeliveryCounty());
            materialAuditSheet.setDeliveryStreet(materialAuditSheetRequest.getDeliveryStreet());
            materialAuditSheet.setResidenceName(materialAuditSheetRequest.getResidenceName());
            materialAuditSheet.setDetailedAddress(materialAuditSheetRequest.getDetailedAddress());
            materialAuditSheet.setIsOwnerReceiving(materialAuditSheetRequest.getIsOwnerReceiving());
            materialAuditSheet.setRemark(materialAuditSheetRequest.getRemark());
            materialAuditSheet.setStatus(1);
            String auditNumber = this.createNumber();
            materialAuditSheet.setAuditNo(auditNumber);
            //把String类型时间转换为LocalDateTime类型
            materialAuditSheet.setReservationDeliveryTime(LocalDateTime.parse(materialAuditSheetRequest.getReservationDeliveryTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            materialAuditSheet.setCreateTime(LocalDateTime.now());
            //保存物料审核单头
            materialAuditSheetService.addMaterialAuditSheet(materialAuditSheet);
            //获取物料审核单id
            Long auditHeaderID = materialAuditSheet.getAuditHeaderID();
            //获取商品相关信息（id，数量，是否赠品）
            ObjectMapper objectMapper = new ObjectMapper();

            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsSimpleInfo.class);
            List<GoodsSimpleInfo> goodsList = (List<GoodsSimpleInfo>) objectMapper.readValue(materialAuditSheetRequest.getGoodsList(), javaType1);
            for (GoodsSimpleInfo goodsSimpleInfo : goodsList) {
                //根据商品id查找对应的商品
                GoodsDO goodsDO = goodsService.queryById(goodsSimpleInfo.getId());
                MaterialAuditGoodsInfo materialAuditGoodsInfo = new MaterialAuditGoodsInfo();
                //对物料审核单商品想起进行赋值
                materialAuditGoodsInfo.setAuditHeaderID(auditHeaderID);
                materialAuditGoodsInfo.setGid(goodsSimpleInfo.getId());
                materialAuditGoodsInfo.setCoverImageUri(goodsDO.getCoverImageUri());
                materialAuditGoodsInfo.setQty(goodsSimpleInfo.getNum());
                materialAuditGoodsInfo.setGoodsSpecification(goodsDO.getGoodsSpecification());
                materialAuditGoodsInfo.setGoodsUnit(goodsDO.getGoodsUnit());
                materialAuditGoodsInfo.setSku(goodsDO.getSku());
                materialAuditGoodsInfo.setSkuName(goodsDO.getSkuName());
                //对物料审核单商品详情进行保存
                materialAuditGoodsInfoService.addMaterialAuditGoodsInfo(materialAuditGoodsInfo);
            }
            //创建一个存储返回参数对象的list
            List<MaterialAuditSheetResponse> materialAuditSheetResponsesList = new ArrayList<>();
            //获取工人所有的物料审核单列表
            List<MaterialAuditSheet> materialAuditSheetList = materialAuditSheetService.queryListByEmployeeID(materialAuditSheetRequest.getUserID());
            //遍历工人所有的物料审核单
            for (MaterialAuditSheet materialAuditSheet1 : materialAuditSheetList) {
                //创建一个返回参数对象
                MaterialAuditSheetResponse materialAuditSheetResponse = new MaterialAuditSheetResponse();
                //查询每单物料审核单所有商品信息
                List<MaterialAuditGoodsInfo> materialAuditGoodsInfoList = materialAuditGoodsInfoService.queryListByAuditHeaderID(auditHeaderID);
                //创建一个图片list存储图片地址
                List<String> pictureList = new ArrayList<>();
                for (MaterialAuditGoodsInfo materialAuditGoodsInfo : materialAuditGoodsInfoList) {
                    pictureList.add(materialAuditGoodsInfo.getCoverImageUri());
                }
                //向返回参数对象中设置
                materialAuditSheetResponse.setAuditNo(materialAuditSheet1.getAuditNo());
                materialAuditSheetResponse.setDeliveryCity(materialAuditSheet1.getDeliveryCity());
                materialAuditSheetResponse.setDeliveryCounty(materialAuditSheet1.getDeliveryCounty());
                materialAuditSheetResponse.setDeliveryStreet(materialAuditSheet1.getDeliveryStreet());
                materialAuditSheetResponse.setResidenceName(materialAuditSheet1.getResidenceName());
                materialAuditSheetResponse.setDetailedAddress(materialAuditSheet1.getDetailedAddress());
                materialAuditSheetResponse.setTotalQty(materialAuditGoodsInfoService.querySumQtyByAuditHeaderID(auditHeaderID));
                //TODO 获取零售价计算总金额
                materialAuditSheetResponse.setTotalPrice(null);
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
            materialAuditDetailsResponse.setReservationDeliveryTime(df.format(materialAuditSheet.getReservationDeliveryTime()));
            //TODO 还需要获取零售价计算总金额
            materialAuditDetailsResponse.setTotalPrice(null);

            //查询物料审核单中对应的商品
            List<MaterialAuditGoodsInfo> materialAuditGoodsInfoList = materialAuditGoodsInfoService.queryListByAuditHeaderID(materialAuditSheet.getAuditHeaderID());
            if (null != materialAuditGoodsInfoList && materialAuditGoodsInfoList.size() > 0) {
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
                for (MaterialAuditGoodsInfo materialAuditGoodsInfo : materialAuditGoodsInfoList) {
                    pictureList.add(materialAuditGoodsInfo.getCoverImageUri());
                }
                //设值
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
                for (MaterialAuditGoodsInfo materialAuditGoodsInfo : materialAuditGoodsInfoList) {
                    pictureList.add(materialAuditGoodsInfo.getCoverImageUri());
                }
                //向返回参数对象中设置
                materialAuditSheetResponse.setAuditNo(materialAuditSheet1.getAuditNo());
                materialAuditSheetResponse.setDeliveryCity(materialAuditSheet1.getDeliveryCity());
                materialAuditSheetResponse.setDeliveryCounty(materialAuditSheet1.getDeliveryCounty());
                materialAuditSheetResponse.setDeliveryStreet(materialAuditSheet1.getDeliveryStreet());
                materialAuditSheetResponse.setResidenceName(materialAuditSheet1.getResidenceName());
                materialAuditSheetResponse.setDetailedAddress(materialAuditSheet1.getDetailedAddress());
                materialAuditSheetResponse.setTotalQty(materialAuditGoodsInfoService.querySumQtyByAuditHeaderID(materialAuditSheet1.getAuditHeaderID()));
                //TODO 获取零售价计算总金额
                materialAuditSheetResponse.setTotalPrice(null);
                materialAuditSheetResponse.setIsAudited(materialAuditSheet1.getIsAudited());
                materialAuditSheetResponse.setStatus(materialAuditSheet1.getStatus());
                materialAuditSheetResponse.setPictureList(pictureList);
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
     * 装饰公司项目经理审核通过去支付
     *
     * @param userID       用户ID
     * @param auditNo      物料审核单编号
     * @param identityType 用户类型
     * @return 返回审核料单商品信息
     */
    public ResultDTO<Object> goPay(Long userID, String auditNo, Integer identityType) {
        ResultDTO<Object> resultDTO;
        logger.info("goPay CALLED,项目经理审核通过去支付，入参 userID:{},auditNo:{},identityType:{}", userID, auditNo, identityType);
        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("goPay OUT,项目经理审核通过去支付失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("goPay OUT,项目经理审核通过去支付失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(auditNo)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "审核单编号不能为空", null);
            logger.info("goPay OUT,项目经理审核通过去支付失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != 3) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该用户类型不能进行支付", null);
            logger.info("goPay OUT,项目经理审核通过去支付失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            //根据物料审核单编号查找物料审核单
            MaterialAuditSheet materialAuditSheet = materialAuditSheetService.queryByAuditNo(auditNo);
            //创建返回对象
            MaterialAuditGoPayResponse materialAuditGoPayResponse = new MaterialAuditGoPayResponse();
            if (null == materialAuditSheet) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此物料审核单", null);
                logger.info("goPay OUT,项目经理审核通过去支付失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //查询物料审核单中对应的商品
            List<MaterialAuditGoodsInfo> materialAuditGoodsInfoList = materialAuditGoodsInfoService.queryListByAuditHeaderID(materialAuditSheet.getAuditHeaderID());
            if (null != materialAuditGoodsInfoList && materialAuditGoodsInfoList.size() > 0) {
                //把物料审核单中所有的商品list放入返回值对象中
                materialAuditGoPayResponse.setGoodsList(materialAuditGoodsInfoList);
                materialAuditGoPayResponse.setAuditNo(auditNo);
                materialAuditGoPayResponse.setWorker(materialAuditSheet.getEmployeeName());
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, materialAuditGoPayResponse);
                logger.info("goPay OUT,项目经理审核通过去支付成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此物料审核单商品", null);
                logger.info("goPay OUT,项目经理审核通过去支付失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，项目经理审核通过去支付失败", null);
            logger.warn("goPay EXCEPTION,项目经理审核通过去支付失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * 生成物料审核单编号
     *
     * @return 返回编号
     */
    public String createNumber() {
        //定义时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        //获取当前时间
        Date d = new Date();
        //转换为String
        String str = sdf.format(d);
        //获取6位随机数并转换为String
        String st = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
        return "SH" + str + st;
    }
}
