package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.foundation.dao.MaterialAuditSheetDAO;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditSheet;
import cn.com.leyizhuang.app.foundation.pojo.request.MaterialAuditSheetRequest;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialAuditDetailsResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialAuditSheetResponse;
import cn.com.leyizhuang.app.foundation.service.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by caiyu on 2017/10/17.
 */
@Service
@Transactional
public class MaterialAuditSheetServiceImpl implements MaterialAuditSheetService {

    @Autowired
    private MaterialAuditSheetDAO materialAuditSheetDAO;

    @Resource
    private AppEmployeeService appEmployeeService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private GoodsPriceService goodsPriceService;

    @Resource
    private MaterialAuditGoodsInfoService materialAuditGoodsInfoService;

    @Autowired
    private MaterialListService materialListService;


    @Override
    public List<MaterialAuditSheet> queryListByStatus(int status) {
        return materialAuditSheetDAO.queryListByStatus(status);
    }

    @Override
    public List<MaterialAuditSheet> queryListByEmployeeID(Long employeeID) {
        return materialAuditSheetDAO.queryListByEmployeeID(employeeID);
    }

    @Override
    @Transactional
    public void addMaterialAuditSheet(MaterialAuditSheetRequest materialAuditSheetRequest,AppEmployee appEmployee) throws IOException {
            //新增物料审核单头赋值
            MaterialAuditSheet materialAuditSheet = materialAuditSheetRequestToMaterialAuditSheet(materialAuditSheetRequest);
            materialAuditSheet.setEmployeeName(appEmployee.getName());
            materialAuditSheet.setDeliveryType(AppDeliveryType.HOUSE_DELIVERY);
            materialAuditSheet.setStoreID(appEmployee.getStoreId());
            materialAuditSheet.setStatus(1);
            materialAuditSheet.setIsAudited(false);
            String auditNumber = this.createNumber();
            materialAuditSheet.setAuditNo(auditNumber);
            materialAuditSheet.setCreateTime(LocalDateTime.now());
            //保存物料审核单头
            materialAuditSheetDAO.addMaterialAuditSheet(materialAuditSheet);


            //获取物料审核单id
            Long auditHeaderID = materialAuditSheet.getAuditHeaderID();
            //获取商品相关信息（id，数量，是否赠品）
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsSimpleInfo.class);
            List<GoodsSimpleInfo> goodsList = objectMapper.readValue(materialAuditSheetRequest.getGoodsList(), javaType1);
            for (GoodsSimpleInfo goodsSimpleInfo : goodsList) {
                //根据商品id查找对应的商品
                GoodsDO goodsDO = goodsService.queryById(goodsSimpleInfo.getId());
                MaterialAuditGoodsInfo materialAuditGoodsInfo = new MaterialAuditGoodsInfo();
                //对物料审核单商品详情请进行赋值
                materialAuditGoodsInfo.setAuditHeaderID(auditHeaderID);
                materialAuditGoodsInfo.setGid(goodsSimpleInfo.getId());
                materialAuditGoodsInfo.setCoverImageUri(goodsDO.getCoverImageUri());
                materialAuditGoodsInfo.setQty(goodsSimpleInfo.getNum());
                materialAuditGoodsInfo.setGoodsSpecification(goodsDO.getGoodsSpecification());
                materialAuditGoodsInfo.setGoodsUnit(goodsDO.getGoodsUnit());
                materialAuditGoodsInfo.setSku(goodsDO.getSku());
                materialAuditGoodsInfo.setSkuName(goodsDO.getSkuName());
                //获取商品零售价
                Double goodsPrice = goodsPriceService.findGoodsRetailPriceByGoodsIDAndStoreID(goodsSimpleInfo.getId(), appEmployee.getStoreId());
                materialAuditGoodsInfo.setRetailPrice(goodsPrice);
                //对物料审核单商品详情进行保存
                materialAuditGoodsInfoService.addMaterialAuditGoodsInfo(materialAuditGoodsInfo);
            }

            //工人料单提交审核之后，需要清空工人下料清单中对应的商品
            List<Long> deleteGoodsIds = new ArrayList<>();
            for (GoodsSimpleInfo simpleInfo:goodsList){
                deleteGoodsIds.add(simpleInfo.getId());
            }

            materialListService.deleteMaterialListByUserIdAndIdentityTypeAndGoodsId(appEmployee.getEmpId(),
                    appEmployee.getIdentityType(),deleteGoodsIds);

    }

    public MaterialAuditSheet materialAuditSheetRequestToMaterialAuditSheet(MaterialAuditSheetRequest materialAuditSheetRequest){
        MaterialAuditSheet materialAuditSheet = new MaterialAuditSheet();
        materialAuditSheet.setEmployeeID(materialAuditSheetRequest.getUserID());
        materialAuditSheet.setReceiver(materialAuditSheetRequest.getReceiver());
        materialAuditSheet.setReceiverPhone(materialAuditSheetRequest.getReceiverPhone());
        materialAuditSheet.setDeliveryCity(materialAuditSheetRequest.getDeliveryCity());
        materialAuditSheet.setDeliveryCounty(materialAuditSheetRequest.getDeliveryCounty());
        materialAuditSheet.setDeliveryStreet(materialAuditSheetRequest.getDeliveryStreet());
        materialAuditSheet.setResidenceName(materialAuditSheetRequest.getResidenceName());
        materialAuditSheet.setDetailedAddress(materialAuditSheetRequest.getDetailedAddress());
        materialAuditSheet.setIsOwnerReceiving(materialAuditSheetRequest.getIsOwnerReceiving());
        materialAuditSheet.setRemark(materialAuditSheetRequest.getRemark());
        //把String类型时间转换为LocalDateTime类型
        materialAuditSheet.setReservationDeliveryTime(materialAuditSheetRequest.getReservationDeliveryTime());
        return  materialAuditSheet;
    }

    @Override
    public void modifyMaterialAuditSheet(MaterialAuditSheet materialAuditSheet) {
        materialAuditSheetDAO.modifyMaterialAuditSheet(materialAuditSheet);
    }

    @Override
    public void modifyStatus(int status,String auditNumber) {
        materialAuditSheetDAO.modifyStatus(status,auditNumber);
    }

    @Override
    public MaterialAuditSheet queryByAuditNo(String auditNumber) {
        return materialAuditSheetDAO.queryByAuditNo(auditNumber);
    }

    @Override
    public MaterialAuditDetailsResponse queryDetailsByAuditNo(String auditNo) {
        return materialAuditSheetDAO.queryDetailsByAuditNo(auditNo);
    }

    @Override
    public List<MaterialAuditSheetResponse> queryListByEmployeeIDAndStatus(Long employeeID, Integer status) {
        return materialAuditSheetDAO.queryListByEmployeeIDAndStatus(employeeID,status);
    }

    @Override
    public List<MaterialAuditSheet> queryListByStoreIDAndStatus(Long storeID, Integer status) {
        return materialAuditSheetDAO.queryListByStoreIDAndStatus(storeID,status);
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
