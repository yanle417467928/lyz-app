package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.utils.oss.FileUploadOSSUtils;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.service.MaEmployeeService;
import cn.com.leyizhuang.app.foundation.vo.EmployeeVO;
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

import java.util.List;

@RestController
@RequestMapping(value = MaEmployeeRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaEmployeeRestController extends BaseRestController  {

    protected static final String PRE_URL = "/rest/employees";

    private final Logger logger = LoggerFactory.getLogger(MaCustomerRestController.class);

    @Autowired
    private MaEmployeeService maEmployeeService;

    /**
     * 显示所有员工信息
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
     * @param id
     * @return
     */
    @GetMapping(value = "{id}")
    public ResultDTO<EmployeeVO> queryEmployeeById(@PathVariable(value = "id") Long id) {
        EmployeeVO employeeVO = this.maEmployeeService.queryEmployeeById(id);
        if (null == employeeVO) {
            logger.warn("查找品牌失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,null, employeeVO);
        }
    }


    /**
     * 查询该门店下的导购
     * @param storeId
     * @return
     */
    @GetMapping(value = "/findGuidesListById/{storeId}")
    public List<EmployeeVO> findGuidesListById(@PathVariable("storeId") Long storeId) {
        return maEmployeeService.findGuideListById(storeId);
    }


    /**
     * 查询该门店下的员工类型
     * @param storeId
     * @param
     * @return
     */
    @GetMapping(value = "/findEmpTypeByStoreId/{storeId}")
    public List<EmployeeVO> findEmpTypeByStoreId( @PathVariable("storeId") Long storeId) {
        return maEmployeeService.findEmpTypeByStoreId(storeId);
    }

    /**
     * 查询该城市下的员工类型
     * @param
     * @param cityId
     * @return
     */
    @GetMapping(value = "/findEmpTypeByCityId/{cityId}")
    public List<EmployeeVO> findEmpTypeByCityId(@PathVariable("cityId") Long cityId) {
        return maEmployeeService.findEmpTypeByCityId(cityId);
    }


    /**
     * 查询所有员工类型
     * @param
     * @return
     */
    @GetMapping(value = "/findEmpTypeList")
    public List<EmployeeVO> findEmpTypeList() {
        return maEmployeeService.findEmpTypeList();
    }


    /**
     * 根据身份信息查询员工
     * @param offset
     * @param size
     * @param keywords
     * @param identityType
     * @param storeId
     * @return
     */
    @GetMapping(value = "/page/conditionGrid")
    public GridDataVO<EmployeeVO> restEmployeesPageGirdByCondition(Integer offset, Integer size, String keywords,@RequestParam(value = "identityType") String identityType,
           @RequestParam(value = "storeId") Long storeId,@RequestParam(value = "cityId") Long cityId,@RequestParam(value = "enabled") String enabled) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<EmployeeDO> employeesPage = this.maEmployeeService.queryPageVOByCondition(page, size,identityType,storeId,cityId,enabled);
        List<EmployeeDO> employeesList = employeesPage.getList();
        List<EmployeeVO> employeesVOList = EmployeeVO.transform(employeesList);
        return new GridDataVO<EmployeeVO>().transform(employeesVOList, employeesPage.getTotal());
    }

    /**
     * 根据搜索信息查询员工
     * @param offset
     * @param size
     * @param keywords
     * @param queryEmpInfo
     * @return
     */
    @GetMapping(value = "/page/infoGrid/{queryEmpInfo}")
    public GridDataVO<EmployeeVO> restEmployeesPageGirdByInfo(Integer offset, Integer size, String keywords,@PathVariable(value = "queryEmpInfo") String queryEmpInfo) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<EmployeeDO> employeesPage = this.maEmployeeService.queryPageVOByInfo(page, size,queryEmpInfo);
        List<EmployeeDO> employeesList = employeesPage.getList();
        List<EmployeeVO> employeesVOList = EmployeeVO.transform(employeesList);
        return new GridDataVO<EmployeeVO>().transform(employeesVOList, employeesPage.getTotal());
    }

}
