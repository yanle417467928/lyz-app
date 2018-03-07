package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.PreDepositWithdrawStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 门店预存款提现 申请单
 * Created by panjie on 2018/2/5.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StPreDepositWithdraw {

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
    private Long applyStId;

    /**
     * 申请人名称
     */
    private String applyStName;

    /**
     * 身份类型
     */
    private AppIdentityType identityType;

    /**
     * 申请门店电话
     */
    private String applyStPhone;

    /**
     * 提现帐号类型
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
     * 状态
     */
    private PreDepositWithdrawStatus status;

    /**
     * 审核人id
     */
    private Long checkId;

    /**
     * 审核人名称
     */
    private String checkName;

    /**
     * 审核人编码
     */
    private String checkCode;

    /**
     * 城市id
     */

    private Long cityId;

    /**
     * 城市名称
     */
    private String cityName;

    // 帐号类型
    private String accountTypeStr;

    // 状态
    private String statusStr;

    public static final StPreDepositWithdraw transform(StPreDepositWithdraw apply) {
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

    public static final List<StPreDepositWithdraw> transform(List<StPreDepositWithdraw> list) {
        List<StPreDepositWithdraw> vOList;
        if (null != list && list.size() > 0) {
            vOList = new ArrayList<>(list.size());
            list.forEach(apply -> vOList.add(transform(apply)));
        } else {
            vOList = new ArrayList<>(0);
        }
        return vOList;
    }
}
