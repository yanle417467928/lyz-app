package cn.com.leyizhuang.app.web.controller.rest;


import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.MaCityService;
import cn.com.leyizhuang.app.foundation.vo.CityVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = MaCityRestController.PRE_URL,produces = "application/json;charset=utf-8")
public class MaCityRestController extends BaseRestController {
    protected final static String PRE_URL = "/rest/citys";

    private final Logger logger = LoggerFactory.getLogger(MaCityRestController.class);

    @Autowired
    private MaCityService maCityService;

    /**城市信息分页查询
     * @descripe
     * @param
     * @return
     * @throws
     * @author
     * @date 2017/11/3
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<CityVO> restCitysPageGird(Integer offset, Integer size, String keywords){
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<CityVO> cityPage = this.maCityService.queryPageVO(page,size);
        List<CityVO> citysList = cityPage.getList();
        return new GridDataVO<CityVO>().transform(citysList,cityPage.getTotal());

    }

    /**
     * @title   根据ID查询城市信息
     * @descripe
     * @param cityId
     * @return
     * @throws
     * @author
     * @date 2017/11/3
     */
    @GetMapping(value = "/{cityId}")
    public ResultDTO<CityVO> restCityIdGet(@PathVariable(value = "cityId") Long cityId) {
        CityVO cityVO = this.maCityService.queryCityVOById(cityId);
        if (null == cityVO) {
            logger.warn("查找城市失败：Role(id = {}) == null", cityId);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,null, cityVO);
        }
    }


    /**
     * @title   查询城市列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author
     * @date 2017/11/3
     */
    @GetMapping(value = "/findCitylist")
    public List<CityVO> findCitysList(){
        List<CityVO> citysList =  this.maCityService.findCitysList();
        return  citysList ;
    }

}
