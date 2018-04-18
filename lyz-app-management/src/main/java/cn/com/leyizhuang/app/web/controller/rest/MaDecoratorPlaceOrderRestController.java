package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.constant.FitExcelImportGoodsErrorType;
import cn.com.leyizhuang.app.core.constant.MaterialListType;
import cn.com.leyizhuang.app.core.utils.ExcelImportUtil;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.AssertUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
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

    @RequestMapping(value = "/import/goods", method = RequestMethod.POST)
    public Map<String, Object> importOrderGoods(@RequestParam(value = "file") MultipartFile excelFile,
                                                @RequestParam(value = "storeId") Long storeId, @RequestParam(value = "guideId") Long guideId) {
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
    public ResultDTO<Object> submitOrderGoods(Long storeId, Long guideId, String remark, String goodsDetails) {
        ResultDTO<Object> resultDTO;
        System.out.println("进入方法");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JavaType goodsSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsSkuQtyParam.class);
        try {
            AppEmployee employee = employeeService.findById(guideId);
            if (null != employee) {
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
                                }else {
                                    MaterialListDO materialListDOTemp = transformRepeat(goodsDO);
                                    materialListDOTemp.setUserId(guideId);
                                    materialListDOTemp.setIdentityType(employee.getIdentityType());
                                    materialListDOTemp.setRemark(remark);
                                    materialListDOTemp.setQty(goodsList.get(i).getQty());
                                    materialListDOTemp.setMaterialListType(MaterialListType.NORMAL);
                                    materialListSave.add(materialListDOTemp);
                                }
                            } else {
                                MaterialListDO materialListDOTemp = transformRepeat(goodsDO);
                                materialListDOTemp.setUserId(guideId);
                                materialListDOTemp.setIdentityType(employee.getIdentityType());
                                materialListDOTemp.setRemark(remark);
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
}
