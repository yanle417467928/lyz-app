package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditGoodsInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by caiyu on 2017/10/17.
 */
@Repository
public interface MaterialAuditGoodsInfoDAO {
    //新增物料审核单商品信息
    void addMaterialAuditGoodsInfo(MaterialAuditGoodsInfo materialAuditGoodsInfo);

    //修改物料审核单商品信息
    void modifyMaterialAuditGoodsInfo(MaterialAuditGoodsInfo materialAuditGoodsInfo);

    //根据物料审核单头id查找对应所有商品
    List<MaterialAuditGoodsInfo> queryListByAuditHeaderID(Long auditHeaderID);

    //根据物料审核单头id查询对应所有商品数量总和
    int querySumQtyByAuditHeaderID(@Param("auditHeaderID") Long auditHeaderID);


}
