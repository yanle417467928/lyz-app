package cn.com.leyizhuang.app.web.controller.city;

import cn.com.leyizhuang.app.foundation.pojo.City;
import cn.com.leyizhuang.app.foundation.pojo.response.CityListResponse;
import cn.com.leyizhuang.app.foundation.service.impl.CityService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 城市控制器
 *
 * @author Richard
 *         Created on 2017-09-21 14:22
 **/
@RestController
@RequestMapping(value = "/app/city")
public class CityController {

    private static final Logger logger = LoggerFactory.getLogger(CityController.class);

    @Resource
    private CityService cityService;


    /**
     * 获取城市列表
     *
     * @return ResultDTO
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultDTO getCityList() {
        logger.info("customerLogin CALLED,获取城市列表，入参:");

        ResultDTO resultDTO;
        List<City> cityList = cityService.findAll();
        List<CityListResponse> responseList = new ArrayList<>();
        for (City c : cityList) {
            CityListResponse response = new CityListResponse();
            response.setCityId(c.getId());
            response.setCityName(c.getTitle());
            responseList.add(response);
        }
        if (responseList.size() > 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, responseList);
            logger.info("customerLogin OUT,城市列表获取成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } else {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("customerLogin OUT,城市列表获取成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

    }


}
