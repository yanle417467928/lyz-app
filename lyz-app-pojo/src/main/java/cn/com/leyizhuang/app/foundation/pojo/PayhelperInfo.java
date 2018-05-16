package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.LoanSubjectType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * create 2018-05-14 9:34
 * desc:装饰公司代付信息实体类
 **/

@Getter
@Setter
@ToString
public class PayhelperInfo {


    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 贷方ID
     */
    private Long lenderId;
    /**
     * 贷方姓名
     */
    private String lenderName;
    /**
     * 贷方电话
     */
    private String lenderPhone;
    /**
     * 装饰公司门店编码
     */
    private String storeCode;
    /**
     * 贷方主体类型
     */
    private LoanSubjectType loanSubjectType;
    /**
     * 客户经理ID
     */
    private Long sellerManagerId;
    /**
     * 客户经理姓名
     */
    private String sellerManagerName;
    /**
     * 客户经理电话
     */
    private String sellerManagerPhone;
    /**
     * 是否开通代付功能
     */
    private Boolean isOpenPayhepler;
}
