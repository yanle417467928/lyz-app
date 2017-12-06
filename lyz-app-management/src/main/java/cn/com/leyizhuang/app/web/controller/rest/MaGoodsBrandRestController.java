package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsBrand;
import cn.com.leyizhuang.app.foundation.service.MaGoodsBrandService;
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
@RequestMapping(value = MaGoodsBrandRestController.PRE_URL,produces = "application/json;charset=utf-8")
public class MaGoodsBrandRestController extends  BaseRestController {

    protected final static String PRE_URL = "/rest/goodsBrand";

    private final Logger logger = LoggerFactory.getLogger(MaGoodsBrandRestController.class);

    @Autowired
    private MaGoodsBrandService maGoodsBrandService;

    /**
     * 查询所有商品品牌
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<GoodsBrand> getGoodsBrandList(Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GoodsBrand> goodsBrandPage = this.maGoodsBrandService.queryPageVO(page, size);
        List<GoodsBrand> pageAllGoodsBrandList = goodsBrandPage.getList();
        return new GridDataVO<GoodsBrand>().transform(pageAllGoodsBrandList, goodsBrandPage.getTotal());
    }

    /**
     * 根据品牌id查询商品品牌
     * @param brdId
     * @return
     */
    @GetMapping(value = "/{brdId}")
    public ResultDTO<GoodsBrand> restGoodsBrandGet(@PathVariable(value = "brdId") Long brdId) {
        GoodsBrand goodsBrand = this.maGoodsBrandService.queryGoodsBrandVOById(brdId);
        if (null == goodsBrand) {
            logger.warn("查找品牌失败：Role(id = {}) == null", brdId);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,null, goodsBrand);
        }
    }

    /**
     * 根据查询信息查询品牌
     * @param queryStoreInfo
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/findBrandByName/{queryStoreInfo}")
    public  GridDataVO<GoodsBrand> findBrandByName(@PathVariable(value = "queryStoreInfo") String queryStoreInfo,Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GoodsBrand> goodsBrandPage = this.maGoodsBrandService.findGoodsBrandByName(queryStoreInfo,page, size);
        List<GoodsBrand> pageAllGoodsBrandList = goodsBrandPage.getList();
        return new GridDataVO<GoodsBrand>().transform(pageAllGoodsBrandList, goodsBrandPage.getTotal());
    }


    /**
     * 判断是否有该品牌
     * @param brandName
     * @return
     */
    @PostMapping(value = "/isExistBrandName")
    public ValidatorResultDTO isExistBrandName(@RequestParam(value="brandName") String brandName){
        Boolean result = this.maGoodsBrandService.isExistBrandName(brandName);
        return  new ValidatorResultDTO(!result);
    }

    /**
     * 新增商品品牌
     * @param goodsBrand
     * @param result
     * @return
     */
    @PostMapping
    public ResultDTO<?> saveGoodsBrand(GoodsBrand goodsBrand, BindingResult result) {
        if (!result.hasErrors()) {
            this.maGoodsBrandService.save(goodsBrand);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            return actFor400(result,"提交的数据有误");
        }

    }

    /**
     * 更新商品品牌
     *
     * @param goodsBrand
     * @param result
     * @return
     */
    @PutMapping(value = "/{id}")
    public ResultDTO<String> updateGoodsBrand(GoodsBrand goodsBrand, BindingResult result) {
        if (!result.hasErrors()) {
            this.maGoodsBrandService.update(goodsBrand);
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
     * 下拉框获取所有品牌
     * @return
     */
    @GetMapping(value = "/page/brandGrid")
    public List<GoodsBrand> queryGoodsBrandList() {
        List<GoodsBrand> pageAllGoodsBrandList = this.maGoodsBrandService.queryGoodsBrandList();
        return pageAllGoodsBrandList;
    }

}
