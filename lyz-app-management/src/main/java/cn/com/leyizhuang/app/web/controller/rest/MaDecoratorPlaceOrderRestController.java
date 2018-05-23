package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.constant.ApplicationConstant;
import cn.com.leyizhuang.app.core.constant.FitExcelImportGoodsErrorType;
import cn.com.leyizhuang.app.core.constant.MaterialListType;
import cn.com.leyizhuang.app.core.utils.ExcelImportUtil;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.DeliveryAddressDO;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.AssertUtil;
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
import javax.servlet.http.HttpServletResponse;
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
    private DeliveryAddressService deliveryAddressService;

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
                                              String city,String county,String street,String residenceName,String estateInfo,String detailedAddress) {
        ResultDTO<Object> resultDTO;
        System.out.println("进入方法");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JavaType goodsSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsSkuQtyParam.class);
        try {
            AppEmployee employee = employeeService.findById(guideId);
            if (null != employee) {

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
                //*******************************************************************************************

                //获取地址id
                Long deliveryId = deliveryAddressDO.getId();

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
                                    materialListDOTemp.setDeliveryId(deliveryId);
                                    materialListDOTemp.setQty(goodsList.get(i).getQty());
                                    materialListDOTemp.setMaterialListType(MaterialListType.NORMAL);
                                    materialListSave.add(materialListDOTemp);
                                }
                            } else {
                                MaterialListDO materialListDOTemp = transformRepeat(goodsDO);
                                materialListDOTemp.setUserId(guideId);
                                materialListDOTemp.setIdentityType(employee.getIdentityType());
                                materialListDOTemp.setRemark(remark);
                                materialListDOTemp.setDeliveryId(deliveryId);
                                materialListDOTemp.setQty(goodsList.get(i).getQty());
                                materialListDOTemp.setMaterialListType(MaterialListType.NORMAL);
                                materialListSave.add(materialListDOTemp);
                            }
                        } else {
                            if (null != materialListDO) {
                                materialListDO.setQty(materialListDO.getQty() + goodsList.get(i).getQty());
                                materialListUpdate.add(materialListDO);
                            }
                            if (null != materialAuditListDO) {
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
}
