package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaterialAuditSheetDAO;
import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditSheet;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialAuditDetailsResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialAuditSheetResponse;
import cn.com.leyizhuang.app.foundation.service.MaterialAuditSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by caiyu on 2017/10/17.
 */
@Service
@Transactional
public class MaterialAuditSheetServiceImpl implements MaterialAuditSheetService {
    @Autowired
    private MaterialAuditSheetDAO materialAuditSheetDAO;
    @Override
    public List<MaterialAuditSheet> queryListByStatus(int status) {
        return this.materialAuditSheetDAO.queryListByStatus(status);
    }

    @Override
    public List<MaterialAuditSheet> queryListByEmployeeID(Long employeeID) {
        return this.materialAuditSheetDAO.queryListByEmployeeID(employeeID);
    }

    @Override
    public void addMaterialAuditSheet(MaterialAuditSheet materialAuditSheet) {
        this.materialAuditSheetDAO.addMaterialAuditSheet(materialAuditSheet);
    }

    @Override
    public void modifyMaterialAuditSheet(MaterialAuditSheet materialAuditSheet) {
        this.materialAuditSheetDAO.modifyMaterialAuditSheet(materialAuditSheet);
    }

    @Override
    public void modifyStatus(int status,String auditNumber) {
        this.materialAuditSheetDAO.modifyStatus(status,auditNumber);
    }

    @Override
    public MaterialAuditSheet queryByAuditNo(String auditNumber) {
        return this.materialAuditSheetDAO.queryByAuditNo(auditNumber);
    }

    @Override
    public MaterialAuditDetailsResponse queryDetailsByAuditNo(String auditNo) {
        return this.materialAuditSheetDAO.queryDetailsByAuditNo(auditNo);
    }

    @Override
    public List<MaterialAuditSheetResponse> queryListByEmployeeIDAndStatus(Long employeeID, Integer status) {
        return this.materialAuditSheetDAO.queryListByEmployeeIDAndStatus(employeeID,status);
    }

    @Override
    public List<MaterialAuditSheet> queryListByStoreIDAndStatus(Long storeID, Integer status) {
        return this.materialAuditSheetDAO.queryListByStoreIDAndStatus(storeID,status);
    }
}
