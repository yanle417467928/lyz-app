package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.pojo.store.StoreDO;
import cn.com.leyizhuang.app.foundation.service.MaEmployeeService;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.vo.DecorativeCompanyVO;
import cn.com.leyizhuang.app.foundation.vo.DecorativeEmployeeVO;
import cn.com.leyizhuang.app.foundation.vo.EmployeeVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = MaDecorativeCompanyEmpRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaDecorativeCompanyEmpRestController extends BaseRestController {

    protected static final String PRE_URL = "/rest/decorativeEmp";

    private final Logger logger = LoggerFactory.getLogger(MaDecorativeCompanyEmpRestController.class);

    @Autowired
    private MaEmployeeService maEmployeeService;

    /**
     *装饰公司员工列表
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<DecorativeEmployeeVO> restDecorativeEmpPageGird(Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<EmployeeDO> employeesPage = this.maEmployeeService.queryDecorativeEmpPageVO(page, size);
        List<EmployeeDO> employeesList = employeesPage.getList();
        List<DecorativeEmployeeVO> decorativeVOList = DecorativeEmployeeVO.transform(employeesList);
        return new GridDataVO<DecorativeEmployeeVO>().transform(decorativeVOList, employeesPage.getTotal());
    }

    /**
     *查询装饰公司员工详情信息
     * @param id
     * @return
     */
    @GetMapping(value = "/page/{id}")
    public ResultDTO<DecorativeEmployeeVO> queryEmployeeById(@PathVariable(value = "id") Long id) {
        DecorativeEmployeeVO decorativeEmployeeVO = this.maEmployeeService.queryDecorativeEmployeeById(id);
        if (null == decorativeEmployeeVO) {
            logger.warn("查找装饰公司员工失败：Role(id = {}) == null", id);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "指定数据不存在，请联系管理员", null);
        } else {
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,null, decorativeEmployeeVO);
        }
    }

    /**
     *根据下拉框筛选装饰公司工人
     * @param enabled
     * @param identityType
     * @param diyId
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/conditionGrid")
    public GridDataVO<DecorativeEmployeeVO> findDecorativeEmpByCondition(@RequestParam("enabled") String enabled, @RequestParam("identityType") String identityType, @RequestParam("diyId") String diyId,Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<EmployeeDO> employeesPage = this.maEmployeeService.findDecorativeEmpByCondition(page, size, enabled, diyId,identityType);
        List<EmployeeDO> employeesList = employeesPage.getList();
        List<DecorativeEmployeeVO> pageDecorativeCompanyList = DecorativeEmployeeVO.transform(employeesList);
        return new GridDataVO<DecorativeEmployeeVO>().transform(pageDecorativeCompanyList, employeesPage.getTotal());
    }

    /**
     * 根据姓名  电话 登录名查询装饰公司员工
     * @param queryEmpInfo
     * @param offset
     * @param size
     * @param keywords
     * @return
     */
    @GetMapping(value = "/page/infoGrid/{queryEmpInfo}")
    public GridDataVO<DecorativeEmployeeVO> findDecorativeEmpByNameOrCode(@PathVariable(value = "queryEmpInfo") String queryEmpInfo, Integer offset, Integer size, String keywords) {
        size = getSize(size);
        Integer page = getPage(offset, size);
        PageInfo<EmployeeDO> employeesPage = this.maEmployeeService.queryDecorativeEmpPageVOByInfo(page, size,queryEmpInfo);
        List<EmployeeDO> employeesList = employeesPage.getList();
        List<DecorativeEmployeeVO> decorativeEmployeeVOList = DecorativeEmployeeVO.transform(employeesList);
        return new GridDataVO<DecorativeEmployeeVO>().transform(decorativeEmployeeVOList, employeesPage.getTotal());
    }
}
