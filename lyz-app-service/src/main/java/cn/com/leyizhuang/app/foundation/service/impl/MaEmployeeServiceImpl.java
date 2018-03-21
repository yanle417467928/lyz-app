package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppEmployeeDAO;
import cn.com.leyizhuang.app.foundation.dao.MaEmployeeDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeType;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaEmployeeResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.MaEmployeeService;
import cn.com.leyizhuang.app.foundation.vo.management.employee.DecorativeEmployeeDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.employee.EmployeeDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.employee.EmployeeVO;
import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class MaEmployeeServiceImpl implements MaEmployeeService{

    @Autowired
    private MaEmployeeDAO maEmployeeDAO;

    @Autowired
    private AppEmployeeDAO appEmployeeDAO;

    @Override
    public PageInfo<EmployeeDO> queryPageVO(Integer page, Integer size){
        PageHelper.startPage(page, size);
        List<EmployeeDO> pageEmployeeList = this.maEmployeeDAO.findAllEmployee();
        return new PageInfo<>(pageEmployeeList);
    }

    @Override
    public EmployeeDetailVO queryEmployeeById(Long id){
        EmployeeDO employeeDO = this.maEmployeeDAO.queryEmployeeById(id);
        EmployeeDetailVO employeeVO=  EmployeeDetailVO.transform(employeeDO);
        return employeeVO;
    }


    @Override
    public List<EmployeeVO> findGuideListById(Long storeId) {
        List<EmployeeDO> guideList = maEmployeeDAO.findGuideListById(storeId);
        return EmployeeVO.transform(guideList);
    }

    @Override
    public List<EmployeeType> findEmpTypeByStoreId(Long storeId) {
        List<EmployeeDO> empDOList = maEmployeeDAO.findEmpTypeByStoreId(storeId);
        List<EmployeeType> empTypeList =EmployeeType.transform(empDOList);
        return empTypeList;
    }

    @Override
    public List<EmployeeType> findEmpTypeByCityId(Long cityId) {
        List<EmployeeDO> empDOList = maEmployeeDAO.findEmpTypeByCityId(cityId);
        List<EmployeeType> empTypeList =EmployeeType.transform(empDOList);
        return empTypeList;
    }

    @Override
    public List<EmployeeType> findEmpTypeList() {
        List<EmployeeDO> empDOList = maEmployeeDAO.findEmpTypeList();
        List<EmployeeType> empTypeList =EmployeeType.transform(empDOList);
        return empTypeList;
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
    public DecorativeEmployeeDetailVO queryDecorativeEmployeeById(Long id){
        EmployeeDO employeeDO = this.maEmployeeDAO.queryEmployeeById(id);
        return  DecorativeEmployeeDetailVO.transform(employeeDO);
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

    @Override
    public PageInfo<GuideVO> queryGuideVOPage(Integer page, Integer size){
        PageHelper.startPage(page, size);
        List<GuideVO> pageGuideVOList = this.maEmployeeDAO.findAllGuide();
        return new PageInfo<>(pageGuideVOList);
    }


    @Override
    public GuideVO queryGuideVOById(Long id){
        GuideVO guideVO = this.maEmployeeDAO.queryGuideVOById(id);
        return guideVO;
    }

    @Override
    public PageInfo<GuideVO> queryGuideVOByCondition(Integer page, Integer size,Long cityId, Long storeId){
        if(-1==cityId){
          cityId=null;
        }
        if(-1==storeId){
            storeId=null;
        }
        PageHelper.startPage(page, size);
        List<GuideVO> pageGuideVOList = this.maEmployeeDAO.queryGuideVOByCondition(cityId,storeId);
        return new PageInfo<>(pageGuideVOList);
    }

    @Override
    public PageInfo<GuideVO> queryGuideVOByInfo(Integer page, Integer size,String queryGuideVOInfo){
        PageHelper.startPage(page, size);
        List<GuideVO> pageGuideVOList = this.maEmployeeDAO.queryGuideVOByInfo(queryGuideVOInfo);
        return new PageInfo<>(pageGuideVOList);
    }

    @Override
    public EmployeeDO findEmployeeDOByEmpId(Long id) {
        return this.maEmployeeDAO.findEmployeeDOByEmpId(id);
    }

    @Override
    public List<MaEmployeeResponse> findMaEmployeeByCityIdAndStoreId(List<Long> storeIds) {
        return this.maEmployeeDAO.findEmployeeByCityIdAndStoreId(storeIds);
    }

    /**
     * 后台购买产品券条件查询导购
     *
     * @return
     */
    @Override
    public List<MaEmployeeResponse> findEmployeeByCityIdAndStoreIdAndSellerNameAndSellerPhone(String sellerQueryConditions, Long cityId, Long storeId){
        return this.maEmployeeDAO.findEmployeeByCityIdAndStoreIdAndSellerNameAndSellerPhone(sellerQueryConditions, cityId, storeId);
    }

    @Override
    public PageInfo<GuideVO> queryGuideArrearsPage(Integer page, Integer size){
        PageHelper.startPage(page, size);
        List<GuideVO> pageGuideVOList = this.maEmployeeDAO.queryGuideArrears();
        return new PageInfo<>(pageGuideVOList);
    }

    @Override
    public PageInfo<GuideVO> queryGuideRepaymentPage(Integer page, Integer size){
        PageHelper.startPage(page, size);
        List<GuideVO> pageGuideVOList = this.maEmployeeDAO.queryGuideRepayment();
        return new PageInfo<>(pageGuideVOList);
    }

    /**
     * 更新导购二维码
     * @param qrcodeUrl
     * @param empId
     */
    @Override
    public void updateQrcode(String qrcodeUrl , Long empId){
        AppEmployee employeeDO = appEmployeeDAO.findById(empId);

        if (employeeDO != null){
            employeeDO.setQrCode(qrcodeUrl);
            appEmployeeDAO.update(employeeDO);
        }
    }

    /**
     * 更新导购头像
     * @param url
     * @param empId
     */
    @Override
    public void updatePhoto(String url , Long empId){
        AppEmployee employeeDO = appEmployeeDAO.findById(empId);

        if (employeeDO != null){
            employeeDO.setPicUrl(url);
            appEmployeeDAO.update(employeeDO);
        }
    }
}
