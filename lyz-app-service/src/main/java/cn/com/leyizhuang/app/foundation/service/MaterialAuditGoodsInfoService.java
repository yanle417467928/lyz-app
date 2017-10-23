package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditGoodsInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by caiyu on 2017/10/18.
 */
public interface MaterialAuditGoodsInfoService {
    //新增物料审核单商品信息
    void addMaterialAuditGoodsInfo(MaterialAuditGoodsInfo materialAuditGoodsInfo);
    //修改物料审核单商品信息
    void modifyMaterialAuditGoodsInfo(MaterialAuditGoodsInfo materialAuditGoodsInfo);
    //根据物料审核单头id查找对应所有商品
    List<MaterialAuditGoodsInfo> queryListByAuditHeaderID(Long auditHeaderID);
    //根据物料审核单头id查询对应所有商品数量总和
    int querySumQtyByAuditHeaderID(Long auditHeaderID);

}
