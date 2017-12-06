package cn.com.leyizhuang.app.web.controller.rest;


import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsCategoryDO;
import cn.com.leyizhuang.app.foundation.service.MaGoodsCategoryService;
import cn.com.leyizhuang.app.foundation.vo.GoodsCategoryVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.ValidatorResultDTO;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping(value = MaGoodsCategoryRestController.PRE_URL,produces = "application/json;charset=utf-8")
public class MaGoodsCategoryRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/goodsCategorys";

    private final Logger logger = LoggerFactory.getLogger(MaGoodsCategoryRestController.class);

    @Autowired
    private MaGoodsCategoryService maGoodsCategoryService;

    /**
     * 初始化货物分类列表
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<GoodsCategoryVO> getGoodsCategoryList(Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GoodsCategoryDO> goodsCategoryPage = this.maGoodsCategoryService.queryPageVO(page, size);
        List<GoodsCategoryDO> pageAllGoodsCategoryList = goodsCategoryPage.getList();
        List<GoodsCategoryVO> pageGoodsCategoryVOList = GoodsCategoryVO.transform(pageAllGoodsCategoryList);
        return new GridDataVO<GoodsCategoryVO>().transform(pageGoodsCategoryVOList, goodsCategoryPage.getTotal());
    }


    @GetMapping(value = "/findGoodsCategorySelection")
    public  List<GoodsCategoryVO> findGoodsCategorySelection() {
        List<GoodsCategoryVO> goodsCategoryList = this.maGoodsCategoryService.findGoodsCategorySelection();
        return goodsCategoryList;
    }

    @GetMapping(value = "/findGoodsCategoryByPid/{pid}")
    public  GridDataVO<GoodsCategoryVO> findGoodsCategorySelection(@PathVariable(value = "pid") Long pid,Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GoodsCategoryDO> goodsCategoryPage = this.maGoodsCategoryService.findGoodsCategoryByPid(pid,page, size);
        List<GoodsCategoryDO> pageGoodsCategoryList = goodsCategoryPage.getList();
        List<GoodsCategoryVO> pageGoodsCategoryVOList = GoodsCategoryVO.transform(pageGoodsCategoryList);
        return new GridDataVO<GoodsCategoryVO>().transform(pageGoodsCategoryVOList, goodsCategoryPage.getTotal());
    }



    @GetMapping(value = "/findGoodsCategoryByPcode/{queryStoreInfo}")
    public  GridDataVO<GoodsCategoryVO> findGoodsCategoryByPcode(@PathVariable(value = "queryStoreInfo") String queryStoreInfo,Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GoodsCategoryDO> goodsCategoryPage = this.maGoodsCategoryService.findGoodsCategoryByPcode(queryStoreInfo,page, size);
        List<GoodsCategoryDO> pageGoodsCategoryList = goodsCategoryPage.getList();
        List<GoodsCategoryVO> pageGoodsCategoryVOList = GoodsCategoryVO.transform(pageGoodsCategoryList);
        return new GridDataVO<GoodsCategoryVO>().transform(pageGoodsCategoryVOList, goodsCategoryPage.getTotal());
    }

    @PostMapping
    public ResultDTO<?> saveGoodsCategory(GoodsCategoryVO goodsCategoryVO, BindingResult result) {
        if (!result.hasErrors()) {
            this.maGoodsCategoryService.save(goodsCategoryVO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            return actFor400(result,"提交的数据有误");
        }
    }

    @PutMapping(value = "/{id}")
    public ResultDTO<String> updateGoodsCategory(GoodsCategoryVO goodsCategoryVO, BindingResult result) {
        if (!result.hasErrors()) {
            this.maGoodsCategoryService.update(goodsCategoryVO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            System.err.print(allErrors);
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }


    @PostMapping(value = "/isExistCategoryName")
    public ValidatorResultDTO isExistCategoryName(@RequestParam(value="categoryName") String categoryName){
        Boolean result = this.maGoodsCategoryService.isExistCategoryName(categoryName);
        return  new ValidatorResultDTO(!result);
    }


    @GetMapping(value = "/findEditGoodsCategory")
    public  List<GoodsCategoryVO> findEditGoodsCategory() {
        List<GoodsCategoryVO> goodsCategoryList = this.maGoodsCategoryService.findEditGoodsCategory();
        return goodsCategoryList;
    }


}
