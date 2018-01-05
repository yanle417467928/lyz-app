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
import com.github.pagehelper.PageInfo;
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
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<EmployeeDO> employeesPage = this.maEmployeeService.queryPageVO(page, size);
        List<EmployeeDO> employeesList = employeesPage.getList();
        List<EmployeeVO> employeesVOList = EmployeeVO.transform(employeesList);
        return new GridDataVO<EmployeeVO>().transform(employeesVOList, employeesPage.getTotal());
    }


    /**
     * 查询该id下的员工详情信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "{id}")
    public ResultDTO<EmployeeDetailVO> queryEmployeeById(@PathVariable(value = "id") Long id) {
        EmployeeDetailVO employeeVO = this.maEmployeeService.queryEmployeeById(id);
        if (null == employeeVO) {
            logger.warn("查找员工失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, employeeVO);
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
        return maEmployeeService.findGuideListById(storeId);
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
        List<EmployeeType> employeeTypeList = maEmployeeService.findEmpTypeByStoreId(storeId);
        return employeeTypeList;
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
        List<EmployeeType> employeeTypeList =  maEmployeeService.findEmpTypeByCityId(cityId);
        return  employeeTypeList;
    }


    /**
     * 查询所有员工类型(下拉框)
     *
     * @param
     * @return
     */
    @GetMapping(value = "/findEmpTypeList")
    public List<EmployeeType> findEmpTypeList() {
        List<EmployeeType> employeeTypeList = maEmployeeService.findEmpTypeList();
        return  employeeTypeList;
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
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<EmployeeDO> employeesPage = this.maEmployeeService.queryPageVOByCondition(page, size, identityType, storeId, cityId, enabled);
        List<EmployeeDO> employeesList = employeesPage.getList();
        List<EmployeeVO> employeesVOList = EmployeeVO.transform(employeesList);
        return new GridDataVO<EmployeeVO>().transform(employeesVOList, employeesPage.getTotal());
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
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<EmployeeDO> employeesPage = this.maEmployeeService.queryPageVOByInfo(page, size, queryEmpInfo);
        List<EmployeeDO> employeesList = employeesPage.getList();
        List<EmployeeVO> employeesVOList = EmployeeVO.transform(employeesList);
        return new GridDataVO<EmployeeVO>().transform(employeesVOList, employeesPage.getTotal());
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
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GuideVO> guideVOPage = this.maEmployeeService.queryGuideVOPage(page, size);
        List<GuideVO> guideVOList = guideVOPage.getList();
        return new GridDataVO<GuideVO>().transform(guideVOList, guideVOPage.getTotal());
    }


    /**
     * 查询员工额度详情信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/guide/{id}")
    public ResultDTO<GuideVO> queryGuideVOById(@PathVariable(value = "id") Long id) {
        GuideVO guideVO = this.maEmployeeService.queryGuideVOById(id);
        if (null == guideVO) {
            logger.warn("查找员工额度失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, guideVO);
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
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GuideVO> guideVOPage = this.maEmployeeService.queryGuideVOByCondition(page, size, cityId, storeId);
        List<GuideVO> guideVOList = guideVOPage.getList();
        return new GridDataVO<GuideVO>().transform(guideVOList, guideVOPage.getTotal());
    }


    /**
     * 根据搜索信息查询导购额度
     * @param offset
     * @param size
     * @param keywords
     * @param queryGuideVOInfo
     * @return
     */
    @GetMapping(value = "/guidePage/infoGrid/{queryGuideVOInfo}")
    public GridDataVO<GuideVO> queryGuideVOByInfo(Integer offset, Integer size, String keywords,@PathVariable(value = "queryGuideVOInfo") String queryGuideVOInfo) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<GuideVO> guideVOPage = this.maEmployeeService.queryGuideVOByInfo(page, size,queryGuideVOInfo);
        List<GuideVO> guideVOList = guideVOPage.getList();
        return new GridDataVO<GuideVO>().transform(guideVOList, guideVOPage.getTotal());
    }

}
