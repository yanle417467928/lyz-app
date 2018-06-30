package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.*;
import cn.com.leyizhuang.app.core.utils.ExcelImportUtil;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.request.GoodsIdQtyParam;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderGoodsSimpleRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaCreateOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaOrderCalulatedAmountRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.*;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.DeliveryAddressDO;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.queue.MaSinkSender;
import cn.com.leyizhuang.app.remote.wms.MaICallWms;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.AssertUtil;
import cn.com.leyizhuang.common.util.CountUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Description: 装饰公司下单
 * @Author Richard
 * @Date 2018/4/169:22
 */
@RestController
@RequestMapping(value = "/rest/admin/fit/place/order")
@Slf4j
public class MaDecoratorPlaceOrderRestController {

    private static final Logger logger = LoggerFactory.getLogger(MaDecoratorPlaceOrderRestController.class);
    @Resource
    private AppStoreService storeService;

    @Resource
    private MaCityInventoryService cityInventoryService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private GoodsPriceService goodsPriceService;

    @Resource
    private MaterialListService materialListService;

    @Resource
    private AppEmployeeService employeeService;

    @Resource
    private CommonService commonService;

    @Resource
    private DeliveryFeeRuleService deliveryFeeRuleService;

    @Resource
    private DeliveryAddressService deliveryAddressService;

    @Resource
    private AppOrderService appOrderService;

    @Resource
    private AppActDutchService dutchService;

    @Resource
    private AppCashCouponDutchService cashCouponDutchService;

    @Resource
    private TransactionalSupportService transactionalSupportService;

    @Resource
    private MaOrderService maOrderService;

    @Resource
    private MaICallWms iCallWms;

    @Resource
    private MaSinkSender maSinkSender;


    @Resource
    private AppCashReturnDutchService cashReturnDutchService;
    @RequestMapping(value = "/import/goods", method = RequestMethod.POST)
    public Map<String, Object> importOrderGoods(@RequestParam(value = "file") MultipartFile excelFile,
                                                @RequestParam(value = "storeId") Long storeId) {
        Map<String, Object> map = new HashMap<>(5);
        if (null == excelFile) {
            map.put("code", -1);
            map.put("message", "产品excel不能为空!");
            return map;
        }
        String fileName = excelFile.getOriginalFilename();//report.xls
        String fileName2 = excelFile.getName();//excelFile

        InputStream fis = null;
        try {
            fis = excelFile.getInputStream();
            FitOrderExcel fitOrderExcel = ExcelImportUtil.readFitOrderExcel(excelFile);
            System.out.println("转化成功!");
            //获取全部内部编码
            List<FitOrderExcelModel> excelModelList = fitOrderExcel.getExcelModelList();
            //是否可以提交到购物车标志
            final boolean[] submitFlag = {true};
            AppStore store = storeService.findById(storeId);
            if (AssertUtil.isNotEmpty(excelModelList)) {
                List<String> internalCodeList = excelModelList.stream().map(FitOrderExcelModel::getInternalCode).collect(Collectors.toList());
                List<GoodsDO> goodsList = goodsService.findGoodsListBySkuList(internalCodeList);
                List<CityInventory> cityInventoryList = new ArrayList<>(300);
                List<GoodsPrice> goodsPriceList = new ArrayList<>(300);
                if (null != store) {
                    //查询全部内部编码对应的城市可用量
                    cityInventoryList = cityInventoryService.findCityInventoryListByCityIdAndSkuList(store.getCityId(),
                            internalCodeList);
                    //查询全部内部编码对应的门店价目表
                    goodsPriceList = goodsPriceService.findGoodsPriceListByStoreIdAndSkuList(storeId, internalCodeList);
                } else {
                    //todo
                    map.put("code", -1);
                    map.put("message", "找不到该门店信息");
                    return map;
                }

                //页面返回对象
                List<FitOrderExcelPageVO> pageVOList = new ArrayList<>();
                List<CityInventory> finalCityInventoryList = cityInventoryList;
                List<GoodsPrice> finalGoodsPriceList = goodsPriceList;
                excelModelList.forEach(p -> {
                    FitOrderExcelPageVO pageVO = new FitOrderExcelPageVO();
                    pageVO.setExternalCode(p.getExternalCode());
                    pageVO.setQty(p.getQty());
                    pageVO.setInternalCode(p.getInternalCode());
                    pageVO.setInternalName(p.getInternalName());
                    //设置内部商品是否存在
                    if (StringUtils.isBlank(p.getInternalCode())) {
                        pageVO.setIsInternalCodeExists(false);
                    } else {
                        if (goodsList.stream().map(GoodsDO::getSku).collect(Collectors.toList()).contains(p.getInternalCode())) {
                            pageVO.setIsInternalCodeExists(true);
                        } else {
                            pageVO.setIsInternalCodeExists(false);
                        }
                    }
                    if (pageVO.getIsInternalCodeExists()) {
                        //设置库存相关信息
                        List<CityInventory> cityInventoryListTemp = finalCityInventoryList.stream().filter(q -> q.getSku().equals(p.getInternalCode())).collect(Collectors.toList());
                        if (AssertUtil.isNotEmpty(cityInventoryListTemp)) {
                            pageVO.setInventory(cityInventoryListTemp.get(0).getAvailableIty() == null ? 0 : cityInventoryListTemp.get(0).getAvailableIty());
                        } else {
                            pageVO.setInventory(0);
                        }
                        if (pageVO.getInventory() >= pageVO.getQty()) {
                            pageVO.setIsInvEnough(true);
                            pageVO.setInvDifference(0);
                        } else {
                            pageVO.setIsInvEnough(false);
                            pageVO.setInvDifference(pageVO.getInventory() - pageVO.getQty());
                        }

                        //设置价目表是否存在
                        List<GoodsPrice> goodsPriceListTemp = finalGoodsPriceList.stream().filter(t -> t.getSku().equals(p.getInternalCode())).collect(Collectors.toList());
                        if (AssertUtil.isNotEmpty(goodsPriceListTemp)) {
                            pageVO.setIsPriceItemExists(true);
                        } else {
                            pageVO.setIsPriceItemExists(false);
                        }
                    }
                    if (!pageVO.getIsInternalCodeExists()) {
                        pageVO.setErrorType(FitExcelImportGoodsErrorType.GOODS_NOT_EXISTS);
                    } else if (!pageVO.getIsInvEnough()) {
                        pageVO.setErrorType(FitExcelImportGoodsErrorType.INV_NOT_ENOUGH);
                    } else if (!pageVO.getIsPriceItemExists()) {
                        pageVO.setErrorType(FitExcelImportGoodsErrorType.PRICE_NOT_EXISTS);
                    }
                    if (!pageVO.getIsInternalCodeExists() || !pageVO.getIsInvEnough() || !pageVO.getIsPriceItemExists()) {
                        submitFlag[0] = false;
                    }
                    pageVOList.add(pageVO);
                });
                map.put("submitFlag", submitFlag[0]);
                map.put("code", 0);
                map.put("content", pageVOList);
                map.put("remark", fitOrderExcel.getRemark());
                return map;
            } else {
                map.put("code", -1);
                map.put("message", "excel信息不能为空!");
                return map;
            }
        } catch (IOException e) {
            e.printStackTrace();
            map.put("code", -1);
            map.put("message", "发生未知异常，下单失败!");
            return map;
        }
    }

    @RequestMapping(value = "/submit/goods", method = RequestMethod.POST)
    public ResultDTO<Object> submitOrderGoods(Long storeId, Long guideId, String remark, String goodsDetails,String receiverName,String receiverPhone,String province,
                                              String city,String county,String street,String residenceName,String estateInfo,
                                              String detailedAddress,Long goAddDeliveryAddressType,Long deliveryId) {
        ResultDTO<Object> resultDTO;
        System.out.println("进入方法");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JavaType goodsSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsSkuQtyParam.class);
        try {
            AppEmployee employee = employeeService.findById(guideId);
            if (null != employee) {

                Long deliveryIds = null;
                if (null != deliveryId && -1 != deliveryId) {
                    deliveryIds = deliveryId;
                }

                if (0 == goAddDeliveryAddressType && (null == deliveryId || -1 == deliveryId)) {
                    //*************************************增加地址信息**************************************
                    String provinceName = deliveryAddressService.findAreaNameByCode(province);
                    String cityName = deliveryAddressService.findAreaNameByCode(city);
                    String countyName = deliveryAddressService.findAreaNameByCode(county);

                    DeliveryAddressDO deliveryAddressDO = new DeliveryAddressDO();
                    deliveryAddressDO.setReceiver(receiverName);
                    deliveryAddressDO.setReceiverPhone(receiverPhone);
                    deliveryAddressDO.setDeliveryProvince(provinceName);
                    deliveryAddressDO.setDeliveryCity(cityName);
                    deliveryAddressDO.setDeliveryCounty(countyName);
                    deliveryAddressDO.setDeliveryStreet(street);
                    deliveryAddressDO.setDetailedAddress(detailedAddress);
                    deliveryAddressDO.setResidenceName(residenceName);
                    deliveryAddressDO.setUserId(employee.getEmpId());
                    deliveryAddressDO.setIdentityType(employee.getIdentityType());
                    deliveryAddressDO.setStatus(Boolean.TRUE);
                    deliveryAddressDO.setIsDefault(Boolean.FALSE);
                    deliveryAddressDO.setEstateInfo(estateInfo);

                    deliveryAddressService.maAddDeliveryAddress(deliveryAddressDO);
                    //获取地址id
                    deliveryIds = deliveryAddressDO.getId();
                    //*******************************************************************************************
                }

                List<GoodsSkuQtyParam> goodsList = objectMapper.readValue(goodsDetails, goodsSimpleInfo);
                log.info("{}", goodsList);
                //新增记录
                List<MaterialListDO> materialListSave = new ArrayList<>();
                //更新记录
                List<MaterialListDO> materialListUpdate = new ArrayList<>();
                for (int i = 0; i < goodsList.size(); i++) {//查询商品信息
                    GoodsDO goodsDO = goodsService.findBySku(goodsList.get(i).getSku());
                    if (null != goodsDO) {
                        //查询下料清单普通商品
                        MaterialListDO materialListDO = materialListService.findByUserIdAndIdentityTypeAndGoodsId(guideId,
                                employee.getIdentityType(), goodsDO.getGid());
                        //查询经理料单商品
                        MaterialListDO materialAuditListDO = materialListService.findAuditListByUserIdAndIdentityTypeAndGoodsId(guideId,
                                employee.getIdentityType(), goodsDO.getGid());
                        if (null == materialListDO && null == materialAuditListDO) {
                            if (AssertUtil.isNotEmpty(materialListSave)) {
                                List<String> skuList = materialListSave.stream().map(MaterialListDO::getSku).collect(Collectors.toList());
                                if (skuList.contains(goodsList.get(i).getSku())) {
                                    int finalI = i;
                                    MaterialListDO materialListDOTemp = materialListSave.stream().filter(p -> p.getSku().
                                            equals(goodsList.get(finalI).getSku())).collect(Collectors.toList()).get(0);
                                    materialListDOTemp.setQty(materialListDOTemp.getQty() + goodsList.get(i).getQty());
                                    materialListSave.remove(materialListDOTemp);
                                    materialListSave.add(materialListDOTemp);
                                } else {
                                    MaterialListDO materialListDOTemp = transformRepeat(goodsDO);
                                    materialListDOTemp.setUserId(guideId);
                                    materialListDOTemp.setIdentityType(employee.getIdentityType());
                                    materialListDOTemp.setRemark(remark);
                                    materialListDOTemp.setDeliveryId(deliveryIds);
                                    materialListDOTemp.setQty(goodsList.get(i).getQty());
                                    materialListDOTemp.setMaterialListType(MaterialListType.NORMAL);
                                    materialListSave.add(materialListDOTemp);
                                }
                            } else {
                                MaterialListDO materialListDOTemp = transformRepeat(goodsDO);
                                materialListDOTemp.setUserId(guideId);
                                materialListDOTemp.setIdentityType(employee.getIdentityType());
                                materialListDOTemp.setRemark(remark);
                                materialListDOTemp.setDeliveryId(deliveryIds);
                                materialListDOTemp.setQty(goodsList.get(i).getQty());
                                materialListDOTemp.setMaterialListType(MaterialListType.NORMAL);
                                materialListSave.add(materialListDOTemp);
                            }
                        } else {
                            if (null != materialListDO) {
                                materialListDO.setRemark(remark);
                                materialListDO.setDeliveryId(deliveryIds);
                                materialListDO.setQty(materialListDO.getQty() + goodsList.get(i).getQty());
                                materialListUpdate.add(materialListDO);
                            }
                            if (null != materialAuditListDO) {
                                materialAuditListDO.setRemark(remark);
                                materialAuditListDO.setDeliveryId(deliveryIds);
                                materialAuditListDO.setQty(materialAuditListDO.getQty() + goodsList.get(i).getQty());
                                materialListUpdate.add(materialAuditListDO);
                            }
                        }
                    } else {
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                                "商品: '" + goodsDO.getSkuName() + "' 不存在!", null);
                        return resultDTO;
                    }
                }
                commonService.saveAndUpdateMaterialList(materialListSave, materialListUpdate);
                resultDTO = new ResultDTO(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                return resultDTO;
            } else {
                resultDTO = new ResultDTO(CommonGlobal.COMMON_CODE_FAILURE, "该员工信息不存在!", null);
                return resultDTO;
            }
        } catch (IOException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO(CommonGlobal.COMMON_CODE_FAILURE, "参数转换异常", null);
            return resultDTO;
        }
    }


    @RequestMapping(value = "/download/sample", method = RequestMethod.GET)
    public void downloadSampleFile(HttpServletResponse response) {
        String templateName = "fit_order_template.xlsx";
        // 下载文件名
        String fileName = "装饰公司下单模板.xlsx";
        try {
            fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        // 获取模板位置，读取数据库（也可以读取配置文件或写死）
        String templatePath = ApplicationConstant.FIT_ORDER_TEMPLATE_URL;
        // 实际位置
        String path = templatePath + File.separator + templateName;
        System.out.println(path);
        // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        // 2.设置文件头：最后一个参数是设置下载文件名
        response.setHeader("Content-Disposition", "attachment;fileName="
                + fileName);
        response.addHeader("Content-Type", "application/vnd.ms-excel");
        OutputStream out;
        // 通过文件路径获得File对象(假如此路径中有一个download.pdf文件)
        File file = new File(path);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            // 3.通过response获取OutputStream对象(out)
            out = response.getOutputStream();
            byte[] buffer = new byte[512];
            int b = inputStream.read(buffer);
            while (b != -1) {
                // 4.写到输出流(out)中
                out.write(buffer, 0, b);
                b = inputStream.read(buffer);
            }
            inputStream.close();
            out.close();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private MaterialListDO transformRepeat(GoodsDO goodsDO) {
        MaterialListDO materialListDOTemp = new MaterialListDO();
        materialListDOTemp.setGid(goodsDO.getGid());
        materialListDOTemp.setSku(goodsDO.getSku());
        materialListDOTemp.setSkuName(goodsDO.getSkuName());
        materialListDOTemp.setGoodsSpecification(goodsDO.getGoodsSpecification());
        materialListDOTemp.setGoodsUnit(goodsDO.getGoodsUnit());
        if (null != goodsDO.getCoverImageUri()) {
            String uri[] = goodsDO.getCoverImageUri().split(",");
            materialListDOTemp.setCoverImageUri(uri[0]);
        }
        return materialListDOTemp;
    }

    @RequestMapping(value ="/find/areaManagement", method = RequestMethod.GET)
    private List<AreaManagementDO> findDelivery(){
        return deliveryAddressService.findAllAreaManagement();
    }

    @RequestMapping(value ="/find/areaManagement/{type}/{code}", method = RequestMethod.GET)
    private List<AreaManagementDO> conditionalQueryAreaManagement(String province,String city,
                                                                  @PathVariable(value = "type") Long type,@PathVariable(value = "code") String code){
        logger.info("conditionalQueryAreaManagement 装饰公司后台下单查询省、市、区及街道  入参 province{},city{},type{},code{}",province,city,type,code);

        return deliveryAddressService.findAreaManagementByProvinceCode(code,type);

    }

    /**
     * 装饰公司后台下单计算金额
     *
     * @param maOrderCalulatedAmountRequest 装饰公司后台下单计算金额,商品信息DTO对象
     * @return
     * @author
     */
    @RequestMapping(value = "/calculated/amount", method = RequestMethod.POST)
    public ResultDTO<Object> calculatedAmount(@Valid MaOrderCalulatedAmountRequest maOrderCalulatedAmountRequest) {

        logger.info("calculatedAmount CALLED,装饰公司后台下单计算金额，入参 goodsSimpleRequest:{}", maOrderCalulatedAmountRequest);

        ResultDTO<Object> resultDTO;
        if (null == maOrderCalulatedAmountRequest.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("calculatedAmount OUT,装饰公司后台下单计算金额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == maOrderCalulatedAmountRequest.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("calculatedAmount OUT,装饰公司后台下单计算金额失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Long userId = maOrderCalulatedAmountRequest.getUserId();
        Integer identityType = maOrderCalulatedAmountRequest.getIdentityType();
//        List<GoodsIdQtyParam> goodsList = goodsSimpleRequest.getGoodsList();
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType goodsSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsSkuQtyParam.class);
//        List<PromotionSimpleInfo> giftList = goodsSimpleRequest.getGiftList();
//        List<GoodsIdQtyParam> couponList = goodsSimpleRequest.getProductCouponList();
        try {
            List<GoodsSkuQtyParam> maGoodsList = objectMapper.readValue(maOrderCalulatedAmountRequest.getGoodsList(), goodsSimpleInfo);
//            List<GoodsSkuQtyParam> maGoodsList = maOrderCalulatedAmountRequest.getGoodsList();
            int goodsQty = 0;
//            int giftQty = 0;
//            int couponQty = 0;
            Double totalPrice = 0.00;
            Double memberDiscount = 0.00;
            Double orderDiscount = 0.00;
//            Double proCouponDiscount = 0D;
            //运费暂时还没出算法
            Double freight = 0.00;
            Double totalOrderAmount = 0.00;
//            List<Long> goodsIds = new ArrayList<Long>();
            List<String> goodsSkus = new ArrayList<String>();
//            List<Long> giftIds = new ArrayList<Long>();
//            List<Long> couponIds = new ArrayList<Long>();
//            List<GoodsIdQtyParam> giftsList = new ArrayList<>();
            List<OrderGoodsSimpleResponse> goodsInfo = null;
//            List<OrderGoodsSimpleResponse> giftsInfo = null;
//            List<OrderGoodsSimpleResponse> productCouponInfo = null;
//            List<CashCouponResponse> cashCouponResponseList = null;
//            Map<String, Object> goodsSettlement = new HashMap<>();
            Long cityId = 0L;
            AppStore appStore = null;
            AppCustomer customer = new AppCustomer();
//            boolean isShowSalesNumber = false;
//            if (identityType == 6) {
//                customer = appCustomerService.findById(userId);
//                if (null == customer) {
//                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "数据发生异常!请联系客服", null);
//                    logger.info("enterOrder OUT,用户确认订单计算商品价格明细失败，出参 resultDTO:{}", resultDTO);
//                    return resultDTO;
//                }
//                cityId = customer.getCityId();
//                appStore = appStoreService.findById(customer.getStoreId());
//            } else if (identityType == 0) {
//                if (null == goodsSimpleRequest.getCustomerId()) {
//                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "导购代下单客户不能为空", null);
//                    logger.info("enterOrder OUT,用户确认订单计算商品价格明细失败，出参 resultDTO:{}", resultDTO);
//                    return resultDTO;
//                }
//                Long customerId = goodsSimpleRequest.getCustomerId();
//                customer = appCustomerService.findById(customerId);
//                if (null == customer) {
//                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "数据发生异常!请联系客服", null);
//                    logger.info("enterOrder OUT,用户确认订单计算商品价格明细失败，出参 resultDTO:{}", resultDTO);
//                    return resultDTO;
//                }
//                //是否显示纸质销售单号
//                AppEmployee appEmployee = appEmployeeService.findById(userId);
//                cityId = appEmployee.getCityId();
//                appStore = appStoreService.findById(appEmployee.getStoreId());
//                //如果是四川直营门店导购返回门店编码
//                if ("ZY".equals(appStore.getStoreType().getValue()) && ("FZY009".equals(appStore.getStoreCode()) || "HLC004".equals(appStore.getStoreCode()) || "ML001".equals(appStore.getStoreCode()) || "QCMJ008".equals(appStore.getStoreCode()) ||
//                        "SB010".equals(appStore.getStoreCode()) || "YC002".equals(appStore.getStoreCode()) || "ZC002".equals(appStore.getStoreCode()) || "RC005".equals(appStore.getStoreCode()) ||
//                        "FZM007".equals(appStore.getStoreCode()) || "SH001".equals(appStore.getStoreCode()) || "YJ001".equals(appStore.getStoreCode()) || "HS001".equals(appStore.getStoreCode()) ||
//                        "XC001".equals(appStore.getStoreCode()))) {
//                    isShowSalesNumber = true;
//                }
//            } else if (identityType == 2) {
//            }
            AppEmployee employee = employeeService.findById(userId);
            cityId = employee.getCityId();

            //取出所有本品的id和计算本品数量
//            if (AssertUtil.isNotEmpty(goodsList)) {
//                for (GoodsIdQtyParam aGoodsList : goodsList) {
//                    goodsIds.add(aGoodsList.getId());
//                    goodsQty = goodsQty + aGoodsList.getQty();
//                }
//            }
            if (AssertUtil.isNotEmpty(maGoodsList)) {
                for (GoodsSkuQtyParam aGoodsList : maGoodsList) {
                    goodsSkus.add(aGoodsList.getSku());
                    goodsQty = goodsQty + aGoodsList.getQty();
                }
            }
//            //取出所有赠品的id和计算赠品数量
//            if (AssertUtil.isNotEmpty(giftList)) {
//                for (PromotionSimpleInfo promotionSimpleInfo : giftList) {
//
//                    if (null != promotionSimpleInfo.getPresentInfo()) {
//                        giftsList.addAll(promotionSimpleInfo.getPresentInfo());
//                        for (GoodsIdQtyParam goodsIdQtyParam : promotionSimpleInfo.getPresentInfo()) {
//                            giftIds.add(goodsIdQtyParam.getId());
//                            giftQty = giftQty + goodsIdQtyParam.getQty();
//                        }
//                    }
//                }
//            }
//            //取出所有产品券商品的id和计算产品券商品数量
//            if (AssertUtil.isNotEmpty(couponList)) {
//                for (GoodsIdQtyParam couponSimpleInfo : couponList) {
//                    couponIds.add(couponSimpleInfo.getId());
//                    couponQty = couponQty + couponSimpleInfo.getQty();
//                }
//            }
//            if (identityType == 6) {
//                //获取商品信息
//                goodsInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, goodsIds);
//                //获取赠品信息
//                giftsInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, giftIds);
//                //获取产品券信息
//                productCouponInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, couponIds);
//            } else {
                //获取商品信息
                goodsInfo = goodsService.findGoodsListByEmployeeIdAndGoodsSkuList(userId, goodsSkus);
            if (null == goodsInfo){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品信息不能为空", null);
                logger.info("calculatedAmount OUT,装饰公司后台下单计算金额失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
//                //获取赠品信息
//                giftsInfo = goodsService.findGoodsListByEmployeeIdAndGoodsIdList(userId, giftIds);
//                //获取产品券信息
//                productCouponInfo = goodsService.findGoodsListByEmployeeIdAndGoodsIdList(userId, couponIds);
//            }
            //加本品标识
            if (AssertUtil.isNotEmpty(goodsInfo)) {
                for (OrderGoodsSimpleResponse simpleResponse : goodsInfo) {
                    for (GoodsSkuQtyParam goodsSkuQtyParam : maGoodsList) {
                        if (simpleResponse.getSku().equals(goodsSkuQtyParam.getSku())) {
                            simpleResponse.setGoodsQty(goodsSkuQtyParam.getQty());
                            break;
                        }
                    }
                    simpleResponse.setGoodsLineType(AppGoodsLineType.GOODS.getValue());
                    //算总金额
                    totalPrice = CountUtil.add(totalPrice, CountUtil.mul(simpleResponse.getRetailPrice(), simpleResponse.getGoodsQty()));
                    //算会员折扣(先判断是否是会员还是零售会员)
                    if (identityType == 2 || (null != customer.getCustomerType() && customer.getCustomerType().equals(AppCustomerType.MEMBER))) {
                        memberDiscount = CountUtil.add(memberDiscount, CountUtil.mul(CountUtil.sub(simpleResponse.getRetailPrice(),
                                simpleResponse.getVipPrice()), simpleResponse.getGoodsQty()));
                    }
                }
            }
//            // 本品集合 用来计算立减促销
//            List<OrderGoodsSimpleResponse> bGoodsList = goodsInfo;

//            //赠品的数量和标识
//            if (AssertUtil.isNotEmpty(giftsInfo)) {
//                for (GoodsIdQtyParam goodsIdQtyParam : giftsList) {
//                    for (OrderGoodsSimpleResponse aGiftInfo : giftsInfo) {
//                        if (aGiftInfo.getId().equals(goodsIdQtyParam.getId())) {
//                            aGiftInfo.setGoodsQty(aGiftInfo.getGoodsQty() + goodsIdQtyParam.getQty());
//                            aGiftInfo.setRetailPrice(0D);
//                            break;
//                        }
//                        aGiftInfo.setGoodsLineType(AppGoodsLineType.PRESENT.getValue());
//                    }
//                }
//                //合并商品和赠品集合
//                goodsInfo.addAll(giftsInfo);
//            }
//            //产品券加标识
//            if (AssertUtil.isNotEmpty(productCouponInfo)) {
//                for (OrderGoodsSimpleResponse orderGoodsSimpleResponse : productCouponInfo) {
//                    for (GoodsIdQtyParam goodsIdQtyParam : couponList) {
//                        if (orderGoodsSimpleResponse.getId().equals(goodsIdQtyParam.getId())) {
//                            orderGoodsSimpleResponse.setGoodsQty(goodsIdQtyParam.getQty());
//                            break;
//                        }
//                    }
//                    orderGoodsSimpleResponse.setGoodsLineType(AppGoodsLineType.PRODUCT_COUPON.getValue());
//                    //算产品券总金额
//                    proCouponDiscount = CountUtil.add(proCouponDiscount, CountUtil.mul(orderGoodsSimpleResponse.getRetailPrice(), orderGoodsSimpleResponse.getGoodsQty()));
//                }
//                //合并商品和赠品集合
//                if (AssertUtil.isNotEmpty(goodsInfo)) {
//                    goodsInfo.addAll(productCouponInfo);
//                } else {
//                    goodsInfo = productCouponInfo;
//                }
//            }

            //计算订单金额小计
            //********* 计算促销立减金额 *************
//            List<PromotionDiscountListResponse> discountListResponseList = actService.countDiscount(userId, AppIdentityType.getAppIdentityTypeByValue(identityType), bGoodsList, customer.getCusId(),"GOODS");
//            for (PromotionDiscountListResponse discountResponse : discountListResponseList) {
//                orderDiscount = CountUtil.add(orderDiscount, discountResponse.getDiscountPrice());
//                PromotionSimpleInfo promotionSimpleInfo = new PromotionSimpleInfo();
//                promotionSimpleInfo.setPromotionId(discountResponse.getPromotionId());
//                promotionSimpleInfo.setDiscount(discountResponse.getDiscountPrice());
//                promotionSimpleInfo.setEnjoyTimes(discountResponse.getEnjoyTimes());
//                giftList.add(promotionSimpleInfo);
//            }

            totalOrderAmount = CountUtil.sub(totalPrice, memberDiscount, orderDiscount);


//            if (identityType == 6) {
//                //计算顾客乐币
//                Map<String, Object> leBi = appCustomerService.findLeBiByUserIdAndGoodsMoney(userId, totalOrderAmount);
//                //计算可用的优惠券
//                cashCouponResponseList = appCustomerService.findCashCouponUseableByCustomerId(userId, totalOrderAmount);
//                //查询顾客预存款
//                Double preDeposit = appCustomerService.findPreDepositBalanceByUserIdAndIdentityType(userId, identityType);
//
//                goodsSettlement.put("leBi", leBi);
//                goodsSettlement.put("cashCouponList", cashCouponResponseList);
//                goodsSettlement.put("preDeposit", preDeposit);
//            } else if (identityType == 0) {
//                //现金券还需要传入订单金额判断是否满减
//                cashCouponResponseList = appCustomerService.findCashCouponUseableByCustomerId(customer.getCusId(), totalOrderAmount);
//                //查询导购预存款和信用金
//                SellerCreditMoneyResponse sellerCreditMoneyResponse = appEmployeeService.findCreditMoneyBalanceByUserIdAndIdentityType(userId, identityType);
//                Double creditMoney = null != sellerCreditMoneyResponse ? sellerCreditMoneyResponse.getAvailableBalance() : 0D;
//                //导购门店预存款
//                Double storePreDeposit = appStoreService.findPreDepositBalanceByUserId(userId);
//
//                goodsSettlement.put("cashCouponList", cashCouponResponseList);
//                goodsSettlement.put("creditMoney", creditMoney);
//                goodsSettlement.put("storePreDeposit", storePreDeposit);
//            } else if (identityType == 2) {
                //获取装饰公司门店预存款，信用金，现金返利。
                Double storePreDeposit = storeService.findPreDepositBalanceByUserId(userId);
                Double storeCreditMoney = storeService.findCreditMoneyBalanceByUserId(userId);
                Double storeSubvention = storeService.findSubventionBalanceByUserId(userId);
//            }

            //由于运费不抵扣乐币及优惠券,避免分摊出现负,运费放最后计算
            // 运费计算
            //2018-04-01 generation 产品卷金额加进运费计算
            freight = deliveryFeeRuleService.countDeliveryFee(identityType, cityId, CountUtil.add(totalOrderAmount, 0), goodsInfo);

            totalOrderAmount = CountUtil.add(totalOrderAmount, freight);
//            allGoods.addAll(giftIds);
//            allGoods.addAll(couponIds);


//            goodsSettlement.put("totalQty", goodsQty + giftQty + couponQty);
//            goodsSettlement.put("totalQty", AssertUtil.getArrayList(allGoods).size());
//            goodsSettlement.put("totalPrice", CountUtil.add(totalPrice, proCouponDiscount));
//            goodsSettlement.put("totalGoodsInfo", goodsInfo);
//            goodsSettlement.put("orderDiscount", orderDiscount);
//            goodsSettlement.put("proCouponDiscount", proCouponDiscount);
//            goodsSettlement.put("memberDiscount", memberDiscount);
//            goodsSettlement.put("freight", freight);
//            goodsSettlement.put("totalOrderAmount", totalOrderAmount);
//            goodsSettlement.put("promotionInfo", giftList);
//            goodsSettlement.put("isShowNumber", isShowSalesNumber);amountsPayable

            MaOrderCalulatedAmountResponse maOrderCalulatedAmountResponse = new MaOrderCalulatedAmountResponse();
            maOrderCalulatedAmountResponse.setTotalGoodsAmount(totalOrderAmount == null?0.00:totalOrderAmount);
            maOrderCalulatedAmountResponse.setMemberDiscount(memberDiscount== null?0.00:memberDiscount);
            maOrderCalulatedAmountResponse.setPromotionDiscount(0.00);
            maOrderCalulatedAmountResponse.setStCreditMoney(storeCreditMoney== null?0.00:storeCreditMoney);
            maOrderCalulatedAmountResponse.setStPreDeposit(storePreDeposit== null?0.00:storePreDeposit);
            maOrderCalulatedAmountResponse.setStSubvention(storeSubvention== null?0.00:storeSubvention);
            maOrderCalulatedAmountResponse.setFreight(freight);
//            List<AppDeliveryType> deliveryTypeList = new ArrayList<>();
//            goodsIds.addAll(giftIds);
//            goodsIds.addAll(couponIds);
//            //判断商品是否有专供商品
//            List<GiftListResponseGoods> goodsZGList = this.goodsPriceService.findGoodsPriceListByGoodsIdsAndUserId(goodsIds, userId, AppIdentityType.getAppIdentityTypeByValue(identityType));
//            //有专供商品只能选择送货上门
//            if (null != goodsZGList && goodsZGList.size() > 0) {
//                deliveryTypeList.add(AppDeliveryType.HOUSE_DELIVERY);
//            } else {
//                if (AppDeliveryType.SELF_TAKE.equals(goodsSimpleRequest.getSysDeliveryType())) {
//                    deliveryTypeList.add(AppDeliveryType.SELF_TAKE);
//                } else {
//                    deliveryTypeList.add(AppDeliveryType.SELF_TAKE);
//                    deliveryTypeList.add(AppDeliveryType.HOUSE_DELIVERY);
//                }
//            }
//            goodsSettlement.put("deliveryTypeList", deliveryTypeList);
            //非门店自提,为城市库存充足及门店库存充足
            if (!AppDeliveryType.SELF_TAKE.equals(maOrderCalulatedAmountRequest.getSysDeliveryType())) {

                //2018-04-01 generation 修改 提示所有城市库存不足的商品
                //判断库存的特殊处理
                List<String> goodsSkuList = appOrderService.existMaOrderGoodsInventory(cityId, maGoodsList);
                if (goodsSkuList != null && goodsSkuList.size() > 0) {
                    String message = "商品 ";
                    for (String sku : goodsSkuList) {
                        GoodsDO goodsDO = goodsService.queryBySku(sku);
                        message += "“";
                        message += goodsDO.getSkuName();
                        message += "” ";
                    }
                    message += "仓库库存不足，请更改购买数量!";
                    //如果这里发现库存不足还是要返回去商品列表
//                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该商品:" + goodsDO.getSkuName() + "商品库存不足！", goodsSettlement);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, message, null);
                    logger.info("calculatedAmount OUT,装饰公司后台下单计算金额，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }

//            Boolean isSpecialSelfTake = false;
            /**************/
            //2018-04-03 generation 加盟门店自提为特殊自提单
//            if (null != appStore && StoreType.JM == appStore.getStoreType()) {
//                isSpecialSelfTake = true;
//            }
//            goodsSettlement.put("isSpecialSelfTake", isSpecialSelfTake);
            /**************/

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,maOrderCalulatedAmountResponse);
            logger.info("calculatedAmount OUT,装饰公司后台下单计算金额成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,装饰公司后台下单计算金额失败!", null);
            logger.warn("calculatedAmount EXCEPTION,装饰公司后台下单计算金额失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * 后台创建订单方法
     *
     * @param orderParam 前台提交的订单相关参数
     * @param request    request对象
     * @return 订单创建结果
     */
    @RequestMapping(value = "/ma/create", method = RequestMethod.POST)
    public ResultDTO<Object> maCreateOrder(@Valid MaCreateOrderRequest orderParam, HttpServletRequest request) {
        logger.info("maCreateOrder CALLED,后台创建订单方法,入参:{}", JSON.toJSONString(orderParam));
        System.out.println(JSON.toJSONString(orderParam));
        ResultDTO<Object> resultDTO;
        //获取客户端ip地址
//        String ipAddress = IpUtils.getIpAddress(request);
//        if (null == orderParam.getCityId()) {
//            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市id不允许为空!", "");
//            logger.warn("maCreateOrder OUT,后台创建订单失败,出参 resultDTO:{}", resultDTO);
//            return resultDTO;
//        }
        Long userId = orderParam.getUserId();
        if (null == orderParam.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不允许为空!", "");
            logger.warn("maCreateOrder OUT,后台创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Integer identityType = orderParam.getIdentityType();
        if (null == orderParam.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份类型不允许为空!", "");
            logger.warn("maCreateOrder OUT,后台创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderParam.getGoodsList())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品信息不允许为空!", "");
            logger.warn("maCreateOrder OUT,后台创建订单,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderParam.getDeliveryMsg())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "配送信息不允许为空!", "");
            logger.warn("maCreateOrder OUT,后台创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        //判断创单人身份是否合法
        if (orderParam.getIdentityType() != 2) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "创单人身份不合法!", "");
            logger.warn("maCreateOrder OUT,后台创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            JavaType maGoodsSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, MaGoodsSimpleInfo.class);
//            JavaType cashCouponSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
//            JavaType productCouponSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ProductCouponSimpleInfo.class);
//            JavaType promotionSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, PromotionSimpleInfo.class);

            //*************************** 转化前台提交过来的json类型参数 ************************

            //商品信息
            List<MaGoodsSimpleInfo> goodsList = objectMapper.readValue(orderParam.getGoodsList(), maGoodsSimpleInfo);
            //配送信息
            DeliverySimpleInfo deliverySimpleInfo = objectMapper.readValue(orderParam.getDeliveryMsg(), DeliverySimpleInfo.class);
            if (StringUtils.isBlank(deliverySimpleInfo.getDeliveryType())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "配送方式不允许为空!", "");
                logger.warn("maCreateOrder OUT,后台创建订单失败,出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(deliverySimpleInfo.getReceiver())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人姓名不能为空，后台创建订单方法失败！", null);
                logger.info("maCreateOrder EXCEPTION，收货人姓名不能为空，后台创建订单方法失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(deliverySimpleInfo.getReceiverPhone())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "收货人电话号码不能为空，后台创建订单方法失败！", null);
                logger.info("maCreateOrder EXCEPTION，收货人姓名不能为空，后台创建订单方法失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(deliverySimpleInfo.getDeliveryProvince())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "省不能为空，后台创建订单方法失败！", null);
                logger.info("maCreateOrder EXCEPTION，省不能为空，后台创建订单方法失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(deliverySimpleInfo.getDeliveryCity())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "市不能为空，后台创建订单方法失败！", null);
                logger.info("maCreateOrder EXCEPTION，市不能为空，保存拍照下单失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(deliverySimpleInfo.getDeliveryCounty())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "区不能为空，后台创建订单方法失败！", null);
                logger.info("maCreateOrder EXCEPTION，区不能为空，后台创建订单方法失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(deliverySimpleInfo.getDeliveryStreet())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "街道不能为空，后台创建订单方法失败！", null);
                logger.info("maCreateOrder EXCEPTION，街道不能为空，后台创建订单方法失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(deliverySimpleInfo.getResidenceName())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "小区名不能为空，后台创建订单方法失败！", null);
                logger.info("maCreateOrder EXCEPTION，小区名不能为空，后台创建订单方法失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(deliverySimpleInfo.getDetailedAddress())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "详细地址不能为空，后台创建订单方法失败！", null);
                logger.info("maCreateOrder EXCEPTION，详细地址不能为空，后台创建订单方法失败，出参 ResultDTO:{}", resultDTO);
                return resultDTO;
            }
//            //优惠券信息
            List<Long> cashCouponList = new ArrayList<>();
//            if (StringUtils.isNotBlank(orderParam.getCashCouponIds())) {
//                cashCouponList = objectMapper.readValue(orderParam.getCashCouponIds(), cashCouponSimpleInfo);
//            }
//            //产品券信息
            List<ProductCouponSimpleInfo> productCouponList = new ArrayList<>();
//            if (StringUtils.isNotBlank(orderParam.getProductCouponInfo())) {
//                productCouponList = objectMapper.readValue(orderParam.getProductCouponInfo(), productCouponSimpleInfo);
//            }
//            //促销信息
            List<PromotionSimpleInfo> promotionSimpleInfoList = new ArrayList<>();
//            if (StringUtils.isNotBlank(orderParam.getPromotionInfo())) {
//                promotionSimpleInfoList = objectMapper.readValue(orderParam.getPromotionInfo(), promotionSimpleInfo);
//            }
//            // 检查促销是否过期
//            List<Long> promotionIds = new ArrayList<>();
//            for (PromotionSimpleInfo promotion : promotionSimpleInfoList) {
//                promotionIds.add(promotion.getPromotionId());
//            }
//            if (promotionIds.size() > 0) {
//                Boolean outTime = actService.checkActOutTime(promotionIds);
//                if (!outTime) {
//                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "存在过期促销，请重新下单！", "");
//                    logger.warn("createOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
//                    return resultDTO;
//                }
//            }

            //账单信息
            BillingSimpleInfo billing = objectMapper.readValue(orderParam.getBillingMsg(), BillingSimpleInfo.class);


//            //如果是导购下单并且是四川直营门店，判断销售纸质单号是否为空
//            if (orderParam.getIdentityType() == AppIdentityType.SELLER.getValue()) {
//                AppEmployee employee = appEmployeeService.findById(orderParam.getUserId());
//                City city = cityService.findById(orderParam.getCityId());
//                AppStore appStore = appStoreService.findById(employee.getStoreId());
//                if ("ZY".equals(appStore.getStoreType().getValue()) && ("FZY009".equals(appStore.getStoreCode()) || "HLC004".equals(appStore.getStoreCode()) || "ML001".equals(appStore.getStoreCode()) || "QCMJ008".equals(appStore.getStoreCode()) ||
//                        "SB010".equals(appStore.getStoreCode()) || "YC002".equals(appStore.getStoreCode()) || "ZC002".equals(appStore.getStoreCode()) || "RC005".equals(appStore.getStoreCode()) ||
//                        "FZM007".equals(appStore.getStoreCode()) || "SH001".equals(appStore.getStoreCode()) || "YJ001".equals(appStore.getStoreCode()) || "HS001".equals(appStore.getStoreCode()) ||
//                        "XC001".equals(appStore.getStoreCode()))) {
//                    if (StringUtils.isBlank(orderParam.getSalesNumber())) {
//                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "四川直营门店销售纸质单号不能为空！", "");
//                        logger.warn("createOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
//                        return resultDTO;
//                    }
//                }
//            }

            //**********************************开始创建订单 **************************
            //******************* 根据商品确定订单表单号为 XN 或者 XNFW ********************
//            List<Long> allGoodsList = new ArrayList<>();
//            goodsList.forEach(g -> allGoodsList.add(g.getId()));
//            productCouponList.forEach(p -> allGoodsList.add(p.getId()));
//            String orderNumberType = appOrderService.returnType(allGoodsList,orderParam.getUserId(),identityType);
            AppEmployee employee = employeeService.findById(orderParam.getUserId());
            //******************* 创建订单基础信息 *****************
            OrderBaseInfo orderBaseInfo = appOrderService.createOrderBaseInfo(employee.getCityId(), orderParam.getUserId(),
                    orderParam.getIdentityType(), orderParam.getCustomerId(), deliverySimpleInfo.getDeliveryType(), orderParam.getRemark(), null);
//            String oldOrderNumber = orderBaseInfo.getOrderNumber();
//            oldOrderNumber = oldOrderNumber.replace("XN",orderNumberType);
//            orderBaseInfo.setOrderNumber(oldOrderNumber);

            //****************** 创建订单物流信息 ******************
            OrderLogisticsInfo orderLogisticsInfo = appOrderService.createOrderLogisticInfo(deliverySimpleInfo);
            orderLogisticsInfo.setOrdNo(orderBaseInfo.getOrderNumber());

            //****************** 创建订单商品信息 ******************
            CreateOrderGoodsSupport support = maOrderService.createMaOrderGoodsInfo(goodsList, orderParam.getUserId(), orderParam.getIdentityType(),
                    orderParam.getCustomerId(), productCouponList, orderBaseInfo.getOrderNumber());

            //****************** 创建订单券信息 *********************
            List<OrderCouponInfo> orderCouponInfoList = new ArrayList<>();

            //****************** 创建订单优惠券信息 *****************
//            List<OrderCouponInfo> orderCashCouponInfoList = commonService.createOrderCashCouponInfo(orderBaseInfo, null);
//            if (null != orderCashCouponInfoList && orderCashCouponInfoList.size() > 0) {
//                orderCouponInfoList.addAll(orderCashCouponInfoList);
//            }
            //****************** 创建订单产品券信息 *****************
//            List<OrderCouponInfo> orderProductCouponInfoList = commonService.createOrderProductCouponInfo(orderBaseInfo, support.getProductCouponGoodsList());
//            if (null != orderProductCouponInfoList && orderProductCouponInfoList.size() > 0) {
//                orderCouponInfoList.addAll(orderProductCouponInfoList);
//            }

            //****************** 处理订单账单相关信息 ***************
            OrderBillingDetails orderBillingDetails = new OrderBillingDetails();
            orderBillingDetails.setOrderNumber(orderBaseInfo.getOrderNumber());
            orderBillingDetails.setIsOwnerReceiving(orderLogisticsInfo.getIsOwnerReceiving());
            orderBillingDetails.setTotalGoodsPrice(support.getGoodsTotalPrice());
            orderBillingDetails.setMemberDiscount(support.getMemberDiscount());
            orderBillingDetails.setPromotionDiscount(billing.getOrderDiscount());

            orderBillingDetails = appOrderService.createOrderBillingDetails(orderBillingDetails, orderParam.getUserId(), orderParam.getIdentityType(),
                    billing, cashCouponList, support.getProductCouponGoodsList());

            orderBaseInfo.setTotalGoodsPrice(orderBillingDetails.getTotalGoodsPrice());

            //****************** 处理订单账单支付明细信息 ************
            List<OrderBillingPaymentDetails> paymentDetails = commonService.createOrderBillingPaymentDetails(orderBaseInfo, orderBillingDetails);

            /********* 开始计算分摊 促销分摊可能产生新的行记录 所以优先分摊 ******************/
            List<OrderGoodsInfo> orderGoodsInfoList;
            orderGoodsInfoList = dutchService.addGoodsDetailsAndDutch(orderParam.getUserId(), AppIdentityType.getAppIdentityTypeByValue(orderParam.getIdentityType()), null, support.getPureOrderGoodsInfo(), orderParam.getCustomerId());

            //******** 分摊现乐币 策略：每个商品 按单价占比 分摊 *********************
            // 乐币暂时不分摊
//            Integer leBiQty = billing.getLeBiQuantity();
//            orderGoodsInfoList = leBiDutchService.LeBiDutch(leBiQty, orderGoodsInfoList);

            //******** 分摊现现金返利 策略：每个商品 按单价占比 分摊 *********************
            Double cashReturnAmount = billing.getStoreSubvention();
            orderGoodsInfoList = cashReturnDutchService.cashReturnDutch(cashReturnAmount, orderGoodsInfoList);

            //******** 分摊现金券 策略：使用范围商品 按单价占比 分摊 *********************
            orderGoodsInfoList = cashCouponDutchService.cashCouponDutch(cashCouponList, orderGoodsInfoList);

            //******** 分摊完毕 计算退货 单价 ***************************
            orderGoodsInfoList = dutchService.countReturnPrice(orderGoodsInfoList, cashReturnAmount, CountUtil.div(0, 10D), billing.getOrderDiscount());
            //orderGoodsInfoList = dutchService.countReturnPrice(orderGoodsInfoList);

            //将产品券商品加入 分摊完毕的商品列表中
            orderGoodsInfoList.addAll(support.getProductCouponGoodsList());
            support.setOrderGoodsInfoList(orderGoodsInfoList);

            //****************** 创建订单经销差价返还明细 ***********
            List<OrderJxPriceDifferenceReturnDetails> jxPriceDifferenceReturnDetailsList = commonService.createOrderJxPriceDifferenceReturnDetails(orderBaseInfo, support.getOrderGoodsInfoList(), promotionSimpleInfoList);
            if (null != jxPriceDifferenceReturnDetailsList && jxPriceDifferenceReturnDetailsList.size() > 0) {
                orderBillingDetails.setJxPriceDifferenceAmount(jxPriceDifferenceReturnDetailsList.stream().mapToDouble(OrderJxPriceDifferenceReturnDetails::getAmount).sum());
            }

            //**************** 创建要检核库存的商品和商品数量的Map ***********
            Map<Long, Integer> inventoryCheckMap = commonService.createInventoryCheckMap(orderGoodsInfoList);
            support.setInventoryCheckMap(inventoryCheckMap);

            //添加商品专供标志
            orderGoodsInfoList = this.commonService.addGoodsSign(orderGoodsInfoList, orderBaseInfo);
            List<OrderCouponInfo> orderProductCouponInfoList = new ArrayList<>();
            //**************** 1、检查库存和与账单支付金额是否充足,如果充足就扣减相应的数量 ***********
            //**************** 2、持久化订单相关实体信息 ****************
            transactionalSupportService.createOrderBusiness(deliverySimpleInfo, support.getInventoryCheckMap(), employee.getCityId(), orderParam.getIdentityType(),
                    orderParam.getUserId(), orderParam.getCustomerId(), cashCouponList, orderProductCouponInfoList, orderBillingDetails, orderBaseInfo,
                    orderLogisticsInfo, orderGoodsInfoList, orderCouponInfoList, paymentDetails, jxPriceDifferenceReturnDetailsList, null,promotionSimpleInfoList);

            //****** 清空当单购物车商品 ******
            maOrderService.clearOrderGoodsInMaterialList(orderParam.getUserId(), orderParam.getIdentityType(), goodsList, productCouponList);

            if (orderBillingDetails.getAmountPayable() <= AppConstant.PAY_UP_LIMIT) {
                //如果预存款或信用金已支付完成直接发送到WMS出货单
                if (orderBaseInfo.getDeliveryType() == AppDeliveryType.HOUSE_DELIVERY) {
                    iCallWms.sendToWmsRequisitionOrderAndGoods(orderBaseInfo.getOrderNumber());
                }
                //将该订单入拆单消息队列
                maSinkSender.sendOrder(orderBaseInfo.getOrderNumber());
                //添加订单生命周期
                appOrderService.addOrderLifecycle(OrderLifecycleType.PAYED, orderBaseInfo.getOrderNumber());

                // 激活订单赠送的产品券
                // productCouponService.activateCusProductCoupon(orderBaseInfo.getOrderNumber());

                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "后台创建订单成功",
                        new CreateOrderResponse(orderBaseInfo.getOrderNumber(), Double.parseDouble(CountUtil.retainTwoDecimalPlaces(orderBillingDetails.getAmountPayable())), true, false));
                logger.info("maCreateOrder OUT,后台创建订单成功,出参 resultDTO:{}", resultDTO);
            } else {
                //判断是否可选择货到付款
//                Boolean isCashDelivery = this.commonService.checkCashDelivery(orderGoodsInfoList, userId, AppIdentityType.getAppIdentityTypeByValue(identityType));
                Boolean isCashDelivery = Boolean.FALSE;
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "后台创建订单成功",
                        new CreateOrderResponse(orderBaseInfo.getOrderNumber(), Double.parseDouble(CountUtil.retainTwoDecimalPlaces(orderBillingDetails.getAmountPayable())), false, isCashDelivery));
                logger.info("maCreateOrder OUT,后台创建订单成功,出参 resultDTO:{}", resultDTO);
            }
            return resultDTO;
        } catch (LockStoreInventoryException | LockStorePreDepositException | LockCityInventoryException | LockCustomerCashCouponException |
                LockCustomerLebiException | LockCustomerPreDepositException | LockEmpCreditMoneyException | LockStoreCreditMoneyException |
                LockStoreSubventionException | SystemBusyException | LockCustomerProductCouponException | GoodsMultipartPriceException | GoodsNoPriceException |
                OrderPayableAmountException | DutchException | OrderCreditMoneyException | OrderDiscountException | GoodsQtyErrorException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, e.getMessage(), null);
            logger.warn("maCreateOrder OUT,后台创建订单失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        } catch (IOException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单参数转换异常!", null);
            logger.warn("maCreateOrder EXCEPTION,后台创建订单失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        } catch (OrderSaveException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单创建异常!", null);
            logger.warn("maCreateOrder EXCEPTION,后台创建订单失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常,下单失败!", null);
            logger.warn("maCreateOrder EXCEPTION,后台创建订单失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}
