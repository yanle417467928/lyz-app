package cn.com.leyizhuang.app.web.controller.rest;


import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsCategoryDO;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.SimpleGoodsCategoryParam;
import cn.com.leyizhuang.app.foundation.service.MaGoodsCategoryService;
import cn.com.leyizhuang.app.foundation.vo.management.goodscategory.GoodsCategoryVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.exception.data.InvalidDataException;
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

    /**
     * 查询商品分类
     * @return
     */
    @GetMapping(value = "/findGoodsCategorySelection")
    public  List<SimpleGoodsCategoryParam> findGoodsCategorySelection() {
        List<SimpleGoodsCategoryParam> goodsCategoryList = this.maGoodsCategoryService.findGoodsCategorySelection();
        return goodsCategoryList;
    }

    /**
     *查询商品分类详细信息
     * @param pid
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/findGoodsCategoryByPid/{pid}")
    public  GridDataVO<GoodsCategoryVO> findGoodsCategorySelection(@PathVariable(value = "pid") Long pid,Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GoodsCategoryDO> goodsCategoryPage = this.maGoodsCategoryService.findGoodsCategoryByPid(pid,page, size);
        List<GoodsCategoryDO> pageGoodsCategoryList = goodsCategoryPage.getList();
        List<GoodsCategoryVO> pageGoodsCategoryVOList = GoodsCategoryVO.transform(pageGoodsCategoryList);
        return new GridDataVO<GoodsCategoryVO>().transform(pageGoodsCategoryVOList, goodsCategoryPage.getTotal());
    }


    /**
     *通过父类查询分类信息
     * @param queryCategoryInfo
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/findGoodsCategoryByPcode/{queryCategoryInfo}")
    public  GridDataVO<GoodsCategoryVO> findGoodsCategoryByPcode(@PathVariable(value = "queryCategoryInfo") String queryCategoryInfo,Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GoodsCategoryDO> goodsCategoryPage = this.maGoodsCategoryService.findGoodsCategoryByPcode(queryCategoryInfo,page, size);
        List<GoodsCategoryDO> pageGoodsCategoryList = goodsCategoryPage.getList();
        List<GoodsCategoryVO> pageGoodsCategoryVOList = GoodsCategoryVO.transform(pageGoodsCategoryList);
        return new GridDataVO<GoodsCategoryVO>().transform(pageGoodsCategoryVOList, goodsCategoryPage.getTotal());
    }

    /**
     *新增分类信息
     * @param goodsCategoryVO
     * @param result
     * @return
     */
    @PostMapping
    public ResultDTO<?> saveGoodsCategory(GoodsCategoryVO goodsCategoryVO, BindingResult result) {
        if (!result.hasErrors()) {
            this.maGoodsCategoryService.save(goodsCategoryVO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            return actFor400(result,"提交的数据有误");
        }
    }

    /**
     *更新分类信息
     * @param goodsCategoryVO
     * @param result
     * @return
     */
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

    /**
     *是否存在分类名称
     * @param categoryName
     * @return
     */
    @PostMapping(value = "/isExistCategoryName")
    public ValidatorResultDTO isExistCategoryName(@RequestParam(value="categoryName") String categoryName){
        if(null==categoryName||"".equals(categoryName)){
            logger.warn("页面提交的数据有错误");
            return new ValidatorResultDTO(false);
        }
        Boolean result = this.maGoodsCategoryService.isExistCategoryName(categoryName);
        return  new ValidatorResultDTO(!result);
    }


    /**
     *编辑时是否存在分类名称
     * @param categoryName
     * @param id
     * @return
     */
    @PostMapping(value = "/editIsExistCategoryName")
    public ValidatorResultDTO editIsExistCategoryName(@RequestParam(value="categoryName") String categoryName,@RequestParam(value="id") Long id){
        if(null==categoryName||null==id||"".equals(categoryName)){
            logger.warn("页面提交的数据有错误");
            return new ValidatorResultDTO(false);
        }
        Boolean result = this.maGoodsCategoryService.editIsExistCategoryName(categoryName,id);
        return  new ValidatorResultDTO(!result);
    }

    /**
     *是否存在排序号
     * @param sortId
     * @return
     */
    @PostMapping(value = "/isExistSortId")
    public ValidatorResultDTO isExistSortId(@RequestParam(value="sortId") Long sortId){
        if(null==sortId){
            logger.warn("页面提交的数据有错误");
            return new ValidatorResultDTO(false);
        }
        Boolean result = this.maGoodsCategoryService.isExistSortId(sortId);
        return  new ValidatorResultDTO(!result);
    }


    /**
     *编辑时是否存在排序号
     * @param sortId
     * @return
     */
    @PostMapping(value = "/editIsExistSortId")
    public ValidatorResultDTO editIsExistSortId(@RequestParam(value="sortId") Long sortId,@RequestParam(value="id") Long id){
        if(null==sortId||null==id){
            logger.warn("页面提交的数据有错误");
            return new ValidatorResultDTO(false);
        }
        Boolean result = this.maGoodsCategoryService.editIsExistSortId(sortId,id);
        return  new ValidatorResultDTO(!result);
    }

    /**
     * 查询分类列表
     * @return
     */
    @GetMapping(value = "/findEditGoodsCategory")
    public  List<GoodsCategoryVO> findEditGoodsCategory() {
        List<GoodsCategoryVO> goodsCategoryList = this.maGoodsCategoryService.findEditGoodsCategory();
        return goodsCategoryList;
    }

    /**
     * 删除商品品牌
     * @param ids
     * @return
     */
    @DeleteMapping
    public ResultDTO<?> dataBrandDelete(Long[] ids) {
        try {
            for (Long id : ids) {
                this.maGoodsCategoryService.delete(id);
            }
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "用户已成功删除", null);
        } catch (InvalidDataException e) {
            logger.error("批量删除资源数据发生错误");
            logger.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    "加载需删除的数据失败，请稍后重试或联系管理员", null);
        } catch (Exception e) {
            logger.error("批量删除资源数据发生错误");
            logger.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "出现未知错误，请稍后重试或联系管理员", null);
        }
    }


}
