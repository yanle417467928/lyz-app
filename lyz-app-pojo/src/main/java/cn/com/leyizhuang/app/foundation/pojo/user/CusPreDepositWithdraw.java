package cn.com.leyizhuang.app.foundation.pojo.user;

import java.util.Date;

/**
 * 顾客预存款提现 申请记录表
 * Created by panjie on 2018/2/5.
 */
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
    private Long applyCusName;

    /**
     * 申请人电话
     */
    private String applyCusPhone;

    /**
     * 帐号类型
     */
    private String accountType;

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
    private String status;

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
}
