package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.WithdrawDAO;
import cn.com.leyizhuang.app.foundation.pojo.WithdrawRefundInfo;
import cn.com.leyizhuang.app.foundation.service.WithdrawService;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 提现API实现
 *
 * @author Richard
 * Created on 2018-01-11 14:44
 **/
@Service
public class WithdrawServiceImpl implements WithdrawService {


    @Resource
    private WithdrawDAO withdrawDAO;

    @Override
    public void saveWithdrawRefundInfo(WithdrawRefundInfo withdrawRefundInfo) {
        if (null != withdrawRefundInfo) {
            withdrawDAO.saveWithdrawRefundInfo(withdrawRefundInfo);
        }
    }

    @Override
    public WithdrawRefundInfo getWithdrawRefundInfoByRefundNo(String refundNo) {
        if (null != refundNo){
           return withdrawDAO.getWithdrawRefundInfoByRefundNo(refundNo);
        }
        return null;
    }
}
