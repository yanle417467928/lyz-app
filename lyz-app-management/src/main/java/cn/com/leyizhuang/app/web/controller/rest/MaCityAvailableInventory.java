package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.MaCityAvailableItyService;
import cn.com.leyizhuang.app.foundation.vo.management.city.CityInventoryVO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2017/12/28.
 * Time: 13:41.
 */
@RestController
@RequestMapping(value = MaCityAvailableInventory.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCityAvailableInventory extends BaseRestController {

    protected final static String PRE_URL = "/rest/cityInventory";

    private final Logger logger = LoggerFactory.getLogger(MaCityAvailableInventory.class);

    @Autowired
    private MaCityAvailableItyService maCityAvailableItyService;

    /**
     * 城市库存可用量分页查询
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<CityInventoryVO> restCityInventoryPageGird(Integer offset, Integer size, String keywords) {

        logger.info("restCityInventoryPageGird CREATE,城市库存可用量分页查询, 入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<CityInventoryVO> cityPage = this.maCityAvailableItyService.queryPageVO(page, size, keywords);
        List<CityInventoryVO> citysList = cityPage.getList();
        return new GridDataVO<CityInventoryVO>().transform(citysList, cityPage.getTotal());

    }
}
