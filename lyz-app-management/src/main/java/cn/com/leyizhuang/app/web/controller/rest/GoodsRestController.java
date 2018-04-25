package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.oss.FileUploadOSSUtils;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActGoodsMappingDO;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.PhysicalClassify;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.response.CustomerRankInfoResponse;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.app.foundation.service.MaPhysicalClassifyService;
import cn.com.leyizhuang.app.foundation.vo.management.goods.MaGoodsVO;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaActGoodsMapping;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderGoodsSimpleResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.PromotionsListResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.management.MaBuyProductCouponGoodsResponse;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.ValidatorResultDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

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
    private AppEmployeeService appEmployeeService;

    @Autowired
    private GoodsPriceService goodsPriceService;

    @Autowired
    private AppActService appActService;

    @Autowired
    private MaPhysicalClassifyService physicalClassifyService;

    @Autowired
    private AppCustomerService appCustomerService;
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
     * 根据门店 和各种参数查询分页数据 门店调拨时使用
     * @param offset
     * @param size
     * @param keywords
     * @param brandCode
     * @param categoryCode
     * @param companyCode
     * @param storeId
     * @return
     */
    @GetMapping(value = "/page/grid/param")
    public GridDataVO<MaGoodsVO> restGoodsPageGird(Integer offset, Integer size, String keywords,Long brandCode,String categoryCode,
                                                   String companyCode,Long storeId) {
        if (keywords != null && keywords.equals("")){
            keywords = null;
        }

        if (brandCode != null && brandCode.equals(-1L)){
            brandCode = null;
        }

        if (categoryCode != null && categoryCode.equals("-1")){
            categoryCode = null;
        }

        if (companyCode != null && companyCode.equals("-1")){
            companyCode = null;
        }

        if (storeId == null){
            return  null;
        }


        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GoodsDO> goodsDOPage = this.goodsService.getGoodsBykeywordsAndCompanyAndBrandCodeAndCategoryCodeAndStoreId(page,size,keywords,companyCode,brandCode,categoryCode,storeId);
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
    public ResultDTO<Object> restGoodsVOPost(MaGoodsVO goodsVO, BindingResult result) {
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
     *
     * @param file
     * @return
     */
    @PostMapping(value = "updateImg")
    public ResultDTO<Object> updateImg(MultipartFile file) {
        String picUrl = null;
        if (!file.isEmpty()) {
            picUrl = FileUploadOSSUtils.uploadProfilePhoto(file, "goods/");
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    "图片上传失败", null);
        }
            /*FileUploadOSSUtils.uploadProfilePhoto(file, "profile/photo/");*/
        if (null != picUrl || "".equals(picUrl)) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, picUrl);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    "图片上传失败", null);
        }
    }


    /**
     * 更新商品详情页
     *
     * @param imgFile
     * @return
     */
    @PostMapping(value = "updateGoodsDetial")
    public Map<String, Object> updateGoodsDetial(MultipartFile imgFile) {
        String url = null;
        Map<String, Object> res = new HashMap<String, Object>();
        if (!imgFile.isEmpty()) {
            url = FileUploadOSSUtils.uploadProfilePhoto(imgFile, "goods/");
        } else {
            res.put("msg", "图片不存在");
            return res;
        }
            /*;*/
        if (null != url || "".equals(url)) {
            res.put("error", 0);
            res.put("msg", "上传文件成功！");
            res.put("url", url);
            return res;
        } else {
            res.put("msg", "上传文件失败！");
            return res;
        }
    }

    @PostMapping(value = "/uploadQrcode")
    public ResultDTO<Object> uploadQrcode(MultipartFile file) {
        String picUrl = null;
        if (!file.isEmpty()) {
            picUrl = FileUploadOSSUtils.uploadProfilePhoto(file, "seller/qrcode/");
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    "图片上传失败", null);
        }
            /*FileUploadOSSUtils.uploadProfilePhoto(file, "profile/photo/");*/
        if (null != picUrl || "".equals(picUrl)) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, picUrl);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    "图片上传失败", null);
        }
    }
/*

    @PostMapping(value = "updateDetial")
    public ResultDTO<Object> updateDetial(MultipartFile file) {
        String picUrl = "http://img1.leyizhuang.com.cn/app/images/goods/2507/20170303114334899.jpg"; */
/*FileUploadOSSUtils.uploadProfilePhoto(file, "profile/photo/");*//*

        if (null != picUrl || "".equals(picUrl)) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, picUrl);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    "图片上传失败", null);
        }
    }
*/

    /**
     * 根据搜索查询商品信息
     *
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
     *
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
     * 后台买券专用
     * @param offset
     * @param size
     * @param keywords
     * @param brandCode
     * @param categoryCode
     * @param companyCode
     * @param productType
     * @return
     */
    @GetMapping(value = "/page/screenGoodsGrid/buy/coupon")
    public GridDataVO<MaBuyProductCouponGoodsResponse> screenGoodsGridBuyCoupon(Integer offset, Integer size, String keywords,
                                                          @RequestParam(value = "brandCode") Long brandCode,
                                                          @RequestParam(value = "categoryCode") String categoryCode,
                                                          @RequestParam(value = "companyCode") String companyCode,
                                                          @RequestParam(value = "productType") String productType,
                                                          @RequestParam(value = "storeId") Long storeId,
                                                                                Long cusId,Long sellerId) {
        // 检验顾客专供类型
        CustomerRankInfoResponse customerRankInfoResponse = appCustomerService.findCusRankinfoByCusId(cusId);
        String priceType = null;

        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<MaBuyProductCouponGoodsResponse> goodsDOPage = null;

        if (customerRankInfoResponse == null){
            //非专供会员

            if (productType.equals("zg")){
                goodsDOPage = null;
            }else {
                goodsDOPage = this.goodsService.screenGoodsGrid(page, size, brandCode, categoryCode, companyCode, "common",storeId);
            }
        }else {
            // 校验 门店 导购信息是否准确
            AppCustomer appCustomer = null;
            try {
                appCustomer = appCustomerService.findById(cusId);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                logger.info("购买产品券失败，到不到id："+cusId+"顾客找不到");
            }

            Long cusStoreId = appCustomer.getStoreId();
            Long cusSellerId = appCustomer.getSalesConsultId();

            if (cusStoreId.equals(storeId) && cusSellerId.equals(sellerId)){
                if (productType.equals("zg")){
                    priceType = customerRankInfoResponse.getRankCode();
                }else {
                    priceType = productType;
                }

                goodsDOPage = this.goodsService.screenGoodsGrid(page, size, brandCode, categoryCode, companyCode, priceType,storeId);
            }else{
                if (productType.equals("zg")){
                    goodsDOPage = null;
                }else {
                    goodsDOPage = this.goodsService.screenGoodsGrid(page, size, brandCode, categoryCode, companyCode, "common",storeId);
                }
            }
        }

        List<MaBuyProductCouponGoodsResponse> goodsResponseList = goodsDOPage.getList();
        logger.warn("restStoreGoodsPageGird ,门店商品信息分页查询成功", goodsResponseList.size());
        return new GridDataVO<MaBuyProductCouponGoodsResponse>().transform(goodsResponseList, goodsDOPage.getTotal());
    }

    /**
     * 查询物理分类信息
     *
     * @return
     */
    @GetMapping(value = "/page/physicalClassifyGrid")
    public List<PhysicalClassify> findPhysicalClassify() {
        List<PhysicalClassify> physicalClassifyList = this.physicalClassifyService.findPhysicalClassifyList();
        return physicalClassifyList;
    }


    /**
     * 修改商品检验电商名称是否存在
     *
     * @param skuName
     * @param id
     * @return
     */
    @PostMapping(value = "/isExistSkuName")
    public ValidatorResultDTO isExistSkuName(@RequestParam(value = "skuName") String skuName, @RequestParam(value = "id") Long id) {
        if (StringUtils.isBlank(skuName) || null == id) {
            logger.warn("页面提交的数据有错误");
            return new ValidatorResultDTO(false);
        }
        Boolean result = this.goodsService.isExistSkuName(skuName, id);
        return new ValidatorResultDTO(!result);
    }

    /**
     * 修改商品检验排序号是否存在
     *
     * @param sortId
     * @param id
     * @return
     */
    @PostMapping(value = "/isExistSortId")
    public ValidatorResultDTO isExistSortId(@RequestParam(value = "sortId") Long sortId, @RequestParam(value = "id") Long id) {
        if (null == sortId || null == id) {
            logger.warn("页面提交的数据有错误");
            return new ValidatorResultDTO(false);
        }
        Boolean result = this.goodsService.isExistSortId(sortId, id);
        return new ValidatorResultDTO(!result);
    }

    /**
     * @param
     * @return
     * @throws
     * @title 门店商品信息分页查询
     * @descripe
     * @author GenerationRoad
     * @date 2017/9/8
     */
    @GetMapping(value = "/page/grid/{storeId}")
    public GridDataVO<MaBuyProductCouponGoodsResponse> restStoreGoodsPageGird(Integer offset, Integer size, String keywords,@PathVariable(value = "storeId") Long storeId) {
        logger.info("restStoreGoodsPageGird 门店商品信息分页查询,入参 offset:{},size:{},keywords:{},storeId:{}", offset, size, keywords, storeId);
        //获取用户登录名
        String userName = this.getShiroUser().getLoginName();
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageHelper.startPage(page, size);
        List<MaBuyProductCouponGoodsResponse> goodsResponses = goodsService.findMaStoreGoodsByStoreId(storeId);
        PageInfo<MaBuyProductCouponGoodsResponse> goodsResponseListPageInfo = new PageInfo<>(goodsResponses);
        List<MaBuyProductCouponGoodsResponse> goodsResponseList = goodsResponseListPageInfo.getList();
        logger.warn("restStoreGoodsPageGird ,门店商品信息分页查询成功", goodsResponseList.size());
        return new GridDataVO<MaBuyProductCouponGoodsResponse>().transform(goodsResponseList, goodsResponseListPageInfo.getTotal());
    }

    /**
     * 后台买券专用
     * @param offset
     * @param size
     * @param keywords
     * @param storeId
     * @return
     */
    @GetMapping(value = "/page/grid/buy/coupon")
    public GridDataVO<MaBuyProductCouponGoodsResponse> restStoreBuyCouponGoodsPageGird(Integer offset, Integer size,
                                                                                       Long storeId, Long cusId, Long sellerId,
                                                                                       String keywords) {
        logger.info("restStoreGoodsPageGird 门店商品信息分页查询,入参 offset:{},size:{},keywords:{},storeId:{},cusId:{},sellerId:{}",
                offset, size, storeId,cusId,sellerId);

        if (StringUtils.isBlank(keywords)){
            keywords = null;
        }

        //判断会员身份
        CustomerRankInfoResponse customerRankInfoResponse = appCustomerService.findCusRankinfoByCusId(cusId);

        //获取用户登录名
        String userName = this.getShiroUser().getLoginName();
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageHelper.startPage(page, size);
        List<MaBuyProductCouponGoodsResponse> goodsResponses = null;

        if (customerRankInfoResponse == null){
            // 非专供会员 只能找到门店下非专供产品
            goodsResponses = goodsService.findGoodsForBuyCoupon(storeId,cusId,sellerId,keywords,null);
        }else{
            // 校验 门店 导购信息是否准确
            AppCustomer appCustomer = null;
            try {
                appCustomer = appCustomerService.findById(cusId);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                logger.info("购买产品券失败，到不到id："+cusId+"顾客找不到");
            }

            Long cusStoreId = appCustomer.getStoreId();
            Long cusSellerId = appCustomer.getSalesConsultId();

            if (cusStoreId.equals(storeId) && cusSellerId.equals(sellerId)){
               String priceType = customerRankInfoResponse.getRankCode();

                goodsResponses = goodsService.findGoodsForBuyCoupon(storeId,cusId,sellerId,keywords,priceType);
            }else{
                goodsResponses = goodsService.findGoodsForBuyCoupon(storeId,cusId,sellerId,keywords,null);
            }

        }

        PageInfo<MaBuyProductCouponGoodsResponse> goodsResponseListPageInfo = new PageInfo<>(goodsResponses);
        List<MaBuyProductCouponGoodsResponse> goodsResponseList = goodsResponseListPageInfo.getList();
        logger.warn("restStoreGoodsPageGird ,门店商品信息分页查询成功", goodsResponseList.size());
        return new GridDataVO<MaBuyProductCouponGoodsResponse>().transform(goodsResponseList, goodsResponseListPageInfo.getTotal());
    }


    /**
     * 门店商品信息分页条件查询
     *
     * @param offset
     * @param size
     * @param keywords
     * @param brandCode
     * @param categoryCode
     * @param companyCode
     * @return
     */
    @GetMapping(value = "/page/screen/maGoods")
    public GridDataVO<MaBuyProductCouponGoodsResponse> screenMaGoodsGrid(Integer offset, Integer size, String keywords, @RequestParam(value = "storeId") Long storeId, @RequestParam(value = "brandCode") Long brandCode, @RequestParam(value = "categoryCode") String categoryCode, @RequestParam(value = "companyCode") String companyCode) {
        logger.info("screenMaGoodsGrid 门店商品信息分页条件查询,入参 offset:{},size:{},keywords:{},storeId:{}", offset, size, keywords, storeId);
        //获取用户登录名
        String userName = this.getShiroUser().getLoginName();
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageHelper.startPage(page, size);
        List<MaBuyProductCouponGoodsResponse> goodsResponses = goodsService.screenMaGoodsGrid(storeId, brandCode, categoryCode, companyCode);
        PageInfo<MaBuyProductCouponGoodsResponse> goodsResponseListPageInfo = new PageInfo<>(goodsResponses);
        List<MaBuyProductCouponGoodsResponse> goodsResponseList = goodsResponseListPageInfo.getList();
        logger.warn("screenMaGoodsGrid ,门店商品信息分页条件查询", goodsResponseList.size());
        return new GridDataVO<MaBuyProductCouponGoodsResponse>().transform(goodsResponseList, goodsResponseListPageInfo.getTotal());
    }

    /**
     * 根据导购id查询门店赠品列表
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @PostMapping(value = "/page/gifts")
    public ResultDTO<PromotionsListResponse> restGiftsPageBySellerId(Integer offset, Integer size, String keywords, Long sellerId, Long customerId, String goodsDetails) throws IOException {
        logger.info("restGiftsPageBySellerId 根据导购id查询门店赠品列表,入参 offset:{},size:{},keywords:{},sellerId:{},customerId:{},giftDateils:{}", offset, size, keywords, sellerId, customerId, goodsDetails);
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, MaActGoodsMapping.class);
        List<MaActGoodsMapping> goodsList = objectMapper.readValue(goodsDetails, javaType1);
        if (goodsList == null) {
            logger.warn("本品为空");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "本品为空", null);
        }
        //查询导购
        AppEmployee appEmployee = appEmployeeService.findById(sellerId);
        //创建促销查询所需商品参数list
        List<OrderGoodsSimpleResponse> orderGoodsSimpleResponseList = new ArrayList<>();
        for (MaActGoodsMapping goodsMappingDO : goodsList) {
            OrderGoodsSimpleResponse orderGoodsSimpleResponse = new OrderGoodsSimpleResponse();
            //根据商品id和门店id查询商品价格+ 顾客id
            GoodsPrice goodsPrice = goodsPriceService.findGoodsPriceByGoodsIDAndStoreID(goodsMappingDO.getGid(), appEmployee.getStoreId(),customerId);
            orderGoodsSimpleResponse.setId(goodsMappingDO.getGid());
            orderGoodsSimpleResponse.setSku(goodsMappingDO.getSku());
            orderGoodsSimpleResponse.setGoodsQty(goodsMappingDO.getQty());
            orderGoodsSimpleResponse.setVipPrice(goodsPrice.getVIPPrice());
            orderGoodsSimpleResponse.setRetailPrice(goodsPrice.getRetailPrice());
            //将参数添加到list中
            orderGoodsSimpleResponseList.add(orderGoodsSimpleResponse);
        }
        //查询所有符合条件的促销
        PromotionsListResponse promotionsListResponse = appActService.countAct(sellerId, AppIdentityType.SELLER, orderGoodsSimpleResponseList,customerId);
        if (promotionsListResponse == null) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,"无促销活动可参加！",null);
        }
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "促销查询成功", promotionsListResponse);
    }

    /**
     * 根据搜索查询商品信息
     *
     * @param offset
     * @param size
     * @param keywords
     * @param queryGoodsInfo
     * @return
     */
    @GetMapping(value = "/page/query/goodsInfo")
    public GridDataVO<MaBuyProductCouponGoodsResponse> queryGoodsPageByStoreIdAndInfo(Integer offset, Integer size, String keywords, Long storeId ,String queryGoodsInfo) {
        logger.info("queryGoodsPageByStoreIdAndInfo 根据搜索查询商品信息,入参 offset:{},size:{},keywords:{},storeId:{},queryGoodsInfo:{}", offset, size, keywords, storeId, queryGoodsInfo);
        //获取用户登录名
        String userName = this.getShiroUser().getLoginName();
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageHelper.startPage(page, size);
        List<MaBuyProductCouponGoodsResponse> goodsResponses = goodsService.queryGoodsPageByStoreIdAndInfo(storeId, queryGoodsInfo);
        PageInfo<MaBuyProductCouponGoodsResponse> goodsResponseListPageInfo = new PageInfo<>(goodsResponses);
        List<MaBuyProductCouponGoodsResponse> goodsResponseList = goodsResponseListPageInfo.getList();
        logger.warn("queryGoodsPageByStoreIdAndInfo ,根据搜索查询商品信息成功", goodsResponseList.size());
        return new GridDataVO<MaBuyProductCouponGoodsResponse>().transform(goodsResponseList, goodsResponseListPageInfo.getTotal());
    }
}
