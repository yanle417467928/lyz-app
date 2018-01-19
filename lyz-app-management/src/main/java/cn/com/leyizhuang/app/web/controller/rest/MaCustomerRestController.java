package cn.com.leyizhuang.app.web.controller.rest;


import cn.com.leyizhuang.app.core.utils.oss.FileUploadOSSUtils;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.CustomerDO;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.service.MaCustomerService;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.foundation.pojo.dto.ValidatorResultDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = MaCustomerRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCustomerRestController extends BaseRestController {

    protected static final String PRE_URL = "/rest/customers";

    private final Logger logger = LoggerFactory.getLogger(MaCustomerRestController.class);

    @Autowired
    private MaCustomerService maCustomerService;

    /**
     * 初始化顾客页面
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<CustomerVO> restCustomersPageGird(Integer offset, Integer size, String keywords) {
        logger.info("restCustomersPageGird 后台初始化顾客页面列表 ,入参 offset:{}, size:{}, kewords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<CustomerDO> custmersPage = this.maCustomerService.queryPageVO(page, size);
            List<CustomerDO> custmersList = custmersPage.getList();
            List<CustomerVO> custmersVOList = CustomerVO.transform(custmersList);
            logger.info("restCustomersPageGird ,后台初始化顾客页面列表成功", custmersVOList.size());
            return new GridDataVO<CustomerVO>().transform(custmersVOList, custmersPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restCustomersPageGird EXCEPTION,发生未知错误，后台初始化顾客页面列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 查看顾客详细信息
     *
     * @param cusId
     * @return
     */
    @GetMapping(value = "/{cusId}")
    public ResultDTO<CustomerDetailVO> restCusIdGet(@PathVariable(value = "cusId") Long cusId) {
        logger.info("restCusIdGet 后台查看顾客详细信息 ,入参 offset:{}, cusId:{}", cusId);
        try {
            CustomerDO customerDO = this.maCustomerService.queryCustomerVOById(cusId);
            CustomerDetailVO customerVO = CustomerDetailVO.transform(customerDO);
            if (null == customerVO) {
                logger.warn("查找顾客失败：Role(id = {}) == null", cusId);
                return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                        "指定数据不存在，请联系管理员", null);
            } else {
                logger.info("restCusIdGet ,后台查看顾客详细信息成功");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, customerVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restCusIdGet EXCEPTION,发生未知错误，后台查看顾客详细信息失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 查询城市顾客
     *
     * @param offset
     * @param size
     * @param keywords
     * @param cityId
     * @return
     */
    @GetMapping(value = "/page/cityGrid/{cityId}")
    public GridDataVO<CustomerVO> getCudByCityId(Integer offset, Integer size, String keywords, @PathVariable(value = "cityId") Long cityId) {
        logger.info("getCudByCityId 查询城市顾客列表 ,入参 offset:{}, size:{}, kewords:{},cityId:{}", offset, size, keywords, cityId);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<CustomerDO> custmersPageByCityID = this.maCustomerService.queryCustomerVOByCityId(page, size, cityId);
            List<CustomerDO> custmersList = custmersPageByCityID.getList();
            List<CustomerVO> custmersVOList = CustomerVO.transform(custmersList);
            logger.info("getCudByCityId ,查询城市顾客列表成功", custmersVOList.size());
            return new GridDataVO<CustomerVO>().transform(custmersVOList, custmersPageByCityID.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("getCudByCityId EXCEPTION,发生未知错误，查询城市顾客列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 查询门店顾客
     *
     * @param offset
     * @param size
     * @param keywords
     * @param storeId
     * @return
     */
    @GetMapping(value = "/page/storeGrid/{storeId}")
    public GridDataVO<CustomerVO> getCudByStoreId(Integer offset, Integer size, String keywords, @PathVariable(value = "storeId") Long storeId) {
        logger.info("getCudByStoreId 查询门店顾客列表 ,入参 offset:{}, size:{}, kewords:{},storeId:{}", offset, size, keywords, storeId);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<CustomerDO> custmersPageByStoreId = this.maCustomerService.queryCustomerVOByStoreId(page, size, storeId);
            List<CustomerDO> custmersList = custmersPageByStoreId.getList();
            List<CustomerVO> custmersVOList = CustomerVO.transform(custmersList);
            logger.info("getCudByStoreId ,查询门店顾客列表成功", custmersVOList.size());
            return new GridDataVO<CustomerVO>().transform(custmersVOList, custmersPageByStoreId.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("getCudByStoreId EXCEPTION,发生未知错误，查询门店顾客列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 查询该导购下的顾客
     *
     * @param offset
     * @param size
     * @param keywords
     * @param guideId
     * @return
     */
    @GetMapping(value = "/page/guideGrid/{guideId}")
    public GridDataVO<CustomerVO> getCudByGuideId(Integer offset, Integer size, String keywords, @PathVariable(value = "guideId") Long guideId) {
        logger.info("getCudByGuideId 查询该导购下的顾客列表 ,入参 offset:{}, size:{}, kewords:{},guideId:{}", offset, size, keywords, guideId);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<CustomerDO> custmersPageByGuideId = this.maCustomerService.queryCustomerVOByGuideId(page, size, guideId);
            List<CustomerDO> custmersList = custmersPageByGuideId.getList();
            List<CustomerVO> custmersVOList = CustomerVO.transform(custmersList);
            logger.info("getCudByGuideId ,查询该导购下的顾客列表成功", custmersVOList.size());
            return new GridDataVO<CustomerVO>().transform(custmersVOList, custmersPageByGuideId.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("getCudByGuideId EXCEPTION,发生未知错误，查询该导购下的顾客列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 通过电话查询顾客
     *
     * @param offset
     * @param size
     * @param keywords
     * @param queryCusInfo
     * @return
     */
    @GetMapping(value = "/page/phoneGrid/{queryCusInfo}")
    public GridDataVO<CustomerVO> getCudByPhone(Integer offset, Integer size, String keywords, @PathVariable(value = "queryCusInfo") Long queryCusInfo) {
        logger.info("getCudByPhone 通过电话查询顾客列表 ,入参 offset:{}, size:{}, kewords:{},queryCusInfo:{}", offset, size, keywords, queryCusInfo);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<CustomerDO> custmersPageByPhone = this.maCustomerService.queryCustomerVOByPhone(page, size, queryCusInfo);
            List<CustomerDO> custmersList = custmersPageByPhone.getList();
            List<CustomerVO> custmersVOList = CustomerVO.transform(custmersList);
            logger.info("getCudByPhone ,通过电话查询顾客列表成功", custmersVOList.size());
            return new GridDataVO<CustomerVO>().transform(custmersVOList, custmersPageByPhone.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("getCudByPhone EXCEPTION,发生未知错误，通过电话查询顾客列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 通过姓名查询顾客
     *
     * @param offset
     * @param size
     * @param keywords
     * @param queryCusInfo
     * @return
     */
    @GetMapping(value = "/page/nameGrid/{queryCusInfo}")
    public GridDataVO<CustomerVO> getCudByName(Integer offset, Integer size, String keywords, @PathVariable(value = "queryCusInfo") String queryCusInfo) {
        logger.info("getCudByName 通过姓名查询顾客列表 ,入参 offset:{}, size:{}, kewords:{},queryCusInfo:{}", offset, size, keywords, queryCusInfo);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<CustomerDO> custmersPageByName = this.maCustomerService.queryCustomerVOByName(page, size, queryCusInfo);
            List<CustomerDO> custmersList = custmersPageByName.getList();
            List<CustomerVO> custmersVOList = CustomerVO.transform(custmersList);
            logger.info("getCudByName ,通过姓名查询顾客列表成功", custmersVOList.size());
            return new GridDataVO<CustomerVO>().transform(custmersVOList, custmersPageByName.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("getCudByName EXCEPTION,发生未知错误，通过姓名查询顾客列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 新增顾客信息
     *
     * @param customer
     * @param result
     * @return
     */
    @PostMapping
    public ResultDTO<Object> restCustomerVOPost(@Valid CustomerDetailVO customer, BindingResult result, MultipartFile file) {
        logger.info("restCustomerVOPost 新增顾客信息 ,入参 customer:{}", customer);
        try {
            if (!result.hasErrors()) {
                if (!file.isEmpty()) {
                    //上传图片得到图片地址
                    String picUrl = FileUploadOSSUtils.uploadProfilePhoto(file, "profile/photo/");
                    customer.setPicUrl(picUrl);
                } else {
                    //没有上传图片 返回默认头像地址
                    customer.setPicUrl("http://img3.leyizhuang.com.cn/app/images/goods/3875/20171116165950104.png");
                }
                this.maCustomerService.saveCustomer(customer);
                logger.info("restCustomerVOPost ,新增顾客信息成功");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            } else {
                List<ObjectError> allErrors = result.getAllErrors();
                logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
                return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                        errorMsgToHtml(allErrors), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restCustomerVOPost EXCEPTION,发生未知错误，新增顾客信息失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 新增顾客检验电话号码是否存在
     *
     * @param mobile
     * @return
     */
    @PostMapping(value = "/isExistPhoneNumber")
    public ValidatorResultDTO isExistPhoneNumber(@RequestParam(value = "mobile") Long mobile) {
        logger.info("isExistPhoneNumber 新增顾客检验电话号码是否存在 ,入参 mobile:{}", mobile);
        try {
            if (null == mobile) {
                logger.warn("页面提交的数据有错误,号码为空");
                return new ValidatorResultDTO(false);
            }
            Boolean result = this.maCustomerService.isExistPhoneNumber(mobile);
            logger.info("isExistPhoneNumber 新增顾客检验电话号码是否存在判断成功");
            return new ValidatorResultDTO(!result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("isExistPhoneNumber EXCEPTION,发生未知错误，新增顾客检验电话号码是否存在判断失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 后台购买产品券选择顾客
     *
     * @param offset
     * @param size
     * @param keywords
     * @param cityId
     * @param storeId
     * @return
     */
    @GetMapping(value = "/select/customer")
    public GridDataVO<CustomerDO> selectCustomer(Integer offset, Integer size, String keywords, @RequestParam(value = "cityId") Long cityId, @RequestParam(value = "storeId") Long storeId) {
        logger.info("selectCustomer 后台购买产品券选择顾客,入参 offset:{},size:{},keywords:{},cityId:{},storeId:{}", offset, size, keywords, cityId, storeId);
        try {
            String userName = this.getShiroUser().getLoginName();
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageHelper.startPage(page, size);
            List<CustomerDO> employeeDOList = this.maCustomerService.findCustomerByCityIdAndStoreId(cityId, storeId);
            PageInfo<CustomerDO> customerDOPageInfo = new PageInfo<>(employeeDOList);
            List<CustomerDO> customerDOList = customerDOPageInfo.getList();
            logger.warn("selectCustomer ,后台购买产品券选择顾客成功", customerDOList.size());
            return new GridDataVO<CustomerDO>().transform(customerDOList, customerDOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("selectCustomer EXCEPTION,发生未知错误，后台购买产品券选择顾客失败");
            logger.warn("{}", e);
            return null;
        }
    }
}
