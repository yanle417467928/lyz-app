package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaEmployeeDAO;
import cn.com.leyizhuang.app.foundation.pojo.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.service.MaCityDeliveryTimeService;
import cn.com.leyizhuang.app.foundation.service.MaEmployeeService;
import cn.com.leyizhuang.app.foundation.vo.DecorativeEmployeeVO;
import cn.com.leyizhuang.app.foundation.vo.EmployeeVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
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
    public List<EmployeeVO> findGuideListById(Long storeId) {
        List<EmployeeDO> guideList = maEmployeeDAO.findGuideListById(storeId);
        return EmployeeVO.transform(guideList);
    }

    @Override
    public List<EmployeeVO> findEmpTypeByStoreId(Long storeId) {
        List<EmployeeDO> empTypeList = maEmployeeDAO.findEmpTypeByStoreId(storeId);
        return EmployeeVO.transform(empTypeList);
    }

    @Override
    public List<EmployeeVO> findEmpTypeByCityId(Long cityId) {
        List<EmployeeDO> empTypeList = maEmployeeDAO.findEmpTypeByCityId(cityId);
        return EmployeeVO.transform(empTypeList);
    }

    @Override
    public List<EmployeeVO> findEmpTypeList() {
        List<EmployeeDO> empTypeList = maEmployeeDAO.findEmpTypeList();
        return EmployeeVO.transform(empTypeList);
    }

    @Override
    public PageInfo<EmployeeDO> queryPageVOByCondition(Integer page, Integer size,String identityType,Long storeId,Long cityId,String enabled){
        PageHelper.startPage(page, size);
        String identityTypeConvert;
        if("导购".equals(identityType)){
            identityTypeConvert="SELLER";
        }else if("装饰经理".equals(identityType)){
            identityTypeConvert="DECORATE_MANAGER";
        }else if("装饰工人".equals(identityType)){
            identityTypeConvert="DECORATE_EMPLOYEE";
        }else if("配送员".equals(identityType)){
            identityTypeConvert="DELIVERY_CLERK";
        }else {
            identityTypeConvert=null;
        }
        if(-1==cityId){
            cityId=null;
        }
        if("-1".equals(enabled)){
            enabled=null;
        }
        //如果 门店有id 就根据门店查询 如果没有 按照城市查询
        List<EmployeeDO> pageEmployeeList=new ArrayList<EmployeeDO>();
        if(-1!=storeId){
             pageEmployeeList = this.maEmployeeDAO.queryPageVOByStoreCondition(identityTypeConvert,storeId,enabled);
        }else{
            pageEmployeeList = this.maEmployeeDAO.queryPageVOByCityCondition(identityTypeConvert,cityId,enabled);
        }
        return new PageInfo<>(pageEmployeeList);
    }

    @Override
    public PageInfo<EmployeeDO> queryPageVOByInfo(Integer page, Integer size,String queryEmpInfo){
        PageHelper.startPage(page, size);
        List<EmployeeDO> pageEmployeeList = this.maEmployeeDAO.findEmployeeByInfo(queryEmpInfo);
        return new PageInfo<>(pageEmployeeList);
    }


    @Override
    public PageInfo<EmployeeDO> queryDecorativeEmpPageVO(Integer page, Integer size){
        PageHelper.startPage(page, size);
        List<EmployeeDO> pageEmployeeList = this.maEmployeeDAO.queryDecorativeEmpPageVO();
        return new PageInfo<>(pageEmployeeList);
    }

    @Override
    public DecorativeEmployeeVO queryDecorativeEmployeeById(Long id){
        EmployeeDO employeeDO = this.maEmployeeDAO.queryEmployeeById(id);
        DecorativeEmployeeVO decorativeEmployeeVO=  DecorativeEmployeeVO.transform(employeeDO);
        return decorativeEmployeeVO;
    }

    @Override
    public PageInfo<EmployeeDO> queryDecorativeEmpPageVOByInfo(Integer page, Integer size,String queryEmpInfo){
        PageHelper.startPage(page, size);
        List<EmployeeDO> pageEmployeeList = this.maEmployeeDAO.queryDecorativeEmpPageVOByInfo(queryEmpInfo);
        return new PageInfo<>(pageEmployeeList);
    }

    @Override
    public PageInfo<EmployeeDO> findDecorativeEmpByCondition(Integer page, Integer size,String enabled,String diyId,String identityType){
        PageHelper.startPage(page, size);
        if("-1".equals(enabled)){
            enabled=null;
        }
        if("-1".equals(diyId)){
            diyId=null;
        }
        if("-1".equals(identityType)){
            identityType=null;
        }
        List<EmployeeDO> pageEmployeeList = this.maEmployeeDAO.findDecorativeEmpByCondition(enabled,diyId,identityType);
        return new PageInfo<>(pageEmployeeList);
    }
}
