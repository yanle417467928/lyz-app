package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditSheet;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialAuditDetailsResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialAuditSheetResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *装饰公司物料审核
 * Created by caiyu on 2017/10/17.
 */
@Repository
public interface MaterialAuditSheetDAO {
    //根据状态查询物料审核list
   List<MaterialAuditSheet> queryListByStatus(int status);
    //根据工人id查询物料审核list
   List<MaterialAuditSheet> queryListByEmployeeID(@Param("employeeID") Long employeeID);
   //添加物料审核单
   void addMaterialAuditSheet(MaterialAuditSheet materialAuditSheet);
   //修改物料审核单
   void modifyMaterialAuditSheet(MaterialAuditSheet materialAuditSheet);
   //修改物料审核单状态
    void modifyStatus(@Param("status") int status, @Param("auditNumber") String auditNumber);
    //根据物料审核单号查找物料单
    MaterialAuditSheet queryByAuditNo(@Param("auditNumber") String auditNumber);
    //根据物料审核单号查询物料单（返回返回值对象）
    MaterialAuditDetailsResponse queryDetailsByAuditNo(@Param("auditNo") String auditNo);
    //根据工人id与料单状态查询list
    List<MaterialAuditSheetResponse> queryListByEmployeeIDAndStatus(@Param("employeeID") Long employeeID,@Param("status") Integer status);
    //项目经理根据状态查看所属装饰公司下的所有物料审核单
    List<MaterialAuditSheet> queryListByStoreIDAndStatus(@Param("storeID") Long storeID,@Param("status") Integer status);
}
