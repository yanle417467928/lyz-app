package cn.com.leyizhuang.app.foundation.pojo.management.guide;

import cn.com.leyizhuang.app.core.constant.EmpCreditMoneyChangeType;
import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideCreditChangeDetailVO;
import lombok.*;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * 后台导购额度详情
 *
 * @author liuh
 * Date: 2018/02/27.
 * Time: 10:41.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GuideCreditChangeDetail {

    private Long id;
    //创建时间
    private Date  createTime;
    //临时额度改变
    private Long tempCreditChangeId;
    //固定额度改变
    private Long fixedCreditChangeId;
    //可用额度变更
    private Long availableCreditChangId;
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
    private Long  empId;
    //操作人身份类型
    private String operatorType;
    //操作人IP
    private String operatorIp;
    //修改原因
    private String changeReason;
}
