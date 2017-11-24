package cn.com.leyizhuang.app.web.controller.rest;


import cn.com.leyizhuang.app.core.utils.oss.FileUploadOSSUtils;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.management.Customer;
import cn.com.leyizhuang.app.foundation.service.MaCustomerService;
import cn.com.leyizhuang.app.foundation.vo.CustomerVO;
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
import java.util.Date;
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
        PageInfo<CustomerVO> CustmersPage = this.maCustomerService.queryPageVO(page, size);
        List<CustomerVO> CustmersList = CustmersPage.getList();
        return new GridDataVO<CustomerVO>().transform(CustmersList, CustmersPage.getTotal());
    }

    /**
     * 查看顾客详细信息
     *
     * @param cusId
     * @return
     */
    @GetMapping(value = "/{cusId}")
    public ResultDTO<CustomerVO> restCusIdGet(@PathVariable(value = "cusId") Long cusId) {
        CustomerVO customerVO = this.maCustomerService.queryCustomerVOById(cusId);
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
    @GetMapping(value = "/page/citygrid/{cityId}")
    public GridDataVO<CustomerVO> getCudByCityId(Integer offset, Integer size, String keywords, @PathVariable(value = "cityId") Long cityId) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<CustomerVO> CustmersPageByCityID = this.maCustomerService.queryCustomerVOByCityId(page, size, cityId);
        List<CustomerVO> CustmersList = CustmersPageByCityID.getList();
        return new GridDataVO<CustomerVO>().transform(CustmersList, CustmersPageByCityID.getTotal());
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
    @GetMapping(value = "/page/storegrid/{storeId}")
    public GridDataVO<CustomerVO> getCudByStoreId(Integer offset, Integer size, String keywords, @PathVariable(value = "storeId") Long storeId) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<CustomerVO> CustmersPageByStoreId = this.maCustomerService.queryCustomerVOByStoreId(page, size, storeId);
        List<CustomerVO> CustmersList = CustmersPageByStoreId.getList();
        return new GridDataVO<CustomerVO>().transform(CustmersList, CustmersPageByStoreId.getTotal());
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
    @GetMapping(value = "/page/guidegrid/{guideId}")
    public GridDataVO<CustomerVO> getCudByGuideId(Integer offset, Integer size, String keywords, @PathVariable(value = "guideId") Long guideId) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<CustomerVO> CustmersPageByGuideId = this.maCustomerService.queryCustomerVOByGuideId(page, size, guideId);
        List<CustomerVO> CustmersList = CustmersPageByGuideId.getList();
        return new GridDataVO<CustomerVO>().transform(CustmersList, CustmersPageByGuideId.getTotal());
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
    @GetMapping(value = "/page/phonegrid/{queryCusInfo}")
    public GridDataVO<CustomerVO> getCudByPhone(Integer offset, Integer size, String keywords, @PathVariable(value = "queryCusInfo") Long queryCusInfo) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<CustomerVO> CustmersPageByPhone = this.maCustomerService.queryCustomerVOByPhone(page, size, queryCusInfo);
        List<CustomerVO> CustmersList = CustmersPageByPhone.getList();
        return new GridDataVO<CustomerVO>().transform(CustmersList, CustmersPageByPhone.getTotal());
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
    @GetMapping(value = "/page/Namegrid/{queryCusInfo}")
    public GridDataVO<CustomerVO> getCudByName(Integer offset, Integer size, String keywords, @PathVariable(value = "queryCusInfo") String queryCusInfo) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<CustomerVO> CustmersPageByName = this.maCustomerService.queryCustomerVOByName(page, size, queryCusInfo);
        List<CustomerVO> CustmersList = CustmersPageByName.getList();
        return new GridDataVO<CustomerVO>().transform(CustmersList, CustmersPageByName.getTotal());
    }

    /**
     * 新增顾客信息
     *
     * @param customer
     * @param result
     * @return
     */
    @PostMapping
    public ResultDTO<Object> restCustomerVOPost(@Valid Customer customer, BindingResult result, MultipartFile file) {
        if (!result.hasErrors()) {
            if (null != file) {
                String picUrl = FileUploadOSSUtils.uploadProfilePhoto(file, "/profile/photo/");
                customer.setPicUrl(picUrl);
            } else {
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
     * 新增顾客检验电话号码是否存在
     * @param mobile
     * @return
     */
    @PostMapping(value = "/isExistPhoneNumber")
    public ValidatorResultDTO isExistPhoneNumber(@RequestParam(value = "mobile") Long mobile) {
        Boolean result = this.maCustomerService.isExistPhoneNumber(mobile);
        return new ValidatorResultDTO(!result);
    }

}
