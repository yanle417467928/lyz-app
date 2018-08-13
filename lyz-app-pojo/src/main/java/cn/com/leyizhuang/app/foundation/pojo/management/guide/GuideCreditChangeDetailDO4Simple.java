package cn.com.leyizhuang.app.foundation.pojo.management.guide;

import cn.com.leyizhuang.app.core.constant.EmpCreditMoneyChangeType;
import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideCreditChangeDetailVO;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GuideCreditChangeDetailDO4Simple {

    private Long id;
    //创建时间
    private String createTime;
    //临时额度改变
    private GuideTempCreditChange tempCreditChangeId;
    //固定额度改变
    private GuideFixedCreditChange fixedCreditChangeId;
    //可用额度变更
    private GuideAvailableCreditChange availableCreditChangId;
    //相关单号
    private String referenceNumber;
    //变更类型
    private EmpCreditMoneyChangeType changeType;
    //变更类型描述
    private String changeTypeDesc;
    //操作人id
    private Long operatorId;
    //操作人id
    private String operatorName;
    //导购id
    private Long empId;
    //操作人身份类型
    private String operatorType;
    //操作人IP
    private String operatorIp;

    //修改原因
    private String changeReason;

    //修改金额
    private String changeMoney;

}
