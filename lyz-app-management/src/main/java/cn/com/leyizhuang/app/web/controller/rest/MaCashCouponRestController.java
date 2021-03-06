package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.constant.AppCashCouponType;
import cn.com.leyizhuang.app.foundation.pojo.CashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.CashCouponGoods;
import cn.com.leyizhuang.app.foundation.pojo.CashCouponStore;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.CashCouponSendService;
import cn.com.leyizhuang.app.foundation.service.CashCouponService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 现金券控制器
 * Created by panjie on 2018/1/2.
 */
@RestController
@RequestMapping(value = MaCashCouponRestController.PRE_URL,produces = "application/json;charset=utf-8")
public class MaCashCouponRestController extends  BaseRestController{

    protected final static String PRE_URL = "/rest/cashCoupon";

    private final Logger logger = LoggerFactory.getLogger(MaCashCouponRestController.class);

    @Resource
    private CashCouponService cashCouponService;

    @Resource
    private CashCouponSendService cashCouponSendService;

    @GetMapping("/grid")
    public GridDataVO<CashCoupon> gridData(Integer offset, Integer size, String keywords,String startTime,String endTime){
        GridDataVO<CashCoupon> gridDataVO = new GridDataVO<>();
        Integer page = getPage(offset, size);

        PageInfo<CashCoupon> pageInfo = cashCouponService.queryPage(page,size,keywords,startTime,endTime);

        return gridDataVO.transform(pageInfo.getList(),pageInfo.getTotal());
    }

    @PostMapping("/save")
    public ResultDTO<?> save(@Valid CashCoupon cashCoupon, String goodsDetails, String companys, String brands ,String stores , BindingResult result,Long optId)throws IOException {
        if (!result.hasErrors()) {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, CashCouponGoods.class);
            JavaType javaType3 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, CashCouponStore.class);
            List<CashCouponGoods> goodsList = objectMapper.readValue(goodsDetails, javaType1);
            List<String> brandList = new ArrayList<>();
            List<String> companyList = new ArrayList<>();
            List<CashCouponStore> storeList = objectMapper.readValue(stores,javaType3);

            AppCashCouponType type = cashCoupon.getType();

            if (type.equals(AppCashCouponType.GENERAL)){

            }else if (type.equals(AppCashCouponType.COMPANY)){
                String[] strArr = companys.split(",");
                companyList = Arrays.asList(strArr);
                if(companyList == null || companyList.size() == 0){
                    new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "公司数据为空", null);
                }
            }else if (type.equals(AppCashCouponType.BRAND)){
                String[] strArr = brands.split(",");
                brandList = Arrays.asList(strArr);
                if(brandList == null || brandList.size() == 0){
                    new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "品牌数据为空", null);
                }
            }else if (type.equals(AppCashCouponType.GOODS)){
                if (goodsList == null || goodsList.size() == 0){
                    new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品数据为空", null);
                }
            }

            if (cashCoupon.getIsSpecifiedStore()){
                if (storeList == null || storeList.size() == 0){
                    new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店数据为空", null);
                }
            }

            /* 持久化数据 */
            optId=this.getShiroUser().getId();
            cashCouponService.saveCashCouponTemplate(cashCoupon,companyList,brandList,goodsList,storeList,optId);

            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "新增现金券模版成功！", null);
        } else {
            return actFor400(result,"提交的数据有误");
        }
    }

    @PostMapping("/edit")
    public ResultDTO<?> edit(@Valid CashCoupon cashCoupon, String goodsDetails, String companys, String brands ,String stores , BindingResult result)throws IOException {
        if (!result.hasErrors()) {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, CashCouponGoods.class);
            JavaType javaType3 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, CashCouponStore.class);
            List<CashCouponGoods> goodsList = objectMapper.readValue(goodsDetails, javaType1);
            List<String> brandList = new ArrayList<>();
            List<String> companyList = new ArrayList<>();
            List<CashCouponStore> storeList = objectMapper.readValue(stores,javaType3);

            AppCashCouponType type = cashCoupon.getType();

            if (type.equals(AppCashCouponType.GENERAL)){

            }else if (type.equals(AppCashCouponType.COMPANY)){
                String[] strArr = companys.split(",");
                companyList = Arrays.asList(strArr);
                if(companyList == null || companyList.size() == 0){
                    new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "公司数据为空", null);
                }
            }else if (type.equals(AppCashCouponType.BRAND)){
                String[] strArr = brands.split(",");
                brandList = Arrays.asList(strArr);
                if(brandList == null || brandList.size() == 0){
                    new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "品牌数据为空", null);
                }
            }else if (type.equals(AppCashCouponType.GOODS)){
                if (goodsList == null || goodsList.size() == 0){
                    new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品数据为空", null);
                }
            }

            if (cashCoupon.getIsSpecifiedStore()){
                if (storeList == null || storeList.size() == 0){
                    new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店数据为空", null);
                }
            }

            /* 持久化数据 优惠券不能修改 */
            //cashCouponService.updateCashCouponTemplate(cashCoupon,companyList,brandList,goodsList,storeList);

            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "新增现金券模版成功！", null);
        } else {
            return actFor400(result,"提交的数据有误");
        }
    }

    @PutMapping(value = "/delete")
    public ResultDTO<?> deleteByIdList(String ids) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
        List<Long> idList = objectMapper.readValue(ids, javaType1);

        cashCouponService.deleteCashCouponTemplate(idList);

        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "删除成功", null);

    }

    @PostMapping(value = "/send")
    public ResultDTO<?> send(Long customerId,Long cashCouponId,Integer qty,Long optId) throws IOException {
        optId=this.getShiroUser().getId();
        if(customerId == null || cashCouponId == null || qty == 0){
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发送失败", null);
        }

        return cashCouponSendService.send(customerId,cashCouponId,qty,optId);
    }

    @PostMapping(value = "/sendBatch")
    public ResultDTO<?> sendBatch(String customerIds,Long cashCouponId,Integer qty,Long optId) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
        List<Long> idList = objectMapper.readValue(customerIds, javaType1);

        if(idList == null || idList == null || qty == 0){
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发送失败", null);
        }

        return cashCouponSendService.sendBatch(idList,cashCouponId,qty,optId);
    }
}
