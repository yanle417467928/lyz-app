package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.utils.oss.exception.ImageClientException;
import cn.com.leyizhuang.app.core.utils.oss.utils.ImageClientUtils;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeType;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaEmployeeResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.AdminUserStoreService;
import cn.com.leyizhuang.app.foundation.service.MaEmployeeService;
import cn.com.leyizhuang.app.foundation.service.QrcodeProduceService;
import cn.com.leyizhuang.app.foundation.vo.management.employee.EmployeeDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideVO;
import cn.com.leyizhuang.app.foundation.vo.management.employee.EmployeeVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping(value = MaEmployeeRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaEmployeeRestController extends BaseRestController {

    protected static final String PRE_URL = "/rest/employees";

    private final Logger logger = LoggerFactory.getLogger(MaEmployeeRestController.class);

    @Value("${deploy.lyz.qrcodeRegister}")
    private String qrcodeRegisterUrl;

    @Autowired
    private MaEmployeeService maEmployeeService;

    @Autowired
    private AppEmployeeService appEmployeeService;

    @Autowired
    private QrcodeProduceService qrcodeProduceService;

    @Autowired
    private AdminUserStoreService adminUserStoreService;
    /**
     * 显示所有员工信息
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<EmployeeVO> restEmployeesPageGird(Integer offset, Integer size, String keywords) {
        logger.info("restEmployeesPageGird 后台获取所有员工信息列表 ,入参 offset:{},size:{},keywords:{},id:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<EmployeeDO> employeesPage = this.maEmployeeService.queryPageVO(page, size);
            List<EmployeeDO> employeesList = employeesPage.getList();
            List<EmployeeVO> employeesVOList = EmployeeVO.transform(employeesList);
            logger.info("restEmployeesPageGird ,后台获取所有员工信息列表成功", employeesVOList.size());
            return new GridDataVO<EmployeeVO>().transform(employeesVOList, employeesPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restEmployeesPageGird EXCEPTION,发生未知错误，后台获取所有员工信息列表列表失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 查询该id下的员工详情信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "{id}")
    public ResultDTO<EmployeeDetailVO> queryEmployeeById(@PathVariable(value = "id") Long id) {
        logger.info("queryEmployeeById 后台获取员工详情信息 ,入参 id:{}", id);
        try {
            EmployeeDetailVO employeeVO = this.maEmployeeService.queryEmployeeById(id);
            if (null == employeeVO) {
                logger.warn("查找员工失败：Role(id = {}) == null", id);
                return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                        "指定数据不存在，请联系管理员", null);
            } else {
                logger.info("queryEmployeeById ,后台获取员工详情信息成功");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, employeeVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("queryEmployeeById EXCEPTION,发生未知错误，后台获取员工详情信息失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 查询该门店下的导购(下拉框)
     *
     * @param storeId
     * @return
     */
    @GetMapping(value = "/findGuidesListById/{storeId}")
    public List<EmployeeVO> findGuidesListById(@PathVariable("storeId") Long storeId) {
        logger.info("findGuidesListById 后台查询该门店下的导购(下拉框) ,入参 storeId:{}", storeId);
        try {
            logger.info("findGuidesListById ,后台查询该门店下的导购(下拉框)成功");
            return maEmployeeService.findGuideListById(storeId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findGuidesListById EXCEPTION,发生未知错误，后台查询该门店下的导购(下拉框)失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 查询该门店下的员工类型(下拉框)
     *
     * @param storeId
     * @param
     * @return
     */
    @GetMapping(value = "/findEmpTypeByStoreId/{storeId}")
    public List<EmployeeType> findEmpTypeByStoreId(@PathVariable("storeId") Long storeId) {
        logger.info("findEmpTypeByStoreId 后台查询该门店下的员工类型(下拉框) ,入参 storeId:{}", storeId);
        try {
            List<EmployeeType> employeeTypeList = maEmployeeService.findEmpTypeByStoreId(storeId);
            logger.info("findEmpTypeByStoreId ,后台查询该门店下的员工类型(下拉框)成功", employeeTypeList.size());
            return employeeTypeList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findEmpTypeByStoreId EXCEPTION,发生未知错误，后台查询该门店下的员工类型(下拉框)失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 查询该城市下的员工类型(下拉框)
     *
     * @param
     * @param cityId
     * @return
     */
    @GetMapping(value = "/findEmpTypeByCityId/{cityId}")
    public List<EmployeeType> findEmpTypeByCityId(@PathVariable("cityId") Long cityId) {
        logger.info("findEmpTypeByCityId 后台查询该城市下的员工类型(下拉框) ,入参 storeId:{}", cityId);
        try {
            List<EmployeeType> employeeTypeList = maEmployeeService.findEmpTypeByCityId(cityId);
            logger.info("findEmpTypeByCityId ,后台查询该城市下的员工类型(下拉框)成功", employeeTypeList.size());
            return employeeTypeList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findEmpTypeByCityId EXCEPTION,发生未知错误，后台查询该城市下的员工类型(下拉框)失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 查询所有员工类型(下拉框)
     *
     * @param
     * @return
     */
    @GetMapping(value = "/findEmpTypeList")
    public List<EmployeeType> findEmpTypeList() {
        logger.info("findEmpTypeList 后台查询所有员工类型(下拉框) ");
        try {
            List<EmployeeType> employeeTypeList = maEmployeeService.findEmpTypeList();
            logger.info("findEmpTypeList ,后台查询所有员工类型(下拉框)成功", employeeTypeList.size());
            return employeeTypeList;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findEmpTypeList EXCEPTION,发生未知错误，后台查询所有员工类型(下拉框)失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 根据身份信息查询员工
     *
     * @param offset
     * @param size
     * @param keywords
     * @param identityType
     * @param storeId
     * @return
     */
    @GetMapping(value = "/page/conditionGrid")
    public GridDataVO<EmployeeVO> restEmployeesPageGirdByCondition(Integer offset, Integer size, String keywords, @RequestParam(value = "identityType") String identityType,
                                                                   @RequestParam(value = "storeId") Long storeId, @RequestParam(value = "cityId") Long cityId, @RequestParam(value = "enabled") String enabled) {
        logger.info("restEmployeesPageGirdByCondition 后台根据身份信息查询员工,入参 offset:{},size:{},keywords:{},identityType:{},storeId:{},cityId:{},enabled:{}", offset, size, keywords, identityType, storeId, cityId, enabled);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<EmployeeDO> employeesPage = this.maEmployeeService.queryPageVOByCondition(page, size, identityType, storeId, cityId, enabled);
            List<EmployeeDO> employeesList = employeesPage.getList();
            List<EmployeeVO> employeesVOList = EmployeeVO.transform(employeesList);
            logger.info("restEmployeesPageGirdByCondition ,后台根据身份信息查询员工成功", employeesVOList.size());
            return new GridDataVO<EmployeeVO>().transform(employeesVOList, employeesPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restEmployeesPageGirdByCondition EXCEPTION,发生未知错误，后台根据身份信息查询员工)失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 根据搜索信息查询员工
     *
     * @param offset
     * @param size
     * @param keywords
     * @param queryEmpInfo
     * @return
     */
    @GetMapping(value = "/page/infoGrid/{queryEmpInfo}")
    public GridDataVO<EmployeeVO> restEmployeesPageGirdByInfo(Integer offset, Integer size, String keywords, @PathVariable(value = "queryEmpInfo") String queryEmpInfo) {
        logger.info("restEmployeesPageGirdByInfo 后台根据搜索信息查询员工,入参 offset:{},size:{},keywords:{},queryEmpInfo:{}", offset, size, keywords, queryEmpInfo);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<EmployeeDO> employeesPage = this.maEmployeeService.queryPageVOByInfo(page, size, queryEmpInfo);
            List<EmployeeDO> employeesList = employeesPage.getList();
            List<EmployeeVO> employeesVOList = EmployeeVO.transform(employeesList);
            logger.info("restEmployeesPageGirdByInfo ,后台根据搜索信息查询员工成功", employeesVOList.size());
            return new GridDataVO<EmployeeVO>().transform(employeesVOList, employeesPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restEmployeesPageGirdByInfo EXCEPTION,发生未知错误，后台根据搜索信息查询员工失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 显示导购列表
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/guidePage/grid")
    public GridDataVO<GuideVO> restGuideVOPageGird(Integer offset, Integer size, String keywords) {
        logger.info("restGuideVOPageGird 后台显示导购列表,入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<GuideVO> guideVOPage = this.maEmployeeService.queryGuideVOPage(page, size);
            List<GuideVO> guideVOList = guideVOPage.getList();
            logger.info("restGuideVOPageGird ,后台显示导购列表成功", guideVOList.size());
            return new GridDataVO<GuideVO>().transform(guideVOList, guideVOPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restGuideVOPageGird EXCEPTION,发生未知错误，后台显示导购列表失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 查询员工额度详情信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/guide/{id}")
    public ResultDTO<GuideVO> queryGuideVOById(@PathVariable(value = "id") Long id) {
        logger.info("queryGuideVOById 后台查询员工额度详情信息,入参 id:{}", id);
        try {
            GuideVO guideVO = this.maEmployeeService.queryGuideVOById(id);
            if (null == guideVO) {
                logger.warn("查找员工额度失败：Role(id = {}) == null", id);
                return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                        "指定数据不存在，请联系管理员", null);
            } else {
                logger.info("queryGuideVOById ,后台查询员工额度详情信息成功");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, guideVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("queryGuideVOById EXCEPTION,发生未知错误，后台查询员工额度详情信息失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 根据身份信息查询员工额度
     *
     * @param offset
     * @param size
     * @param keywords
     * @param cityId
     * @param storeId
     * @return
     */
    @GetMapping(value = "/guidePage/conditionGrid")
    public GridDataVO<GuideVO> queryGuideVOByCondition(Integer offset, Integer size, String keywords, @RequestParam(value = "cityId") Long cityId,
                                                       @RequestParam(value = "storeId") Long storeId) {
        logger.info("queryGuideVOByCondition 后台根据身份信息查询员工额度,入参 offset:{},size:{},keywords:{},cityId:{},storeId:{}", offset, size, keywords, cityId, storeId);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<GuideVO> guideVOPage = this.maEmployeeService.queryGuideVOByCondition(page, size, cityId, storeId);
            List<GuideVO> guideVOList = guideVOPage.getList();
            logger.info("queryGuideVOByCondition ,后台根据身份信息查询员工额度成功");
            return new GridDataVO<GuideVO>().transform(guideVOList, guideVOPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("queryGuideVOByCondition,发生未知错误，后台根据身份信息查询员工额度失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 根据搜索信息查询导购额度
     *
     * @param offset
     * @param size
     * @param keywords
     * @param queryGuideVOInfo
     * @return
     */
    @GetMapping(value = "/guidePage/infoGrid/{queryGuideVOInfo}")
    public GridDataVO<GuideVO> queryGuideVOByInfo(Integer offset, Integer size, String keywords, @PathVariable(value = "queryGuideVOInfo") String queryGuideVOInfo) {
        logger.info("queryGuideVOByInfo 后台根据搜索信息查询导购额度,入参 offset:{},size:{},keywords:{},queryGuideVOInfo:{}", offset, size, keywords, queryGuideVOInfo);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<GuideVO> guideVOPage = this.maEmployeeService.queryGuideVOByInfo(page, size, queryGuideVOInfo);
            List<GuideVO> guideVOList = guideVOPage.getList();
            logger.info("queryGuideVOByInfo ,后台根据搜索信息查询导购额度成功");
            return new GridDataVO<GuideVO>().transform(guideVOList, guideVOPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("queryGuideVOByInfo,发生未知错误，后台根根据搜索信息查询导购额度");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 后台购买产品券选择导购
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/select/seller")
    public GridDataVO<MaEmployeeResponse> selectSeller(Integer offset, Integer size, String keywords) {
        logger.info("selectSeller 后台购买产品券选择导购,入参 offset:{},size:{},keywords:{},cityId:{},storeId:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageHelper.startPage(page, size);
            //查询登录用户门店权限的门店ID
            List<Long> storeIds = this.adminUserStoreService.findStoreIdList();
            List<MaEmployeeResponse> employeeDOList = this.maEmployeeService.findMaEmployeeByCityIdAndStoreId(storeIds);

            PageInfo<MaEmployeeResponse> maEmployeeDOPageInfo = new PageInfo<>(employeeDOList);
            List<MaEmployeeResponse> EmployeeDOList = maEmployeeDOPageInfo.getList();
            logger.warn("selectSeller ,后台购买产品券选择导购成功", EmployeeDOList.size());
            return new GridDataVO<MaEmployeeResponse>().transform(EmployeeDOList, maEmployeeDOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("selectSeller EXCEPTION,发生未知错误，后台购买产品券选择导购失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 显示导购欠款列表
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/guideArrearsPage/grid")
    public GridDataVO<GuideVO> restGuideArrearsPagePageGird(Integer offset, Integer size, String keywords) {
        logger.info("restGuideArrearsPagePageGird 后台显示导购欠款列表,入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<GuideVO> guideVOPage = this.maEmployeeService.queryGuideArrearsPage(page, size);
            List<GuideVO> guideVOList = guideVOPage.getList();
            logger.info("restGuideArrearsPagePageGird ,后台显示导购欠款列表成功", guideVOList.size());
            return new GridDataVO<GuideVO>().transform(guideVOList, guideVOPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restGuideArrearsPagePageGird EXCEPTION,发生未知错误，后台显示导购欠款列表失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 显示导购还款列表
     *
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/guideRepaymentPage/grid")
    public GridDataVO<GuideVO> restGuideRepaymentPagePageGird(Integer offset, Integer size, String keywords) {
        logger.info("restGuideRepaymentPagePageGird 后台显示导购还款列表,入参 offset:{},size:{},keywords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<GuideVO> guideVOPage = this.maEmployeeService.queryGuideRepaymentPage(page, size);
            List<GuideVO> guideVOList = guideVOPage.getList();
            logger.info("restGuideRepaymentPagePageGird ,后台显示导购还款列表成功", guideVOList.size());
            return new GridDataVO<GuideVO>().transform(guideVOList, guideVOPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restGuideRepaymentPagePageGird EXCEPTION,发生未知错误，后台显示导购还款列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 后台购买产品券条件查询导购
     *
     * @param offset
     * @param size
     * @param keywords
     * @param sellerQueryConditions
     * @return
     */
    @GetMapping(value = "/select/seller/{sellerQueryConditions}")
    public GridDataVO<MaEmployeeResponse> selectSellerBySellerNameOrSellerPhone(Integer offset, Integer size, String keywords, @PathVariable(value = "sellerQueryConditions") String sellerQueryConditions) {
        logger.info("selectSellerBySellerNameOrSellerPhone 后台购买产品券条件查询导购,入参 offset:{},size:{},keywords:{},sellerQueryConditions:{}", offset, size, keywords, sellerQueryConditions);
        try {
            Long cityId = null;
            Long storeId = null;
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageHelper.startPage(page, size);
            List<MaEmployeeResponse> employeeDOList = this.maEmployeeService.findEmployeeByCityIdAndStoreIdAndSellerNameAndSellerPhone(sellerQueryConditions, cityId, storeId);
            PageInfo<MaEmployeeResponse> maEmployeeDOPageInfo = new PageInfo<>(employeeDOList);
            List<MaEmployeeResponse> EmployeeDOList = maEmployeeDOPageInfo.getList();
            logger.warn("selectSellerBySellerNameOrSellerPhone ,后台购买产品券条件查询导购成功", EmployeeDOList.size());
            return new GridDataVO<MaEmployeeResponse>().transform(EmployeeDOList, maEmployeeDOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("selectSellerBySellerNameOrSellerPhone EXCEPTION,发生未知错误，后台购买产品券条件查询导购失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 更新导购二维码
     *
     * @param qrcodeUrl
     * @return
     */
    @PutMapping("/update/qrcode")
    public ResultDTO updateQrcode(String qrcodeUrl, Long empId) {
        if (qrcodeUrl == null || empId == null) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "保存失败，数据有误", null);
        }

        try {
            maEmployeeService.updateQrcode(qrcodeUrl, empId);
        } catch (Exception e) {
            logger.info("updateQrcode 更新导购二维码失败");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "保存失败,发送异常", null);
        }


        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "保存成功", null);
    }

    /**
     * 更新导购头像
     *
     * @param url
     * @return
     */
    @PutMapping("/update/photo")
    public ResultDTO updatePhoto(String url, Long empId) {
        if (url == null || empId == null) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "保存失败，数据有误", null);
        }

        try {
            maEmployeeService.updatePhoto(url,empId);
        } catch (Exception e) {
            logger.info("updatePhoto 更新导购头像失败");
            logger.info(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "保存失败,发送异常", null);
        }


        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "保存成功", null);
    }

    /**
     * 为导购生成二维码
     *
     * @param empId
     * @return
     */
    @PostMapping("/create/qrcode")
    public ResultDTO createQrcode(Long empId) {
        if (empId == null) {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "失败，数据有误", null);
        }

        AppEmployee employeeDetailVO = appEmployeeService.findById(empId);
        String photo = employeeDetailVO.getPicUrl();
        String registerUrl = qrcodeRegisterUrl+"/qrcode/register/" + empId;


        try {
            File qrcodeFile = null;
            File photoFile = null;
            if (photo == null || photo.equals("") || !photo.contains("http://")){
                qrcodeFile = qrcodeProduceService.createQrcode(registerUrl);
            }else{
                URL url = new URL(photo);
                photoFile = qrcodeProduceService.urlToFile(url);
                qrcodeFile = qrcodeProduceService.createQrcode(registerUrl,photoFile);

                if (photoFile.exists()){
                    photoFile.delete();
                }
            }

            // 上传二维码
            String picUrl = null;
            if (qrcodeFile.exists()) {

                InputStream stream = new FileInputStream(qrcodeFile);
                try {
                    picUrl = ImageClientUtils.getInstance().uploadImage(stream,qrcodeFile.length(),qrcodeFile.getName(),"seller/qrcode/");
                } catch (ImageClientException e) {
                    e.printStackTrace();
                    return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                            "二维码上传失败", null);
                }finally {
                    stream.close();
                }

                String url = ImageClientUtils.getInstance().getAbsProjectImagePath(picUrl);
                // 保存二维码url信息
                employeeDetailVO.setQrCode(url);
                appEmployeeService.update(employeeDetailVO);

                //删除文件
                if (qrcodeFile.exists()){
                    qrcodeFile.delete();
                }
            } else {
                //删除文件
                if (qrcodeFile.exists()){
                    qrcodeFile.delete();
                }
                return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                        "二维码上传失败", null);

            }
            /*FileUploadOSSUtils.uploadProfilePhoto(file, "profile/photo/");*/
            if (null != picUrl || "".equals(picUrl)) {
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, picUrl);
            } else {
                return new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE,
                        "图片上传失败", null);
            }


        } catch (WriterException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "生成失败，发送异常", null);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "生成失败，发送异常", null);
        }
    }

    /**
     * 为所有无二维码导购生成二维码
     * @return
     */
    @GetMapping("/create/qrcode/all")
    public ResultDTO createQrcodeAll(HttpServletResponse response) {
        ResultDTO resultDTO = null;
        // 返回二维码为空的导购
        List<AppEmployee> appEmployeeList = appEmployeeService.findQrcodeIsNull();
        try {

            PrintWriter out = response.getWriter();

            out.println("开始生成所有无二维码导购二维码！》》》》》》》》》》》》");
            for (AppEmployee appEmployee : appEmployeeList){

                resultDTO = this.createQrcode(appEmployee.getEmpId());
                if (resultDTO.getCode().equals(0)){
                    out.println(appEmployee.getName()+"：二维码生成成功！");
                }else{
                    out.println(appEmployee.getName()+"：二维码生成 失败失败！！！");
                }
                out.flush();
            }
            out.println("生成完毕 共 ："+appEmployeeList.size()+"个");

            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }


        return resultDTO;
    }

}
