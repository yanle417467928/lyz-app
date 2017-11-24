package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.dto.GoodsDTO;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.app.foundation.vo.GoodsVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    private GoodsService goodsService;

    /**
     * @title 商品信息分页查询
     * @descripe
     * @param
     * @return
     * @throws
     * @title 商品信息分页查询
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/8
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<GoodsVO> restGoodsPageGird(Integer offset, Integer size, String keywords){
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GoodsDO> goodsDOPage = this.goodsService.queryPage(page, size);
        List<GoodsDO> goodsDOList = goodsDOPage.getList();
        List<GoodsVO> goodsVOList = GoodsVO.transform(goodsDOList);
        return new GridDataVO<GoodsVO>().transform(goodsVOList,goodsDOPage.getTotal());
    }

    /**
     * @title   根据ID查询商品信息
     * @descripe
     * @param id
     * @return
     * @throws
     * @title 根据ID查询商品信息
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/9
     */
    @GetMapping(value = "/{id}")
    public ResultDTO<GoodsVO> restGoodsIdGet(@PathVariable(value = "id") Long id) {
        GoodsDO goodsDO = this.goodsService.queryById(id);
        if (null == goodsDO) {
            logger.warn("查找角色失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            GoodsVO goodsVO = GoodsVO.transform(goodsDO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, goodsVO);
        }
    }

    /**
     * @title   根据ID删除商品
     * @descripe
     * @param ids
     * @return
     * @throws
     * @title 根据ID删除商品
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/9
     */
    @DeleteMapping
    public ResultDTO<?> restMenuDelete(Long[] ids) {
        this.goodsService.batchRemove(Arrays.asList(ids));
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "资源已成功删除", null);
    }

    /**
     * @title   编辑商品信息
     * @descripe
     * @param goodsDTO
     * @return
     * @throws
     * @title 编辑商品信息
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/9
     */
    @PutMapping(value = "/{id}")
    public ResultDTO<String> restEmployeeIdPost(@Valid GoodsDTO goodsDTO, BindingResult result) {
        if (!result.hasErrors()) {
            this.goodsService.managerSaveGoods(goodsDTO);
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
