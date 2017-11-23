package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaterialAuditGoodsInfoDAO;
import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditGoodsInfo;
import cn.com.leyizhuang.app.foundation.service.MaterialAuditGoodsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by caiyu on 2017/10/18.
 */
@Service
@Transactional
public class MaterialAuditGoodsInfoServiceImpl implements MaterialAuditGoodsInfoService {
    @Autowired
    private MaterialAuditGoodsInfoDAO materialAuditGoodsInfoDAO;

    @Override
    public void addMaterialAuditGoodsInfo(MaterialAuditGoodsInfo materialAuditGoodsInfo) {
        materialAuditGoodsInfoDAO.addMaterialAuditGoodsInfo(materialAuditGoodsInfo);
    }

    @Override
    public void modifyMaterialAuditGoodsInfo(MaterialAuditGoodsInfo materialAuditGoodsInfo) {
        materialAuditGoodsInfoDAO.modifyMaterialAuditGoodsInfo(materialAuditGoodsInfo);
    }

    @Override
    public List<MaterialAuditGoodsInfo> queryListByAuditHeaderID(Long auditHeaderID) {
        if (null == auditHeaderID) {
            return null;
        }
        return materialAuditGoodsInfoDAO.queryListByAuditHeaderID(auditHeaderID);
    }

    @Override
    public int querySumQtyByAuditHeaderID(Long auditHeaderID) {
        return materialAuditGoodsInfoDAO.querySumQtyByAuditHeaderID(auditHeaderID);
    }
}
