package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditSheet;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialAuditDetailsResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialAuditSheetResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by caiyu on 2017/10/17.
 */
public interface MaterialAuditSheetService {
    //根据状态查询物料审核list
    List<MaterialAuditSheet> queryListByStatus(int status);
    //根据工人id查询物料审核list
    List<MaterialAuditSheet> queryListByEmployeeID(Long employeeID);
    //添加物料审核单
    void addMaterialAuditSheet(MaterialAuditSheet materialAuditSheet);
    //修改物料审核单
    void modifyMaterialAuditSheet(MaterialAuditSheet materialAuditSheet);
    //修改物料审核单状态
    void modifyStatus(int status,String auditNumber);
    //根据物料审核单号查询物料单
    MaterialAuditSheet queryByAuditNo(String auditNumber);
    //根据物料审核单号查询物料单（返回返回值对象）
    MaterialAuditDetailsResponse queryDetailsByAuditNo(String auditNo);
    //根据工人id与料单状态查询list
    List<MaterialAuditSheetResponse> queryListByEmployeeIDAndStatus(Long employeeID, Integer status);
    //项目经理根据状态查看所属装饰公司下的所有物料审核单
    List<MaterialAuditSheet> queryListByStoreIDAndStatus(Long storeID,Integer status);
}
