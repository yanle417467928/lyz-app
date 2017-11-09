package cn.com.leyizhuang.app.remote;

import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.HqAppStoreDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

/**
 * HQ-APP同步门店
 * Created by caiyu on 2017/11/6.
 */
@RestController
@RequestMapping(value = "/remote/store")
public class HqAppStoreController {

    private static final Logger logger = LoggerFactory.getLogger(HqAppEmployeeController.class);

    @Resource
    private AppStoreService appStoreService;

    /**
     * 同步添加门店信息
     * @param hqAppStoreDTO 门店信息
     * @return 成功或失败
     */
    @PostMapping(value = "/save")
    public ResultDTO<String> addStore(@RequestBody HqAppStoreDTO hqAppStoreDTO){
        logger.warn("addStore CALLED,同步添加门店信息，入参 hqAppStoreDTO:{}", hqAppStoreDTO);
        if (hqAppStoreDTO != null){
            if (StringUtils.isBlank(hqAppStoreDTO.getCreatorType())){
                logger.warn("addStore OUT,同步添加门店信息失败，出参 creatorType:{}", hqAppStoreDTO.getCreatorType());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "创建人类型不允许为空！", null);
            }
            if (null == hqAppStoreDTO.getCreateTime()){
                logger.warn("addStore OUT,同步添加门店信息失败，出参 createTime:{}",hqAppStoreDTO.getCreateTime());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "创建时间不允许为空！", null);
            }
            if (StringUtils.isBlank(hqAppStoreDTO.getStoreName())){
                logger.warn("addStore OUT,同步添加门店信息失败，出参 storeName:{}", hqAppStoreDTO.getStoreName());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店名称不允许为空！", null);
            }
            if (StringUtils.isBlank(hqAppStoreDTO.getStoreCode())){
                logger.warn("addStore OUT,同步添加门店信息失败，出参 storeCode:{}", hqAppStoreDTO.getStoreCode());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店编码不允许为空！", null);
            }
            if (null == hqAppStoreDTO.getIsDefault()){
                logger.warn("addStore OUT,同步添加门店信息失败，出参 isDefault:{}", hqAppStoreDTO.getIsDefault());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "是否为默认门店不允许为空！", null);
            }
            if (StringUtils.isBlank(hqAppStoreDTO.getStoreType())){
                logger.warn("addStore OUT,同步添加门店信息失败，出参 storeType:{}", hqAppStoreDTO.getStoreType());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店类型不允许为空！", null);
            }
            if (StringUtils.isBlank(hqAppStoreDTO.getCityCode())){
                logger.warn("addStore OUT,同步添加门店信息失败，出参 cityCode:{}", hqAppStoreDTO.getCityCode());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市编码不允许为空！", null);
            }
            if (StringUtils.isBlank(hqAppStoreDTO.getCity())){
                logger.warn("addStore OUT,同步添加门店信息失败，出参 cityCode:{}", hqAppStoreDTO.getCityCode());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市不允许为空！", null);
            }
            if (StringUtils.isBlank(hqAppStoreDTO.getProvince())){
                logger.warn("addStore OUT,同步添加门店信息失败，出参 cityCode:{}", hqAppStoreDTO.getCityCode());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "省级不允许为空！", null);
            }
            if (StringUtils.isBlank(hqAppStoreDTO.getArea())){
                logger.warn("addStore OUT,同步添加门店信息失败，出参 cityCode:{}", hqAppStoreDTO.getCityCode());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "区不允许为空！", null);
            }
            if (StringUtils.isBlank(hqAppStoreDTO.getDetailedAddress())){
                logger.warn("addStore OUT,同步添加门店信息失败，出参 cityCode:{}", hqAppStoreDTO.getCityCode());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店详细地址不允许为空！", null);
            }
            if (null == hqAppStoreDTO.getEnable()){
                logger.warn("addStore OUT,同步添加门店信息失败，出参 cityCode:{}", hqAppStoreDTO.getCityCode());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店是否生效不允许为空！", null);
            }
            if (null == hqAppStoreDTO.getIsSelfDelivery()){
                logger.warn("addStore OUT,同步添加门店信息失败，出参 cityCode:{}", hqAppStoreDTO.getCityCode());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "是否支持门店自提不允许为空！", null);
            }

            try {
                AppStore store = appStoreService.findByStoreCode(hqAppStoreDTO.getStoreCode());
                if (null == store) {
                    AppStore appStore = new AppStore();
                    if (hqAppStoreDTO.getStoreType().equals("ZY")) {
                        appStore.setStoreType(StoreType.ZY);
                    } else if (hqAppStoreDTO.getStoreType().equals("JM")) {
                        appStore.setStoreType(StoreType.JM);
                    } else if (hqAppStoreDTO.getStoreType().equals("FX")) {
                        appStore.setStoreType(StoreType.FX);
                    }
                    appStore.setCreatorType(hqAppStoreDTO.getCreatorType());
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    appStore.setCreateTime(sdf.parse(hqAppStoreDTO.getCreateTime()));
                    appStore.setIsDefault(hqAppStoreDTO.getIsDefault());
                    appStore.setStoreName(hqAppStoreDTO.getStoreName());
                    appStore.setStoreCode(hqAppStoreDTO.getStoreCode());
                    appStore.setCityCode(hqAppStoreDTO.getCityCode());
                    appStore.setPhone(hqAppStoreDTO.getPhone());
                    appStore.setProvince(hqAppStoreDTO.getProvince());
                    appStore.setCity(hqAppStoreDTO.getCity());
                    appStore.setArea(hqAppStoreDTO.getArea());
                    appStore.setDetailedAddress(hqAppStoreDTO.getDetailedAddress());
                    appStore.setEnable(hqAppStoreDTO.getEnable());
                    appStore.setIsSelfDelivery(hqAppStoreDTO.getIsSelfDelivery());
                    appStoreService.saveStore(appStore);
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                }else{
                    logger.warn("addStore OUT,该门店已存在,同步添加门店信息失败，出参 hqAppStoreDTO:{}",hqAppStoreDTO);
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该门店已存在，同步添加门店信息失败！", null);
                }

            }catch (Exception e){
                logger.warn("addStore EXCEPTION,同步添加门店信息失败，出参 resultDTO:{}",e);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未知异常，同步添加门店信息失败！", null);
            }
        }
        logger.warn("addStore,门店信息为空，同步添加门店信息失败！");
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店信息为空，同步添加门店信息失败！", null);
    }

    /**
     *同步修改门店信息
     * @param hqAppStoreDTO 修改的门店信息
     * @return  成功或失败
     */
    @PostMapping(value = "/update")
    public ResultDTO<String> updateStore(@RequestBody HqAppStoreDTO hqAppStoreDTO){
        logger.warn("updateStore CALLED,同步修改门店信息，入参 hqAppStoreDTO:{}", hqAppStoreDTO);
        if (null != hqAppStoreDTO){
            if (StringUtils.isBlank(hqAppStoreDTO.getModifierType())){
                logger.warn("updateStore OUT,同步修改门店信息失败，出参 modifierType:{}", hqAppStoreDTO.getModifierType());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "修改类型不允许为空！", null);
            }
            if (null == hqAppStoreDTO.getModifyTime()){
                logger.warn("updateStore OUT,同步修改门店信息失败，出参 modifyTime:{}", hqAppStoreDTO.getModifyTime());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "修改时间不允许为空！", null);
            }
            try {
                AppStore appStore = appStoreService.findByStoreCode(hqAppStoreDTO.getStoreCode());
                if (hqAppStoreDTO.getStoreType().equals("ZY")){
                    appStore.setStoreType(StoreType.ZY);
                }else if (hqAppStoreDTO.getStoreType().equals("JM")){
                    appStore.setStoreType(StoreType.JM);
                } else if (hqAppStoreDTO.getStoreType().equals("FX")) {
                    appStore.setStoreType(StoreType.FX);
                }
                appStore.setCreatorType(hqAppStoreDTO.getCreatorType());
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                appStore.setCreateTime(sdf.parse(hqAppStoreDTO.getCreateTime()));
                appStore.setIsDefault(hqAppStoreDTO.getIsDefault());
                appStore.setStoreName(hqAppStoreDTO.getStoreName());
                appStore.setStoreCode(hqAppStoreDTO.getStoreCode());
                appStore.setCityCode(hqAppStoreDTO.getCityCode());
                appStore.setModifierType(hqAppStoreDTO.getModifierType());
                appStore.setModifyTime(sdf.parse(hqAppStoreDTO.getModifyTime()));
                appStore.setPhone(hqAppStoreDTO.getPhone());
                appStore.setProvince(hqAppStoreDTO.getProvince());
                appStore.setCity(hqAppStoreDTO.getCity());
                appStore.setArea(hqAppStoreDTO.getArea());
                appStore.setDetailedAddress(hqAppStoreDTO.getDetailedAddress());
                appStore.setEnable(hqAppStoreDTO.getEnable());
                appStore.setIsSelfDelivery(hqAppStoreDTO.getIsSelfDelivery());
                appStoreService.modifyStore(appStore);
                logger.warn("同步修改门店信息成功！");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            }catch (Exception e){
                logger.warn("updateStore EXCEPTION,同步修改门店信息失败，出参 resultDTO:{}",e);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未知异常，同步修改门店信息失败！", null);
            }
        }
        logger.warn("门店信息为空，同步修改门店信息失败！");
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店信息为空，同步修改门店信息失败！", null);
    }

    /**
     * 同步删除门店信息
     * @param storeCode 门店编码
     * @return  成功或者失败
     */
    @PostMapping(value = "/delete")
    public ResultDTO<String> deleteStore(String storeCode){
        logger.warn("deleteStore CALLED,同步删除门店信息，入参 storeCode:{}", storeCode);
        if (StringUtils.isBlank(storeCode)){
            logger.warn("deleteStore OUT,同步删除门店信息失败，出参 storeCode:{}", storeCode);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店编码不允许为空！", null);
        }
        try {
            appStoreService.deleteStoreByStoreCode(storeCode);
            logger.warn("同步删除门店信息成功！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        }catch (Exception e){
            logger.warn("deleteStore EXCEPTION,同步删除门店信息失败，出参 resultDTO:{}",e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未知异常，同步删除门店信息失败！", null);
        }
    }
}
