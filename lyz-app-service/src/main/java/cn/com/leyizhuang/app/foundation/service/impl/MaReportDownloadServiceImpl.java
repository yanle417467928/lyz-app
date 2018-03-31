package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaReportDownloadDAO;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.NotPickGoodsReportDO;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.ReceiptsReportDO;
import cn.com.leyizhuang.app.foundation.service.MaReportDownloadService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/3/22
 */
@Service
public class MaReportDownloadServiceImpl implements MaReportDownloadService {

    @Autowired
    private MaReportDownloadDAO maReportDownloadDAO;

    @Override
    public PageInfo<ReceiptsReportDO> findReceiptsReportDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, String payType, String keywords, List<Long> storeIds, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<ReceiptsReportDO> receiptsReportDOS = maReportDownloadDAO.findReceiptsReportDOAll(cityId, storeId, storeType, startTime, endTime, payType, keywords, storeIds);
        return new PageInfo<>(receiptsReportDOS);
    }

    @Override
    public PageInfo<NotPickGoodsReportDO> findNotPickGoodsReportDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, String pickType, List<Long> storeIds, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<NotPickGoodsReportDO> notPickGoodsReportDOS = maReportDownloadDAO.findNotPickGoodsReportDOAll(cityId, storeId, storeType, startTime, endTime, pickType, storeIds);
        return new PageInfo<>(notPickGoodsReportDOS);
    }

    @Override
    public List<ReceiptsReportDO> receiptsDownload(Long cityId, Long storeId, String storeType, String startTime, String endTime, String payType, String keywords, List<Long> storeIds) {
        return maReportDownloadDAO.findReceiptsReportDOAll(cityId, storeId, storeType, startTime, endTime, payType, keywords, storeIds);
    }

    @Override
    public List<NotPickGoodsReportDO> notPickGoodsDownload(Long cityId, Long storeId, String storeType, String startTime, String endTime, String pickType, List<Long> storeIds) {
        return maReportDownloadDAO.findNotPickGoodsReportDOAll(cityId, storeId, storeType, startTime, endTime, pickType, storeIds);
    }
}
