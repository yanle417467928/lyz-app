package cn.com.leyizhuang.app.foundation.pojo.user;

import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.PreDepositWithdrawStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 顾客预存款提现 申请记录表
 * Created by panjie on 2018/2/5.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CusPreDepositWithdraw {

    private Long id;

    /**
     * 申请单号
     */
    private String applyNo;

    /**
     * 申请时间
     */
    private Date createTime;

    /**
     * 申请人id
     */
    private Long applyCusId;

    /**
     * 申请人姓名
     */
    private String applyCusName;

    /**
     * 申请人电话
     */
    private String applyCusPhone;

    /**
     * 帐号类型
     */
    private OrderBillingPaymentType accountType;

    /**
     * 提现帐号
     */
    private String account;

    /**
     * 提现金额
     */
    private Double withdrawAmount;

    /**
     * 申请单状态
     */
    private PreDepositWithdrawStatus status;

    /**
     * 审核人id
     */
    private Long checkId;

    /**
     * 审核人姓名
     */
    private String checkName;

    /**
     * 审核人编号
     */
    private String checkCode;

    /** 临时字段 **/

    // 帐号类型
    private String accountTypeStr;

    // 状态
    private String statusStr;


    public static final CusPreDepositWithdraw transform(CusPreDepositWithdraw apply) {
        if (apply != null) {
            if (apply.accountType != null){
                apply.setAccountTypeStr(apply.getAccountType().getDescription());
            }else{
                apply.setAccountTypeStr("未知");
            }

            if (apply.getStatus() != null) {
                apply.setStatusStr(apply.getStatus().getDescription());
            }else{
                apply.setStatusStr("未知");
            }

            return apply;
        } else {
            return null;
        }

    }

    public static final List<CusPreDepositWithdraw> transform(List<CusPreDepositWithdraw> list) {
        List<CusPreDepositWithdraw> vOList;
        if (null != list && list.size() > 0) {
            vOList = new ArrayList<>(list.size());
            list.forEach(apply -> vOList.add(transform(apply)));
        } else {
            vOList = new ArrayList<>(0);
        }
        return vOList;
    }
}
