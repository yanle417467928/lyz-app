package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.Role;
import cn.com.leyizhuang.app.foundation.pojo.vo.GoodsVO;
import cn.com.leyizhuang.app.foundation.pojo.vo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/6
 */
@RestController
@RequestMapping(value = GoodsRestController.PRE_URL,produces = "application/json;charset=utf-8")
public class GoodsRestController extends BaseRestController {
    protected final static String PRE_URL = "/rest/goods";

    private final Logger logger = LoggerFactory.getLogger(GoodsRestController.class);

    @Autowired
    private GoodsService goodsServiceImpl;

    @GetMapping(value = "/page/grid")
    public GridDataVO<GoodsVO> restGoodsPageGird(Integer offset, Integer size, String keywords){
        size = getSize(size);
        Integer page = getPage(offset, size);

        PageInfo<GoodsDO> goodsDOPage = this.goodsServiceImpl.queryPage(page,size);
        List<GoodsDO> goodsDOList = goodsDOPage.getList();
        List<GoodsVO> goodsVOList = GoodsVO.transform(goodsDOList);
        return new GridDataVO<GoodsVO>().transform(goodsVOList,goodsDOPage.getTotal());
    }

    @GetMapping(value = "/{id}")
    public ResultDTO<GoodsVO> restGoodsIdGet(@PathVariable(value = "id") Long id) {
        GoodsDO goodsDO = this.goodsServiceImpl.queryById(id);
        if (null == goodsDO) {
            logger.warn("查找角色失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            GoodsVO goodsVO = GoodsVO.transform(goodsDO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,null, goodsVO);
        }
    }

    @DeleteMapping
    public ResultDTO<?> restMenuDelete(Long[] ids) {
        this.goodsServiceImpl.batchRemove(Arrays.asList(ids));
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "资源已成功删除", null);
    }

}
