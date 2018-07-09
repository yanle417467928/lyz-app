package cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany;

import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.management.store.StoreDO;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 账单还款表
 * @author GenerationRoad
 * @date 2018/6/26
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BillRepaymentInfoVO {

    private Long id;
    //还款单号（BL_RC开头）
    private String repaymentNo;
    //还款人姓名
    private String repaymentUserName;
    //还款系统(app,manage)
    private String repaymentSystem;
    //还款时间
    private Date repaymentTime;
    //还款总金额
    private Double totalRepaymentAmount;

    public static final BillRepaymentInfoVO transfrom(BillRepaymentInfoDO billRepaymentInfoDO){
        if(null !=billRepaymentInfoDO){
            BillRepaymentInfoVO billRepaymentInfoVO = new BillRepaymentInfoVO();
            billRepaymentInfoVO.setId(billRepaymentInfoDO.getId());
            billRepaymentInfoVO.setRepaymentNo(billRepaymentInfoDO.getRepaymentNo());
            billRepaymentInfoVO.setRepaymentSystem(billRepaymentInfoDO.getRepaymentSystem());
            billRepaymentInfoVO.setRepaymentTime(billRepaymentInfoDO.getRepaymentTime());
            billRepaymentInfoVO.setRepaymentUserName(billRepaymentInfoDO.getRepaymentUserName());
            billRepaymentInfoVO.setTotalRepaymentAmount(billRepaymentInfoDO.getTotalRepaymentAmount());
            return billRepaymentInfoVO;
        }else{
            return null;
        }
    }

    public static final List<BillRepaymentInfoVO> transform(List<BillRepaymentInfoDO> billRepaymentInfoDOList) {
        List<BillRepaymentInfoVO> billRepaymentInfoVOList;
        if (null != billRepaymentInfoDOList && billRepaymentInfoDOList.size() > 0) {
            billRepaymentInfoVOList = new ArrayList<>(billRepaymentInfoDOList.size());
            billRepaymentInfoDOList.forEach(billRepaymentInfoDO -> billRepaymentInfoVOList.add(transfrom(billRepaymentInfoDO)));
        } else {
            billRepaymentInfoVOList = new ArrayList<>(0);
        }
        return billRepaymentInfoVOList;
    }

}
