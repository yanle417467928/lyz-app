package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.goods.PhysicalClassify;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.app.foundation.service.MaPhysicalClassifyService;
import cn.com.leyizhuang.app.foundation.vo.MaGoodsVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/6
 */
@RestController
@RequestMapping(value = GoodsRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class GoodsRestController extends BaseRestController {
    protected final static String PRE_URL = "/rest/goods";

    private final Logger logger = LoggerFactory.getLogger(GoodsRestController.class);

    @Autowired
    private GoodsService goodsService;


    @Autowired
    private MaPhysicalClassifyService physicalClassifyService;

    /**
     * @param
     * @return
     * @throws
     * @title 商品信息分页查询
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/8
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<MaGoodsVO> restGoodsPageGird(Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GoodsDO> goodsDOPage = this.goodsService.queryPage(page, size);
        List<GoodsDO> goodsDOList = goodsDOPage.getList();
        List<MaGoodsVO> goodsVOList = MaGoodsVO.transform(goodsDOList);
        return new GridDataVO<MaGoodsVO>().transform(goodsVOList, goodsDOPage.getTotal());
    }

    /**
     * @param id
     * @return
     * @throws
     * @title 根据ID查询商品信息
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/9
     */
    @GetMapping(value = "/{id}")
    public ResultDTO<MaGoodsVO> restGoodsIdGet(@PathVariable(value = "id") Long id) {
        GoodsDO goodsDO = this.goodsService.queryById(id);
        if (null == goodsDO) {
            logger.warn("查找角色失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            MaGoodsVO goodsVO = MaGoodsVO.transform(goodsDO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, goodsVO);
        }
    }
    /**
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
     * @throws
     * @title 编辑商品信息
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/9
     */

    @PutMapping(value = "update")
    public ResultDTO<Object> restGoodsVOPost(MaGoodsVO goodsVO, BindingResult result,MultipartFile file) {
        if (!result.hasErrors()) {
            this.goodsService.updateGoods(goodsVO);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }

    /**
     * 更新商品图片信息
     * @param file
     * @return
     */
    @PostMapping(value = "updateImg")
    public ResultDTO<Object> updateImg(MultipartFile file) {
            String picUrl = "http://img1.leyizhuang.com.cn/app/images/goods/2507/20170303114334899.jpg"; /*FileUploadOSSUtils.uploadProfilePhoto(file, "/profile/photo/");*/
            if (null != picUrl || "".equals(picUrl)) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, picUrl);
            } else {
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    "图片上传失败", null);
        }
    }


    @PostMapping(value = "updateDetial")
    public ResultDTO<Object> updateDetial(MultipartFile file) {
        String picUrl = "http://img1.leyizhuang.com.cn/app/images/goods/2507/20170303114334899.jpg"; /*FileUploadOSSUtils.uploadProfilePhoto(file, "/profile/photo/");*/
        if (null != picUrl || "".equals(picUrl)) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, picUrl);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    "图片上传失败", null);
        }
    }

    /**
     * 根据搜索查询商品信息
     * @param offset
     * @param size
     * @param keywords
     * @param queryGoodsInfo
     * @return
     */
    @GetMapping(value = "/page/goodsGrid/{queryGoodsInfo}")
    public GridDataVO<MaGoodsVO> restGoodsPageByInfo(Integer offset, Integer size, String keywords, @PathVariable(value = "queryGoodsInfo") String queryGoodsInfo) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GoodsDO> goodsDOPage = this.goodsService.queryGoodsPageByInfo(page, size, queryGoodsInfo);
        List<GoodsDO> goodsDOList = goodsDOPage.getList();
        List<MaGoodsVO> goodsVOList = MaGoodsVO.transform(goodsDOList);
        return new GridDataVO<MaGoodsVO>().transform(goodsVOList, goodsDOPage.getTotal());
    }

    /**
     * 根据筛选条件查询商品信息
     * @param offset
     * @param size
     * @param keywords
     * @param brandCode
     * @param categoryCode
     * @param companyCode
     * @return
     */
    @GetMapping(value = "/page/screenGoodsGrid")
    public GridDataVO<MaGoodsVO> screenGoodsGrid(Integer offset, Integer size, String keywords, @RequestParam(value = "brandCode") Long brandCode, @RequestParam(value = "categoryCode") String categoryCode, @RequestParam(value = "companyCode") String companyCode) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GoodsDO> goodsDOPage = this.goodsService.screenGoodsGrid(page, size, brandCode, categoryCode, companyCode);
        List<GoodsDO> goodsDOList = goodsDOPage.getList();
        List<MaGoodsVO> goodsVOList = MaGoodsVO.transform(goodsDOList);
        return new GridDataVO<MaGoodsVO>().transform(goodsVOList, goodsDOPage.getTotal());
    }

    /**
     * 查询物理分类信息
     * @return
     */
    @GetMapping(value = "/page/physicalClassifyGrid")
    public List<PhysicalClassify> findPhysicalClassify() {
        List<PhysicalClassify> physicalClassifyList = this.physicalClassifyService.findPhysicalClassifyList();
        return physicalClassifyList;
    }

}
