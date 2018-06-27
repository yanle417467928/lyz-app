package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaFitBillDAO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillInfoDO;
import cn.com.leyizhuang.app.foundation.service.MaFitBillService;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.MaFitBillVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuh
 * @date 2018/3/15
 */
@Service
public class MaFitBillServiceImpl implements MaFitBillService {

    private final Logger logger = LoggerFactory.getLogger(MaFitBillServiceImpl.class);

    @Autowired
    private MaFitBillDAO maFitBillDAO;


    @Override
    public PageInfo<MaFitBillVO> getFitNotOutBill(Integer page, Integer size, List<Long> storeIds, String keywords) {
        PageHelper.startPage(page, size);
        List<MaFitBillVO> fitBillVOList = this.maFitBillDAO.getFitNotOutBill(storeIds, keywords);
        return new PageInfo<>(fitBillVOList);
    }

    @Override
    public PageInfo<MaFitBillVO> getHistoryFitBill(Integer page, Integer size, List<Long> storeIds, String keywords) {
        PageHelper.startPage(page, size);
        List<MaFitBillVO> fitBillVOList = this.maFitBillDAO.getHistoryFitBill(storeIds, keywords);
        return new PageInfo<>(fitBillVOList);
    }

    @Override
    public PageInfo<MaFitBillVO> getNoPayOrderBillByBillNo(Integer page, Integer size, List<Long> storeIds, String billNo,String startTime,String endTime,String orderNo) {
        PageHelper.startPage(page, size);
        return null;
    }

    @Override
    public PageInfo<MaFitBillVO> getPayOrderBillByBillNo(Integer page, Integer size, List<Long> storeIds, String billNo,String startTime,String endTime,String orderNo) {
        PageHelper.startPage(page, size);
        return null;
    }

    @Override
    public BillInfoDO getFitBillByBillNo(String billNo) {
        if (null == billNo) {
            return null;
        }
        BillInfoDO maFitBillVO = this.maFitBillDAO.getFitBillByBillNo(billNo);
        return maFitBillVO;
    }


}
