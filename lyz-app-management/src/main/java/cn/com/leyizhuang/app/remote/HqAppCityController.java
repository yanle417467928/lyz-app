package cn.com.leyizhuang.app.remote;

import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.City;
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

/**
 * 与HQ同步City
 * Created by caiyu on 2017/10/23.
 */
@RestController
@RequestMapping(value = "/remote/city")
public class HqAppCityController {
    private static final Logger logger = LoggerFactory.getLogger(HqAppCityController.class);

    @Resource
    private CityService cityService;

    @PostMapping(value = "/save")
    public ResultDTO<Object> addCity(@RequestBody HqAppCityDTO hqAppCityDTO) {
        ResultDTO<Object> resultDTO;
        if (null != hqAppCityDTO) {
            if (StringUtils.isBlank(hqAppCityDTO.getTitle())) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市名称不允许为空！", null);
            }
            if (StringUtils.isBlank(hqAppCityDTO.getSpell())) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市名称拼音不允许为空！", null);
            }
            if (StringUtils.isBlank(hqAppCityDTO.getNumber())) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市编码不允许为空！", null);
            }
            if (null == hqAppCityDTO.getStructureId()) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "所属分公司ID不允许为空！", null);
            }
            if (StringUtils.isBlank(hqAppCityDTO.getStructureTitle())) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "所属分公司名称不允许为空！", null);
            }
            if (null == hqAppCityDTO.getEnable()) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "是否生效不允许为空！", null);
            }
            try {
               City city = cityService.findByCityNumber(hqAppCityDTO.getNumber());
                if (null == city){
                   city = new City();
                   city.setName(hqAppCityDTO.getTitle());
                   city.setNumber(hqAppCityDTO.getNumber());
                   city.setSpell(hqAppCityDTO.getSpell());
                   city.setStructureId(hqAppCityDTO.getStructureId());
                   city.setStructureTitle(hqAppCityDTO.getStructureTitle());
                   city.setEnable(hqAppCityDTO.getEnable());
                   city.setEnableFalseTime(hqAppCityDTO.getEnableFalseTime());
                   cityService.save(city);
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                }else{
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该城市已存在！", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，城市同步失败", null);
                logger.warn("addCity EXCEPTION,城市同步失败，出参 resultDTO:{}", resultDTO);
                logger.warn("{}", e);
                return resultDTO;
            }
        }
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市信息为空！", null);
    }
}
