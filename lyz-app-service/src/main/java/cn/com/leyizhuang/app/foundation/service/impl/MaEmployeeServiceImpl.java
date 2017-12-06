package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaEmployeeDAO;
import cn.com.leyizhuang.app.foundation.pojo.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.service.MaCityDeliveryTimeService;
import cn.com.leyizhuang.app.foundation.service.MaEmployeeService;
import cn.com.leyizhuang.app.foundation.vo.EmployeeVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MaEmployeeServiceImpl implements MaEmployeeService{

    @Autowired
    private MaEmployeeDAO maEmployeeDAO;
    @Override
    public PageInfo<EmployeeDO> queryPageVO(Integer page, Integer size){
        PageHelper.startPage(page, size);
        List<EmployeeDO> pageEmployeeList = this.maEmployeeDAO.findAllEmployee();
        return new PageInfo<>(pageEmployeeList);
    }

    @Override
    public EmployeeVO queryEmployeeById(Long id){
        EmployeeDO employeeDO = this.maEmployeeDAO.queryEmployeeById(id);
        EmployeeVO employeeVO=  EmployeeVO.transform(employeeDO);
        return employeeVO;
    }

    @Override
    public PageInfo<EmployeeDO> queryPageVOByCityId(Integer page, Integer size,Long cityId){
        PageHelper.startPage(page, size);
        List<EmployeeDO> pageEmployeeList = this.maEmployeeDAO.findEmployeeByCityId(cityId);
        return new PageInfo<>(pageEmployeeList);
    }

    @Override
    public List<EmployeeVO> findGuideListById(Long storeId) {
        List<EmployeeDO> guideList = maEmployeeDAO.findGuideListById(storeId);
        return EmployeeVO.transform(guideList);
    }

    @Override
    public List<EmployeeVO> findEmpTypeListById(Long id) {
        List<EmployeeDO> EmpTypeList = maEmployeeDAO.findEmpTypeListById(id);
        return EmployeeVO.transform(EmpTypeList);
    }

    @Override
    public PageInfo<EmployeeDO> queryPageVOByStoreId(Integer page, Integer size,Long storeId){
        PageHelper.startPage(page, size);
        List<EmployeeDO> pageEmployeeList = this.maEmployeeDAO.findEmployeeByStoreId(storeId);
        return new PageInfo<>(pageEmployeeList);
    }

    @Override
    public PageInfo<EmployeeDO> queryPageVOByType(Integer page, Integer size,String identityType,Long storeId){
        PageHelper.startPage(page, size);
        String identityTypeConvert;
        if("导购".equals(identityType)){
            identityTypeConvert="SELLER";
        }else if("装饰经理".equals(identityType)){
            identityTypeConvert="DECORATE_MANAGER";
        }else if("装饰工人".equals(identityType)){
            identityTypeConvert="DECORATE_EMPLOYEE";
        }else{
            identityTypeConvert="DELIVERY_CLERK";
        }
        List<EmployeeDO> pageEmployeeList = this.maEmployeeDAO.queryPageVOByType(identityTypeConvert,storeId);
        return new PageInfo<>(pageEmployeeList);
    }

    @Override
    public PageInfo<EmployeeDO> queryPageVOByInfo(Integer page, Integer size,String queryEmpInfo){
        PageHelper.startPage(page, size);
        List<EmployeeDO> pageEmployeeList = this.maEmployeeDAO.findEmployeeByInfo(queryEmpInfo);
        return new PageInfo<>(pageEmployeeList);
    }
}
