package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.constant.ActBaseType;
import cn.com.leyizhuang.app.core.constant.ActConditionType;
import cn.com.leyizhuang.app.core.constant.ActPromotionType;
import cn.com.leyizhuang.app.core.constant.ActStatusType;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActBaseDO;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActGiftDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActGoodsMappingDO;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActStoreDO;
import cn.com.leyizhuang.app.foundation.service.AppActService;
import cn.com.leyizhuang.app.foundation.vo.ActBaseVO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 促销控制器
 * Created by panjie on 2017/12/15.
 */

@RestController
@RequestMapping(value = MaActivityRestController.PRE_URL,produces = "application/json;charset=utf-8")
public class MaActivityRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/activity";

    private final Logger logger = LoggerFactory.getLogger(MaGoodsCategoryRestController.class);

    @Resource
    private AppActService appActService;

    /**
     * 分页查询
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<ActBaseVO> getActBaseList(Integer offset, Integer size, String keywords,String status) {
            GridDataVO<ActBaseVO> gridDataVO = new GridDataVO<>();
            Integer page = getPage(offset, size);

            PageInfo<ActBaseDO> actbasePage = appActService.queryPageVO(page,size,keywords, status);
            List<ActBaseDO> actBaseDOList = actbasePage.getList();
            List<ActBaseVO> actBaseVOList = ActBaseVO.transform(actBaseDOList);
            gridDataVO.transform(actBaseVOList,actbasePage.getTotal());
            return  gridDataVO;
    }

    @PostMapping(value = "/save")
    public ResultDTO<?> save(@Valid ActBaseDO baseDO, String goodsDetails, String giftDetails, String stores,Double subAmount, BindingResult result) throws IOException {
        if (!result.hasErrors()) {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ActGoodsMappingDO.class);
            JavaType javaType2 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ActGiftDetailsDO.class);
            JavaType javaType3 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ActStoreDO.class);
            List<ActGoodsMappingDO> goodsList = objectMapper.readValue(goodsDetails, javaType1);
            List<ActGiftDetailsDO> giftList = objectMapper.readValue(giftDetails, javaType2);
            List<ActStoreDO> storeList =  objectMapper.readValue(stores, javaType3);

            if(goodsList == null){
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "本品为空", null);
            }

            appActService.save(baseDO,goodsList,giftList,subAmount,storeList);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "保存成功", null);
        } else {
            return actFor400(result,"提交的数据有误");
        }
    }

    @PostMapping(value = "/edit")
    public ResultDTO<?> edit(@Valid ActBaseDO baseDO, String goodsDetails, String giftDetails, String stores,Double subAmount, BindingResult result) throws IOException {
        if (!result.hasErrors()) {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ActGoodsMappingDO.class);
            JavaType javaType2 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ActGiftDetailsDO.class);
            JavaType javaType3 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ActStoreDO.class);
            List<ActGoodsMappingDO> goodsList = objectMapper.readValue(goodsDetails, javaType1);
            List<ActGiftDetailsDO> giftList = objectMapper.readValue(giftDetails, javaType2);
            List<ActStoreDO> storeList =  objectMapper.readValue(stores, javaType3);

            if(goodsList == null){
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "本品为空", null);
            }
            if(baseDO.getStatus().equals(ActStatusType.PUBLISH.getValue())){
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "已经发布，不允许修改", null);
            }

            appActService.edit(baseDO,goodsList,giftList,subAmount,storeList);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "修改成功", null);
        } else {
            return actFor400(result,"提交的数据有误");
        }
    }

    /**
     * 返回促销相关枚举类型
     * @return
     */
    @GetMapping(value = "/enums")
    public Map<String,Object> getAllActivityEnum(){
        Map<String,Object> res = new HashMap<>();

        ActBaseType[] baseTypes = ActBaseType.values();
        ActConditionType[] conditionTypes = ActConditionType.values();
        ActPromotionType[] promotionTypes = ActPromotionType.values();

        res.put("baseTypes",baseTypes);
        res.put("conditionTypes",conditionTypes);
        res.put("promotionTypes",promotionTypes);
        return res;
    }

    @PutMapping(value = "/publish")
    public ResultDTO<?> publish(String ids) throws IOException {

            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
            List<Long> idList = objectMapper.readValue(ids, javaType1);

            for (Long id : idList) {
                appActService.publishAct(id);
            }

            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "发布成功", null);

    }

    @PutMapping(value = "/invalid")
    public ResultDTO<?> invalid(String ids) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
            List<Long> idList = objectMapper.readValue(ids, javaType1);

            for (Long id : idList) {
                appActService.inValid(id);
            }

            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "失效成功", null);

    }
}
