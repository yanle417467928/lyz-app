package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.MaterialListType;
import cn.com.leyizhuang.app.core.utils.SmsUtils;
import cn.com.leyizhuang.app.foundation.dto.PhotoOrderDTO;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.PhotoOrderGoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsCategoryDO;
import cn.com.leyizhuang.app.foundation.pojo.response.VerifyCodeResponse;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.service.impl.SmsAccountServiceImpl;
import cn.com.leyizhuang.app.foundation.vo.management.goods.GoodsResponseVO;
import cn.com.leyizhuang.app.foundation.vo.management.order.PhotoOrderVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;
import cn.com.leyizhuang.common.core.exception.data.InvalidDataException;
import cn.com.leyizhuang.common.foundation.pojo.SmsAccount;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;


/**
 * @author GenerationRoad
 * @date 2018/1/22
 */
@RestController
@RequestMapping(value = MaPhotoOrderRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaPhotoOrderRestController extends BaseRestController{

    protected static final String PRE_URL = "/rest/order/photo";

    private final Logger logger = LoggerFactory.getLogger(MaPhotoOrderRestController.class);

    @Autowired
    private MaPhotoOrderService maPhotoOrderService;

    @Autowired
    private MaGoodsCategoryService maGoodsCategoryService;

    @Autowired
    private MaGoodsService maGoodsService;

    @Autowired
    private MaMaterialListService maMaterialListService;

    @Autowired
    private SmsAccountServiceImpl smsAccountService;

    @Autowired
    private MaPhotoOrderGoodsService maPhotoOrderGoodsService;

    @Autowired
    private AdminUserStoreService adminUserStoreService;

    /**
     * @title   获取拍照下单列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/11
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<PhotoOrderVO> restPhotoOrderPageGird(Integer offset, Integer size, String keywords, Long cityId, String status, Long storeId) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        //查询登录用户门店权限的门店ID
        List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
        PageInfo<PhotoOrderVO> photoOrderVOPageInfo = this.maPhotoOrderService.findAllByCityIdAndStoreId(page, size, cityId, storeId, keywords, status, storeIds);
        return new GridDataVO<PhotoOrderVO>().transform(photoOrderVOPageInfo.getList(), photoOrderVOPageInfo.getTotal());
    }

    /**
     * @title   获取拍照下单明细
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/1/22
     */
    @GetMapping(value = "/{id}")
    public ResultDTO<PhotoOrderVO> restCusPreDepositLogGet(@PathVariable(value = "id") Long id) {
        PhotoOrderVO photoOrderVO = this.maPhotoOrderService.findById(id);
        if (null == photoOrderVO) {
            logger.warn("查找拍照下单明细失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, photoOrderVO);
        }
    }

    /**
     * @title   获取商品分类
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/2/22
     */
    @GetMapping(value = "/findCategory")
    public ResultDTO<Object> findCategory(String categoryCode, Long id) {
        logger.info("findCategory,获取商品分类，入参 categoryCode:{} id:{}", categoryCode, id);

        Map<String, Object> returnMap = new HashMap(2);
        List<GoodsCategoryDO> goodsCategoryDOList = this.maGoodsCategoryService.findGoodsCategoryByPCategoryCode(categoryCode);
        if (null == goodsCategoryDOList) {
            logger.warn("查找商品分类：Role(categoryCode = {}) == null", categoryCode);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            returnMap.put("goodsCategory", goodsCategoryDOList);
            PhotoOrderVO photoOrderVO = this.maPhotoOrderService.findById(id);
            List<GoodsResponseVO> goodsList = null;
            if (null != photoOrderVO){
                List<Long> cids = new ArrayList<>();
                for (GoodsCategoryDO goodsCategoryDO:goodsCategoryDOList) {
                    cids.add(goodsCategoryDO.getCid());
                }
                if (AppIdentityType.CUSTOMER.equals(photoOrderVO.getIdentityTypeValue())){
                    goodsList = this.maGoodsService.findGoodsByCidAndCusId(photoOrderVO.getUserId(), cids);
                } else {
                    goodsList = this.maGoodsService.findGoodsByCidAndEmpId(photoOrderVO.getUserId(), cids);
                }
            }
            returnMap.put("goods", goodsList);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, returnMap);
        }
    }

    /**
     * @title   获取商品
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/2/22
     */
    @GetMapping(value = "/findGoods")
    public ResultDTO<Object> findGoods(Long categoryId, Long id) {
        logger.info("findGoods,获取商品，入参 categoryId:{} id:{}", categoryId, id);
        List<Long> cids = new ArrayList<>();
        cids.add(categoryId);
        PhotoOrderVO photoOrderVO = this.maPhotoOrderService.findById(id);
        List<GoodsResponseVO> goodsList = null;
        if (null != photoOrderVO){
            if (AppIdentityType.CUSTOMER.equals(photoOrderVO.getIdentityTypeValue())){
                goodsList = this.maGoodsService.findGoodsByCidAndCusId(photoOrderVO.getUserId(), cids);
            } else {
                goodsList = this.maGoodsService.findGoodsByCidAndEmpId(photoOrderVO.getUserId(), cids);
            }
        }
        if (null == goodsList) {
            logger.warn("查找商品：Role(categoryId = {}, id = {}) == null", categoryId, id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, goodsList);
        }
    }

    /**
     * @title 保存拍照下单
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/2/22
     */
    @PostMapping(value = "/save")
    public ResultDTO<Object> savePhotoOrder(@Valid PhotoOrderDTO photoOrderDTO, BindingResult result) {
        logger.info("savePhotoOrder,保存拍照下单，入参 categoryId:{}", photoOrderDTO);
        if (!result.hasErrors()) {
            if (null != photoOrderDTO && null != photoOrderDTO.getPhotoId() && null != photoOrderDTO.getCombList() && photoOrderDTO.getCombList().size() > 0){
                //查询拍照订单信息
                List<PhotoOrderStatus> status = new ArrayList<>();
                status.add(PhotoOrderStatus.PENDING);
                status.add(PhotoOrderStatus.PROCESSING);
                PhotoOrderVO photoOrderVO = this.maPhotoOrderService.findByIdAndStatus(photoOrderDTO.getPhotoId(), status);
                if (null != photoOrderVO){
                    List<MaterialListDO> combList = photoOrderDTO.getCombList();
                    List<MaterialListDO> materialListSave = new ArrayList<>();
                    List<MaterialListDO> materialListUpdate = new ArrayList<>();
                    List<PhotoOrderGoodsDO> photoOrderGoodsDOList = new ArrayList<>();
                    for (MaterialListDO materialListDO: combList) {
                        GoodsDO goodsDO = maGoodsService.findGoodsById(materialListDO.getGid());
                        if (null != goodsDO){
                            MaterialListDO materialList = maMaterialListService.findByUserIdAndIdentityTypeAndGoodsId(photoOrderVO.getUserId(),
                                    photoOrderVO.getIdentityTypeValue(), materialListDO.getGid());
                            if (null == materialList){
                                MaterialListDO materialListDOTemp = transformRepeat(goodsDO);
                                materialListDOTemp.setUserId(photoOrderVO.getUserId());
                                materialListDOTemp.setIdentityType(photoOrderVO.getIdentityTypeValue());
                                materialListDOTemp.setQty(materialListDO.getQty());
                                materialListDOTemp.setMaterialListType(MaterialListType.NORMAL);
                                materialListSave.add(materialListDOTemp);
                            } else {
                                materialList.setQty(materialList.getQty() + materialListDO.getQty());
                                materialListUpdate.add(materialList);
                            }

                            PhotoOrderGoodsDO photoOrderGoodsDO = new PhotoOrderGoodsDO();
                            photoOrderGoodsDO.setGid(goodsDO.getGid());
                            photoOrderGoodsDO.setSkuName(goodsDO.getSkuName());
                            photoOrderGoodsDO.setGoodsQty(materialListDO.getQty());
                            photoOrderGoodsDO.setPhotoOrderNo(photoOrderVO.getPhotoOrderNo());
                            photoOrderGoodsDOList.add(photoOrderGoodsDO);
                        }
                    }
                    this.maPhotoOrderGoodsService.batchSave(photoOrderGoodsDOList);
                    this.maPhotoOrderService.updateStatusAndsaveAndUpdateMaterialList(photoOrderDTO.getPhotoId(), PhotoOrderStatus.FINISH, materialListSave, materialListUpdate);

                    //短信提醒
                    String info = "您的拍照下单订单(" + photoOrderVO.getPhotoOrderNo() + ")已处理，请登录APP查看。";
                    String content;
                    try {
                        content = URLEncoder.encode(info, "GB2312");
                        System.err.println(content);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知错误，短信验证码发送失败！", null);
                        logger.info("savePhotoOrder EXCEPTION，提醒短信发送失败，出参 ResultDTO:{}", resultDTO);
                        logger.warn("{}", e);
                        return resultDTO;
                    }
                    SmsAccount account = smsAccountService.findOne();
                    String returnCode;
                    try {
                        returnCode = SmsUtils.sendMessageQrCode(account.getEncode(), account.getEnpass(), account.getUserName(), photoOrderVO.getUserMobile(), content);
                    } catch (IOException e) {
                        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "网络故障，提醒短信发送失败！", null);
                        logger.info("savePhotoOrder EXCEPTION，提醒短信发送失败，出参 ResultDTO:{}", resultDTO);
                        logger.warn("{}", e);
                        return resultDTO;
                    } catch (Exception e) {
                        ResultDTO<Object> resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知错误，短信验证码发送失败！", null);
                        logger.info("savePhotoOrder EXCEPTION，提醒短信发送失败，出参 ResultDTO:{}", resultDTO);
                        logger.warn("{}", e);
                        return resultDTO;
                    }

                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                }
            }
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "信息错误！", null);
        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }

    /**
     * @title   批量取消拍照下单
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/2/22
     */
    @DeleteMapping
    public ResultDTO<?> dataResourceDelete(Long[] ids) {
        logger.info("dataResourceDelete,批量取消拍照下单，入参 ids:{}", ids);
        try {
            int num = this.maPhotoOrderService.batchDelete(ids);
            if (num > 0) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "拍照下单已成功取消", null);
            } else {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有可取消的拍照下单", null);
            }
        } catch (InvalidDataException e) {
            logger.error("批量取消拍照下单发生错误");
            logger.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    "批量取消拍照下单发生错误，请稍后重试或联系管理员", null);
        } catch (Exception e) {
            logger.error("批量取消拍照下单发生错误");
            logger.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "出现未知错误，请稍后重试或联系管理员", null);
        }
    }

    /**
     * @title   取消拍照下单
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/2/22
     */
    @PostMapping(value = "/delete")
    public ResultDTO<?> dataResourceDeleteOne(Long photoId) {
        logger.info("dataResourceDeleteOne,取消拍照下单，入参 photoId:{}", photoId);
        try {
            Long[] ids = {photoId};
            int num = this.maPhotoOrderService.batchDelete(ids);
            if (num > 0) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "拍照下单已成功取消", null);
            } else {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有可取消的拍照下单", null);
            }
        } catch (InvalidDataException e) {
            logger.error("取消拍照下单发生错误");
            logger.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    "取消拍照下单发生错误，请稍后重试或联系管理员", null);
        } catch (Exception e) {
            logger.error("取消拍照下单发生错误");
            logger.error(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "出现未知错误，请稍后重试或联系管理员", null);
        }
    }

    private MaterialListDO transformRepeat(GoodsDO goodsDO){
        MaterialListDO materialListDOTemp = new MaterialListDO();
        materialListDOTemp.setGid(goodsDO.getGid());
        materialListDOTemp.setSku(goodsDO.getSku());
        materialListDOTemp.setSkuName(goodsDO.getSkuName());
        materialListDOTemp.setGoodsSpecification(goodsDO.getGoodsSpecification());
        materialListDOTemp.setGoodsUnit(goodsDO.getGoodsUnit());
        if (null != goodsDO.getCoverImageUri()) {
            String uri[] = goodsDO.getCoverImageUri().split(",");
            materialListDOTemp.setCoverImageUri(uri[0]);
        }
        return materialListDOTemp;
    }

}
