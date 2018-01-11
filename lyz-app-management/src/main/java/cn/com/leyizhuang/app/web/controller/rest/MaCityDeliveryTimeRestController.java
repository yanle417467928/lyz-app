package cn.com.leyizhuang.app.web.controller.rest;


import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.management.city.CityDeliveryTime;
import cn.com.leyizhuang.app.foundation.service.MaCityDeliveryTimeService;
import cn.com.leyizhuang.app.foundation.vo.management.city.CityDeliveryTimeVO;
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
@RequestMapping(value = MaCityDeliveryTimeRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCityDeliveryTimeRestController extends BaseRestController {
    protected final static String PRE_URL = "/rest/cityDeliveryTime";

    private final Logger logger = LoggerFactory.getLogger(GoodsRestController.class);
    @Autowired
    private MaCityDeliveryTimeService macityDeliveryTimeService;

    /**
     * 查询当前城市下的配送时间
     *
     * @param
     * @param size
     * @param keywords
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public GridDataVO<CityDeliveryTimeVO> restCityDeliveryTimeGrid(Integer offset, Integer size, String keywords, @PathVariable(value = "id") Long id) {
        logger.info("restCityDeliveryTimeGrid,当前城市下的配送时间页查询, 入参 offset:{},size:{},keywords:{},id:{}", offset, size, keywords, id);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<CityDeliveryTime> cityDeliveryTimePage = this.macityDeliveryTimeService.queryPage(page, size, id);
            List<CityDeliveryTime> cityDeliveryTimeList = cityDeliveryTimePage.getList();
            List<CityDeliveryTimeVO> cityDeliveryTimeVOList = CityDeliveryTime.transform(cityDeliveryTimeList);
            logger.info("restCityDeliveryTimeGrid ,当前城市下的配送时间页查询成功", cityDeliveryTimeVOList.size());
            return new GridDataVO<CityDeliveryTimeVO>().transform(cityDeliveryTimeVOList, cityDeliveryTimePage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restCityDeliveryTimeGrid EXCEPTION,发生未知错误，当前城市下的配送时间页查询失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 判断配送时间是否冲突
     *
     * @param startTime
     * @param endTime
     * @param cityId
     * @return
     */
    @PostMapping(value = "/judgmentTime")
    public ValidatorResultDTO judgmentTime(@RequestParam(value = "startTime") String startTime, @RequestParam(value = "endTime") String endTime, @RequestParam(value = "cityId") Long cityId) {
        logger.info("judgmentTime,判断配送时间与现有时间段是否冲突, 入参 startTime:{},endTime:{},cityId:{}", startTime, endTime, cityId);
        try {
            if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime) || null == cityId) {
                logger.warn("页面提交的数据有错误,无法判断时间段与现有时间段是否有冲突");
                return new ValidatorResultDTO(false);
            }
            Boolean result = this.macityDeliveryTimeService.judgmentTime(startTime, endTime, cityId);
            logger.info("judgmentTime ,判断配送时间与现有时间段是否冲突成功", result);
            return new ValidatorResultDTO(result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("judgmentTime EXCEPTION,发生未知错误，判断配送时间与现有时间段是否冲突失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 保存配送时间
     *
     * @param cityDeliveryTimeVO
     * @param result
     * @return
     */
    @PostMapping
    public ResultDTO<?> saveCityDeliveryTime(CityDeliveryTimeVO cityDeliveryTimeVO, BindingResult result) {
        logger.info("saveCityDeliveryTime,保存配送时间, 入参 cityDeliveryTimeVO:{}", cityDeliveryTimeVO);
        try {
            if (!result.hasErrors()) {
                this.macityDeliveryTimeService.save(cityDeliveryTimeVO);
                logger.info("saveCityDeliveryTime ,保存配送时间成功");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } else {
                return actFor400(result, "提交的数据有误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("saveCityDeliveryTime EXCEPTION,发生未知错误，保存配送时间失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 更新配送时间
     *
     * @param cityDeliveryTimeVO
     * @param result
     * @return
     */
    @PutMapping
    public ResultDTO<String> updateCityDeliveryTime(CityDeliveryTimeVO cityDeliveryTimeVO, BindingResult result) {
        logger.info("updateCityDeliveryTime,更新配送时间, 入参 cityDeliveryTimeVO:{}", cityDeliveryTimeVO);
        try {
            if (!result.hasErrors() && macityDeliveryTimeService.judgmentTime(cityDeliveryTimeVO.getStartTime(), cityDeliveryTimeVO.getEndTime(), cityDeliveryTimeVO.getCityId(), cityDeliveryTimeVO.getId())) {
                this.macityDeliveryTimeService.update(cityDeliveryTimeVO);
                logger.info("updateCityDeliveryTime ,更新配送时间成功");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } else {
                List<ObjectError> allErrors = result.getAllErrors();
                logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
                System.err.print(allErrors);
                return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                        errorMsgToHtml(allErrors), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("updateCityDeliveryTime EXCEPTION,发生未知错误，更新配送时间失败");
            logger.warn("{}", e);
            return null;
        }
    }
}
