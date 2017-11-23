package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.vo.StoreVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = MaStoreRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaStoreRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/stores";

    private final Logger logger = LoggerFactory.getLogger(MaStoreRestController.class);

    @Autowired
    private MaStoreService MaStoreService;

    /**
     * 初始门店页面
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<StoreVO> restStoresPageGird(Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<StoreVO> storePage = this.MaStoreService.queryPageVO(page, size);
        List<StoreVO> PageAllStoresList = storePage.getList();
        return new GridDataVO<StoreVO>().transform(PageAllStoresList, storePage.getTotal());
    }

    /**
     * 查询门店列表
     *
     * @return
     */
    @GetMapping(value = "/findStorelist")
    public List<StoreVO> findStoresList() {
        List<StoreVO> AllStoresList = this.MaStoreService.findStoreList();
        return AllStoresList;
    }

    /**
     * 查询该城市ID的门店列表
     *
     * @param cityId
     * @return
     */
    @GetMapping(value = "/findStoresListByCityId/{cityId}")
    public List<StoreVO> findStoresListByCityId(@PathVariable(value = "cityId") Long cityId) {
        List<StoreVO> storesList = this.MaStoreService.findStoresListByCityId(cityId);
        return storesList;
    }

    /**
     * 查询门店信息
     *
     * @param storeId
     * @return
     */
    @GetMapping(value = "/{storeId}")
    public ResultDTO<StoreVO> restStoreIdGet(@PathVariable(value = "storeId") Long storeId) {
        StoreVO storeVO = this.MaStoreService.queryStoreVOById(storeId);
        if (null == storeVO) {
            logger.warn("查找门店失败：Role(id = {}) == null", storeId);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, storeVO);
        }
    }


    /**
     * 查询该城市下的门店
     *
     * @param cityId
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/findStoresListByCity/{cityId}")
    public GridDataVO<StoreVO> findStoresListByCity(@PathVariable(value = "cityId") Long cityId, Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<StoreVO> storePage = this.MaStoreService.queryStoreListByCityId(page, size, cityId);
        List<StoreVO> PageAllStoresList = storePage.getList();
        return new GridDataVO<StoreVO>().transform(PageAllStoresList, storePage.getTotal());
    }

    /**
     * 查询可用或不可用的门店
     *
     * @param enabled
     * @param cityId
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/findStoresListByEnable")
    public GridDataVO<StoreVO> findStoresListByEnable(@RequestParam("enabled") Boolean enabled, @RequestParam("cityId") Long cityId, Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<StoreVO> storePage = this.MaStoreService.findStoresListByEnable(page, size, enabled, cityId);
        List<StoreVO> PageAllStoresList = storePage.getList();
        return new GridDataVO<StoreVO>().transform(PageAllStoresList, storePage.getTotal());
    }


    /**
     * 通过门店名称或者门店编码查询门店
     *
     * @param queryStoreInfo
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/storeGrid/{queryStoreInfo}")
    public GridDataVO<StoreVO> findStoresListByStoreInfo(@PathVariable(value = "queryStoreInfo") String queryStoreInfo, Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<StoreVO> storePage = this.MaStoreService.findStoresListByStoreInfo(page, size, queryStoreInfo);
        List<StoreVO> PageAllStoresList = storePage.getList();
        return new GridDataVO<StoreVO>().transform(PageAllStoresList, storePage.getTotal());
    }

}
