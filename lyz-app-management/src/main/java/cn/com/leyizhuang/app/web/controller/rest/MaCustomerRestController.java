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
import com.github.pagehelper.PageInfo;
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
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<CustomerDO> custmersPage = this.maCustomerService.queryPageVO(page, size);
        List<CustomerDO> custmersList = custmersPage.getList();
        List<CustomerVO> custmersVOList =   CustomerVO.transform(custmersList);
        return new GridDataVO<CustomerVO>().transform(custmersVOList, custmersPage.getTotal());
    }

    /**
     * 查看顾客详细信息
     *
     * @param cusId
     * @return
     */
    @GetMapping(value = "/{cusId}")
    public ResultDTO<CustomerDetailVO> restCusIdGet(@PathVariable(value = "cusId") Long cusId) {
        CustomerDO customerDO = this.maCustomerService.queryCustomerVOById(cusId);
        CustomerDetailVO customerVO =  CustomerDetailVO.transform(customerDO);
        if (null == customerVO) {
            logger.warn("查找顾客失败：Role(id = {}) == null", cusId);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, customerVO);
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
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<CustomerDO> custmersPageByCityID = this.maCustomerService.queryCustomerVOByCityId(page, size, cityId);
        List<CustomerDO> custmersList = custmersPageByCityID.getList();
        List<CustomerVO> custmersVOList =   CustomerVO.transform(custmersList);
        return new GridDataVO<CustomerVO>().transform(custmersVOList, custmersPageByCityID.getTotal());
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
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<CustomerDO> custmersPageByStoreId = this.maCustomerService.queryCustomerVOByStoreId(page, size, storeId);
        List<CustomerDO> custmersList = custmersPageByStoreId.getList();
        List<CustomerVO> custmersVOList =   CustomerVO.transform(custmersList);
        return new GridDataVO<CustomerVO>().transform(custmersVOList, custmersPageByStoreId.getTotal());
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
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<CustomerDO> custmersPageByGuideId = this.maCustomerService.queryCustomerVOByGuideId(page, size, guideId);
        List<CustomerDO> custmersList = custmersPageByGuideId.getList();
        List<CustomerVO> custmersVOList =   CustomerVO.transform(custmersList);
        return new GridDataVO<CustomerVO>().transform(custmersVOList, custmersPageByGuideId.getTotal());
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
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<CustomerDO> custmersPageByPhone = this.maCustomerService.queryCustomerVOByPhone(page, size, queryCusInfo);
        List<CustomerDO> custmersList = custmersPageByPhone.getList();
        List<CustomerVO> custmersVOList =   CustomerVO.transform(custmersList);
        return new GridDataVO<CustomerVO>().transform(custmersVOList, custmersPageByPhone.getTotal());
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
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<CustomerDO> custmersPageByName = this.maCustomerService.queryCustomerVOByName(page, size, queryCusInfo);
        List<CustomerDO> custmersList = custmersPageByName.getList();
        List<CustomerVO> custmersVOList =   CustomerVO.transform(custmersList);
        return new GridDataVO<CustomerVO>().transform(custmersVOList, custmersPageByName.getTotal());
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
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
        } else {
            List<ObjectError> allErrors = result.getAllErrors();
            logger.warn("页面提交的数据有错误：errors = {}", errorMsgToHtml(allErrors));
            return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                    errorMsgToHtml(allErrors), null);
        }
    }

    /**
     * 新增顾客检验电话号码是否存在z
     * @param mobile
     * @return
     */
    @PostMapping(value = "/isExistPhoneNumber")
    public ValidatorResultDTO isExistPhoneNumber(@RequestParam(value = "mobile") Long mobile) {
          if(null==mobile){
            logger.warn("页面提交的数据有错误");
            return new ValidatorResultDTO(false);
        }
        Boolean result = this.maCustomerService.isExistPhoneNumber(mobile);
        return new ValidatorResultDTO(!result);
    }

}
