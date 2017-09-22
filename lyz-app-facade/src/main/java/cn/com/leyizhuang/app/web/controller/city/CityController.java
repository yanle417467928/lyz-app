package cn.com.leyizhuang.app.web.controller.city;

import cn.com.leyizhuang.app.foundation.pojo.City;
import cn.com.leyizhuang.app.foundation.pojo.rest.CityListResponse;
import cn.com.leyizhuang.app.foundation.service.Impl.CityService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市控制器
 *
 * @author Richard
 * Created on 2017-09-21 14:22
 **/
@RestController
@RequestMapping(value = "/app/city")
public class CityController {

    @Autowired
    private CityService cityService;


    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResultDTO<Object> getCityList() {
        List<City> cityList = cityService.findAll();
        List<CityListResponse> responseList = new ArrayList<>();
        for (City c : cityList) {
            CityListResponse response = new CityListResponse();
            response.setCityId(c.getId());
            response.setCityName(c.getTitle());
            responseList.add(response);
        }
        if (responseList.size() > 0) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, responseList);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        }

    }


}
