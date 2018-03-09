package cn.com.leyizhuang.app.remote;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.service.CityService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.HqAppCityDTO;
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
 * 与HQ同步City
 * Created by caiyu on 2017/10/23.
 */
@RestController
@RequestMapping(value = "/remote/city")
public class HqAppCityController {
    private static final Logger logger = LoggerFactory.getLogger(HqAppCityController.class);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Resource
    private CityService cityService;

    /**
     * 同步存储城市信息
     *
     * @param hqAppCityDTO hq传输对象
     */
    @PostMapping(value = "/save")
    public ResultDTO<String> addCity(@RequestBody HqAppCityDTO hqAppCityDTO) {
        if (null != hqAppCityDTO) {
            logger.warn("addCity CALLED,同步存储城市信息，入参 hqAppCityDTO:{}", hqAppCityDTO);
            if (StringUtils.isBlank(hqAppCityDTO.getTitle())) {
                logger.warn("addCity OUT,同步存储城市信息失败，出参 resultDTO:{}", hqAppCityDTO.getTitle());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市名称不允许为空！", null);
            }
            if (StringUtils.isBlank(hqAppCityDTO.getSpell())) {
                logger.warn("addCity OUT,同步存储城市信息失败，出参 resultDTO:{}", hqAppCityDTO.getSpell());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市名称拼音不允许为空！", null);
            }
            if (StringUtils.isBlank(hqAppCityDTO.getNumber())) {
                logger.warn("addCity OUT,同步存储城市信息失败，出参 resultDTO:{}", hqAppCityDTO.getNumber());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市编码不允许为空！", null);
            }
            if (null == hqAppCityDTO.getStructureId()) {
                logger.warn("addCity OUT,同步存储城市信息失败，出参 resultDTO:{}", hqAppCityDTO.getStructureId());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "所属分公司ID不允许为空！", null);
            }
            if (StringUtils.isBlank(hqAppCityDTO.getStructureTitle())) {
                logger.warn("addCity OUT,同步存储城市信息失败，出参 resultDTO:{}", hqAppCityDTO.getStructureTitle());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "所属分公司名称不允许为空！", null);
            }
            if (null == hqAppCityDTO.getEnable()) {
                logger.warn("addCity OUT,同步存储城市信息失败，出参 resultDTO:{}", hqAppCityDTO.getEnable());
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "是否生效不允许为空！", null);
            }
            try {
                City city = cityService.findByCityNumber(hqAppCityDTO.getNumber());
                if (null == city) {
                    city = new City();
                    city.setName(hqAppCityDTO.getTitle());
                    city.setNumber(hqAppCityDTO.getNumber());
                    city.setSpell(hqAppCityDTO.getSpell());
                    city.setStructureId(hqAppCityDTO.getStructureId());
                    city.setStructureTitle(hqAppCityDTO.getStructureTitle());
                    city.setEnable(hqAppCityDTO.getEnable());
                    if (StringUtils.isNotBlank(hqAppCityDTO.getEnableFalseTime())) {
                        city.setEnableFalseTime(sdf.parse(hqAppCityDTO.getEnableFalseTime()));
                    }
                    cityService.save(city);
                    logger.warn("addCity EXCEPTION,城市存储同步成功！",city);
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                } else {
                    logger.warn("addCity OUT,同步存储城市信息失败，出参 resultDTO:{}", "该城市已存在");
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该城市已存在！", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("addCity EXCEPTION,城市存储同步成功，出参 e:{}", e);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，城市同步失败", null);
            }
        }
        logger.warn("城市信息为空！");
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市信息为空！", null);
    }

    /**
     * 同步修改城市信息
     *
     * @param hqAppCityDTO hq传输对象
     */
    @PostMapping(value = "/update")
    public ResultDTO<String> updateCity(@RequestBody HqAppCityDTO hqAppCityDTO) {
        if (null != hqAppCityDTO) {
            logger.warn("updateCity CALLED,同步修改城市信息，入参 hqAppCityDTO:{}", hqAppCityDTO);
            try {
                City city = cityService.findByCityNumber(hqAppCityDTO.getNumber());
                if (null == city) {
                    logger.warn("updateCity OUT,同步修改城市信息失败，出参 hqAppCityDTO:{}", "未查询到此城市信息");
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此城市信息，同步修改城市信息失败", null);
                }
                city.setEnable(hqAppCityDTO.getEnable());
                city.setStructureTitle(hqAppCityDTO.getStructureTitle());
                city.setStructureId(hqAppCityDTO.getStructureId());
                city.setSpell(hqAppCityDTO.getSpell());
                city.setName(hqAppCityDTO.getTitle());
                city.setEnableFalseTime(sdf.parse(hqAppCityDTO.getEnableFalseTime()));
                city.setNumber(hqAppCityDTO.getNumber());
                cityService.modifyCity(city);
                logger.warn("updateCity OUT,同步修改城市信息成功，出参 resultDTO:{}", city);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } catch (Exception e) {
                logger.warn("updateCity OUT,发生未知异常，同步修改城市信息失败！，出参 e:{}", e);
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，同步修改城市信息失败！", null);
            }
        }
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市信息为空！", null);
    }

    /**
     * 同步删除城市信息
     *
     * @param code 城市编码
     * @return 返回成功或失败
     */
    @PostMapping(value = "/delete")
    public ResultDTO<String> deleteCity(String code) {
        if (StringUtils.isBlank(code)) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市编码不能为空！", null);
        }
        logger.warn("deleteCity CALLED,同步删除城市信息，入参 code:{}", code);

        try {
            cityService.deleteCityByCode(code);
            logger.warn("deleteCity OUT,同步删除城市信息成功！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } catch (Exception e) {
            logger.warn("updateCity OUT,发生未知异常，同步删除城市信息失败！，出参 e:{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，同步删除城市信息失败！", null);
        }
    }
}
