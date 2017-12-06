package cn.com.leyizhuang.app.web.controller.rest;


import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.city.CityDeliveryTime;
import cn.com.leyizhuang.app.foundation.service.MaCityDeliveryTimeService;
import cn.com.leyizhuang.app.foundation.vo.CityDeliveryTimeVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.ValidatorResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = MaCityDeliveryTimeRestController.PRE_URL,produces = "application/json;charset=utf-8")
public class MaCityDeliveryTimeRestController extends BaseRestController {
    protected final static String PRE_URL = "/rest/cityDeliveryTime";

    private final Logger logger = LoggerFactory.getLogger(GoodsRestController.class);
    @Autowired
    private MaCityDeliveryTimeService macityDeliveryTimeService;

    /**
     * 查询当前城市下的配送时间
     * @param
     * @param size
     * @param keywords
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public GridDataVO<CityDeliveryTimeVO> restCityDeliveryTimeIdGet(Integer offset, Integer size, String keywords, @PathVariable(value = "id") Long id) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<CityDeliveryTime> cityDeliveryTimePage = this.macityDeliveryTimeService.queryPage(page, size,id);
        List<CityDeliveryTime> cityDeliveryTimeList = cityDeliveryTimePage.getList();
        List<CityDeliveryTimeVO> cityDeliveryTimeVOList = CityDeliveryTime.transform(cityDeliveryTimeList);
        return new GridDataVO<CityDeliveryTimeVO>().transform(cityDeliveryTimeVOList,cityDeliveryTimePage.getTotal());
    }


    /**
     * 判断配送时间是否冲突
     * @param startTime
     * @param endTime
     * @param cityId
     * @return
     */
    @PostMapping(value = "/judgmentTime")
    public ValidatorResultDTO judgmentStartTime(@RequestParam(value="startTime") String startTime,@RequestParam(value="endTime") String endTime,@RequestParam(value="cityId") Long cityId){
        Boolean result = this.macityDeliveryTimeService.judgmentTime(startTime,endTime,cityId);
        return  new ValidatorResultDTO(result);
    }


    /**
     * 保存配送时间
     * @param cityDeliveryTimeVO
     * @param result
     * @return
     */
    @PostMapping
    public ResultDTO<?> saveCityDeliveryTime(CityDeliveryTimeVO cityDeliveryTimeVO, BindingResult result) {
        if (!result.hasErrors()) {
            this.macityDeliveryTimeService.save(cityDeliveryTimeVO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            return actFor400(result,"提交的数据有误");
        }

    }

    /**
     * 更新配送时间
     * @param cityDeliveryTimeVO
     * @param result
     * @return
     */
    @PutMapping
    public ResultDTO<String> updateCityDeliveryTime(CityDeliveryTimeVO cityDeliveryTimeVO, BindingResult result) {
        if (!result.hasErrors()&& macityDeliveryTimeService.judgmentTime(cityDeliveryTimeVO.getStartTime(),cityDeliveryTimeVO.getEndTime(),cityDeliveryTimeVO.getCityId(),cityDeliveryTimeVO.getId())) {
            this.macityDeliveryTimeService.update(cityDeliveryTimeVO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            System.err.print(allErrors);
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }
}
