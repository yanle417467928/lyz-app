package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeType;
import cn.com.leyizhuang.app.foundation.service.MaEmployeeService;
import cn.com.leyizhuang.app.foundation.vo.management.employee.EmployeeDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideVO;
import cn.com.leyizhuang.app.foundation.vo.management.employee.EmployeeVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = MaEmployeeRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaEmployeeRestController extends BaseRestController {

    protected static final String PRE_URL = "/rest/employees";

    private final Logger logger = LoggerFactory.getLogger(MaCustomerRestController.class);

    @Autowired
    private MaEmployeeService maEmployeeService;

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
     * @param cityId
     * @param storeId
     * @return
     */
    @GetMapping(value = "/select/seller")
    public GridDataVO<EmployeeDO> selectSeller(Integer offset, Integer size, String keywords, @RequestParam(value = "cityId") Long cityId, @RequestParam(value = "storeId") Long storeId) {
        logger.info("selectSeller 后台购买产品券选择导购,入参 offset:{},size:{},keywords:{},cityId:{},storeId:{}", offset, size, keywords, cityId, storeId);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageHelper.startPage(page, size);
            List<EmployeeDO> employeeDOList = this.maEmployeeService.findEmployeeByCityIdAndStoreId(cityId, storeId);

            PageInfo<EmployeeDO> maEmployeeDOPageInfo = new PageInfo<>(employeeDOList);
            List<EmployeeDO> EmployeeDOList = maEmployeeDOPageInfo.getList();
            logger.warn("selectSeller ,后台购买产品券选择导购成功", EmployeeDOList.size());
            return new GridDataVO<EmployeeDO>().transform(EmployeeDOList, maEmployeeDOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("selectSeller EXCEPTION,发生未知错误，后台购买产品券选择导购失败");
            logger.warn("{}", e);
            return null;
        }
    }

}
