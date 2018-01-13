package cn.com.leyizhuang.app.web.controller.rest;


import cn.com.leyizhuang.app.core.utils.StringUtils;
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
@RequestMapping(value = MaGoodsCategoryRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaGoodsCategoryRestController extends BaseRestController {

    protected final static String PRE_URL = "/rest/goodsCategorys";

    private final Logger logger = LoggerFactory.getLogger(MaGoodsCategoryRestController.class);

    @Autowired
    private MaGoodsCategoryService maGoodsCategoryService;

    /**
     * 初始化货物分类列表
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<GoodsCategoryVO> getGoodsCategoryList(Integer offset, Integer size, String keywords) {
        logger.info("getGoodsCategoryList 后台初始化货物分类列表 ,入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<GoodsCategoryDO> goodsCategoryPage = this.maGoodsCategoryService.queryPageVO(page, size);
            List<GoodsCategoryDO> pageAllGoodsCategoryList = goodsCategoryPage.getList();
            List<GoodsCategoryVO> pageGoodsCategoryVOList = GoodsCategoryVO.transform(pageAllGoodsCategoryList);
            logger.info("getGoodsCategoryList ,后台初始化货物分类列表成功", pageGoodsCategoryVOList.size());
            return new GridDataVO<GoodsCategoryVO>().transform(pageGoodsCategoryVOList, goodsCategoryPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("getGoodsCategoryList EXCEPTION,发生未知错误，后台初始化货物分类列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 查询商品分类
     *
     * @return
     */
    @GetMapping(value = "/findGoodsCategorySelection")
    public List<SimpleGoodsCategoryParam> findGoodsCategorySelection() {
        logger.info("findGoodsCategorySelection 后台查询商品分类");
        try {
            List<SimpleGoodsCategoryParam> goodsCategoryList = this.maGoodsCategoryService.findGoodsCategorySelection();
            logger.info("findGoodsCategorySelection ,后台查询商品分类成功", goodsCategoryList.size());
            return goodsCategoryList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findGoodsCategorySelection EXCEPTION,发生未知错误，后台查询商品分类失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 查询商品分类详细信息
     *
     * @param pid
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/findGoodsCategoryByPid/{pid}")
    public GridDataVO<GoodsCategoryVO> findGoodsCategorySelection(@PathVariable(value = "pid") Long pid, Integer offset, Integer size, String keywords) {
        logger.info("findGoodsCategorySelection 后台查询商品分类详细信息 ,入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<GoodsCategoryDO> goodsCategoryPage = this.maGoodsCategoryService.findGoodsCategoryByPid(pid, page, size);
            List<GoodsCategoryDO> pageGoodsCategoryList = goodsCategoryPage.getList();
            List<GoodsCategoryVO> pageGoodsCategoryVOList = GoodsCategoryVO.transform(pageGoodsCategoryList);
            logger.info("findGoodsCategorySelection ,后台查询商品分类详细信息成功", pageGoodsCategoryVOList.size());
            return new GridDataVO<GoodsCategoryVO>().transform(pageGoodsCategoryVOList, goodsCategoryPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findGoodsCategorySelection EXCEPTION,发生未知错误，后台查询商品分类详细信息失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 通过父类查询分类信息
     *
     * @param queryCategoryInfo
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/findGoodsCategoryByPcode/{queryCategoryInfo}")
    public GridDataVO<GoodsCategoryVO> findGoodsCategoryByPcode(@PathVariable(value = "queryCategoryInfo") String queryCategoryInfo, Integer offset, Integer size, String keywords) {
        logger.info("findGoodsCategoryByPcode 后台通过父类查询分类信息 ,入参 offset:{},size:{},keywords:{},queryCategoryInfo{}", offset, size, keywords, queryCategoryInfo);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<GoodsCategoryDO> goodsCategoryPage = this.maGoodsCategoryService.findGoodsCategoryByPcode(queryCategoryInfo, page, size);
            List<GoodsCategoryDO> pageGoodsCategoryList = goodsCategoryPage.getList();
            List<GoodsCategoryVO> pageGoodsCategoryVOList = GoodsCategoryVO.transform(pageGoodsCategoryList);
            logger.info("findGoodsCategoryByPcode ,后台通过父类查询分类信息成功", pageGoodsCategoryVOList.size());
            return new GridDataVO<GoodsCategoryVO>().transform(pageGoodsCategoryVOList, goodsCategoryPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findGoodsCategoryByPcode EXCEPTION,发生未知错误，后台通过父类查询分类信息失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 新增分类信息
     *
     * @param goodsCategoryVO
     * @param result
     * @return
     */
    @PostMapping
    public ResultDTO<?> saveGoodsCategory(GoodsCategoryVO goodsCategoryVO, BindingResult result) {
        logger.info("saveGoodsCategory 后台新增分类信息 ,入参 ogoodsCategoryVO:{}", goodsCategoryVO);
        try {
            if (!result.hasErrors()) {
                this.maGoodsCategoryService.save(goodsCategoryVO);
                logger.info("saveGoodsCategory ,后台新增分类信息成功");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } else {
                return actFor400(result, "提交的数据有误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("saveGoodsCategory EXCEPTION,发生未知错误，后台新增分类信息失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 更新分类信息
     *
     * @param goodsCategoryVO
     * @param result
     * @return
     */
    @PutMapping(value = "/{id}")
    public ResultDTO<String> updateGoodsCategory(GoodsCategoryVO goodsCategoryVO, BindingResult result) {
        logger.info("updateGoodsCategory 后台更新分类信息 ,入参 ogoodsCategoryVO:{}", goodsCategoryVO);
        try {
            if (!result.hasErrors()) {
                this.maGoodsCategoryService.update(goodsCategoryVO);
                logger.info("updateGoodsCategory ,后台更新分类信息成功");
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
            logger.warn("updateGoodsCategory EXCEPTION,发生未知错误,后台更新分类信息失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 是否存在分类名称
     *
     * @param categoryName
     * @return
     */
    @PostMapping(value = "/isExistCategoryName")
    public ValidatorResultDTO isExistCategoryName(@RequestParam(value = "categoryName") String categoryName) {
        logger.info("isExistCategoryName 后台判断是否存在分类名称 ,入参 categoryName:{}", categoryName);
        try {
            if (StringUtils.isBlank(categoryName)) {
                logger.warn("页面提交的数据有错误");
                return new ValidatorResultDTO(false);
            }
            Boolean result = this.maGoodsCategoryService.isExistCategoryName(categoryName);
            logger.info("isExistCategoryName ,后台判断是否存在分类名称成功");
            return new ValidatorResultDTO(!result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("isExistCategoryName EXCEPTION,发生未知错误,后台判断是否存在分类名称失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 编辑时是否存在分类名称
     *
     * @param categoryName
     * @param id
     * @return
     */
    @PostMapping(value = "/editIsExistCategoryName")
    public ValidatorResultDTO editIsExistCategoryName(@RequestParam(value = "categoryName") String categoryName, @RequestParam(value = "id") Long id) {
        logger.info("editIsExistCategoryName 后台编辑时判断是否存在分类名称 ,入参 categoryName:{},id:{}", categoryName, id);
        try {
            if (null == id || StringUtils.isBlank(categoryName)) {
                logger.warn("页面提交的数据有错误");
                return new ValidatorResultDTO(false);
            }
            Boolean result = this.maGoodsCategoryService.editIsExistCategoryName(categoryName, id);
            logger.info("editIsExistCategoryName ,后台编辑时判断是否存在分类名称成功");
            return new ValidatorResultDTO(!result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("editIsExistCategoryName EXCEPTION,发生未知错误,后台编辑时判断是否存在分类名称失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 是否存在排序号
     *
     * @param sortId
     * @return
     */
    @PostMapping(value = "/isExistSortId")
    public ValidatorResultDTO isExistSortId(@RequestParam(value = "sortId") Long sortId) {
        logger.info("isExistSortId 后台判断是否存在排序号 ,入参 sortId:{}", sortId);
        try {
            if (null == sortId) {
                logger.warn("页面提交的数据有错误");
                return new ValidatorResultDTO(false);
            }
            Boolean result = this.maGoodsCategoryService.isExistSortId(sortId);
            logger.info("isExistSortId ,后台判断是否存在排序号成功");
            return new ValidatorResultDTO(!result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("isExistSortId EXCEPTION,发生未知错误,后台判断是否存在排序号失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 编辑时是否存在排序号
     *
     * @param sortId
     * @return
     */
    @PostMapping(value = "/editIsExistSortId")
    public ValidatorResultDTO editIsExistSortId(@RequestParam(value = "sortId") Long sortId, @RequestParam(value = "id") Long id) {
        logger.info("editIsExistSortId 后台编辑时是否存在排序号 ,入参 sortId:{},id:{}", sortId, id);
        try {
            if (null == sortId || null == id) {
                logger.warn("页面提交的数据有错误");
                return new ValidatorResultDTO(false);
            }
            Boolean result = this.maGoodsCategoryService.editIsExistSortId(sortId, id);
            logger.info("editIsExistSortId ,后台编辑时是否存在排序号成功");
            return new ValidatorResultDTO(!result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("editIsExistSortId EXCEPTION,发生未知错误,后台编辑时是否存在排序号失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 查询分类列表
     *
     * @return
     */
    @GetMapping(value = "/findEditGoodsCategory")
    public List<GoodsCategoryVO> findEditGoodsCategory() {
        logger.info("findEditGoodsCategory 后台查询分类列表");
        try {
            List<GoodsCategoryVO> goodsCategoryList = this.maGoodsCategoryService.findEditGoodsCategory();
            logger.info("findEditGoodsCategory ,后台查询分类列表成功");
            return goodsCategoryList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findEditGoodsCategory EXCEPTION,发生未知错误,后台查询分类列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 删除商品品牌
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public ResultDTO<?> dataBrandDelete(Long[] ids) {
        logger.info("dataBrandDelete 后台删除商品品牌 ,入参 ids:{}", ids);
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
